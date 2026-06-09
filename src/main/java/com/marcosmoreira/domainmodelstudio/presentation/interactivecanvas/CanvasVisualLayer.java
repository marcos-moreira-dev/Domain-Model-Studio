package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/**
 * Categorías semánticas de profundidad del canvas común.
 *
 * <p>No reemplaza las capas físicas de {@code ZoomableDiagramSurface}; define
 * qué nodos deben montarse como fondo semántico y cuáles como elementos
 * operables para que los conectores queden entre ambos grupos.</p>
 */
public enum CanvasVisualLayer {
    CONTAINER(0),
    NODE(10);

    private final int order;

    CanvasVisualLayer(int order) {
        this.order = order;
    }

    public int order() {
        return order;
    }
}
