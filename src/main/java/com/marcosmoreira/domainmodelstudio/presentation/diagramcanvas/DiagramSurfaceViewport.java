package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

/** Estado liviano del viewport de una superficie zoomable. */
public record DiagramSurfaceViewport(double hvalue, double vvalue, double zoomFactor) {

    public DiagramSurfaceViewport {
        hvalue = clamp01(hvalue);
        vvalue = clamp01(vvalue);
        if (!Double.isFinite(zoomFactor) || zoomFactor <= 0.0) {
            throw new IllegalArgumentException("zoomFactor debe ser positivo y finito.");
        }
    }

    public static DiagramSurfaceViewport initial() {
        return new DiagramSurfaceViewport(0.5, 0.5, 1.0);
    }

    public DiagramSurfaceViewport withZoom(double newZoomFactor) {
        return new DiagramSurfaceViewport(hvalue, vvalue, newZoomFactor);
    }

    private static double clamp01(double value) {
        if (!Double.isFinite(value)) {
            return 0.0;
        }
        return Math.max(0.0, Math.min(1.0, value));
    }
}
