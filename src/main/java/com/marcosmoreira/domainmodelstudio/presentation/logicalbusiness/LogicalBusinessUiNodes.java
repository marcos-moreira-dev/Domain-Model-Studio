package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Pequeñas utilidades visuales para mantener los paneles del levantamiento legibles. */
final class LogicalBusinessUiNodes {

    private LogicalBusinessUiNodes() {
    }

    static VBox panelRoot() {
        VBox root = new VBox(8);
        root.setPadding(new Insets(10));
        root.getStyleClass().add("logical-business-side-panel");
        return root;
    }

    static Label title(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("logical-business-panel-title");
        label.setWrapText(true);
        return label;
    }

    static Label subtitle(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("logical-business-panel-subtitle");
        label.setWrapText(true);
        return label;
    }

    static Label text(String text) {
        Label label = new Label(LogicalBusinessDisplayText.clean(text));
        label.setWrapText(true);
        label.getStyleClass().add("logical-business-text");
        return label;
    }

    static Label meta(String label, String value) {
        Label node = new Label(label + ": " + LogicalBusinessDisplayText.clean(value));
        node.setWrapText(true);
        node.getStyleClass().add("logical-business-meta");
        return node;
    }


    static VBox inspectorCard(String title, String detail, String... extraStyleClasses) {
        VBox card = new VBox(6);
        card.getStyleClass().add("logical-business-inspector-card");
        if (extraStyleClasses != null) {
            for (String styleClass : extraStyleClasses) {
                if (styleClass != null && !styleClass.isBlank()) {
                    card.getStyleClass().add(styleClass);
                }
            }
        }
        card.getChildren().add(subtitle(title));
        if (detail != null && !detail.isBlank()) {
            card.getChildren().add(text(detail));
        }
        return card;
    }

    static Label compactMeta(String text) {
        Label label = new Label(LogicalBusinessDisplayText.clean(text));
        label.setWrapText(true);
        label.getStyleClass().add("logical-business-compact-meta");
        return label;
    }

    static void grow(Node node) {
        VBox.setVgrow(node, Priority.ALWAYS);
    }
}
