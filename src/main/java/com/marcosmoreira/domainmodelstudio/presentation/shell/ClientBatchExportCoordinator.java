package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.application.ApplicationServices;
import com.marcosmoreira.domainmodelstudio.application.batchexport.ClientBatchExportItemResult;
import com.marcosmoreira.domainmodelstudio.application.batchexport.ClientBatchExportRequest;
import com.marcosmoreira.domainmodelstudio.application.batchexport.ClientBatchExportResult;
import com.marcosmoreira.domainmodelstudio.application.batchexport.OpenProjectExportItem;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ActiveOutputResolver;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportFormat;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportableOutput;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ProjectExportFormatPolicy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javafx.stage.DirectoryChooser;

/**
 * Orquesta la exportación de todas las pestañas abiertas para entrega a cliente.
 *
 * <p>Este flujo es deliberadamente impuro: abre un selector de carpeta, activa pestañas
 * para tomar snapshots PNG de la vista visual y escribe un resumen en el manifiesto. Al
 * aislarlo aquí, el {@link MainShellCommandHandler} conserva solo la decisión de menú y
 * no carga con la construcción del lote, los snapshots ni la restauración de pestaña.</p>
 */
final class ClientBatchExportCoordinator {

    private final MainShellState shellState;
    private final ApplicationServices applicationServices;
    private final ProjectExportFormatPolicy projectExportFormatPolicy;
    private final ActiveOutputResolver activeOutputResolver;
    private final Map<String, ProjectSession> projectSessions;
    private final Function<ProjectSession, DiagramProject> projectForSaving;
    private final BiConsumer<ProjectSession, String> projectActivator;
    private final Runnable homeActivator;
    private final Supplier<String> activeTabIdSupplier;

    ClientBatchExportCoordinator(
            MainShellState shellState,
            ApplicationServices applicationServices,
            ProjectExportFormatPolicy projectExportFormatPolicy,
            ActiveOutputResolver activeOutputResolver,
            Map<String, ProjectSession> projectSessions,
            Function<ProjectSession, DiagramProject> projectForSaving,
            BiConsumer<ProjectSession, String> projectActivator,
            Runnable homeActivator,
            Supplier<String> activeTabIdSupplier
    ) {
        this.shellState = Objects.requireNonNull(shellState, "shellState");
        this.applicationServices = Objects.requireNonNull(applicationServices, "applicationServices");
        this.projectExportFormatPolicy = Objects.requireNonNull(projectExportFormatPolicy, "projectExportFormatPolicy");
        this.activeOutputResolver = Objects.requireNonNull(activeOutputResolver, "activeOutputResolver");
        this.projectSessions = Objects.requireNonNull(projectSessions, "projectSessions");
        this.projectForSaving = Objects.requireNonNull(projectForSaving, "projectForSaving");
        this.projectActivator = Objects.requireNonNull(projectActivator, "projectActivator");
        this.homeActivator = Objects.requireNonNull(homeActivator, "homeActivator");
        this.activeTabIdSupplier = Objects.requireNonNull(activeTabIdSupplier, "activeTabIdSupplier");
    }

    void requestExportClientBatch() {
        if (projectSessions.isEmpty()) {
            shellState.updateStatus("No hay proyectos abiertos para exportar.");
            return;
        }
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Elegir carpeta base de exportación");
        java.io.File selectedDirectory = chooser.showDialog(null);
        if (selectedDirectory == null) {
            shellState.updateStatus("Exportación cancelada.");
            return;
        }

        Path destinationRoot = selectedDirectory.toPath();
        String originalActiveTabId = activeTabIdSupplier.get();
        try {
            List<OpenProjectExportItem> items = openProjectExportItems();
            if (items.isEmpty()) {
                restoreActiveTab(originalActiveTabId);
                shellState.updateStatus("No hay proyectos exportables en las pestañas abiertas.");
                return;
            }
            ClientBatchExportRequest request = new ClientBatchExportRequest(
                    clientNameFromDestination(destinationRoot),
                    destinationRoot,
                    items);
            ClientBatchExportResult result = applicationServices.exportOpenProjectsForClientUseCase().export(request);
            int pngCount = exportPendingPngSnapshots(result);
            appendPngSnapshotSummary(result, pngCount);
            restoreActiveTab(originalActiveTabId);
            shellState.updateStatus("Exportación de proyectos abiertos lista: "
                    + result.itemResults().size() + " proyectos, "
                    + (result.exportedFiles().size() + pngCount) + " archivos en "
                    + result.rootFolder().toAbsolutePath());
        } catch (IOException | IllegalArgumentException | IllegalStateException exception) {
            restoreActiveTab(originalActiveTabId);
            shellState.updateStatus("No se pudieron exportar los proyectos abiertos: " + exception.getMessage());
        }
    }

