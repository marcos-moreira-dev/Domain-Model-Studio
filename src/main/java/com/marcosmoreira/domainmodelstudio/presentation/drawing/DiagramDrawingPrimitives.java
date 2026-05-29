package com.marcosmoreira.domainmodelstudio.presentation.drawing;

import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/** Fábrica de primitivas vectoriales básicas para render kits. */
public final class DiagramDrawingPrimitives {

    public Rectangle rectangle(double x, double y, double width, double height, DiagramShapeStyle style) {
        Rectangle rectangle = new Rectangle(x, y, positive(width, "width"), positive(height, "height"));
        applyShapeStyle(rectangle, style);
        return rectangle;
    }

    public Rectangle roundedRectangle(double x, double y, double width, double height, DiagramShapeStyle style) {
        Rectangle rectangle = rectangle(x, y, width, height, style);
        DiagramShapeStyle safeStyle = style == null ? DiagramShapeStyle.node("", false) : style;
        rectangle.setArcWidth(safeStyle.arcWidth());
        rectangle.setArcHeight(safeStyle.arcHeight());
        return rectangle;
    }

    public Ellipse ellipse(double centerX, double centerY, double radiusX, double radiusY, DiagramShapeStyle style) {
        Ellipse ellipse = new Ellipse(centerX, centerY, positive(radiusX, "radiusX"), positive(radiusY, "radiusY"));
        applyShapeStyle(ellipse, style);
        return ellipse;
    }

    public Polygon diamond(double centerX, double centerY, double width, double height, DiagramShapeStyle style) {
        double halfWidth = positive(width, "width") / 2.0;
        double halfHeight = positive(height, "height") / 2.0;
        Polygon polygon = new Polygon(
                centerX, centerY - halfHeight,
                centerX + halfWidth, centerY,
                centerX, centerY + halfHeight,
                centerX - halfWidth, centerY
        );
        applyShapeStyle(polygon, style);
        return polygon;
    }

    public Polygon packageTab(double x, double y, double width, double height, DiagramShapeStyle style) {
        return packageTab(x, y, width, height, style, Math.min(positive(width, "width") * 0.42, 96.0));
    }

    public Polygon packageTab(double x, double y, double width, double height, DiagramShapeStyle style, double preferredTabWidth) {
        double safeWidth = positive(width, "width");
        double safeHeight = positive(height, "height");
        double tabWidth = Math.min(Math.max(96.0, preferredTabWidth), Math.max(108.0, safeWidth - 24.0));
        double tabHeight = Math.min(safeHeight * 0.22, 26.0);
        Polygon polygon = new Polygon(
                x, y,
                x + tabWidth, y,
                x + tabWidth + 12.0, y + tabHeight,
                x + safeWidth, y + tabHeight,
                x + safeWidth, y + safeHeight,
                x, y + safeHeight
        );
        applyShapeStyle(polygon, style);
        return polygon;
    }

    private static void applyShapeStyle(javafx.scene.shape.Shape shape, DiagramShapeStyle style) {
        DiagramShapeStyle safeStyle = style == null ? DiagramShapeStyle.node("", false) : style;
        shape.getStyleClass().addAll(safeStyle.styleClasses());
    }

    private static double positive(double value, String fieldName) {
        if (!Double.isFinite(value) || value <= 0.0) {
            throw new IllegalArgumentException(fieldName + " debe ser positivo y finito.");
        }
        return value;
    }
}
