package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Utilidades de composición para inspectores y paneles laterales del workbench. */
public final class WorkbenchPanelSupport {

    private WorkbenchPanelSupport() {
    }

    public static void configurePropertiesScroll(ScrollPane scrollPane, String localStyleClass) {
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPannable(true);
        scrollPane.getStyleClass().addAll(localStyleClass, "diagram-workbench-properties-scroll");
    }

    public static VBox section(String localSectionClass, String localTitleClass, String title) {
        VBox box = new VBox(8);
        box.getStyleClass().addAll(localSectionClass, "diagram-workbench-property-section");
        Label label = new Label(title);
        label.getStyleClass().addAll(localTitleClass, "diagram-workbench-section-title");
        label.setWrapText(true);
        label.setTooltip(new Tooltip(title));
        box.getChildren().add(label);
        return box;
    }


    public static VBox focusCard(String title, String detail) {
        VBox box = new VBox(4);
        box.getStyleClass().addAll("diagram-workbench-focus-card", "diagram-workbench-side-card");
        box.setPadding(new Insets(7, 8, 7, 8));
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("diagram-workbench-focus-title");
        titleLabel.setWrapText(true);
        Label detailLabel = new Label(detail);
        detailLabel.getStyleClass().add("diagram-workbench-focus-detail");
        detailLabel.setWrapText(true);
        box.getChildren().addAll(titleLabel, detailLabel);
        return box;
    }

    public static VBox emptySelectionCard(String targetDescription) {
        return focusCard("Sin selección", "Selecciona " + targetDescription + " para revisar y editar sus propiedades.");
    }

    public static GridPane grid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setMinWidth(92.0);
        labelColumn.setPrefWidth(126.0);
        labelColumn.setMaxWidth(154.0);

        ColumnConstraints valueColumn = new ColumnConstraints();
        valueColumn.setHgrow(Priority.ALWAYS);
        valueColumn.setFillWidth(true);

        grid.getColumnConstraints().setAll(labelColumn, valueColumn);
        return grid;
    }

    public static void addRow(GridPane grid, int row, String localLabelClass, String labelText, Node input) {
        Label label = new Label(labelText);
        label.getStyleClass().addAll(localLabelClass, "diagram-workbench-field-label");
        label.setWrapText(true);
        label.setTooltip(new Tooltip(labelText));
        label.setMaxWidth(154.0);

        grid.add(label, 0, row);
        grid.add(input, 1, row);
        GridPane.setValignment(label, VPos.TOP);
        GridPane.setHgrow(input, Priority.ALWAYS);
        configureInputWidth(input);
    }

    public static Button button(String localButtonClass, String text, String tooltip) {
        Button button = new Button(text);
        button.getStyleClass().addAll(localButtonClass, "diagram-workbench-action-button");
        button.setTooltip(new Tooltip(tooltip));
        return button;
    }

    public static void configureArea(TextArea area, int rows) {
        area.setPrefRowCount(rows);
        area.setWrapText(true);
        area.setMaxWidth(Double.MAX_VALUE);
    }

    private static void configureInputWidth(Node input) {
        if (input instanceof TextField textField) {
            textField.setMaxWidth(Double.MAX_VALUE);
            return;
        }
        if (input instanceof TextArea textArea) {
            textArea.setMaxWidth(Double.MAX_VALUE);
            return;
        }
        if (input instanceof ComboBox<?> comboBox) {
            comboBox.setMaxWidth(Double.MAX_VALUE);
            return;
        }
        if (input instanceof Control control) {
            control.setMaxWidth(Double.MAX_VALUE);
        }
    }
}
