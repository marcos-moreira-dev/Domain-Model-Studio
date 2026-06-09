package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

/**
 * Modo de encuadre aplicado a una superficie visual.
 *
 * <p>El nombre visible en la interfaz debe hablar de "Ajustar vista" o "Centrar";
 * este enum solo expresa la política interna de viewport.</p>
 */
public enum ViewportFitMode {
    /** Ajusta ancho y alto del contenido al viewport disponible. */
    FIT_TO_CONTENT,

    /** Ajusta principalmente al ancho disponible. Útil en diagramas muy horizontales. */
    FIT_WIDTH,

    /** Ajusta principalmente a la altura disponible. Útil en flujos muy verticales. */
    FIT_HEIGHT,

    /** Conserva el zoom actual y solo centra el contenido. */
    CENTER_CONTENT
}
