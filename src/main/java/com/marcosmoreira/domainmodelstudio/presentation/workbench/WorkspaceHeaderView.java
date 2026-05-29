package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import java.util.Objects;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/** Encabezado común, cerrable y desacoplado del modelo conceptual. */
public final class WorkspaceHeaderView {

    private final BorderPane root = new BorderPane();
    private final Label titleLabel = new Label();
    private final Label subtitleLabel = new Label();
    private final Label statusLabel = new Label();
    private final Button dismissButton = new Button("×");
    private WorkspaceHeaderState state;

    public WorkspaceHeaderView(WorkspaceHeaderState initialState, Runnable onDismiss) {
        this.state = Objects.requireNonNull(initialState, "initialState");
        build(onDismiss == null ? () -> { } : onDismiss);
        applyState(initialState);
    }

    public Parent getRoot() {
        return root;
    }

    public void applyState(WorkspaceHeaderState newState) {
        state = Objects.requireNonNull(newState, "newState");
        titleLabel.setText(state.title());
        subtitleLabel.setText(state.subtitle());
        statusLabel.setText(state.statusText());
        subtitleLabel.setVisible(!state.subtitle().isBlank());
        subtitleLabel.setManaged(!state.subtitle().isBlank());
        statusLabel.setVisible(!state.statusText().isBlank());
        statusLabel.setManaged(!state.statusText().isBlank());
        dismissButton.setVisible(state.dismissible());
        dismissButton.setManaged(state.dismissible());
        root.setVisible(state.visible());
        root.setManaged(state.visible());
    }

    public WorkspaceHeaderState state() {
        return state;
    }

    public void hide() {
        applyState(state.hidden());
    }

    private void build(Runnable onDismiss) {
        root.getStyleClass().add("workspace-header");

        VBox textBox = new VBox(4, titleLabel, subtitleLabel, statusLabel);
        textBox.getStyleClass().add("workspace-header-text");
        titleLabel.getStyleClass().add("workspace-title");
        subtitleLabel.getStyleClass().add("workspace-subtitle");
        statusLabel.getStyleClass().add("workspace-status-text");

        dismissButton.getStyleClass().add("workspace-header-dismiss-button");
        dismissButton.setTooltip(new Tooltip("Ocultar encabezado de esta área de trabajo"));
        dismissButton.setOnAction(event -> {
            hide();
            onDismiss.run();
        });

        BorderPane.setAlignment(dismissButton, Pos.TOP_RIGHT);
        root.setCenter(textBox);
        root.setRight(dismissButton);
    }
}
