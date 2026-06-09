package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Sincroniza el proyecto editado por workspaces especializados con la sesión activa del shell.
 *
 * <p>Centraliza la regla común de marcar la pestaña como modificada y refrescar el estado visible
 * sin mezclarla con comandos concretos de UML, matrices, wireframes o diagramas de arquitectura.</p>
 */
final class SpecializedProjectSynchronizer {

    private final MainShellState shellState;
    private final Supplier<ProjectSession> activeSessionSupplier;

    SpecializedProjectSynchronizer(MainShellState shellState, Supplier<ProjectSession> activeSessionSupplier) {
        this.shellState = Objects.requireNonNull(shellState, "shellState");
        this.activeSessionSupplier = Objects.requireNonNull(activeSessionSupplier, "activeSessionSupplier");
    }

    void synchronize(DiagramProject updatedProject, String status) {
        if (updatedProject == null) {
            return;
        }
        ProjectSession session = activeSessionSupplier.get();
        if (session != null && !session.isPlaceholder()) {
            session.rememberEditBefore(updatedProject);
            session.project = updatedProject;
            session.dirty = true;
            shellState.updateProjectTab(session.tabId, session.title(), true);
        }
        shellState.showProjectState(updatedProject, status);
        shellState.markDirty();
        shellState.updateStatus(status.endsWith(".") ? status : status + ".");
    }
}
