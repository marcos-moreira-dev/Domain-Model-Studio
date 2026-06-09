package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import java.util.Objects;

/**
 * Política pura para calcular el zoom inicial de una superficie de diagrama.
 *
 * <p>La política no conoce UML, BPMN, C4 ni módulos concretos. Los centros de cada
 * workspace eligen el modo adecuado y la superficie aplica el cálculo de forma uniforme.</p>
 */
public final class CanvasViewportFitPolicy {

    private final ViewportFitMode mode;

    public CanvasViewportFitPolicy(ViewportFitMode mode) {
        this.mode = Objects.requireNonNull(mode, "mode");
    }

    public static CanvasViewportFitPolicy fitToContent() {
        return new CanvasViewportFitPolicy(ViewportFitMode.FIT_TO_CONTENT);
    }

    public static CanvasViewportFitPolicy fitWidth() {
        return new CanvasViewportFitPolicy(ViewportFitMode.FIT_WIDTH);
    }

    public static CanvasViewportFitPolicy fitHeight() {
        return new CanvasViewportFitPolicy(ViewportFitMode.FIT_HEIGHT);
    }

    public static CanvasViewportFitPolicy centerOnly() {
        return new CanvasViewportFitPolicy(ViewportFitMode.CENTER_CONTENT);
    }

    public ViewportFitMode mode() {
        return mode;
    }

    public double computeZoom(
            double contentWidth,
            double contentHeight,
            double viewportWidth,
            double viewportHeight,
            DiagramSurfaceConfig config,
            double currentZoom
    ) {
        Objects.requireNonNull(config, "config");
        if (mode == ViewportFitMode.CENTER_CONTENT) {
            return config.clampZoom(validZoomOrDefault(currentZoom));
        }
        if (contentWidth <= 0.0 || contentHeight <= 0.0 || viewportWidth <= 0.0 || viewportHeight <= 0.0) {
            return config.clampZoom(1.0);
        }
        double availableWidth = Math.max(1.0, viewportWidth - config.fitPadding());
        double availableHeight = Math.max(1.0, viewportHeight - config.fitPadding());
        double horizontalZoom = availableWidth / contentWidth;
        double verticalZoom = availableHeight / contentHeight;
        return switch (mode) {
            case FIT_WIDTH -> config.clampZoom(horizontalZoom);
            case FIT_HEIGHT -> config.clampZoom(verticalZoom);
            case FIT_TO_CONTENT -> config.clampZoom(Math.min(horizontalZoom, verticalZoom));
            case CENTER_CONTENT -> config.clampZoom(validZoomOrDefault(currentZoom));
        };
    }

    private static double validZoomOrDefault(double currentZoom) {
        return Double.isFinite(currentZoom) && currentZoom > 0.0 ? currentZoom : 1.0;
    }
}
