package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.application.ApplicationServices;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import javafx.stage.FileChooser;

/**
 * Coordina la semántica de escritorio Guardar / Guardar como para el shell.
 *
 * <p>La clase conserva FileChooser en presentation, mantiene la ruta .dms de la
 * sesión y evita que MainShellCommandHandler vuelva a crecer con detalles de IO.</p>
 */
final class ProjectSaveCoordinator {

    private final MainShellState shellState;
    private final ApplicationServices applicationServices;
    private final Supplier<ProjectSession> activeSessionSupplier;
    private final Function<ProjectSession, DiagramProject> projectForSaving;
    private final Runnable markSaved;

    ProjectSaveCoordinator(
            MainShellState shellState,
            ApplicationServices applicationServices,
            Supplier<ProjectSession> activeSessionSupplier,
            Function<ProjectSession, DiagramProject> projectForSaving,
            Runnable markSaved
    ) {
        this.shellState = Objects.requireNonNull(shellState, "shellState");
        this.applicationServices = Objects.requireNonNull(applicationServices, "applicationServices");
        this.activeSessionSupplier = Objects.requireNonNull(activeSessionSupplier, "activeSessionSupplier");
        this.projectForSaving = Objects.requireNonNull(projectForSaving, "projectForSaving");
        this.markSaved = Objects.requireNonNull(markSaved, "markSaved");
    }

    boolean saveCurrentProject() {
        ProjectSession session = activeSessionSupplier.get();
        if (session != null && session.projectFile != null) {
            return saveCurrentProjectToPath(session.projectFile);
        }
        return saveCurrentProjectWithDialog(false);
    }

    boolean saveCurrentProjectWithDialog(boolean saveAs) {
        ProjectSession session = activeSessionSupplier.get();
        DiagramProject project = projectForSaving.apply(session);
        if (project == null) {
            shellState.updateStatus("No hay proyecto abierto para guardar.");
            return false;
        }
        FileChooser chooser = new FileChooser();
        chooser.setTitle(saveAs ? "Guardar proyecto como" : "Guardar proyecto Domain Model Studio");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Proyecto Domain Model Studio (*.dms)", "*.dms"));
        configureInitialLocation(chooser, session, project);
        java.io.File file = chooser.showSaveDialog(null);
        if (file == null) {
            shellState.updateStatus("Guardado cancelado.");
            return false;
        }
        return saveCurrentProjectToPath(file.toPath());
    }

    private void configureInitialLocation(FileChooser chooser, ProjectSession session, DiagramProject project) {
        if (session != null && session.projectFile != null) {
            Path currentFile = session.projectFile;
            if (currentFile.getParent() != null) {
                java.io.File parent = currentFile.getParent().toFile();
                if (parent.isDirectory()) {
                    chooser.setInitialDirectory(parent);
                }
            }
            chooser.setInitialFileName(currentFile.getFileName().toString());
            return;
        }
        chooser.setInitialFileName(project.metadata().id() + ".dms");
    }

    private boolean saveCurrentProjectToPath(Path targetFile) {
        ProjectSession session = activeSessionSupplier.get();
        DiagramProject project = projectForSaving.apply(session);
        if (project == null) {
            shellState.updateStatus("No hay proyecto abierto para guardar.");
            return false;
        }
        try {
            Path normalizedTarget = ensureDmsExtension(targetFile);
            applicationServices.saveProjectUseCase().save(project, normalizedTarget);
            if (session != null) {
                session.project = project;
                session.projectFile = normalizedTarget;
            }
            markSaved.run();
            shellState.updateStatus("Proyecto .dms guardado: " + normalizedTarget.toAbsolutePath());
            return true;
        } catch (IOException | IllegalArgumentException exception) {
            shellState.updateStatus("No se pudo guardar el proyecto: " + exception.getMessage());
            return false;
        }
    }

    private Path ensureDmsExtension(Path targetFile) {
        String name = targetFile.getFileName().toString();
        return name.toLowerCase(Locale.ROOT).endsWith(".dms")
                ? targetFile
                : targetFile.resolveSibling(name + ".dms");
    }
}
