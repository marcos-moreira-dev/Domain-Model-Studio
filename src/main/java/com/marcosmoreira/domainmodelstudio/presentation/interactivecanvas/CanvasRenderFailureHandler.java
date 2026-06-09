package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas.ZoomableDiagramSurface;

/** Maneja fallos de render sin cargar esa responsabilidad en la superficie interactiva. */
final class CanvasRenderFailureHandler {
    private final InteractiveCanvasAdapter adapter;
    private final ZoomableDiagramSurface surface;
    private final Runnable invalidateRender;

    CanvasRenderFailureHandler(InteractiveCanvasAdapter adapter, ZoomableDiagramSurface surface, Runnable invalidateRender) {
        this.adapter = adapter;
        this.surface = surface;
        this.invalidateRender = invalidateRender;
    }

    void handle(String phase, int connectorCount, int nodeCount, Throwable throwable) {
        invalidateRender.run();
        try {
            surface.clearContent();
        } catch (RuntimeException ignored) {
        }
        if (adapter instanceof CanvasRenderFailurePort failurePort) {
            failurePort.handleCanvasRenderFailure(CanvasRenderFailureReport.from(
                    phase,
                    connectorCount,
                    nodeCount,
                    throwable));
        }
    }
}
