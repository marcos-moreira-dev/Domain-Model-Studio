package com.marcosmoreira.domainmodelstudio.application.visual;

/**
 * Operaciones de orden visual aplicables a nodos seleccionados de un diagrama especializado.
 *
 * <p>Estas acciones modifican únicamente el orden relativo de renderizado dentro del layout
 * visual persistible. No cambian la semántica del diagrama ni la identidad de los elementos.</p>
 */
public enum VisualLayerOrderCommand {
    BRING_TO_FRONT,
    SEND_TO_BACK,
    RAISE,
    LOWER
}
