package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import java.util.Objects;

/**
 * Controlador exclusivo de paneo. El clic derecho debe reservarse para este gesto.
 */
public final class CanvasPanController {

    private final InteractiveCanvasViewport viewport;
    private boolean active;
    private double lastX;
    private double lastY;

    public CanvasPanController(InteractiveCanvasViewport viewport) {
        this.viewport = Objects.requireNonNull(viewport, "El viewport no puede ser null");
    }

    public void begin(double viewX, double viewY) {
        active = true;
        lastX = viewX;
        lastY = viewY;
    }

    public void dragTo(double viewX, double viewY) {
        if (!active) {
            return;
        }
        viewport.panBy(viewX - lastX, viewY - lastY);
        lastX = viewX;
        lastY = viewY;
    }

    public void end() {
        active = false;
    }

    public boolean active() {
        return active;
    }
}