    private List<OpenProjectExportItem> openProjectExportItems() {
        List<OpenProjectExportItem> items = new ArrayList<>();
        for (EditorTabViewState tab : shellState.editorTabs()) {
            ProjectSession session = projectSessions.get(tab.id());
            if (session == null || session.isPlaceholder()) {
                continue;
            }
            projectActivator.accept(session, "Preparando exportación por lote");
            DiagramProject project = projectForSaving.apply(session);
            if (project == null) {
                continue;
            }
            Optional<ExportableOutput> activeOutput = activeOutputResolver.activeOutput();
            Set<ExportFormat> formats = activeOutput
                    .map(output -> output.descriptor().supportedFormats())
                    .orElseGet(() -> projectExportFormatPolicy.formatsForProject(project, true));
            if (formats.isEmpty()) {
                continue;
            }
            DiagramProject outputProject = activeOutput
                    .flatMap(ExportableOutput::visualProject)
                    .orElse(project);
            String fileStem = activeOutput
                    .map(output -> output.descriptor().suggestedFileStem())
                    .orElse(project.metadata().id());
            items.add(new OpenProjectExportItem(
                    session.tabId,
                    session.title(),
                    project.metadata().diagramTypeId(),
                    project,
                    sourceMarkdownPath(project),
                    formats.contains(ExportFormat.MARKDOWN),
                    formats.contains(ExportFormat.SVG),
                    formats.contains(ExportFormat.PNG),
                    formats.contains(ExportFormat.PDF),
                    outputProject,
                    fileStem));
        }
        return items;
    }

    private Optional<Path> sourceMarkdownPath(DiagramProject project) {
        String rawPath = project.metadata().sourceMarkdownPath();
        if (rawPath == null || rawPath.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(Path.of(rawPath));
    }

    private String clientNameFromDestination(Path destinationRoot) {
        Path fileName = destinationRoot.getFileName();
        return fileName == null ? "proyectos_abiertos" : fileName.toString();
    }

    private int exportPendingPngSnapshots(ClientBatchExportResult result) throws IOException {
        int exported = 0;
        for (ClientBatchExportItemResult itemResult : result.itemResults()) {
            Optional<Path> pngTarget = itemResult.pendingPngTarget();
            if (pngTarget.isEmpty()) {
                continue;
            }
            ProjectSession session = projectSessions.get(itemResult.tabId());
            if (session == null || session.isPlaceholder()) {
                continue;
            }
            projectActivator.accept(session, "Exportando PNG");
            var activeOutput = activeOutputResolver.activeOutput();
            if (activeOutput.isEmpty() || activeOutput.get().pngAction().isEmpty()) {
                throw new IllegalStateException("La pestaña '" + itemResult.displayName()
                        + "' no ofrece PNG exportable.");
            }
            activeOutput.get().pngAction().orElseThrow().export(pngTarget.get());
            exported++;
        }
        return exported;
    }

    private void appendPngSnapshotSummary(ClientBatchExportResult result, int pngCount) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("\n## PNG exportados desde la vista visual\n\n");
        if (pngCount == 0) {
            builder.append("No se exportaron PNG desde vistas visuales.\n");
        } else {
            for (ClientBatchExportItemResult itemResult : result.itemResults()) {
                itemResult.pendingPngTarget().ifPresent(path -> {
                    if (Files.exists(path)) {
                        builder.append("- `")
                                .append(result.rootFolder().relativize(path))
                                .append("`\n");
                    }
                });
            }
        }
        Files.writeString(result.manifestFile(), builder.toString(), StandardOpenOption.APPEND);
    }

    private void restoreActiveTab(String originalActiveTabId) {
        if (originalActiveTabId != null && projectSessions.containsKey(originalActiveTabId)) {
            projectActivator.accept(projectSessions.get(originalActiveTabId), "Proyecto abierto");
        } else if (!projectSessions.isEmpty()) {
            projectActivator.accept(projectSessions.values().iterator().next(), "Proyecto abierto");
        } else {
            homeActivator.run();
        }
    }
}
