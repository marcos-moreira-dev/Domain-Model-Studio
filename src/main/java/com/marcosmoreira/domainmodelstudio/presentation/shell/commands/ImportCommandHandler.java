package com.marcosmoreira.domainmodelstudio.presentation.shell.commands;

import com.marcosmoreira.domainmodelstudio.application.examples.DefaultOfficialExampleCatalog;
import com.marcosmoreira.domainmodelstudio.application.examples.OfficialExampleCatalog;
import com.marcosmoreira.domainmodelstudio.application.examples.OfficialExampleDescriptor;
import com.marcosmoreira.domainmodelstudio.application.importmodel.ImportMarkdownModelResult;
import com.marcosmoreira.domainmodelstudio.application.importmodel.ImportedProjectVisualPreparationUseCase;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeImportPreview;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeImportRequest;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.diagram.ProjectType;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.DirectoryChooser;
import com.marcosmoreira.domainmodelstudio.presentation.dialogs.OfficialExampleSelectorDialog;
import com.marcosmoreira.domainmodelstudio.presentation.dialogs.OfficialExampleSelection;
import com.marcosmoreira.domainmodelstudio.presentation.dialogs.SourceCodeImportPreviewDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

/** Comandos de entrada de información: Markdown, ejemplos y recursos para IA. */
public final class ImportCommandHandler {

    private final ShellCommandContext context;
    private final ProjectTabOpener projectTabOpener;
    private final ImportedProjectVisualPreparationUseCase visualPreparationUseCase;
    private final MarkdownFolderImportCoordinator markdownFolderImportCoordinator;
    private final SelectedMarkdownFilesImportCoordinator selectedMarkdownFilesImportCoordinator;
    private final OfficialExampleCatalog officialExampleCatalog;

    public ImportCommandHandler(ShellCommandContext context, ProjectTabOpener projectTabOpener) {
        this(context, projectTabOpener, new DefaultOfficialExampleCatalog());
    }

    public ImportCommandHandler(
            ShellCommandContext context,
            ProjectTabOpener projectTabOpener,
            OfficialExampleCatalog officialExampleCatalog
    ) {
        this.context = Objects.requireNonNull(context, "context");
        this.projectTabOpener = Objects.requireNonNull(projectTabOpener, "projectTabOpener");
        this.officialExampleCatalog = Objects.requireNonNull(officialExampleCatalog, "officialExampleCatalog");
        this.visualPreparationUseCase = new ImportedProjectVisualPreparationUseCase(
                context.applicationServices().visualServices().generateInitialChenLayoutUseCase(),
                context.applicationServices().visualServices().generateInitialCrowsFootLayoutUseCase());
        this.markdownFolderImportCoordinator = new MarkdownFolderImportCoordinator(
                context, projectTabOpener, visualPreparationUseCase);
        this.selectedMarkdownFilesImportCoordinator = new SelectedMarkdownFilesImportCoordinator(
                context, projectTabOpener, visualPreparationUseCase);
    }

    public void requestExportAiResources() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Elegir carpeta para copiar recursos IA");
        java.io.File selectedDirectory = chooser.showDialog(null);
        if (selectedDirectory == null) {
            context.shellState().updateStatus("Copia de recursos IA cancelada.");
            return;
        }

