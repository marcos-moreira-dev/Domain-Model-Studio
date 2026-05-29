package com.marcosmoreira.domainmodelstudio.presentation.shell.tabs;

import com.marcosmoreira.domainmodelstudio.presentation.shell.EditorTabViewState;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.OverrunStyle;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;

/**
 * Celda visual de una pestaña abierta en el área de edición.
 *
 * <p>Centraliza el dibujo y los callbacks de activar/cerrar para que el shell
 * no mezcle orquestación de workspace con construcción de controles.</p>
 */
public final class EditorTabCellView {

    private final EditorTabViewState tab;
    private final boolean active;
    private final Consumer<String> activateTab;
    private final Consumer<String> closeTab;
    private final BiConsumer<String, String> reorderTabAfter;
    private final HBox root;

    public EditorTabCellView(
            EditorTabViewState tab,
            boolean active,
            Consumer<String> activateTab,
            Consumer<String> closeTab,
            BiConsumer<String, String> reorderTabAfter
    ) {
        this.tab = Objects.requireNonNull(tab, "tab");
        this.active = active;
        this.activateTab = Objects.requireNonNull(activateTab, "activateTab");
        this.closeTab = Objects.requireNonNull(closeTab, "closeTab");
        this.reorderTabAfter = Objects.requireNonNull(reorderTabAfter, "reorderTabAfter");
        this.root = build();
    }

    public Parent getRoot() {
        return root;
    }

    private HBox build() {
        Label label = new Label(tab.title());
        label.getStyleClass().add("editor-tab-label");
        label.setMaxWidth(300.0);
        label.setTextOverrun(OverrunStyle.ELLIPSIS);
        label.setMinHeight(24.0);
        label.setPrefHeight(24.0);

        HBox tabBox = new HBox(6);
        if (tab.home()) {
            Label icon = new Label("⌂");
            icon.getStyleClass().add("editor-tab-icon");
            tabBox.getChildren().add(icon);
        }
        tabBox.getChildren().add(label);
        tabBox.getStyleClass().add(active ? "editor-tab-active" : "editor-tab-inactive");
        tabBox.setAlignment(Pos.CENTER_LEFT);
        tabBox.setMinHeight(36.0);
        tabBox.setPrefHeight(36.0);
        tabBox.setMaxHeight(36.0);
        tabBox.setOnMouseClicked(event -> activateTab.accept(tab.id()));
        installDragAndDrop(tabBox);

        if (tab.closeable()) {
            Button close = new Button("×");
            close.getStyleClass().add("editor-tab-close-button");
            close.setMnemonicParsing(false);
            close.setTooltip(new Tooltip("Cerrar esta pestaña del área de trabajo."));
            close.setMinHeight(22.0);
            close.setPrefHeight(22.0);
            close.setMaxHeight(22.0);
            close.setOnAction(event -> {
                closeTab.accept(tab.id());
                event.consume();
            });
            tabBox.getChildren().add(close);
        }
        return tabBox;
    }

    private void installDragAndDrop(HBox tabBox) {
        if (!tab.closeable()) {
            return;
        }
        tabBox.setOnDragDetected(event -> {
            Dragboard dragboard = tabBox.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(tab.id());
            dragboard.setContent(content);
            tabBox.getStyleClass().add("editor-tab-dragging");
            event.consume();
        });
        tabBox.setOnDragOver(event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasString() && !tab.id().equals(dragboard.getString())) {
                event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            }
        });
        tabBox.setOnDragEntered(event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasString() && !tab.id().equals(dragboard.getString())) {
                tabBox.getStyleClass().add("editor-tab-drop-target");
            }
        });
        tabBox.setOnDragExited(event -> tabBox.getStyleClass().remove("editor-tab-drop-target"));
        tabBox.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean completed = false;
            if (dragboard.hasString() && !tab.id().equals(dragboard.getString())) {
                reorderTabAfter.accept(dragboard.getString(), tab.id());
                completed = true;
            }
            tabBox.getStyleClass().remove("editor-tab-drop-target");
            event.setDropCompleted(completed);
            event.consume();
        });
        tabBox.setOnDragDone(event -> tabBox.getStyleClass().remove("editor-tab-dragging"));
    }
}
