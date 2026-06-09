package com.marcosmoreira.domainmodelstudio.presentation.statusbar;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/** Barra de estado inferior con desplazamiento horizontal para mensajes largos. */
public final class StatusBarView {

    private final StatusBarViewModel viewModel;
    private final HBox content = new HBox(8);
    private final ScrollPane root = new ScrollPane(content);

    public StatusBarView(StatusBarViewModel viewModel) {
        this.viewModel = viewModel;
        build();
    }

    public Parent getRoot() {
        return root;
    }

    private void build() {
        root.getStyleClass().add("status-bar-scroll");
        root.setFitToHeight(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        content.getStyleClass().add("status-bar");
        content.setPadding(new Insets(3, 8, 3, 8));

        Label message = statusLabel();
        message.textProperty().bind(viewModel.messageProperty());
        HBox.setHgrow(message, Priority.ALWAYS);
        message.setMaxWidth(Double.MAX_VALUE);

        Label state = prefixed("Estado", viewModel.projectStateProperty());
        Label notation = prefixed("Vista", viewModel.notationProperty());
        Label zoom = prefixed("Zoom", viewModel.zoomProperty());
        Label elements = statusLabel();
        elements.textProperty().bind(viewModel.elementCountProperty());
        Label saveState = statusLabel();
        saveState.textProperty().bind(viewModel.saveStateProperty());

        content.getChildren().addAll(
                message,
                new Separator(), state,
                new Separator(), notation,
                new Separator(), zoom,
                new Separator(), elements,
                new Separator(), saveState
        );
    }

    private Label prefixed(String prefix, javafx.beans.value.ObservableValue<String> value) {
        Label label = statusLabel();
        label.textProperty().bind(Bindings.concat(prefix, ": ", value));
        return label;
    }

    private Label statusLabel() {
        Label label = new Label();
        label.getStyleClass().add("status-label");
        label.setMinWidth(Region.USE_PREF_SIZE);
        return label;
    }
}
