package com.marcosmoreira.domainmodelstudio.presentation.canvas.interaction;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

/**
 * Atajos de teclado del canvas conceptual.
 */
public final class DiagramCanvasKeyboardShortcutController {

    private final Pane canvas;
    private final BooleanSupplier deleteSelectedBendPoint;

    public DiagramCanvasKeyboardShortcutController(Pane canvas, BooleanSupplier deleteSelectedBendPoint) {
        this.canvas = Objects.requireNonNull(canvas, "canvas");
        this.deleteSelectedBendPoint = Objects.requireNonNull(deleteSelectedBendPoint, "deleteSelectedBendPoint");
    }

    public void install() {
        canvas.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
                if (deleteSelectedBendPoint.getAsBoolean()) {
                    event.consume();
                }
            }
        });
    }
}
