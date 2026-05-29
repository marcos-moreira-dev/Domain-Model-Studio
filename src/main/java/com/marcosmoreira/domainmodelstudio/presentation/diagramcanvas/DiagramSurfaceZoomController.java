package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;

/** Gestos y comandos de zoom para una superficie canónica. */
public final class DiagramSurfaceZoomController {

    private final ScrollPane scrollPane;
    private final DiagramSurfaceViewportController viewportController;
    private final DiagramSurfaceConfig config;
    private final Runnable viewportAdjustmentListener;

    DiagramSurfaceZoomController(
            ScrollPane scrollPane,
            DiagramSurfaceViewportController viewportController,
            DiagramSurfaceConfig config,
            Runnable viewportAdjustmentListener
    ) {
        this.scrollPane = scrollPane;
        this.viewportController = viewportController;
        this.config = config;
        this.viewportAdjustmentListener = viewportAdjustmentListener == null ? () -> { } : viewportAdjustmentListener;
    }

    public void install() {
        scrollPane.addEventFilter(ScrollEvent.SCROLL, this::handleScroll);
    }

    public void zoomIn() {
        viewportAdjustmentListener.run();
        viewportController.setZoomAtViewportCenter(viewportController.zoomFactor() * config.zoomStep());
    }

    public void zoomOut() {
        viewportAdjustmentListener.run();
        viewportController.setZoomAtViewportCenter(viewportController.zoomFactor() / config.zoomStep());
    }

    public void resetZoom() {
        viewportAdjustmentListener.run();
        viewportController.setZoomAtViewportCenter(1.0);
    }

    private void handleScroll(ScrollEvent event) {
        if (Math.abs(event.getDeltaY()) < 0.01) {
            return;
        }
        Point2D viewportPoint = scrollPane.sceneToLocal(event.getSceneX(), event.getSceneY());
        double factor = event.getDeltaY() > 0 ? config.zoomStep() : 1.0 / config.zoomStep();
        viewportAdjustmentListener.run();
        viewportController.setZoomAtViewportPoint(
                viewportController.zoomFactor() * factor,
                viewportPoint.getX(),
                viewportPoint.getY()
        );
        event.consume();
    }
}
