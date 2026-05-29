package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import java.util.Objects;

/**
 * Controlador de zoom con scroll del mouse.
 */
public final class CanvasZoomController {

    private static final double ZOOM_STEP = 1.10;

    private final InteractiveCanvasViewport viewport;

    public CanvasZoomController(InteractiveCanvasViewport viewport) {
        this.viewport = Objects.requireNonNull(viewport, "El viewport no puede ser null");
    }

    public void applyScroll(double viewX, double viewY, double scrollDeltaY) {
        if (scrollDeltaY == 0.0) {
            return;
        }
        double factor = scrollDeltaY > 0.0 ? ZOOM_STEP : 1.0 / ZOOM_STEP;
        viewport.zoomAt(viewX, viewY, factor);
    }
}
