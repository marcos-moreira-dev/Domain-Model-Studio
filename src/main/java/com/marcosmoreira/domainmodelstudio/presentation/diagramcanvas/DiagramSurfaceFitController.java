package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

/** Cálculos puros para ajustar contenido al viewport sin depender de familias de diagrama. */
public final class DiagramSurfaceFitController {

    private DiagramSurfaceFitController() {
    }

    public static double computeFitZoom(
            double contentWidth,
            double contentHeight,
            double viewportWidth,
            double viewportHeight,
            DiagramSurfaceConfig config
    ) {
        return computeZoom(
                contentWidth,
                contentHeight,
                viewportWidth,
                viewportHeight,
                config,
                ViewportFitMode.FIT_TO_CONTENT,
                1.0
        );
    }

    public static double computeZoom(
            double contentWidth,
            double contentHeight,
            double viewportWidth,
            double viewportHeight,
            DiagramSurfaceConfig config,
            ViewportFitMode mode,
            double currentZoom
    ) {
        return new CanvasViewportFitPolicy(mode).computeZoom(
                contentWidth,
                contentHeight,
                viewportWidth,
                viewportHeight,
                config,
                currentZoom
        );
    }
}
