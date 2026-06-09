package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.DiagramInteractionProfile;
import java.util.List;
import java.util.Objects;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;

/**
 * Renderiza y mueve la etiqueta común de conectores del canvas interactivo.
 *
 * <p>La etiqueta queda fuera del trazo principal para que los conectores puedan
 * dibujarse sin texto duplicado y cada diagrama conserve una superposición común.</p>
 */
public final class CanvasConnectorLabelOverlayRenderer {

    private final InteractiveCanvasAdapter adapter;
    private final DiagramInteractionProfile interactionProfile;
    private final DiagramDrawingFacade drawingFacade;
    private final CanvasConnectorLabelController connectorLabelController;
    private final CanvasPointMapper canvasPointMapper;
    private final Runnable refreshPreservingViewport;
    private final Runnable requestCanvasFocus;
    private Point2D connectorLabelDragAnchor;

    public CanvasConnectorLabelOverlayRenderer(
            InteractiveCanvasAdapter adapter,
            DiagramInteractionProfile interactionProfile,
            DiagramDrawingFacade drawingFacade,
            CanvasConnectorLabelController connectorLabelController,
            CanvasPointMapper canvasPointMapper,
            Runnable refreshPreservingViewport,
            Runnable requestCanvasFocus
    ) {
        this.adapter = Objects.requireNonNull(adapter, "El adaptador no puede ser null");
        this.interactionProfile = Objects.requireNonNull(interactionProfile, "El perfil de interacción no puede ser null");
        this.drawingFacade = Objects.requireNonNull(drawingFacade, "La fachada de dibujo no puede ser null");
        this.connectorLabelController = connectorLabelController;
        this.canvasPointMapper = Objects.requireNonNull(canvasPointMapper, "El mapeador de coordenadas no puede ser null");
        this.refreshPreservingViewport = Objects.requireNonNull(refreshPreservingViewport, "El refresco no puede ser null");
        this.requestCanvasFocus = Objects.requireNonNull(requestCanvasFocus, "El foco del canvas no puede ser null");
    }

    public Node renderLabel(InteractiveCanvasConnector connector, InteractiveCanvasModel model) {
        if (!interactionProfile.supportsCommonConnectorLabelOverlay()) {
            return null;
        }
        String visibleLabel = visibleConnectorLabel(connector);
        if (visibleLabel.isBlank()) {
            return null;
        }
        ConnectorLayout layout = model.layoutForConnector(connector.id()).orElse(null);
        List<Point2D> route = connectorRoute(connector, model, layout);
        if (route.isEmpty()) {
            return null;
        }
        Point2D labelPoint = CanvasConnectorLabelPositioner.labelPoint(route, layout);
        Label label = CanvasConnectorLabelNodeFactory.createOverlayLabel(
                visibleLabel,
                labelPoint,
                connector.kind(),
                model.selection().isConnectorSelected(connector.id())
        );
        explicitStyleFor(connector.id()).ifPresent(style -> CanvasStyleApplier.applyTextStyle(label, style));
        label.setUserData(connector.id());
        label.setOnMousePressed(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }
            requestCanvasFocus.run();
            if (interactionProfile.supportsConnectorSelection()) {
                adapter.selectConnector(connector.id(), event.isShiftDown());
                label.getStyleClass().add("interactive-canvas-connector-label-selected");
            }
            connectorLabelDragAnchor = canvasPointMapper.toCanvasPoint(event.getSceneX(), event.getSceneY());
            event.consume();
        });
        label.setOnMouseDragged(event -> {
            if (!event.isPrimaryButtonDown()
                    || connectorLabelController == null
                    || connectorLabelDragAnchor == null
                    || !interactionProfile.supportsCommonConnectorLabelOverlay()) {
                return;
            }
            Point2D canvasPoint = canvasPointMapper.toCanvasPoint(event.getSceneX(), event.getSceneY());
            double deltaX = canvasPoint.getX() - connectorLabelDragAnchor.getX();
            double deltaY = canvasPoint.getY() - connectorLabelDragAnchor.getY();
            connectorLabelController.moveBy(connector.id(), deltaX, deltaY);
            label.setLayoutX(label.getLayoutX() + deltaX);
            label.setLayoutY(label.getLayoutY() + deltaY);
            connectorLabelDragAnchor = canvasPoint;
            event.consume();
        });
        label.setOnMouseReleased(event -> {
            connectorLabelDragAnchor = null;
            refreshPreservingViewport.run();
            event.consume();
        });
        return label;
    }

    public String visibleConnectorLabel(InteractiveCanvasConnector connector) {
        String label = connector.label() == null ? "" : connector.label().strip();
        if (!label.isBlank()) {
            return label;
        }
        if (connector.kind().startsWith("free-graph-edge")) {
            String separator = connector.kind().contains("undirected") ? " — " : " → ";
            return readableConnectorEndpoint(connector.sourceNodeId())
                    + separator
                    + readableConnectorEndpoint(connector.targetNodeId());
        }
        return "";
    }

    private String readableConnectorEndpoint(String nodeId) {
        String normalized = nodeId == null ? "" : nodeId.strip();
        int separatorIndex = normalized.indexOf(':');
        if (separatorIndex >= 0 && separatorIndex + 1 < normalized.length()) {
            normalized = normalized.substring(separatorIndex + 1);
        }
        normalized = normalized.replace('-', ' ').replace('_', ' ').strip();
        if (normalized.isBlank()) {
            return "Nodo";
        }
        return Character.toUpperCase(normalized.charAt(0)) + normalized.substring(1);
    }

    private List<Point2D> connectorRoute(InteractiveCanvasConnector connector, InteractiveCanvasModel model, ConnectorLayout layout) {
        return model.layoutForNode(connector.sourceNodeId())
                .flatMap(source -> model.layoutForNode(connector.targetNodeId())
                        .map(target -> CanvasConnectorGeometry.edgeToEdgePoints(
                                source,
                                target,
                                java.util.Optional.ofNullable(layout),
                                drawingFacade)))
                .orElseGet(List::of);
    }

    private java.util.Optional<com.marcosmoreira.domainmodelstudio.domain.style.ElementStyle> explicitStyleFor(String elementId) {
        return adapter instanceof CanvasStylePort stylePort
                ? stylePort.explicitStyleForElement(elementId)
                : java.util.Optional.empty();
    }
}
