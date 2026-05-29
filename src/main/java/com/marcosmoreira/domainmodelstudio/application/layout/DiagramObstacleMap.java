package com.marcosmoreira.domainmodelstudio.application.layout;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** Índice sencillo de obstáculos visuales para ruteo ortogonal. */
public final class DiagramObstacleMap {

    private final Map<DiagramElementId, DiagramNodeBounds> boundsById;

    private DiagramObstacleMap(List<DiagramNodeBounds> bounds) {
        Map<DiagramElementId, DiagramNodeBounds> index = new LinkedHashMap<>();
        for (DiagramNodeBounds nodeBounds : bounds) {
            index.put(nodeBounds.elementId(), nodeBounds);
        }
        this.boundsById = Map.copyOf(index);
    }

    public static DiagramObstacleMap fromNodes(List<NodeLayout> nodes) {
        List<DiagramNodeBounds> bounds = (nodes == null ? List.<NodeLayout>of() : nodes).stream()
                .filter(NodeLayout::visible)
                .map(DiagramNodeBounds::from)
                .toList();
        return new DiagramObstacleMap(bounds);
    }

    public List<DiagramNodeBounds> boundsExcluding(Set<DiagramElementId> excludedIds) {
        Set<DiagramElementId> exclusions = excludedIds == null ? Set.of() : excludedIds;
        return boundsById.values().stream()
                .filter(bounds -> !exclusions.contains(bounds.elementId()))
                .toList();
    }

    public boolean routeCrossesObstacle(List<DiagramPointPair> segments, Set<DiagramElementId> excludedIds, double margin) {
        Objects.requireNonNull(segments, "segments");
        for (DiagramPointPair segment : segments) {
            if (segment.isHorizontal()) {
                if (horizontalSegmentCrossesObstacle(segment.start().x(), segment.end().x(), segment.start().y(), excludedIds, margin)) {
                    return true;
                }
            } else if (segment.isVertical()) {
                if (verticalSegmentCrossesObstacle(segment.start().x(), segment.start().y(), segment.end().y(), excludedIds, margin)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean horizontalSegmentCrossesObstacle(
            double x1,
            double x2,
            double y,
            Set<DiagramElementId> excludedIds,
            double margin
    ) {
        for (DiagramNodeBounds bounds : boundsExcluding(excludedIds)) {
            if (bounds.intersectsHorizontalSegment(x1, x2, y, margin)) {
                return true;
            }
        }
        return false;
    }

    public boolean verticalSegmentCrossesObstacle(
            double x,
            double y1,
            double y2,
            Set<DiagramElementId> excludedIds,
            double margin
    ) {
        for (DiagramNodeBounds bounds : boundsExcluding(excludedIds)) {
            if (bounds.intersectsVerticalSegment(x, y1, y2, margin)) {
                return true;
            }
        }
        return false;
    }
}
