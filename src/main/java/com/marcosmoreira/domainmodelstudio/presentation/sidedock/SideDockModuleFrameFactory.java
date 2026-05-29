package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import java.util.Objects;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

/** Construye el marco visible de un módulo activo del SideDock. */
final class SideDockModuleFrameFactory {

    BorderPane frameFor(SideDockModule module, SideDockContext context, Runnable onClose) {
        Objects.requireNonNull(module, "module");
        Objects.requireNonNull(onClose, "onClose");
        BorderPane frame = new BorderPane();
        frame.getStyleClass().add("side-dock-module-frame");
        frame.setTop(moduleHeader(module, onClose));
        frame.setCenter(scrollableModuleContent(module.createView(context)));
        return frame;
    }

    private Parent scrollableModuleContent(Parent content) {
        if (content instanceof ScrollPane existingScroll) {
            configureScroll(existingScroll);
            return existingScroll;
        }
        if (content.getStyleClass().contains("side-dock-content-owns-scroll")) {
            return content;
        }
        if (content.getParent() instanceof ScrollPane previousScroll) {
            previousScroll.setContent(null);
        }
        ScrollPane scroll = new ScrollPane(content);
        configureScroll(scroll);
        return scroll;
    }

    private void configureScroll(ScrollPane scroll) {
        if (!scroll.getStyleClass().contains("side-dock-module-scroll")) {
            scroll.getStyleClass().add("side-dock-module-scroll");
        }
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setPannable(true);
    }

    private Parent moduleHeader(SideDockModule module, Runnable onClose) {
        HBox header = new HBox(8);
        header.getStyleClass().add("side-dock-module-header");
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label(module.title());
        title.getStyleClass().add("side-dock-module-title");
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setMaxWidth(Double.MAX_VALUE);
        Button close = new Button("×");
        close.getStyleClass().add("side-dock-module-close-button");
        close.setTooltip(new Tooltip("Ocultar " + module.title().toLowerCase()));
        close.setOnAction(event -> onClose.run());
        header.getChildren().addAll(title, close);
        return new StackPane(header);
    }
}
