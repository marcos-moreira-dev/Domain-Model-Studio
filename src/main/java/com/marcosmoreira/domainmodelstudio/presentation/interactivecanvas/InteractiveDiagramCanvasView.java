package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade;
import java.util.Objects;
import javafx.scene.Node;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * Lienzo común mínimo para diagramas de nodos y conectores.
 *
 * <p>Esta vista es una base incremental: renderiza mediante adaptador, reserva clic derecho
 * para paneo, usa scroll para zoom y delega operaciones visuales a controladores pequeños.</p>
 */
public final class InteractiveDiagramCanvasView extends StackPane {

    private final InteractiveCanvasAdapter adapter;
    private final InteractiveCanvasRenderKit renderKit;
    private final DiagramDrawingFacade drawingFacade;
    private final InteractiveCanvasViewport viewport;
    private final CanvasInteractionController interaction;
    private final Pane contentLayer = new Pane();
    private final Pane connectorLayer = new Pane();
    private final Pane nodeLayer = new Pane();
    private final Rectangle selectionRectangle = new Rectangle();

    public InteractiveDiagramCanvasView(InteractiveCanvasAdapter adapter) {
        this(adapter, new DefaultInteractiveCanvasRenderKit());
    }

    public InteractiveDiagramCanvasView(
            InteractiveCanvasAdapter adapter,
            InteractiveCanvasRenderKit renderKit
    ) {
        this.adapter = Objects.requireNonNull(adapter, "El adaptador no puede ser null");
        this.renderKit = Objects.requireNonNull(renderKit, "El renderKit no puede ser null");
        this.drawingFacade = DiagramDrawingFacade.defaults();
        this.viewport = new InteractiveCanvasViewport();
        this.interaction = new CanvasInteractionController(adapter, viewport);
        configureStructure();
        installGestures();
        refresh();
    }

    public CanvasInteractionController interaction() {
        return interaction;
    }

    public InteractiveCanvasViewport viewport() {
        return viewport;
    }

    public void refresh() {
        connectorLayer.getChildren().clear();
        nodeLayer.getChildren().clear();
        InteractiveCanvasModel model = InteractiveCanvasModel.from(adapter);
        for (InteractiveCanvasConnector connector : model.connectors()) {
            if (connector.visible()) {
                connectorLayer.getChildren().add(renderConnector(connector, model));
            }
        }
        for (InteractiveCanvasNode node : model.nodes()) {
            if (node.visible()) {
                adapter.layoutForNode(node.id()).ifPresent(layout -> nodeLayer.getChildren().add(renderNode(node, layout, model)));
            }
        }
        applyViewportTransform();
    }

    private void configureStructure() {
        getStyleClass().add("interactive-canvas-view");
        contentLayer.getStyleClass().add("interactive-canvas-content");
        setPickOnBounds(true);
        contentLayer.setPickOnBounds(true);
        connectorLayer.setPickOnBounds(false);
        nodeLayer.setPickOnBounds(false);
        connectorLayer.getStyleClass().add("interactive-canvas-connectors");
        nodeLayer.getStyleClass().add("interactive-canvas-nodes");
        selectionRectangle.getStyleClass().add("interactive-canvas-selection-rectangle");
        selectionRectangle.setManaged(false);
        selectionRectangle.setVisible(false);
        contentLayer.getChildren().addAll(connectorLayer, nodeLayer, selectionRectangle);
        getChildren().add(contentLayer);
        setFocusTraversable(true);
    }

    private Node renderConnector(InteractiveCanvasConnector connector, InteractiveCanvasModel model) {
        Node line = renderKit.renderConnector(
                connector,
                adapter,
                model.selection().isConnectorSelected(connector.id()),
                drawingFacade
        );
        line.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                adapter.selectConnector(connector.id(), event.isShiftDown());
                refresh();
                event.consume();
            }
        });
        return line;
    }

    private Node renderNode(InteractiveCanvasNode node, NodeLayout layout, InteractiveCanvasModel model) {
        Node rendered = renderKit.renderNode(
                node,
                CanvasBounds.from(layout),
                model.selection().isNodeSelected(node.id()),
                drawingFacade
        );
        rendered.setOnMousePressed(event -> {
            requestFocus();
            if (event.getButton() == MouseButton.SECONDARY) {
                interaction.pan().begin(event.getSceneX(), event.getSceneY());
                event.consume();
                return;
            }
            if (event.getButton() == MouseButton.PRIMARY) {
                Point2D canvasPoint = canvasPoint(event.getSceneX(), event.getSceneY());
                interaction.nodeDrag().begin(
                        node.id(),
                        canvasPoint.getX(),
                        canvasPoint.getY(),
                        event.isShiftDown()
                );
                refresh();
                event.consume();
            }
        });
        rendered.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                Point2D canvasPoint = canvasPoint(event.getSceneX(), event.getSceneY());
                interaction.nodeDrag().dragTo(canvasPoint.getX(), canvasPoint.getY());
                refresh();
                event.consume();
            }
        });
        rendered.setOnMouseReleased(event -> interaction.nodeDrag().end());
        return rendered;
    }

    private void installGestures() {
        setOnScroll(event -> {
            interaction.zoom().applyScroll(event.getX(), event.getY(), event.getDeltaY());
            applyViewportTransform();
            event.consume();
        });
        setOnMousePressed(event -> {
            requestFocus();
            if (event.getButton() == MouseButton.SECONDARY) {
                interaction.pan().begin(event.getSceneX(), event.getSceneY());
                event.consume();
            } else if (event.getButton() == MouseButton.PRIMARY && isBackgroundTarget(event.getTarget())) {
                adapter.clearSelection();
                Point2D canvasPoint = canvasPoint(event.getSceneX(), event.getSceneY());
                interaction.areaSelection().begin(canvasPoint.getX(), canvasPoint.getY(), event.isShiftDown());
                updateSelectionRectangle(interaction.areaSelection().currentBounds());
                event.consume();
            }
        });
        setOnMouseDragged(event -> {
            if (interaction.pan().active()) {
                interaction.pan().dragTo(event.getSceneX(), event.getSceneY());
                applyViewportTransform();
                event.consume();
            } else if (interaction.areaSelection().active()) {
                Point2D canvasPoint = canvasPoint(event.getSceneX(), event.getSceneY());
                CanvasBounds bounds = interaction.areaSelection().dragTo(canvasPoint.getX(), canvasPoint.getY());
                updateSelectionRectangle(bounds);
                event.consume();
            }
        });
        setOnMouseReleased(event -> {
            if (interaction.areaSelection().active()) {
                interaction.areaSelection().end();
                selectionRectangle.setVisible(false);
                refresh();
            }
            interaction.pan().end();
        });
    }

    private boolean isBackgroundTarget(Object target) {
        return target == this || target == contentLayer || target == connectorLayer || target == nodeLayer;
    }

    private Point2D canvasPoint(double sceneX, double sceneY) {
        return contentLayer.sceneToLocal(sceneX, sceneY);
    }

    private void updateSelectionRectangle(CanvasBounds bounds) {
        if (bounds == null) {
            return;
        }
        selectionRectangle.setX(bounds.x());
        selectionRectangle.setY(bounds.y());
        selectionRectangle.setWidth(bounds.width());
        selectionRectangle.setHeight(bounds.height());
        selectionRectangle.setVisible(true);
    }

    private void applyViewportTransform() {
        contentLayer.setScaleX(viewport.scale());
        contentLayer.setScaleY(viewport.scale());
        contentLayer.setTranslateX(viewport.translateX());
        contentLayer.setTranslateY(viewport.translateY());
    }
}
