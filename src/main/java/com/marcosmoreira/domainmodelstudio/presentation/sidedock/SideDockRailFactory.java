package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import java.util.Objects;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;

/** Crea las pestañas verticales del carril del SideDock. */
final class SideDockRailFactory {

    static final double RAIL_ONLY_WIDTH = 36.0;
    /**
     * Ancho lógico del botón antes de rotarlo. Tras la rotación se convierte en altura vertical
     * disponible para el texto; por eso debe cubrir títulos largos como “Entidades y relaciones”.
     */
    private static final double RAIL_BUTTON_WIDTH = 170.0;
    private static final double RAIL_BUTTON_HEIGHT = 24.0;
    private static final double RAIL_SLOT_HEIGHT = 182.0;

    StackPane tabFor(SideDockModule module, boolean active, Runnable onActivate) {
        Objects.requireNonNull(module, "module");
        Objects.requireNonNull(onActivate, "onActivate");
        Button button = buttonFor(module, active, onActivate);
        StackPane tab = new StackPane(button);
        tab.getStyleClass().add("side-dock-rail-tab");
        tab.setMinSize(RAIL_ONLY_WIDTH, RAIL_SLOT_HEIGHT);
        tab.setPrefSize(RAIL_ONLY_WIDTH, RAIL_SLOT_HEIGHT);
        tab.setMaxSize(RAIL_ONLY_WIDTH, RAIL_SLOT_HEIGHT);
        return tab;
    }

    private Button buttonFor(SideDockModule module, boolean active, Runnable onActivate) {
        Button button = new Button(module.title());
        button.getStyleClass().add("side-dock-rail-button");
        if (active) {
            button.getStyleClass().add("side-dock-rail-button-active");
        }
        button.setTooltip(new Tooltip(module.title() + " — " + module.tooltip()));
        button.setRotate(-90);
        button.setMinSize(RAIL_BUTTON_WIDTH, RAIL_BUTTON_HEIGHT);
        button.setPrefSize(RAIL_BUTTON_WIDTH, RAIL_BUTTON_HEIGHT);
        button.setMaxSize(RAIL_BUTTON_WIDTH, RAIL_BUTTON_HEIGHT);
        button.setOnAction(event -> onActivate.run());
        return button;
    }
}
