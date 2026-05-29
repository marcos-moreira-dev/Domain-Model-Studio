package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.layout.BendPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas.ZoomableDiagramSurface;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.DiagramInteractionProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;

/** Renderiza y opera los puntos intermedios editables de conectores. */
public final class CanvasBendPointHandleRenderer {

    private static final double BEND_POINT_RADIUS = 5.0;

    private final InteractiveCanvasAdapter adapter;
    private final DiagramInteractionProfile interactionProfile;
    private final ZoomableDiagramSurface surface;
    private final CanvasBendPointController bendPointController;
    private final CanvasPointMapper canvasPointMapper;
    private final Runnable refreshPreservingViewport;
    private final CanvasConnectorVisualRegistry connectorVisualRegistry;
    private final DiagramDrawingFacade drawingFacade;

    public CanvasBendPointHandleRenderer(
            InteractiveCanvasAdapter adapter,
            DiagramInteractionProfile interactionProfile,
            ZoomableDiagramSurface surface,
            CanvasBendPointController bendPointController,
            CanvasPointMapper canvasPointMapper,
            Runnable refreshPreservingViewport,
            CanvasConnectorVisualRegistry connectorVisualRegistry,
            DiagramDrawingFacade drawingFacade
    ) {
        this.adapter = Objects.requireNonNull(adapter, "El adaptador no puede ser null");
        this.interactionProfile = Objects.requireNonNull(interactionProfile, "El perfil de interacción no puede ser null");
        this.surface = Objects.requireNonNull(surface, "La superficie no puede ser null");
        this.bendPointController = Objects.requireNonNull(bendPointController, "El controlador de puntos no puede ser null");
        this.canvasPointMapper = Objects.requireNonNull(canvasPointMapper, "El mapeador de coordenadas no puede ser null");
        this.refreshPreservingViewport = Objects.requireNonNull(refreshPreservingViewport, "El refresco no puede ser null");
        this.connectorVisualRegistry = Objects.requireNonNull(connectorVisualRegistry, "El registro visual de conectores no puede ser null");
        this.drawingFacade = drawingFacade == null ? DiagramDrawingFacade.defaults() : drawingFacade;
    }

    public List<Node> renderBendPointHandles(InteractiveCanvasConnector connector, InteractiveCanvasModel model) {
        if (!interactionProfile.supportsBendPoints() || !adapter.supportsBendPoints() || !bendPointVisible(connector, model)) {
            return List.of();
        }
        ConnectorLayout layout = model.layoutForConnector(connector.id()).orElse(null);
        if (layout == null || layout.bendPoints().isEmpty()) {
            return List.of();
        }
        List<Node> handles = new ArrayList<>();
        for (int index = 0; index < layout.bendPoints().size(); index++) {
            handles.add(renderBendPointHandle(connector.id(), index, layout.bendPoints().get(index), model));
        }
        return handles;
    }

    private Node renderBendPointHandle(String connectorId, int index, BendPoint point, InteractiveCanvasModel model) {
        Circle handle = createHandle(point, connectorOrPointSelected(connectorId, index, model));
        final double[] lastCanvasX = {point.x()};
        final double[] lastCanvasY = {point.y()};
        final boolean[] moved = {false};

        handle.setOnMousePressed(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }
            bendPointController.select(connectorId, index);
            if (!handle.getStyleClass().contains("interactive-canvas-bend-point-handle-selected")) {
                handle.getStyleClass().add("interactive-canvas-bend-point-handle-selected");
            }
            surface.root().requestFocus();
            event.consume();
        });
        handle.setOnMouseDragged(event -> {
            if (!event.isPrimaryButtonDown()) {
                return;
            }
            Point2D canvasPoint = canvasPointMapper.toCanvasPoint(event.getSceneX(), event.getSceneY());
            moveHandleLive(handle, lastCanvasX, lastCanvasY, canvasPoint);
            bendPointController.move(connectorId, index, canvasPoint.getX(), canvasPoint.getY());
            connectorVisualRegistry.updateLiveRoute(connectorId, adapter, drawingFacade);
            moved[0] = true;
            event.consume();
        });
        handle.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                // El punto ya fue actualizado en tiempo real durante MOUSE_DRAGGED.
                // En release solo estabilizamos el render para no aplicar dos veces el mismo movimiento.
                if (moved[0]) {
                    refreshPreservingViewport.run();
                }
                event.consume();
            }
        });
        return handle;
    }

    private Circle createHandle(BendPoint point, boolean selected) {
        Circle handle = new Circle(point.x(), point.y(), BEND_POINT_RADIUS);
        handle.getStyleClass().add("interactive-canvas-bend-point-handle");
        if (selected) {
            handle.getStyleClass().add("interactive-canvas-bend-point-handle-selected");
        }
        handle.setManaged(false);
        return handle;
    }

    private static void moveHandleLive(Circle handle, double[] lastCanvasX, double[] lastCanvasY, Point2D canvasPoint) {
        lastCanvasX[0] = canvasPoint.getX();
        lastCanvasY[0] = canvasPoint.getY();
        handle.setCenterX(lastCanvasX[0]);
        handle.setCenterY(lastCanvasY[0]);
    }

    private boolean connectorOrPointSelected(String connectorId, int index, InteractiveCanvasModel model) {
        return model.selection().isConnectorSelected(connectorId)
                || model.selection().selectedBendPoint()
                        .filter(selected -> selected.connectorId().equals(connectorId) && selected.index() == index)
                        .isPresent();
    }

    private boolean bendPointVisible(InteractiveCanvasConnector connector, InteractiveCanvasModel model) {
        return model.selection().isConnectorSelected(connector.id())
                || model.selection().selectedBendPoint()
                        .map(SelectedBendPoint::connectorId)
                        .filter(connector.id()::equals)
                        .isPresent();
    }
}
