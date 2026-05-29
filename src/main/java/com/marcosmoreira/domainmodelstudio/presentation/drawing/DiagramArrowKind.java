package com.marcosmoreira.domainmodelstudio.presentation.drawing;

/**
 * Formas de punta disponibles para conectores visuales.
 *
 * <p>El enum no decide semántica de UML, C4, BPMN o módulos; solo nombra la
 * geometría que un render kit específico solicita.</p>
 */
public enum DiagramArrowKind {
    NONE,
    OPEN,
    FILLED_TRIANGLE,
    HOLLOW_TRIANGLE,
    FILLED_DIAMOND,
    HOLLOW_DIAMOND
}
