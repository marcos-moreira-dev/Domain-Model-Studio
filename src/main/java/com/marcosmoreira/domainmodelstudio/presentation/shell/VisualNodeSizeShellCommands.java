package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualNodeSizeCommand;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/** Coordina los comandos globales para ampliar o reducir la figura seleccionada. */
final class VisualNodeSizeShellCommands {
    private final MainShellState shellState;
    private final SpecializedWorkspaceCoordinator specializedWorkspaces;
    private final Supplier<Optional<DiagramProject>> activeProjectSupplier;

    VisualNodeSizeShellCommands(
            MainShellState shellState,
            SpecializedWorkspaceCoordinator specializedWorkspaces,
            Supplier<Optional<DiagramProject>> activeProjectSupplier
    ) {
        this.shellState = Objects.requireNonNull(shellState, "shellState");
        this.specializedWorkspaces = Objects.requireNonNull(specializedWorkspaces, "specializedWorkspaces");
        this.activeProjectSupplier = Objects.requireNonNull(activeProjectSupplier, "activeProjectSupplier");
    }

    void growSelection() {
        resize(VisualNodeSizeCommand.GROW);
    }

    void shrinkSelection() {
        resize(VisualNodeSizeCommand.SHRINK);
    }

    private void resize(VisualNodeSizeCommand command) {
        Optional<DiagramProject> activeProject = activeProjectSupplier.get();
        if (activeProject.isEmpty() || DiagramTypeId.CONCEPTUAL_MODEL.equals(activeProject.get().metadata().diagramTypeId())) {
            shellState.updateStatus("Tamaño visual disponible solo en diagramas especializados con figuras seleccionables.");
            return;
        }
        specializedWorkspaces.resizeSelectedElement(activeProject.get(), command);
    }
}
