package com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.layout.BendPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Calcula límites, rutas, puntos de borde y posiciones de etiquetas del SVG especializado. */
final class SpecializedSvgGeometry {

    SpecializedSvgBounds bounds(DiagramLayout layout, Map<DiagramElementId, SpecializedSvgNode> nodeIndex) {
        if (layout == null || layout.nodes().isEmpty()) {
            return defaultBounds();
        }
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        for (NodeLayout node : layout.nodes()) {
            if (!node.visible() || !nodeIndex.containsKey(node.elementId())) {
                continue;
            }
            minX = Math.min(minX, node.x());
            minY = Math.min(minY, node.y());
            maxX = Math.max(maxX, node.x() + node.width());
            maxY = Math.max(maxY, node.y() + node.height());
        }
        if (!Double.isFinite(minX)) {
            return defaultBounds();
        }
        return new SpecializedSvgBounds(minX, minY, maxX, maxY);
    }

    List<SpecializedSvgPoint> route(
            ConnectorLayout connector,
            NodeLayout source,
            NodeLayout target,
            double offsetX,
            double offsetY
    ) {
        if (source.elementId().equals(target.elementId()) && connector.bendPoints().isEmpty()) {
            return selfLoopRoute(source, offsetX, offsetY);
        }
        List<SpecializedSvgPoint> points = new ArrayList<>();
        SpecializedSvgPoint sourceCenter = new SpecializedSvgPoint(centerX(source) + offsetX, centerY(source) + offsetY);
        SpecializedSvgPoint targetCenter = new SpecializedSvgPoint(centerX(target) + offsetX, centerY(target) + offsetY);
        SpecializedSvgPoint firstTarget = connector.bendPoints().isEmpty()
                ? targetCenter
                : new SpecializedSvgPoint(connector.bendPoints().getFirst().x() + offsetX, connector.bendPoints().getFirst().y() + offsetY);
        points.add(edgePoint(source, firstTarget, offsetX, offsetY));
        for (BendPoint bendPoint : connector.bendPoints()) {
            points.add(new SpecializedSvgPoint(bendPoint.x() + offsetX, bendPoint.y() + offsetY));
        }
        SpecializedSvgPoint lastSource = connector.bendPoints().isEmpty()
                ? sourceCenter
                : new SpecializedSvgPoint(connector.bendPoints().getLast().x() + offsetX, connector.bendPoints().getLast().y() + offsetY);
        points.add(edgePoint(target, lastSource, offsetX, offsetY));
        return points;
    }

    SpecializedSvgPoint edgePoint(NodeLayout node, SpecializedSvgPoint target, double offsetX, double offsetY) {
        double x = node.x() + offsetX;
        double y = node.y() + offsetY;
        double halfWidth = Math.max(1.0, node.width() / 2.0);
        double halfHeight = Math.max(1.0, node.height() / 2.0);
        double cx = x + halfWidth;
        double cy = y + halfHeight;
        double dx = target.x() - cx;
        double dy = target.y() - cy;
        if (Math.abs(dx) < 0.0001 && Math.abs(dy) < 0.0001) {
            return new SpecializedSvgPoint(cx, cy);
        }
        double scale = Math.min(
                Math.abs(dx) < 0.0001 ? Double.POSITIVE_INFINITY : halfWidth / Math.abs(dx),
                Math.abs(dy) < 0.0001 ? Double.POSITIVE_INFINITY : halfHeight / Math.abs(dy));
        return new SpecializedSvgPoint(cx + dx * scale, cy + dy * scale);
    }

    List<SpecializedSvgPoint> selfLoopRoute(NodeLayout node, double offsetX, double offsetY) {
        SpecializedSvgSelfLoop loop = selfLoop(node, offsetX, offsetY);
        return List.of(loop.start(), loop.control1(), loop.control2(), loop.end());
    }

    SpecializedSvgSelfLoop selfLoop(NodeLayout node, double offsetX, double offsetY) {
        double x = node.x() + offsetX;
        double y = node.y() + offsetY;
        double w = node.width();
        return new SpecializedSvgSelfLoop(
                new SpecializedSvgPoint(x + w * 0.78, y + 2.0),
                new SpecializedSvgPoint(x + w + 58.0, y - 64.0),
                new SpecializedSvgPoint(x - 24.0, y - 64.0),
                new SpecializedSvgPoint(x + w * 0.22, y + 2.0));
    }

    SpecializedSvgPoint midPoint(List<SpecializedSvgPoint> route) {
        if (route.size() == 1) {
            return route.get(0);
        }
        double totalLength = 0.0;
        for (int index = 1; index < route.size(); index++) {
            totalLength += distance(route.get(index - 1), route.get(index));
        }
        if (totalLength <= 0.0) {
            return route.get(route.size() / 2);
        }
        double targetLength = totalLength / 2.0;
        double traversed = 0.0;
        for (int index = 1; index < route.size(); index++) {
            SpecializedSvgPoint previous = route.get(index - 1);
            SpecializedSvgPoint current = route.get(index);
            double segmentLength = distance(previous, current);
            if (segmentLength <= 0.0) {
                continue;
            }
            if (traversed + segmentLength >= targetLength) {
                double ratio = (targetLength - traversed) / segmentLength;
                return new SpecializedSvgPoint(
                        previous.x() + (current.x() - previous.x()) * ratio,
                        previous.y() + (current.y() - previous.y()) * ratio
                );
            }
            traversed += segmentLength;
        }
        return route.get(route.size() - 1);
    }

    private SpecializedSvgBounds defaultBounds() {
        return new SpecializedSvgBounds(
                0.0,
                0.0,
                SpecializedSvgConstants.MIN_WIDTH - SpecializedSvgConstants.MARGIN * 2.0,
                SpecializedSvgConstants.MIN_HEIGHT - SpecializedSvgConstants.MARGIN * 2.0);
    }

    private double distance(SpecializedSvgPoint first, SpecializedSvgPoint second) {
        double dx = second.x() - first.x();
        double dy = second.y() - first.y();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private double centerX(NodeLayout node) {
        return node.x() + node.width() / 2.0;
    }

    private double centerY(NodeLayout node) {
        return node.y() + node.height() / 2.0;
    }
}
