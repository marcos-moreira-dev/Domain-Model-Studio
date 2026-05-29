package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.List;
import java.util.Optional;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polyline;

/**
 * Renderizador base sobrio para pruebas, migraciones iniciales y diagramas simples.
 */
public final class DefaultInteractiveCanvasRenderKit implements InteractiveCanvasRenderKit {

    private static final CanvasNodeViewFactory NODE_VIEW_FACTORY = new CanvasNodeViewFactory();

    @Override
    public Node renderNode(InteractiveCanvasNode node, CanvasBounds bounds, boolean selected) {
        Label title = new Label(node.title().isBlank() ? node.id() : node.title());
        title.getStyleClass().add("interactive-canvas-node-title");
        StackPane content = new StackPane(title);
        return NODE_VIEW_FACTORY.wrap(
                node.id(),
                bounds,
                content,
                selected,
                "interactive-canvas-node",
                "interactive-canvas-node-" + node.kind(),
                selected ? "interactive-canvas-node-selected" : ""
        );
    }

    @Override
    public Node renderConnector(
            InteractiveCanvasConnector connector,
            InteractiveCanvasAdapter adapter,
            boolean selected
    ) {
        Optional<NodeLayout> sourceLayout = adapter.layoutForNode(connector.sourceNodeId());
        Optional<NodeLayout> targetLayout = adapter.layoutForNode(connector.targetNodeId());
        if (sourceLayout.isEmpty() || targetLayout.isEmpty()) {
            return new Polyline();
        }
        List<Point2D> points = CanvasConnectorGeometry.edgeToEdgePoints(
                sourceLayout.get(),
                targetLayout.get(),
                adapter.layoutForConnector(connector.id()),
                null);
        Polyline line = polylineFrom(points);
        line.getStyleClass().add("interactive-canvas-connector");
        line.getStyleClass().add("interactive-canvas-connector-" + connector.kind());
        if (selected) {
            line.getStyleClass().add("interactive-canvas-connector-selected");
        }
        Polyline hitbox = polylineFrom(points);
        hitbox.getStyleClass().add("interactive-canvas-connector-hitbox");
        Group group = new Group(hitbox, line);
        group.setManaged(false);
        group.setUserData(connector.id());
        return group;
    }

    private static Polyline polylineFrom(List<Point2D> points) {
        Polyline line = new Polyline();
        for (Point2D point : points) {
            line.getPoints().addAll(point.getX(), point.getY());
        }
        return line;
    }
}
