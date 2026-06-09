package com.marcosmoreira.domainmodelstudio.domain.layout;

/**
 * Punto geométrico propio del dominio visual.
 *
 * <p>No usa tipos JavaFX para mantener el layout independiente de la presentación.
 * Sirve para posiciones de nodos y vértices intermedios de conectores.</p>
 */
public record DiagramPoint(double x, double y) {

    public DiagramPoint {
        ensureFinite(x, "x");
        ensureFinite(y, "y");
    }

    public static DiagramPoint of(double x, double y) {
        return new DiagramPoint(x, y);
    }

    public DiagramPoint translatedBy(double deltaX, double deltaY) {
        return new DiagramPoint(x + deltaX, y + deltaY);
    }

    private static void ensureFinite(double value, String name) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("La coordenada " + name + " debe ser finita");
        }
    }
}
