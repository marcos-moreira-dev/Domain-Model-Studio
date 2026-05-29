package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;

/**
 * Aplica un resaltado transversal fuerte a relaciones seleccionadas.
 *
 * <p>Los render kits de cada familia pueden declarar estilos propios, pero la
 * selección debe verse igual en todos los diagramas y debe ganar sobre estilos
 * acumulados por tipo. Por eso se aplica al final del render, después de estilos
 * explícitos del proyecto.</p>
 */
public final class CanvasConnectorSelectionVisualSupport {

    private static final String SELECTED_ROOT = "interactive-canvas-connector-selected-root";
    private static final String SELECTED_LINE = "interactive-canvas-connector-selected";
    private static final String SELECTED_ARROW = "interactive-canvas-arrow-head-selected";

    private CanvasConnectorSelectionVisualSupport() {
    }

    public static void apply(Node node) {
        if (node == null) {
            return;
        }
        addStyleClass(node, SELECTED_ROOT);
        applyRecursively(node);
    }

    private static void applyRecursively(Node node) {
        if (node == null || skip(node)) {
            return;
        }
        if (node instanceof Line || node instanceof Polyline) {
            addStyleClass(node, SELECTED_LINE);
            appendStyle(node, "-fx-stroke: -dms-accent; -fx-stroke-width: 3.0; -fx-fill: transparent;");
        } else if (node instanceof Shape shape) {
            addStyleClass(shape, SELECTED_ARROW);
            String fill = shape.getStyleClass().contains("diagram-connector-arrow-hollow")
                    ? "-dms-bg-workspace"
                    : "-dms-accent-soft";
            appendStyle(shape, "-fx-stroke: -dms-accent; -fx-stroke-width: 2.0; -fx-fill: " + fill + ";");
        }
        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                applyRecursively(child);
            }
        }
    }

    private static boolean skip(Node node) {
        return node.getStyleClass().stream().anyMatch(styleClass ->
                styleClass.contains("hitbox")
                        || styleClass.contains("bend-point")
                        || styleClass.contains("handle")
                        || styleClass.contains("label"));
    }

    private static void addStyleClass(Node node, String styleClass) {
        if (!node.getStyleClass().contains(styleClass)) {
            node.getStyleClass().add(styleClass);
        }
    }

    private static void appendStyle(Node node, String style) {
        String current = node.getStyle() == null ? "" : node.getStyle().strip();
        node.setStyle(current.isBlank() ? style : current + " " + style);
    }
}
