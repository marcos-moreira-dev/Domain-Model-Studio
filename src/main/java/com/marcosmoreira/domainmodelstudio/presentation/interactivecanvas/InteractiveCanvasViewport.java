package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/**
 * Estado de viewport compartido: escala y traslación del tablero.
 */
public final class InteractiveCanvasViewport {

    public static final double MIN_SCALE = 0.25;
    public static final double MAX_SCALE = 2.75;

    private double scale = 1.0;
    private double translateX;
    private double translateY;

    public double scale() {
        return scale;
    }

    public double translateX() {
        return translateX;
    }

    public double translateY() {
        return translateY;
    }

    public double canvasX(double viewX) {
        return (viewX - translateX) / scale;
    }

    public double canvasY(double viewY) {
        return (viewY - translateY) / scale;
    }

    public double viewX(double canvasX) {
        return canvasX * scale + translateX;
    }

    public double viewY(double canvasY) {
        return canvasY * scale + translateY;
    }

    public void panBy(double deltaX, double deltaY) {
        ensureFinite(deltaX, "deltaX");
        ensureFinite(deltaY, "deltaY");
        translateX += deltaX;
        translateY += deltaY;
    }

    public void zoomAt(double viewX, double viewY, double factor) {
        ensureFinite(viewX, "viewX");
        ensureFinite(viewY, "viewY");
        ensureFinite(factor, "factor");
        if (factor <= 0.0) {
            throw new IllegalArgumentException("El factor de zoom debe ser positivo");
        }
        double beforeX = canvasX(viewX);
        double beforeY = canvasY(viewY);
        scale = clamp(scale * factor, MIN_SCALE, MAX_SCALE);
        translateX = viewX - beforeX * scale;
        translateY = viewY - beforeY * scale;
    }

    public void reset() {
        scale = 1.0;
        translateX = 0.0;
        translateY = 0.0;
    }

    public void centerOn(CanvasBounds bounds, double viewportWidth, double viewportHeight) {
        scale = 1.0;
        translateX = viewportWidth / 2.0 - bounds.centerX();
        translateY = viewportHeight / 2.0 - bounds.centerY();
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static void ensureFinite(double value, String name) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("El valor " + name + " debe ser finito");
        }
    }
}
