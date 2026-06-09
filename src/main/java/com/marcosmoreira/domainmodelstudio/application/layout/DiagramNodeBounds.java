package com.marcosmoreira.domainmodelstudio.application.layout;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.Objects;

/** Bounds geométricos de un nodo para layout y ruteo. */
public record DiagramNodeBounds(
        DiagramElementId elementId,
        double x,
        double y,
        double width,
        double height
) {

    public DiagramNodeBounds {
        Objects.requireNonNull(elementId, "elementId");
        if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(width) || !Double.isFinite(height)) {
            throw new IllegalArgumentException("Los bounds del nodo deben ser finitos");
        }
        if (width <= 0.0 || height <= 0.0) {
            throw new IllegalArgumentException("El tamaño del nodo debe ser positivo");
        }
    }

    public static DiagramNodeBounds from(NodeLayout nodeLayout) {
        Objects.requireNonNull(nodeLayout, "nodeLayout");
        return new DiagramNodeBounds(
                nodeLayout.elementId(),
                nodeLayout.x(),
                nodeLayout.y(),
                nodeLayout.width(),
                nodeLayout.height()
        );
    }

    public double right() {
        return x + width;
    }

    public double bottom() {
        return y + height;
    }

    public double centerX() {
        return x + width / 2.0;
    }

    public double centerY() {
        return y + height / 2.0;
    }

    public DiagramPoint center() {
        return DiagramPoint.of(centerX(), centerY());
    }

    public boolean overlaps(DiagramNodeBounds other, double margin) {
        Objects.requireNonNull(other, "other");
        double safeMargin = Math.max(0.0, margin);
        return x - safeMargin < other.right()
                && right() + safeMargin > other.x()
                && y - safeMargin < other.bottom()
                && bottom() + safeMargin > other.y();
    }

    public boolean intersectsHorizontalSegment(double x1, double x2, double segmentY, double margin) {
        double safeMargin = Math.max(0.0, margin);
        double minX = Math.min(x1, x2);
        double maxX = Math.max(x1, x2);
        return segmentY >= y - safeMargin
                && segmentY <= bottom() + safeMargin
                && maxX >= x - safeMargin
                && minX <= right() + safeMargin;
    }

    public boolean intersectsVerticalSegment(double segmentX, double y1, double y2, double margin) {
        double safeMargin = Math.max(0.0, margin);
        double minY = Math.min(y1, y2);
        double maxY = Math.max(y1, y2);
        return segmentX >= x - safeMargin
                && segmentX <= right() + safeMargin
                && maxY >= y - safeMargin
                && minY <= bottom() + safeMargin;
    }
}
