package com.marcosmoreira.domainmodelstudio.domain.layout;

/**
 * Vértice intermedio editable de una línea.
 */
public record BendPoint(double x, double y) {

    public BendPoint {
        if (!Double.isFinite(x) || !Double.isFinite(y)) {
            throw new IllegalArgumentException("El punto intermedio debe tener coordenadas finitas");
        }
    }

    public static BendPoint of(double x, double y) {
        return new BendPoint(x, y);
    }

    public DiagramPoint asPoint() {
        return DiagramPoint.of(x, y);
    }

    public BendPoint translatedBy(double deltaX, double deltaY) {
        return new BendPoint(x + deltaX, y + deltaY);
    }
}
