package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Coordina undo/redo de la sesión activa sin cargar el shell principal con la
 * mecánica de historial y restauración de proyectos.
 */
final class ProjectHistoryCoordinator {

    private final MainShellState shellState;
    private final ProjectSessionCoordinator sessionCoordinator;
    private final Supplier<ProjectSession> activeSessionSupplier;
    private final Function<ProjectSession, DiagramProject> currentProjectResolver;
    private final Predicate<DiagramProject> replaceActiveProjectIfCompatible;
    private final BiConsumer<DiagramProject, String> projectDisplayer;
    private final Consumer<ProjectSession> sessionDirtyRefresher;
    private final Runnable activeOutputRefresher;

    ProjectHistoryCoordinator(
            MainShellState shellState,
            ProjectSessionCoordinator sessionCoordinator,
            Supplier<ProjectSession> activeSessionSupplier,
            Function<ProjectSession, DiagramProject> currentProjectResolver,
            Predicate<DiagramProject> replaceActiveProjectIfCompatible,
            BiConsumer<DiagramProject, String> projectDisplayer,
            Consumer<ProjectSession> sessionDirtyRefresher,
            Runnable activeOutputRefresher
    ) {
        this.shellState = Objects.requireNonNull(shellState, "shellState");
        this.sessionCoordinator = Objects.requireNonNull(sessionCoordinator, "sessionCoordinator");
        this.activeSessionSupplier = Objects.requireNonNull(activeSessionSupplier, "activeSessionSupplier");
        this.currentProjectResolver = Objects.requireNonNull(currentProjectResolver, "currentProjectResolver");
        this.replaceActiveProjectIfCompatible = Objects.requireNonNull(
                replaceActiveProjectIfCompatible,
                "replaceActiveProjectIfCompatible");
        this.projectDisplayer = Objects.requireNonNull(projectDisplayer, "projectDisplayer");
        this.sessionDirtyRefresher = Objects.requireNonNull(sessionDirtyRefresher, "sessionDirtyRefresher");
        this.activeOutputRefresher = Objects.requireNonNull(activeOutputRefresher, "activeOutputRefresher");
    }

    void requestUndo() {
        ProjectSession session = activeSessionSupplier.get();
        DiagramProject currentProject = currentProjectResolver.apply(session);
        if (session == null || session.isPlaceholder() || currentProject == null) {
            shellState.updateStatus("No hay proyecto abierto para deshacer cambios.");
            return;
        }
        Optional<DiagramProject> restored = session.undo(currentProject);
        if (restored.isEmpty()) {
            shellState.updateStatus("No hay cambios para deshacer.");
            return;
        }
        restoreProjectFromHistory(session, restored.get(), "Cambio deshecho.");
    }

    void requestRedo() {
        ProjectSession session = activeSessionSupplier.get();
        DiagramProject currentProject = currentProjectResolver.apply(session);
        if (session == null || session.isPlaceholder() || currentProject == null) {
            shellState.updateStatus("No hay proyecto abierto para rehacer cambios.");
            return;
        }
        Optional<DiagramProject> restored = session.redo(currentProject);
        if (restored.isEmpty()) {
            shellState.updateStatus("No hay cambios para rehacer.");
            return;
        }
        restoreProjectFromHistory(session, restored.get(), "Cambio rehecho.");
    }

    private void restoreProjectFromHistory(ProjectSession session, DiagramProject project, String statusMessage) {
        session.project = project;
        session.dirty = true;
        sessionCoordinator.runActivating(() -> {
            if (!replaceActiveProjectIfCompatible.test(project)) {
                projectDisplayer.accept(project, "Proyecto en edición");
            }
            shellState.showProjectState(project, "Proyecto en edición");
            sessionDirtyRefresher.accept(session);
            activeOutputRefresher.run();
        });
        shellState.markDirty();
        shellState.updateStatus(statusMessage);
    }
}
