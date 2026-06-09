package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import javafx.scene.layout.BorderPane;

/** Política única de anchura para SideDock abierto o colapsado. */
final class SideDockSizingPolicy {

    private static final double OPEN_PREF_WIDTH = 460.0;
    private static final double OPEN_MAX_WIDTH = 1000.0;

    void apply(BorderPane root, boolean open) {
        if (open) {
            root.setMinWidth(SideDockRailFactory.RAIL_ONLY_WIDTH + 260.0);
            root.setPrefWidth(OPEN_PREF_WIDTH);
            root.setMaxWidth(OPEN_MAX_WIDTH);
        } else {
            root.setMinWidth(SideDockRailFactory.RAIL_ONLY_WIDTH);
            root.setPrefWidth(SideDockRailFactory.RAIL_ONLY_WIDTH);
            root.setMaxWidth(SideDockRailFactory.RAIL_ONLY_WIDTH);
        }
    }
}
