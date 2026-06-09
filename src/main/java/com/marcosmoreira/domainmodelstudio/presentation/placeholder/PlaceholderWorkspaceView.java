package com.marcosmoreira.domainmodelstudio.presentation.placeholder;

import java.util.stream.Collectors;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Vista informativa para tipos de proyecto todavía en preparación. */
public final class PlaceholderWorkspaceView {

    private final BorderPane root = new BorderPane();

    public PlaceholderWorkspaceView() {
        root.getStyleClass().add("placeholder-workspace-root");
        clear();
    }

    public Parent getRoot() {
        return root;
    }

    public void show(PlaceholderWorkspaceViewModel viewModel) {
        VBox panel = new VBox(14);
        panel.getStyleClass().add("placeholder-workspace-panel");
        panel.setPadding(new Insets(34, 42, 34, 42));
        panel.setAlignment(Pos.TOP_LEFT);
        panel.getChildren().addAll(
                label("Tipo de proyecto", "placeholder-eyebrow"),
                label(viewModel.title(), "placeholder-title"),
                label(viewModel.statusLabel(), "placeholder-status"),
                paragraph(viewModel.message()),
                detailRow("Categoría", viewModel.category()),
                detailRow("Materiales disponibles", actionSummary(viewModel)),
                paragraph("Esta guía conserva el alcance del tipo seleccionado y sus materiales de referencia."));
        root.setCenter(panel);
    }

    public void clear() {
        Label empty = label("Selecciona o abre un proyecto para comenzar.", "placeholder-empty");
        BorderPane.setAlignment(empty, Pos.CENTER);
        root.setCenter(empty);
    }

    private static HBox detailRow(String labelText, String valueText) {
        Label label = label(labelText + ":", "placeholder-detail-label");
        Label value = paragraph(valueText == null || valueText.isBlank() ? "—" : valueText);
        HBox row = new HBox(10, label, value);
        HBox.setHgrow(value, Priority.ALWAYS);
        row.setAlignment(Pos.TOP_LEFT);
        return row;
    }

    private static Label label(String text, String styleClass) {
        Label label = new Label(text == null ? "" : text);
        label.getStyleClass().add(styleClass);
        label.setWrapText(true);
        return label;
    }

    private static Label paragraph(String text) {
        return label(text, "placeholder-paragraph");
    }

    private static String actionSummary(PlaceholderWorkspaceViewModel viewModel) {
        if (viewModel.allowedActions().isEmpty()) {
            return "Materiales disponibles según el tipo de proyecto.";
        }
        return viewModel.allowedActions().stream()
                .map(PlaceholderWorkspaceView::displayAction)
                .collect(Collectors.joining(", "));
    }

    private static String displayAction(PlaceholderAction action) {
        return switch (action) {
            case SHOW_THEORY -> "ver guía teórica";
            case EXPORT_AI_RESOURCES -> "exportar plantillas para IA";
            case BACK_TO_NEW_PROJECT -> "volver a Nuevo proyecto";
        };
    }
}
