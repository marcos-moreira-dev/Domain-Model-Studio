package com.marcosmoreira.domainmodelstudio.presentation.freegraph;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/** Barra local de herramientas del lienzo de Grafo libre. */
final class FreeGraphCanvasToolBar {

    private final FreeGraphViewModel viewModel;
    private final HBox root = new HBox(8);
    private final Map<FreeGraphCanvasTool, ToggleButton> buttons = new EnumMap<>(FreeGraphCanvasTool.class);
    private final Label hint = new Label();
    private boolean updating;

    FreeGraphCanvasToolBar(FreeGraphViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        build();
        bind();
    }

    Parent root() {
        return root;
    }

    private void build() {
        root.setPadding(new Insets(6, 8, 6, 8));
        root.getStyleClass().add("free-graph-canvas-tool-bar");
        root.getChildren().add(new Label("Herramienta:"));
        addToolButton(FreeGraphCanvasTool.ADD_NODE);
        addToolButton(FreeGraphCanvasTool.ADD_EDGE);
        hint.setWrapText(true);
        hint.getStyleClass().add("free-graph-canvas-tool-hint");
        HBox.setHgrow(hint, Priority.ALWAYS);
        root.getChildren().add(hint);
    }

    private void addToolButton(FreeGraphCanvasTool tool) {
        ToggleButton button = new ToggleButton(tool.displayName());
        button.setTooltip(new Tooltip(tool.helpText()));
        button.getStyleClass().add("free-graph-canvas-tool-button");
        button.setUserData(tool);
        buttons.put(tool, button);
        root.getChildren().add(button);
    }

    private void bind() {
        buttons.forEach((tool, button) -> button.setOnAction(event -> {
            if (updating) {
                return;
            }
            if (button.isSelected()) {
                viewModel.activateCanvasTool(tool);
                return;
            }
            viewModel.activateSelectionTool();
        }));
        viewModel.activeCanvasToolProperty().addListener((observable, previous, current) -> selectTool(current));
        selectTool(viewModel.activeCanvasTool());
    }

    private void selectTool(FreeGraphCanvasTool tool) {
        FreeGraphCanvasTool safeTool = tool == null ? FreeGraphCanvasTool.SELECT : tool;
        updating = true;
        try {
            buttons.forEach((candidate, button) -> button.setSelected(candidate == safeTool));
            hint.setText(safeTool.helpText());
        } finally {
            updating = false;
        }
    }
}
