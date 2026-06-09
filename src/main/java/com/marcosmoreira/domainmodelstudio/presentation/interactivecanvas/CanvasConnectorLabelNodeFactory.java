package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/** Crea etiquetas visuales comunes para conectores del canvas interactivo y su exportación. */
final class CanvasConnectorLabelNodeFactory {

    private static final double LABEL_CHAR_WIDTH_ESTIMATE = 6.8;
    private static final double LABEL_HORIZONTAL_PADDING = 18.0;
    private static final double LABEL_HEIGHT_ESTIMATE = 24.0;

    private CanvasConnectorLabelNodeFactory() {
        // Utilidad estática.
    }

    static Label createOverlayLabel(String text, Point2D anchor, String kind, boolean selected) {
        Label label = createBaseLabel(text, anchor, kind);
        if (selected) {
            label.getStyleClass().add("interactive-canvas-connector-label-selected");
        }
        return label;
    }

    static Label createExportLabel(String text, Point2D anchor, String kind) {
        Label label = createBaseLabel(text, anchor, kind);
        label.getStyleClass().add("interactive-canvas-connector-label-export");
        label.setMouseTransparent(true);
        return label;
    }

    private static Label createBaseLabel(String text, Point2D anchor, String kind) {
        String safeText = text == null ? "" : text;
        Point2D safeAnchor = anchor == null ? Point2D.ZERO : anchor;
        Label label = new Label(safeText);
        label.getStyleClass().add("interactive-canvas-connector-label");
        String safeKind = kind == null || kind.isBlank() ? "generic" : kind.strip();
        label.getStyleClass().add("interactive-canvas-connector-label-" + safeKind);
        label.setManaged(false);
        label.setVisible(true);
        label.setOpacity(1.0);
        label.setMouseTransparent(false);
        label.setPickOnBounds(true);
        label.setViewOrder(-1000.0);
        label.setStyle("-fx-background-color: rgba(255,255,255,0.99);"
                + "-fx-background-radius: 4;"
                + "-fx-border-color: #111827;"
                + "-fx-border-radius: 4;"
                + "-fx-border-width: 1.1;"
                + "-fx-text-fill: #111827;"
                + "-fx-font-size: 12px;"
                + "-fx-font-weight: 700;"
                + "-fx-padding: 3 7;"
                + "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.30), 6, 0.18, 0, 1);");
        label.autosize();
        double width = measuredLabelWidth(label, safeText);
        double height = measuredLabelHeight(label);
        label.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        label.setPrefSize(width, height);
        label.relocate(safeAnchor.getX() - width / 2.0, safeAnchor.getY() - height / 2.0);
        label.toFront();
        return label;
    }

    private static double measuredLabelWidth(Label label, String text) {
        double visualWidth = label == null ? 0.0 : label.prefWidth(-1.0);
        if (Double.isFinite(visualWidth) && visualWidth > 0.0) {
            return visualWidth;
        }
        return estimatedLabelWidth(text);
    }

    private static double measuredLabelHeight(Label label) {
        double visualHeight = label == null ? 0.0 : label.prefHeight(-1.0);
        if (Double.isFinite(visualHeight) && visualHeight > 0.0) {
            return visualHeight;
        }
        return LABEL_HEIGHT_ESTIMATE;
    }

    private static double estimatedLabelWidth(String text) {
        String normalized = text == null ? "" : text.strip();
        if (normalized.isBlank()) {
            return LABEL_HORIZONTAL_PADDING;
        }
        return Math.max(48.0, normalized.length() * LABEL_CHAR_WIDTH_ESTIMATE + LABEL_HORIZONTAL_PADDING);
    }
}
