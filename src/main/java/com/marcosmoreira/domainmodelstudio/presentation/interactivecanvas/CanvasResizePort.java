package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/** Puerto opcional para redimensionar nodos o componentes visuales. */
public interface CanvasResizePort {

    void resizeNode(String elementId, double width, double height);

    default boolean supportsNodeResize() {
        return true;
    }

    /** Permite filtrar qué nodos concretos pueden mostrar agarradores de redimensionado. */
    default boolean supportsNodeResize(String elementId) {
        return supportsNodeResize();
    }
}
