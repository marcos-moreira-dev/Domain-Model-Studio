package com.marcosmoreira.domainmodelstudio.presentation.logicalbusinessgraph;

import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramArrowFactory;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramArrowKind;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramConnectorStyle;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBounds;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasConnectorGeometry;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasAdapter;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasConnector;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasNode;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasRenderKit;
import java.util.Optional;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/** Renderizador de tarjetas semánticas del Grafo lógico del negocio. */
public final class LogicalBusinessGraphRenderKit implements InteractiveCanvasRenderKit {

    private static final double SELF_LOOP_ARROW_LENGTH = 15.0;
    private static final double SELF_LOOP_ARROW_WIDTH = 10.0;
    private final DiagramArrowFactory arrowFactory = new DiagramArrowFactory();

    @Override
    public Node renderNode(InteractiveCanvasNode node, CanvasBounds bounds, boolean selected) {
        return renderNode(node, bounds, selected, DiagramDrawingFacade.defaults());
    }

    @Override
    public Node renderNode(InteractiveCanvasNode node, CanvasBounds bounds, boolean selected, DiagramDrawingFacade drawingFacade) {
        Group group = new Group();
        group.setLayoutX(bounds.x());
        group.setLayoutY(bounds.y());
        group.getStyleClass().add("logical-business-graph-node-visual-group");

        Rectangle body = new Rectangle(0.0, 0.0, bounds.width(), bounds.height());
        body.getStyleClass().addAll("logical-business-graph-node-body", "diagram-node", node.kind());
        if (selected) {
            body.getStyleClass().addAll("diagram-node-selected", "logical-business-graph-node-selected");
        }

        Label badge = new Label(prefixFromTitle(node.title()));
        badge.getStyleClass().add("logical-business-graph-node-badge");
        badge.relocate(12.0, 10.0);

        Label title = new Label(clamp(titleWithoutPrefix(node.title()), 58));
        title.getStyleClass().add("logical-business-graph-node-title");
        title.setWrapText(true);
        title.setMaxWidth(Math.max(88.0, bounds.width() - 72.0));
        title.setPrefWidth(Math.max(88.0, bounds.width() - 72.0));
        title.relocate(58.0, 10.0);

        Label subtitle = new Label(clamp(node.subtitle(), 130));
        subtitle.getStyleClass().add("logical-business-graph-node-subtitle");
        subtitle.setWrapText(true);
        subtitle.setMaxWidth(Math.max(96.0, bounds.width() - 24.0));
        subtitle.setPrefWidth(Math.max(96.0, bounds.width() - 24.0));
        subtitle.setPrefHeight(Math.max(42.0, bounds.height() - 54.0));
        subtitle.relocate(12.0, 50.0);

        if (selected) {
            title.getStyleClass().add("logical-business-graph-node-text-selected");
            subtitle.getStyleClass().add("logical-business-graph-node-text-selected");
            badge.getStyleClass().add("logical-business-graph-node-badge-selected");
        }
        group.getChildren().addAll(body, badge, title, subtitle);
        if (selected) {
            group.getChildren().add(drawingFacade.selection().selectionHalo(0.0, 0.0, bounds.width(), bounds.height()));
        }
        group.setUserData(node.id());
        return group;
    }

    @Override
    public Node renderConnector(InteractiveCanvasConnector connector, InteractiveCanvasAdapter adapter, boolean selected) {
        return renderConnector(connector, adapter, selected, DiagramDrawingFacade.defaults());
    }

