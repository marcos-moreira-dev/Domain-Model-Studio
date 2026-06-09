package com.marcosmoreira.domainmodelstudio.domain.layout;

/**
 * Tamaño de una figura del diagrama.
 */
public record DiagramSize(double width, double height) {

    public DiagramSize {
        if (!Double.isFinite(width) || width <= 0) {
            throw new IllegalArgumentException("El ancho debe ser finito y mayor que cero");
        }
        if (!Double.isFinite(height) || height <= 0) {
            throw new IllegalArgumentException("El alto debe ser finito y mayor que cero");
        }
    }

    public static DiagramSize of(double width, double height) {
        return new DiagramSize(width, height);
    }
}
