package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.style.ElementStyle;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;

/** Aplica estilos explícitos de proyecto sobre nodos JavaFX ya renderizados. */
public final class CanvasStyleApplier {

    private CanvasStyleApplier() {
    }

    public static void applyNodeStyle(Node node, ElementStyle style) {
        if (node == null || style == null) {
            return;
        }
        applyRecursive(node, style, StyleMode.NODE);
    }

    public static void applyConnectorStyle(Node node, ElementStyle style) {
        if (node == null || style == null) {
            return;
        }
        applyRecursive(node, style, StyleMode.CONNECTOR);
    }

    public static void applyTextStyle(Node node, ElementStyle style) {
        if (node == null || style == null) {
            return;
        }
        if (node instanceof Labeled labeled) {
            labeled.setTextFill(Color.web(style.text().color().toHex()));
        }
    }

    private static void applyRecursive(Node node, ElementStyle style, StyleMode mode) {
        if (skip(node)) {
            return;
        }
        if (node instanceof Shape shape) {
            applyShape(shape, style, mode);
        }
        if (mode == StyleMode.NODE && node instanceof Region region && !(node instanceof Labeled)) {
            applyRegion(region, style);
        }
        if (node instanceof Labeled labeled) {
            labeled.setTextFill(Color.web(style.text().color().toHex()));
        }
        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                applyRecursive(child, style, mode);
            }
        }
    }

    private static void applyShape(Shape shape, ElementStyle style, StyleMode mode) {
        Color strokeColor = Color.web(style.stroke().color().toHex());
        shape.setStroke(strokeColor);
        shape.setStrokeWidth(style.stroke().width());
        if (mode == StyleMode.NODE) {
            shape.setStyle(shapeStyle(style, style.fill().color().toHex()));
            shape.setFill(Color.web(style.fill().color().toHex()));
            return;
        }
        if (shape instanceof Line || shape instanceof Polyline) {
            shape.setStyle(shapeStyle(style, "transparent"));
            shape.setFill(Color.TRANSPARENT);
            return;
        }
        shape.setStyle(shapeStyle(style, style.stroke().color().toHex()));
        shape.setFill(strokeColor);
    }

    private static void applyRegion(Region region, ElementStyle style) {
        Color fill = Color.web(style.fill().color().toHex());
        Color stroke = Color.web(style.stroke().color().toHex());
        double width = style.stroke().width();
        CornerRadii radii = CornerRadii.EMPTY;
        region.setBackground(new Background(new BackgroundFill(fill, radii, null)));
        region.setBorder(new Border(new BorderStroke(
                stroke,
                BorderStrokeStyle.SOLID,
                radii,
                new BorderWidths(width)
        )));
        region.setStyle(regionStyle(style));
    }

    private static String regionStyle(ElementStyle style) {
        return "-fx-background-color: " + style.fill().color().toHex() + ";"
                + " -fx-background-radius: 0;"
                + " -fx-border-color: " + style.stroke().color().toHex() + ";"
                + " -fx-border-width: " + style.stroke().width() + ";"
                + " -fx-border-radius: 0;";
    }

    private static String shapeStyle(ElementStyle style, String fill) {
        String stroke = style.stroke().color().toHex();
        return "-fx-fill: " + fill + ";"
                + " -fx-stroke: " + stroke + ";"
                + " -fx-stroke-width: " + style.stroke().width() + ";";
    }

    private static boolean skip(Node node) {
        return node.getStyleClass().stream().anyMatch(styleClass ->
                styleClass.contains("selection")
                        || styleClass.contains("bend-point")
                        || styleClass.contains("handle"));
    }

    private enum StyleMode {
        NODE,
        CONNECTOR
    }
}
