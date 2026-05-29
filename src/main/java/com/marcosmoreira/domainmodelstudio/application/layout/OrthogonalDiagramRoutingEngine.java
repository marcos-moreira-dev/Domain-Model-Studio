package com.marcosmoreira.domainmodelstudio.application.layout;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.layout.AnchorSide;
import com.marcosmoreira.domainmodelstudio.domain.layout.BendPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Ruteador ortogonal liviano para reducir líneas diagonales y cruces obvios.
 *
 * <p>No intenta resolver el problema general de diagramación automática. Produce una ruta
 * inicial legible, con pocos codos y evitando obstáculos cuando el caso es sencillo.</p>
 */
public final class OrthogonalDiagramRoutingEngine {

    public List<BendPoint> route(
            NodeLayout source,
            NodeLayout target,
            AnchorSide sourceAnchor,
            AnchorSide targetAnchor,
            DiagramObstacleMap obstacleMap,
            AutoLayoutProfile profile
    ) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(target, "target");
        DiagramObstacleMap obstacles = obstacleMap == null ? DiagramObstacleMap.fromNodes(List.of()) : obstacleMap;
        AutoLayoutProfile resolvedProfile = profile == null ? AutoLayoutProfile.chen(1) : profile;
        AnchorSide resolvedSourceAnchor = sourceAnchor == null || sourceAnchor == AnchorSide.AUTO
                ? LayoutAnchorResolver.sideFacing(source, target)
                : sourceAnchor;
        AnchorSide resolvedTargetAnchor = targetAnchor == null || targetAnchor == AnchorSide.AUTO
                ? LayoutAnchorResolver.sideFacing(target, source)
                : targetAnchor;
        DiagramPoint start = LayoutAnchorResolver.anchorPoint(source, resolvedSourceAnchor, target);
        DiagramPoint end = LayoutAnchorResolver.anchorPoint(target, resolvedTargetAnchor, source);
        Set<DiagramElementId> excluded = new LinkedHashSet<>();
        excluded.add(source.elementId());
        excluded.add(target.elementId());

        if (isAligned(start, end) && !routeCrosses(obstacles, List.of(start, end), excluded, resolvedProfile.nodeMargin())) {
            return List.of();
        }

        List<DiagramPoint> horizontalThenVertical = deduplicate(List.of(
                start,
                DiagramPoint.of(end.x(), start.y()),
                end
        ));
        if (!routeCrosses(obstacles, horizontalThenVertical, excluded, resolvedProfile.nodeMargin())) {
            return bendPointsFromRoute(horizontalThenVertical);
        }

        List<DiagramPoint> verticalThenHorizontal = deduplicate(List.of(
                start,
                DiagramPoint.of(start.x(), end.y()),
                end
        ));
        if (!routeCrosses(obstacles, verticalThenHorizontal, excluded, resolvedProfile.nodeMargin())) {
            return bendPointsFromRoute(verticalThenHorizontal);
        }

        List<DiagramPoint> midXRoute = deduplicate(List.of(
                start,
                DiagramPoint.of(midXOutside(source, target, resolvedProfile.routeMargin()), start.y()),
                DiagramPoint.of(midXOutside(source, target, resolvedProfile.routeMargin()), end.y()),
                end
        ));
        if (!routeCrosses(obstacles, midXRoute, excluded, resolvedProfile.nodeMargin())) {
            return bendPointsFromRoute(midXRoute);
        }

        double upperY = Math.min(source.y(), target.y()) - Math.max(36.0, resolvedProfile.routeMargin());
        List<DiagramPoint> upperRoute = deduplicate(List.of(
                start,
                DiagramPoint.of(start.x(), upperY),
                DiagramPoint.of(end.x(), upperY),
                end
        ));
        if (!routeCrosses(obstacles, upperRoute, excluded, resolvedProfile.nodeMargin())) {
            return bendPointsFromRoute(upperRoute);
        }

        double lowerY = Math.max(source.y() + source.height(), target.y() + target.height())
                + Math.max(36.0, resolvedProfile.routeMargin());
        List<DiagramPoint> lowerRoute = deduplicate(List.of(
                start,
                DiagramPoint.of(start.x(), lowerY),
                DiagramPoint.of(end.x(), lowerY),
                end
        ));
        if (!routeCrosses(obstacles, lowerRoute, excluded, resolvedProfile.nodeMargin())) {
            return bendPointsFromRoute(lowerRoute);
        }

        double midY = (start.y() + end.y()) / 2.0;
        List<DiagramPoint> fallback = deduplicate(List.of(
                start,
                DiagramPoint.of(start.x(), midY),
                DiagramPoint.of(end.x(), midY),
                end
        ));
        return bendPointsFromRoute(fallback);
    }

    private double midXOutside(NodeLayout source, NodeLayout target, double routeMargin) {
        double sourceRight = source.x() + source.width();
        double targetRight = target.x() + target.width();
        if (sourceRight < target.x()) {
            return (sourceRight + target.x()) / 2.0;
        }
        if (targetRight < source.x()) {
            return (targetRight + source.x()) / 2.0;
        }
        return Math.max(sourceRight, targetRight) + Math.max(24.0, routeMargin);
    }

    private boolean isAligned(DiagramPoint start, DiagramPoint end) {
        return Math.abs(start.x() - end.x()) <= 0.001 || Math.abs(start.y() - end.y()) <= 0.001;
    }

    private boolean routeCrosses(
            DiagramObstacleMap obstacleMap,
            List<DiagramPoint> route,
            Set<DiagramElementId> excludedIds,
            double margin
    ) {
        return obstacleMap.routeCrossesObstacle(segments(route), excludedIds, margin);
    }

    private List<DiagramPointPair> segments(List<DiagramPoint> route) {
        List<DiagramPointPair> segments = new ArrayList<>();
        for (int index = 0; index < route.size() - 1; index++) {
            segments.add(new DiagramPointPair(route.get(index), route.get(index + 1)));
        }
        return segments;
    }

    private List<BendPoint> bendPointsFromRoute(List<DiagramPoint> route) {
        if (route.size() <= 2) {
            return List.of();
        }
        List<BendPoint> bendPoints = new ArrayList<>();
        for (int index = 1; index < route.size() - 1; index++) {
            DiagramPoint point = route.get(index);
            bendPoints.add(BendPoint.of(point.x(), point.y()));
        }
        return bendPoints;
    }

    private List<DiagramPoint> deduplicate(List<DiagramPoint> route) {
        List<DiagramPoint> cleaned = new ArrayList<>();
        for (DiagramPoint point : route) {
            if (cleaned.isEmpty()) {
                cleaned.add(point);
                continue;
            }
            DiagramPoint previous = cleaned.get(cleaned.size() - 1);
            if (Math.abs(previous.x() - point.x()) > 0.001 || Math.abs(previous.y() - point.y()) > 0.001) {
                cleaned.add(point);
            }
        }
        return cleaned;
    }
}
