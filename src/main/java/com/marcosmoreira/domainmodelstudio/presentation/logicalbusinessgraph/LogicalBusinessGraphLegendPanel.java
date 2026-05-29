package com.marcosmoreira.domainmodelstudio.presentation.logicalbusinessgraph;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphRelationKind;
import java.util.Arrays;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/** Leyenda operativa de abreviaciones y relaciones del Grafo lógico del negocio. */
final class LogicalBusinessGraphLegendPanel {

    private final ScrollPane root = new ScrollPane(buildContent());

    LogicalBusinessGraphLegendPanel() {
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setPannable(true);
        root.getStyleClass().addAll("logical-business-graph-legend-scroll", "diagram-workbench-properties-scroll");
    }

    Parent root() {
        return root;
    }

    private static VBox buildContent() {
        VBox content = new VBox(12);
        content.setPadding(new Insets(10));
        content.getStyleClass().addAll("logical-business-graph-legend-panel", "diagram-workbench-panel-content");
        content.getChildren().add(sectionTitle("Abreviaciones de nodos"));
        Arrays.stream(LogicalBusinessGraphNodeKind.values())
                .map(kind -> card(kind.legendEntry(), kind.description()))
                .forEach(content.getChildren()::add);
        content.getChildren().add(sectionTitle("Relaciones semánticas"));
        Arrays.stream(LogicalBusinessGraphRelationKind.values())
                .map(kind -> card(kind.code(), kind.description()))
                .forEach(content.getChildren()::add);
        return content;
    }

    private static Label sectionTitle(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("logical-business-graph-section-title");
        label.setWrapText(true);
        return label;
    }

    private static VBox card(String title, String detail) {
        VBox box = new VBox(3);
        box.getStyleClass().add("logical-business-graph-legend-card");
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("logical-business-graph-legend-item-title");
        titleLabel.setWrapText(true);
        Label detailLabel = new Label(detail);
        detailLabel.getStyleClass().add("logical-business-graph-legend-item-detail");
        detailLabel.setWrapText(true);
        box.getChildren().addAll(titleLabel, detailLabel);
        return box;
    }
}
