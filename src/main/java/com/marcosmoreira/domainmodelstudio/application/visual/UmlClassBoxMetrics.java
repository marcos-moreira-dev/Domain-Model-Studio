package com.marcosmoreira.domainmodelstudio.application.visual;

/** Tamaño visual calculado para una caja UML de clase/interfaz/enum. */
public record UmlClassBoxMetrics(double width, double height, int attributeCount, int methodCount) {

    public UmlClassBoxMetrics {
        if (!Double.isFinite(width) || width <= 0.0) {
            throw new IllegalArgumentException("El ancho UML calculado debe ser positivo.");
        }
        if (!Double.isFinite(height) || height <= 0.0) {
            throw new IllegalArgumentException("El alto UML calculado debe ser positivo.");
        }
        attributeCount = Math.max(0, attributeCount);
        methodCount = Math.max(0, methodCount);
    }
}