        Path destinationDirectory = selectedDirectory.toPath().resolve("domain-model-studio-recursos-ia");
        try {
            var result = context.applicationServices().documentationServices().exportAiResourcesUseCase().exportTo(destinationDirectory);
            context.shellState().updateStatus("Recursos IA copiados (" + result.exportedFiles().size()
                    + " archivos) en: " + result.destinationFolder().toAbsolutePath());
        } catch (IOException exception) {
            context.shellState().updateStatus("No se pudieron copiar los recursos IA: " + exception.getMessage());
        }
    }

    public void requestOpenExampleProject() {
        List<OfficialExampleDescriptor> examples = officialExampleCatalog.findAll();
        if (examples.isEmpty()) {
            context.shellState().updateStatus("No hay ejemplos oficiales registrados.");
            return;
        }

        Optional<OfficialExampleSelection> selected = OfficialExampleSelectorDialog.showSelection(
                null,
                examples,
                context.shellState().activeDiagramTypeProperty().get());
        if (selected.isEmpty() || selected.get().empty()) {
            context.shellState().updateStatus("Apertura de ejemplo cancelada.");
            return;
        }
        openOfficialExamples(selected.get().examples());
    }

    private void openOfficialExamples(List<OfficialExampleDescriptor> examples) {
        int opened = 0;
        for (OfficialExampleDescriptor example : examples) {
            if (openOfficialExample(example)) {
                opened++;
            }
        }
        if (examples.size() > 1) {
            context.shellState().updateStatus("Ejemplos oficiales abiertos: " + opened + " de " + examples.size() + ".");
        }
    }

    private boolean openOfficialExample(OfficialExampleDescriptor example) {
        if (!example.importable()) {
            context.shellState().updateStatus("El ejemplo seleccionado es una referencia documental, no importable todavía.");
            return false;
        }

        try (InputStream stream = ImportCommandHandler.class.getClassLoader()
                .getResourceAsStream(example.classpathLocation())) {
            if (stream == null) {
                context.shellState().updateStatus("No se encontró el recurso del ejemplo: " + example.sourceName());
                return false;
            }
            String markdown = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            return importMarkdownContent(markdown, example.sourceName(), "Ejemplo abierto — " + example.title());
        } catch (IOException exception) {
            context.shellState().updateStatus("No se pudo abrir el ejemplo: " + exception.getMessage());
            return false;
        }
    }


    public void requestImportUmlClassFromSourceCode() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Importar UML Clases desde código fuente");
        java.io.File selectedDirectory = chooser.showDialog(null);
        if (selectedDirectory == null) {
            context.shellState().updateStatus("Importación de código fuente cancelada.");
            return;
        }
        previewAndImportUmlClassFromSourceDirectory(selectedDirectory.toPath());
    }

    public boolean importUmlClassFromSourceDirectory(Path sourceDirectory) {
        return importUmlClassFromSourceRequest(SourceCodeImportRequest.flexible(sourceDirectory));
    }

    public boolean previewAndImportUmlClassFromSourceDirectory(Path sourceDirectory) {
        try {
            SourceCodeImportRequest initialRequest = SourceCodeImportRequest.flexible(sourceDirectory);
            // PreviewSourceCodeImportUseCase se conserva separado del importador Markdown.
            SourceCodeImportPreview preview = context.applicationServices().importServices().previewSourceCodeImportUseCase()
                    .preview(initialRequest);
            if (!preview.importable()) {
                context.shellState().updateStatus("No se detectaron archivos Java/TypeScript importables.");
                return false;
            }
            Optional<SourceCodeImportRequest> confirmed = SourceCodeImportPreviewDialog.showConfirmation(
                    null,
                    preview,
                    initialRequest);
            if (confirmed.isEmpty()) {
                context.shellState().updateStatus("Importación de código fuente cancelada desde vista previa.");
                return false;
            }
            return startBackgroundUmlClassImport(confirmed.get());
        } catch (RuntimeException exception) {
            context.shellState().updateStatus("No se pudo preparar la vista previa: " + exception.getMessage());
            return false;
        }
    }

    private boolean startBackgroundUmlClassImport(SourceCodeImportRequest request) {
        DialogProgress progressDialog = DialogProgress.show("Generando UML desde código fuente",
                "Procesando el proyecto sin bloquear la interfaz...");
        Task<UmlClassDiagramDocument> task = new Task<>() {
            private long lastStatusUpdate;

            @Override
            protected UmlClassDiagramDocument call() {
                updateImportProgress("Iniciando importación de código fuente...");
                return context.applicationServices().importServices().generateUmlClassDiagramFromSourceCodeUseCase()
                        .generate(request, this::updateImportProgress);
            }

            private void updateImportProgress(String message) {
                String safeMessage = message == null || message.isBlank()
                        ? "Importación UML desde código en proceso..."
                        : message;
                updateMessage(safeMessage);
                long now = System.currentTimeMillis();
                if (now - lastStatusUpdate > 400L) {
                    lastStatusUpdate = now;
                    Platform.runLater(() -> context.shellState().updateStatus(safeMessage));
                }
            }
        };
        progressDialog.bind(task);
        task.setOnSucceeded(event -> {
            progressDialog.close();
            openGeneratedUmlClassDocument(request, task.getValue());
        });
        task.setOnFailed(event -> {
            progressDialog.close();
            handleSourceImportFailure(task.getException());
        });
        Thread thread = new Thread(task, "dms-uml-source-import");
        thread.setDaemon(true);
        thread.start();
        context.shellState().updateStatus("Importación UML desde código en proceso...");
        return true;
    }

    public boolean importUmlClassFromSourceRequest(SourceCodeImportRequest request) {
        try {
            UmlClassDiagramDocument document = context.applicationServices().importServices().generateUmlClassDiagramFromSourceCodeUseCase()
                    .generate(request);
            return openGeneratedUmlClassDocument(request, document);
        } catch (RuntimeException exception) {
            context.shellState().updateStatus("No se pudo importar código fuente: " + exception.getMessage());
            return false;
        } catch (OutOfMemoryError error) {
            showErrorDialog("Memoria insuficiente", memoryMessage(error));
            return false;
        }
    }

    private boolean openGeneratedUmlClassDocument(SourceCodeImportRequest request, UmlClassDiagramDocument document) {
        if (document == null || document.classes().isEmpty()) {
            String message = "No se detectaron clases Java/TypeScript para generar UML. "
                    + "Si seleccionaste una carpeta de paquete o módulo puntual, verifica que contenga archivos .java/.ts reales "
                    + "y que no sean solo tests excluidos por la opción actual.";
            context.shellState().updateStatus(message);
            showInfoDialog("No se generó UML", message);
            return false;
        }
        DiagramProject project = DiagramProject.blank(
                projectIdFor(request.projectRoot()),
                document.projectName(),
                ProjectType.CONCEPTUAL_MODEL,
                DiagramTypeId.UML_CLASS).withUmlClassDiagram(document);
        context.shellState().updateStatus("Abriendo editor UML Clases y preparando lienzo visual...");
        try {
            projectTabOpener.openProjectInNewTab(project, "UML Clases importado desde código", true);
        } catch (OutOfMemoryError error) {
            showErrorDialog("Memoria insuficiente al abrir UML", memoryMessage(error));
            return false;
        }
        context.shellState().updateStatus(summaryFor(document, request.projectRoot()));
        return true;
    }

    private void handleSourceImportFailure(Throwable throwable) {
        if (isOutOfMemory(throwable)) {
            showErrorDialog("Memoria insuficiente", memoryMessage(throwable));
            return;
        }
        String message = throwable == null ? "error desconocido" : throwable.getMessage();
        context.shellState().updateStatus("No se pudo importar código fuente: " + message);
        showErrorDialog("No se pudo importar código fuente", message);
    }

    private boolean isOutOfMemory(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof OutOfMemoryError) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    private String memoryMessage(Throwable throwable) {
        String detail = throwable == null || throwable.getMessage() == null ? "sin detalle adicional" : throwable.getMessage();
        return "La importación o exportación requiere más memoria de la disponible. "
                + "Prueba importando menos carpetas, excluyendo tests o cerrando otras aplicaciones. Detalle: " + detail;
    }

    private void showErrorDialog(String title, String message) {
        Runnable show = () -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            alert.setTitle(title);
            alert.setHeaderText(title);
            alert.showAndWait();
        };
        if (Platform.isFxApplicationThread()) {
            show.run();
        } else {
            Platform.runLater(show);
        }
    }

    private void showInfoDialog(String title, String message) {
        Runnable show = () -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
            alert.setTitle(title);
            alert.setHeaderText(title);
            alert.showAndWait();
        };
        if (Platform.isFxApplicationThread()) {
            show.run();
        } else {
            Platform.runLater(show);
        }
    }

    public void requestImportMarkdown() {
        selectedMarkdownFilesImportCoordinator.requestImportMarkdownFiles();
    }

    public void requestImportMarkdownFolder() {
        markdownFolderImportCoordinator.requestImportMarkdownFolder();
    }

    public boolean importMarkdownFile(Path markdownFile) {
        try {
            ImportMarkdownModelResult result = context.applicationServices().importServices().importMarkdownModelUseCase().importFile(markdownFile);
            return openImportedProject(result, "Markdown importado");
        } catch (IOException exception) {
            context.shellState().updateStatus("No se pudo leer el Markdown: " + exception.getMessage());
            return false;
        } catch (MarkdownModelParsingException | IllegalArgumentException exception) {
            context.shellState().updateStatus("Markdown inválido: " + exception.getMessage());
            return false;
        }
    }

    private boolean importMarkdownContent(String markdownContent, String sourceName, String statusLabel) {
        try {
            ImportMarkdownModelResult result = context.applicationServices().importServices().importMarkdownModelUseCase()
                    .importContent(markdownContent, sourceName);
            return openImportedProject(result, statusLabel);
        } catch (MarkdownModelParsingException | IllegalArgumentException exception) {
            context.shellState().updateStatus("Markdown inválido: " + exception.getMessage());
            return false;
        }
    }


    private String summaryFor(UmlClassDiagramDocument document, Path sourceDirectory) {
        return "UML Clases generado desde " + sourceDirectory.toAbsolutePath()
                + ": " + document.classes().size() + " clases, "
                + document.modules().size() + " módulos, "
                + document.relations().size() + " relaciones, "
                + document.views().size() + " vistas internas.";
    }

    private String projectIdFor(Path sourceDirectory) {
        String name = sourceDirectory.getFileName() == null ? "codigo-fuente" : sourceDirectory.getFileName().toString();
        String normalized = name.strip().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-");
        normalized = normalized.replaceAll("^-+|-+$", "");
        return (normalized.isBlank() ? "codigo-fuente" : normalized) + "-uml-codigo";
    }

    private boolean openImportedProject(ImportMarkdownModelResult result, String sourceLabel) {
        DiagramProject project = visualPreparationUseCase.prepare(result.project());
        projectTabOpener.openProjectInNewTab(project, sourceLabel, true);
        String suffix = result.validationResult().warnings().isEmpty()
                ? ""
                : " Con " + result.validationResult().warnings().size() + " advertencias.";
        context.shellState().updateStatus(sourceLabel + ": " + result.summary() + "." + suffix);
        return true;
    }

    private static final class DialogProgress {
        private final javafx.scene.control.Dialog<Void> dialog;
        private final Label label;

        private DialogProgress(javafx.scene.control.Dialog<Void> dialog, Label label) {
            this.dialog = dialog;
            this.label = label;
        }

        static DialogProgress show(String title, String header) {
            javafx.scene.control.Dialog<Void> dialog = new javafx.scene.control.Dialog<>();
            dialog.setTitle(title);
            dialog.setHeaderText(header);
            dialog.getDialogPane().getButtonTypes().setAll(ButtonType.CANCEL);
            ProgressIndicator indicator = new ProgressIndicator();
            indicator.setMaxSize(42, 42);
            Label label = new Label("Preparando...");
            label.setWrapText(true);
            VBox content = new VBox(12, indicator, label);
            content.setMinWidth(460);
            dialog.getDialogPane().setContent(content);
            dialog.show();
            return new DialogProgress(dialog, label);
        }

        void bind(Task<?> task) {
            label.textProperty().bind(task.messageProperty());
            var cancelButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            if (cancelButton != null) {
                cancelButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                    task.cancel();
                    dialog.close();
                });
            }
        }

        void close() {
            label.textProperty().unbind();
            dialog.close();
        }
    }

}