    @Override
    public Node renderConnector(InteractiveCanvasConnector connector, InteractiveCanvasAdapter adapter,
                                boolean selected, DiagramDrawingFacade drawingFacade) {
        Optional<NodeLayout> sourceLayout = adapter.layoutForNode(connector.sourceNodeId());
        Optional<NodeLayout> targetLayout = adapter.layoutForNode(connector.targetNodeId());
        if (sourceLayout.isEmpty() || targetLayout.isEmpty()) {
            return new Group();
        }
        DiagramConnectorStyle style = DiagramConnectorStyle
                .directed("logical-business-graph-connector-" + connector.kind(), selected)
                .withArrowKind(DiagramArrowKind.FILLED_TRIANGLE)
                .withLineStyleClass("logical-business-graph-connector-line");
        Group group = connector.sourceNodeId().equals(connector.targetNodeId())
                ? renderSelfLoop(sourceLayout.get(), style)
                : renderNormalConnector(connector, adapter, sourceLayout.get(), targetLayout.get(), style, drawingFacade);
        group.getStyleClass().add("logical-business-graph-connector-visual-group");
        group.setUserData(connector.id());
        return group;
    }

    private Group renderNormalConnector(InteractiveCanvasConnector connector, InteractiveCanvasAdapter adapter,
                                        NodeLayout source, NodeLayout target, DiagramConnectorStyle style,
                                        DiagramDrawingFacade drawingFacade) {
        DiagramDrawingFacade safeDrawingFacade = drawingFacade == null ? DiagramDrawingFacade.defaults() : drawingFacade;
        var points = CanvasConnectorGeometry.edgeToEdgePoints(source, target, adapter.layoutForConnector(connector.id()), safeDrawingFacade);
        return safeDrawingFacade.connectors().polyline(points, "", style);
    }

    private Group renderSelfLoop(NodeLayout layout, DiagramConnectorStyle style) {
        SelfLoop loop = SelfLoop.from(layout);
        Path path = new Path(
                new MoveTo(loop.start().getX(), loop.start().getY()),
                new CubicCurveTo(loop.control1().getX(), loop.control1().getY(), loop.control2().getX(), loop.control2().getY(), loop.end().getX(), loop.end().getY())
        );
        path.getStyleClass().addAll(style.lineStyleClasses());
        path.getStyleClass().add("logical-business-graph-connector-line");
        Polygon arrow = arrowFactory.triangle(loop.arrowPrevious(), loop.end(), SELF_LOOP_ARROW_LENGTH, SELF_LOOP_ARROW_WIDTH);
        arrow.getStyleClass().addAll(style.arrowStyleClasses());
        return new Group(path, arrow);
    }

    private static String prefixFromTitle(String title) {
        String normalized = title == null ? "" : title.strip();
        int hyphen = normalized.indexOf('-');
        if (hyphen > 0 && hyphen <= 5) {
            return normalized.substring(0, hyphen).strip();
        }
        int space = normalized.indexOf(' ');
        return space > 0 ? normalized.substring(0, space).strip() : normalized;
    }

    private static String titleWithoutPrefix(String title) {
        String normalized = title == null ? "" : title.strip();
        int separator = normalized.indexOf('—');
        return separator >= 0 ? normalized.substring(separator + 1).strip() : normalized;
    }

    private static String clamp(String value, int maxLength) {
        String normalized = value == null ? "" : value.strip();
        if (normalized.length() <= maxLength) {
            return normalized;
        }
        return normalized.substring(0, Math.max(0, maxLength - 1)) + "…";
    }

    private record SelfLoop(Point2D start, Point2D control1, Point2D control2, Point2D end, Point2D arrowPrevious) {
        static SelfLoop from(NodeLayout layout) {
            double x = layout.x();
            double y = layout.y();
            double w = layout.width();
            double topAnchorY = y - 4.0;
            Point2D start = new Point2D(x + w * 0.80, topAnchorY + 3.0);
            Point2D control1 = new Point2D(x + w + 72.0, y - 82.0);
            Point2D control2 = new Point2D(x - 40.0, y - 82.0);
            Point2D end = new Point2D(x + w * 0.18, topAnchorY);
            Point2D arrowPrevious = new Point2D(end.getX() - 16.0, end.getY() - 24.0);
            return new SelfLoop(start, control1, control2, end, arrowPrevious);
        }
    }
}
