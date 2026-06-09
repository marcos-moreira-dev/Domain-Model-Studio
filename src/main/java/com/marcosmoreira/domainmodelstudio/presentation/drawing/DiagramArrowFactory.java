package com.marcosmoreira.domainmodelstudio.presentation.drawing;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.Polygon;

/** Fábrica de puntas de conectores. */
public final class DiagramArrowFactory {

    public static final double DEFAULT_LENGTH = 16.0;
    public static final double DEFAULT_WIDTH = 11.0;

    public Node create(Point2D previous, Point2D end, DiagramConnectorStyle style) {
        DiagramConnectorStyle safeStyle = style == null ? DiagramConnectorStyle.directed("", false) : style;
        if (safeStyle.arrowKind() == DiagramArrowKind.NONE || previous == null || end == null) {
            return new Group();
        }
        Polygon polygon = switch (safeStyle.arrowKind()) {
            case OPEN, FILLED_TRIANGLE, HOLLOW_TRIANGLE -> triangle(previous, end, DEFAULT_LENGTH, DEFAULT_WIDTH);
            case FILLED_DIAMOND, HOLLOW_DIAMOND -> diamond(previous, end, DEFAULT_LENGTH + 5.0, DEFAULT_WIDTH + 2.0);
            case NONE -> new Polygon();
        };
        polygon.getStyleClass().addAll(safeStyle.arrowStyleClasses());
        if (safeStyle.arrowKind() == DiagramArrowKind.HOLLOW_TRIANGLE
                || safeStyle.arrowKind() == DiagramArrowKind.HOLLOW_DIAMOND
                || safeStyle.arrowKind() == DiagramArrowKind.OPEN) {
            polygon.getStyleClass().add("diagram-connector-arrow-hollow");
        }
        return polygon;
    }

    public Polygon triangle(Point2D previous, Point2D end, double length, double width) {
        double angle = Math.atan2(end.getY() - previous.getY(), end.getX() - previous.getX());
        double backX = end.getX() - positive(length, "length") * Math.cos(angle);
        double backY = end.getY() - positive(length, "length") * Math.sin(angle);
        double normalX = Math.cos(angle + Math.PI / 2.0) * positive(width, "width") / 2.0;
        double normalY = Math.sin(angle + Math.PI / 2.0) * positive(width, "width") / 2.0;
        return new Polygon(
                end.getX(), end.getY(),
                backX + normalX, backY + normalY,
                backX - normalX, backY - normalY
        );
    }

    public Polygon diamond(Point2D previous, Point2D end, double length, double width) {
        double angle = Math.atan2(end.getY() - previous.getY(), end.getX() - previous.getX());
        double halfLength = positive(length, "length") / 2.0;
        double halfWidth = positive(width, "width") / 2.0;
        double centerX = end.getX() - halfLength * Math.cos(angle);
        double centerY = end.getY() - halfLength * Math.sin(angle);
        double normalX = Math.cos(angle + Math.PI / 2.0) * halfWidth;
        double normalY = Math.sin(angle + Math.PI / 2.0) * halfWidth;
        double tailX = end.getX() - length * Math.cos(angle);
        double tailY = end.getY() - length * Math.sin(angle);
        return new Polygon(
                end.getX(), end.getY(),
                centerX + normalX, centerY + normalY,
                tailX, tailY,
                centerX - normalX, centerY - normalY
        );
    }

    private static double positive(double value, String fieldName) {
        if (!Double.isFinite(value) || value <= 0.0) {
            throw new IllegalArgumentException(fieldName + " debe ser positivo y finito.");
        }
        return value;
    }
}
