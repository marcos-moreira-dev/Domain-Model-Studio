package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.DiagramInteractionProfile;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.DiagramInteractionProfileResolver;
import java.util.Objects;

/**
 * Coordinador de interacción interna del canvas común.
 */
public final class CanvasInteractionController implements InteractiveCanvasCommandSink {

    private final InteractiveCanvasViewport viewport;
    private final DiagramInteractionProfile interactionProfile;
    private final CanvasPanController panController;
    private final CanvasZoomController zoomController;
    private final CanvasAreaSelectionController areaSelectionController;
    private final CanvasNodeDragController nodeDragController;
    private final CanvasBendPointController bendPointController;

    public CanvasInteractionController(InteractiveCanvasAdapter adapter, InteractiveCanvasViewport viewport) {
        this(
                adapter,
                viewport,
                DiagramInteractionProfileResolver.resolve(Objects.requireNonNull(adapter, "El adaptador no puede ser null").diagramTypeId())
        );
    }

    public CanvasInteractionController(
            InteractiveCanvasAdapter adapter,
            InteractiveCanvasViewport viewport,
            DiagramInteractionProfile interactionProfile
    ) {
        Objects.requireNonNull(adapter, "El adaptador no puede ser null");
        this.viewport = Objects.requireNonNull(viewport, "El viewport no puede ser null");
        this.interactionProfile = Objects.requireNonNull(interactionProfile, "El perfil de interacción no puede ser null");
        this.panController = new CanvasPanController(viewport);
        this.zoomController = new CanvasZoomController(viewport);
        this.areaSelectionController = new CanvasAreaSelectionController(adapter);
        this.nodeDragController = new CanvasNodeDragController(adapter);
        this.bendPointController = new CanvasBendPointController(adapter);
    }

    public CanvasPanController pan() {
        return panController;
    }

    public CanvasZoomController zoom() {
        return zoomController;
    }

    public CanvasAreaSelectionController areaSelection() {
        return areaSelectionController;
    }

    public CanvasNodeDragController nodeDrag() {
        return nodeDragController;
    }

    public CanvasBendPointController bendPoints() {
        return bendPointController;
    }

    public InteractiveCanvasViewport viewport() {
        return viewport;
    }

    public DiagramInteractionProfile interactionProfile() {
        return interactionProfile;
    }

    @Override
    public void execute(InteractiveCanvasCommand command) {
        if (command == null) {
            return;
        }
        switch (command) {
            case ZOOM_IN -> viewport.zoomAt(0.0, 0.0, 1.10);
            case ZOOM_OUT -> viewport.zoomAt(0.0, 0.0, 1.0 / 1.10);
            case RESET_ZOOM -> viewport.reset();
            case DELETE_SELECTED_BEND_POINT -> {
                if (interactionProfile.supportsBendPoints()) {
                    bendPointController.removeSelected();
                }
            }
            default -> {
                // Las demás acciones dependen del editor activo y de sus capacidades disponibles.
            }
        }
    }
}
