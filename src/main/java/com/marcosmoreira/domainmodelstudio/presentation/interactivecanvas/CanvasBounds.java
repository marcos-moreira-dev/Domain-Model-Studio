package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;

/**
 * Rectángulo geométrico independiente de JavaFX para selección y exportación.
 */
public record CanvasBounds(double x, double y, double width, double height) {

    public CanvasBounds {
        ensureFinite(x, "x");
        ensureFinite(y, "y");
        ensurePositive(width, "width");
        ensurePositive(height, "height");
    }

    public static CanvasBounds of(double x, double y, double width, double height) {
        return new CanvasBounds(x, y, width, height);
    }

    public static CanvasBounds from(NodeLayout layout) {
        return new CanvasBounds(layout.x(), layout.y(), layout.width(), layout.height());
    }

    public static CanvasBounds union(CanvasBounds first, CanvasBounds second) {
        if (first == null) {
            return second;
        }
        if (second == null) {
            return first;
        }
        double minX = Math.min(first.x, second.x);
        double minY = Math.min(first.y, second.y);
        double maxX = Math.max(first.right(), second.right());
        double maxY = Math.max(first.bottom(), second.bottom());
        return new CanvasBounds(minX, minY, maxX - minX, maxY - minY);
    }

    public CanvasBounds expandedBy(double padding) {
        double safePadding = Double.isFinite(padding) && padding > 0.0 ? padding : 0.0;
        return new CanvasBounds(x - safePadding, y - safePadding, width + safePadding * 2.0, height + safePadding * 2.0);
    }

    public static CanvasBounds between(double startX, double startY, double endX, double endY) {
        double minX = Math.min(startX, endX);
        double minY = Math.min(startY, endY);
        double width = Math.max(1.0, Math.abs(endX - startX));
        double height = Math.max(1.0, Math.abs(endY - startY));
        return new CanvasBounds(minX, minY, width, height);
    }

    public double centerX() {
        return x + width / 2.0;
    }

    public double centerY() {
        return y + height / 2.0;
    }

    public boolean contains(double pointX, double pointY) {
        return pointX >= x && pointX <= right() && pointY >= y && pointY <= bottom();
    }

    public boolean intersects(CanvasBounds other) {
        return x < other.right() && right() > other.x && y < other.bottom() && bottom() > other.y;
    }

    public double right() {
        return x + width;
    }

    public double bottom() {
        return y + height;
    }

    private static void ensureFinite(double value, String name) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("La coordenada " + name + " debe ser finita");
        }
    }

    private static void ensurePositive(double value, String name) {
        ensureFinite(value, name);
        if (value <= 0.0) {
            throw new IllegalArgumentException("La dimensión " + name + " debe ser mayor que cero");
        }
    }
}
