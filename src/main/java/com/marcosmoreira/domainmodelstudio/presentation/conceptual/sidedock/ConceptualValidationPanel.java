package com.marcosmoreira.domainmodelstudio.presentation.conceptual.sidedock;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.validation.ValidationIssue;
import com.marcosmoreira.domainmodelstudio.domain.validation.ValidationResult;
import com.marcosmoreira.domainmodelstudio.presentation.conceptual.ConceptualCanvasLegacyBridge;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/** Panel de validación conceptual montado dentro del SideDock común. */
final class ConceptualValidationPanel {

    private final ConceptualCanvasLegacyBridge bridge;
    private final VBox root = new VBox(10);
    private final Label summary = new Label();
    private final VBox issuesBox = new VBox(6);

    ConceptualValidationPanel(ConceptualCanvasLegacyBridge bridge) {
        this.bridge = Objects.requireNonNull(bridge, "bridge");
        build();
        bridge.currentProjectObservable().addListener((observable, previous, current) -> refresh());
        refresh();
    }

    Parent getRoot() {
        return root;
    }

    private void build() {
        root.getStyleClass().addAll("conceptual-side-dock-panel", "conceptual-validation-panel");
        root.setPadding(new Insets(10));

        Label lead = new Label("Revisa coherencia del modelo ER actual sin modificar el proyecto.");
        lead.setWrapText(true);
        lead.getStyleClass().add("conceptual-side-dock-lead");

        summary.setWrapText(true);
        summary.getStyleClass().add("conceptual-validation-summary");

        Button refresh = new Button("Actualizar validación");
        refresh.getStyleClass().add("side-dock-action-button");
        refresh.setMaxWidth(Double.MAX_VALUE);
        refresh.setOnAction(event -> refresh(true));

        issuesBox.getStyleClass().add("conceptual-validation-issues");
        root.getChildren().addAll(lead, refresh, summary, issuesBox);
    }

    private void refresh() {
        refresh(false);
    }

    private void refresh(boolean notifyUser) {
        issuesBox.getChildren().clear();
        DiagramProject project = bridge.currentProject();
        if (project == null) {
            summary.setText("No hay modelo conceptual activo.");
            issuesBox.getChildren().add(message("Abre o importa un modelo conceptual para revisar sus hallazgos."));
            if (notifyUser) {
                showValidationMessage(
                        "Validación conceptual",
                        "No hay modelo conceptual activo",
                        "Abre o importa un modelo conceptual antes de validar.",
                        Alert.AlertType.INFORMATION
                );
            }
            return;
        }

        ValidationResult result = bridge.validation().validateActiveProject();
        if (result.issueCount() == 0) {
            summary.setText("Sin hallazgos: el modelo conceptual no reporta errores ni advertencias semánticas.");
            issuesBox.getChildren().add(message("Puedes continuar editando, exportar o guardar el proyecto."));
            if (notifyUser) {
                showValidationMessage(
                        "Validación conceptual",
                        "Todo está bien",
                        "El modelo conceptual no reporta errores ni advertencias semánticas.",
                        Alert.AlertType.INFORMATION
                );
            }
            return;
        }

        summary.setText(result.errors().size() + " errores y " + result.warnings().size() + " advertencias.");
        for (ValidationIssue issue : result.issues()) {
            issuesBox.getChildren().add(issueLabel(issue));
        }
        if (notifyUser) {
            showValidationMessage(
                    "Validación conceptual",
                    "Hay hallazgos que revisar",
                    result.errors().size() + " errores y " + result.warnings().size()
                            + " advertencias. Revisa el módulo Validación del SideDock.",
                    result.errors().isEmpty() ? Alert.AlertType.INFORMATION : Alert.AlertType.WARNING
            );
        }
    }

    private void showValidationMessage(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        if (root.getScene() != null && root.getScene().getWindow() != null) {
            alert.initOwner(root.getScene().getWindow());
        }
        alert.showAndWait();
    }

    private Label issueLabel(ValidationIssue issue) {
        Label label = message((issue.isError() ? "ERROR" : "ADVERTENCIA") + " — " + issue.message());
        label.getStyleClass().add(issue.isError() ? "conceptual-validation-error" : "conceptual-validation-warning");
        return label;
    }

    private Label message(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.getStyleClass().add("conceptual-side-dock-message");
        return label;
    }
}
