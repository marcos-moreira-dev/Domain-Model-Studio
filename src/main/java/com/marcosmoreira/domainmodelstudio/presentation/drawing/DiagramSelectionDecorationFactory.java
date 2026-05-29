package com.marcosmoreira.domainmodelstudio.presentation.drawing;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/** Decoraciones comunes para selección, handles y puntos intermedios. */
public final class DiagramSelectionDecorationFactory {

    public Rectangle selectionHalo(double x, double y, double width, double height) {
        Rectangle halo = new Rectangle(x, y, positive(width, "width"), positive(height, "height"));
        halo.getStyleClass().add(DiagramPalette.SELECTION_HALO);
        halo.setManaged(false);
        return halo;
    }

    public Group resizeHandles(double x, double y, double width, double height) {
        Group group = new Group();
        group.getChildren().addAll(handle(x, y), handle(x + width, y), handle(x, y + height), handle(x + width, y + height));
        return group;
    }

    public List<Circle> bendPointHandles(List<Point2D> points) {
        List<Circle> handles = new ArrayList<>();
        if (points == null) {
            return handles;
        }
        for (Point2D point : points) {
            handles.add(handle(point.getX(), point.getY()));
        }
        return handles;
    }

    public Circle handle(double centerX, double centerY) {
        Circle handle = new Circle(centerX, centerY, 4.5);
        handle.getStyleClass().add(DiagramPalette.HANDLE);
        handle.setManaged(false);
        return handle;
    }

    private static double positive(double value, String fieldName) {
        if (!Double.isFinite(value) || value <= 0.0) {
            throw new IllegalArgumentException(fieldName + " debe ser positivo y finito.");
        }
        return value;
    }
}
