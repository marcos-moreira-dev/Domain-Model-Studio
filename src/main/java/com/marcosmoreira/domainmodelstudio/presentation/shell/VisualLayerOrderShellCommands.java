package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayerOrderCommand;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Encapsula los comandos globales de orden visual de nodos seleccionados.
 *
 * <p>El shell conserva los métodos públicos consumidos por toolbar/menú, pero
 * la regla de disponibilidad y el ruteo al workspace especializado viven aquí
 * para evitar que {@link MainShellCommandHandler} vuelva a crecer.</p>
 */
final class VisualLayerOrderShellCommands {
    private final MainShellState shellState;
    private final SpecializedWorkspaceCoordinator specializedWorkspaces;
    private final Supplier<Optional<DiagramProject>> activeProjectSupplier;

    VisualLayerOrderShellCommands(
            MainShellState shellState,
            SpecializedWorkspaceCoordinator specializedWorkspaces,
            Supplier<Optional<DiagramProject>> activeProjectSupplier
    ) {
        this.shellState = Objects.requireNonNull(shellState, "shellState");
        this.specializedWorkspaces = Objects.requireNonNull(specializedWorkspaces, "specializedWorkspaces");
        this.activeProjectSupplier = Objects.requireNonNull(activeProjectSupplier, "activeProjectSupplier");
    }

    void bringSelectionToFront() {
        reorder(VisualLayerOrderCommand.BRING_TO_FRONT, "Elemento seleccionado traído al frente.");
    }

    void sendSelectionToBack() {
        reorder(VisualLayerOrderCommand.SEND_TO_BACK, "Elemento seleccionado enviado al fondo.");
    }

    void raiseSelectionLayer() {
        reorder(VisualLayerOrderCommand.RAISE, "Elemento seleccionado subido una capa.");
    }

    void lowerSelectionLayer() {
        reorder(VisualLayerOrderCommand.LOWER, "Elemento seleccionado bajado una capa.");
    }

    private void reorder(VisualLayerOrderCommand command, String successMessage) {
        Optional<DiagramProject> activeProject = activeProjectSupplier.get();
        if (activeProject.isEmpty() || DiagramTypeId.CONCEPTUAL_MODEL.equals(activeProject.get().metadata().diagramTypeId())) {
            shellState.updateStatus("Orden visual disponible solo en diagramas especializados con nodos seleccionables.");
            return;
        }
        if (specializedWorkspaces.reorderSelectedElement(activeProject.get(), command)) {
            shellState.updateStatus(successMessage);
        }
    }
}
