package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import javafx.geometry.Bounds;

/**
 * Superficie visual exportable calculada desde los límites reales del diagrama.
 */
public record CanvasExportSurface(CanvasBounds bounds, double padding) {

    public CanvasExportSurface {
        if (bounds == null) {
            throw new IllegalArgumentException("Los límites de exportación no pueden ser null");
        }
        if (!Double.isFinite(padding) || padding < 0.0) {
            throw new IllegalArgumentException("El padding de exportación debe ser finito y no negativo");
        }
    }

    public static CanvasExportSurface of(CanvasBounds bounds) {
        return new CanvasExportSurface(bounds, 48.0);
    }

    public static CanvasExportSurface of(Bounds bounds) {
        if (bounds == null) {
            return of(CanvasBounds.of(0.0, 0.0, 880.0, 600.0));
        }
        return of(CanvasBounds.of(
                bounds.getMinX(),
                bounds.getMinY(),
                Math.max(1.0, bounds.getWidth()),
                Math.max(1.0, bounds.getHeight())
        ));
    }

    public double exportX() {
        return bounds.x() - padding;
    }

    public double exportY() {
        return bounds.y() - padding;
    }

    public double exportWidth() {
        return bounds.width() + padding * 2.0;
    }

    public double exportHeight() {
        return bounds.height() + padding * 2.0;
    }
}
