package com.marcosmoreira.domainmodelstudio.presentation.behavior;

import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasConnectorGeometry;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasAdapter;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasConnector;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasNode;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;

/** Geometría específica para que UML Casos de uso conecte actores y elipses desde su símbolo real. */
final class UseCaseConnectorGeometry {

    private UseCaseConnectorGeometry() {
    }

    static List<Point2D> route(
            InteractiveCanvasConnector connector,
            InteractiveCanvasAdapter adapter,
            NodeLayout sourceLayout,
            NodeLayout targetLayout,
            DiagramDrawingFacade drawingFacade
    ) {
        String connectorKind = connector.kind() == null ? "" : connector.kind();
        if (!connectorKind.startsWith("uml-use-case-")) {
            return CanvasConnectorGeometry.edgeToEdgePoints(
                    sourceLayout,
                    targetLayout,
                    adapter.layoutForConnector(connector.id()),
                    drawingFacade
            );
        }
        InteractiveCanvasNode sourceNode = nodeById(adapter, connector.sourceNodeId());
        InteractiveCanvasNode targetNode = nodeById(adapter, connector.targetNodeId());
        Point2D sourceCenter = CanvasConnectorGeometry.center(sourceLayout, drawingFacade);
        Point2D targetCenter = CanvasConnectorGeometry.center(targetLayout, drawingFacade);
        List<Point2D> points = new ArrayList<>();
        points.add(anchor(sourceNode, sourceLayout, targetCenter, drawingFacade));
        adapter.layoutForConnector(connector.id()).map(ConnectorLayout::bendPoints).orElseGet(List::of)
                .forEach(point -> points.add(new Point2D(point.x(), point.y())));
        points.add(anchor(targetNode, targetLayout, sourceCenter, drawingFacade));
        return points;
    }

    private static InteractiveCanvasNode nodeById(InteractiveCanvasAdapter adapter, String nodeId) {
        return adapter.nodes().stream()
                .filter(node -> node.id().equals(nodeId))
                .findFirst()
                .orElse(null);
    }

    private static Point2D anchor(
            InteractiveCanvasNode node,
            NodeLayout layout,
            Point2D target,
            DiagramDrawingFacade drawingFacade
    ) {
        String kind = node == null ? "" : semanticKind(node.kind());
        if ("actor".equals(kind)) {
            return actorAnchor(layout, target);
        }
        if ("use-case".equals(kind)) {
            return ellipseAnchor(layout, target);
        }
        return CanvasConnectorGeometry.edgePoint(layout, target, drawingFacade);
    }

    private static Point2D actorAnchor(NodeLayout layout, Point2D target) {
        double width = layout.width();
        double height = layout.height();
        double centerX = layout.x() + width / 2.0;
        double top = Math.max(6.0, height * 0.12);
        double headRadius = Math.min(9.0, Math.max(5.0, height * 0.08));
        double bodyTop = top + headRadius * 2.0 + 2.0;
        double bodyBottom = Math.min(height - 28.0, bodyTop + Math.max(22.0, height * 0.28));
        double armY = layout.y() + bodyTop + Math.max(7.0, (bodyBottom - bodyTop) * 0.32);
        double armHalf = Math.min(22.0, width * 0.22);
        double direction = target.getX() >= centerX ? 1.0 : -1.0;
        return new Point2D(centerX + direction * armHalf, armY);
    }

    private static Point2D ellipseAnchor(NodeLayout layout, Point2D target) {
        double centerX = layout.x() + layout.width() / 2.0;
        double centerY = layout.y() + layout.height() / 2.0;
        double radiusX = Math.max(18.0, layout.width() / 2.0 - 6.0);
        double radiusY = Math.max(12.0, layout.height() / 2.0 - 8.0);
        double dx = target.getX() - centerX;
        double dy = target.getY() - centerY;
        if (Math.abs(dx) < 0.0001 && Math.abs(dy) < 0.0001) {
            return new Point2D(centerX, centerY);
        }
        double scale = 1.0 / Math.sqrt((dx * dx) / (radiusX * radiusX) + (dy * dy) / (radiusY * radiusY));
        return new Point2D(centerX + dx * scale, centerY + dy * scale);
    }

    private static String semanticKind(String visualKind) {
        String normalized = visualKind == null ? "" : visualKind;
        return normalized
                .replaceFirst("^uml-use-case-", "")
                .replaceFirst("^uml-activity-", "")
                .replaceFirst("^uml-state-", "")
                .replaceFirst("^bpmn-", "")
                .replaceFirst("^operational-", "");
    }
}
