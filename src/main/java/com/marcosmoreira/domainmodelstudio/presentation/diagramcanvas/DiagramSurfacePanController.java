package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/** Paneo con clic derecho usando scrollbars, no translate permanente. */
public final class DiagramSurfacePanController {

    private final ScrollPane scrollPane;
    private final Node gestureTarget;
    private double startSceneX;
    private double startSceneY;
    private double startHvalue;
    private double startVvalue;
    private boolean active;
    private final Runnable viewportAdjustmentListener;

    DiagramSurfacePanController(ScrollPane scrollPane, Node gestureTarget, Runnable viewportAdjustmentListener) {
        this.scrollPane = scrollPane;
        this.gestureTarget = gestureTarget;
        this.viewportAdjustmentListener = viewportAdjustmentListener == null ? () -> { } : viewportAdjustmentListener;
    }

    public void install() {
        gestureTarget.addEventFilter(MouseEvent.MOUSE_PRESSED, this::begin);
        gestureTarget.addEventFilter(MouseEvent.MOUSE_DRAGGED, this::drag);
        gestureTarget.addEventFilter(MouseEvent.MOUSE_RELEASED, this::end);
    }

    public boolean active() {
        return active;
    }

    private void begin(MouseEvent event) {
        if (event.getButton() != MouseButton.SECONDARY) {
            return;
        }
        active = true;
        viewportAdjustmentListener.run();
        startSceneX = event.getSceneX();
        startSceneY = event.getSceneY();
        startHvalue = scrollPane.getHvalue();
        startVvalue = scrollPane.getVvalue();
        gestureTarget.setCursor(Cursor.CLOSED_HAND);
        event.consume();
    }

    private void drag(MouseEvent event) {
        if (!active) {
            return;
        }
        double viewportWidth = Math.max(1.0, scrollPane.getViewportBounds().getWidth());
        double viewportHeight = Math.max(1.0, scrollPane.getViewportBounds().getHeight());
        double horizontalRange = Math.max(1.0, scrollPane.getContent().getBoundsInLocal().getWidth() - viewportWidth);
        double verticalRange = Math.max(1.0, scrollPane.getContent().getBoundsInLocal().getHeight() - viewportHeight);
        scrollPane.setHvalue(clamp01(startHvalue - (event.getSceneX() - startSceneX) / horizontalRange));
        scrollPane.setVvalue(clamp01(startVvalue - (event.getSceneY() - startSceneY) / verticalRange));
        event.consume();
    }

    private void end(MouseEvent event) {
        if (!active || event.getButton() != MouseButton.SECONDARY) {
            return;
        }
        active = false;
        gestureTarget.setCursor(Cursor.DEFAULT);
        event.consume();
    }

    private static double clamp01(double value) {
        if (!Double.isFinite(value)) {
            return 0.0;
        }
        return Math.max(0.0, Math.min(1.0, value));
    }
}
