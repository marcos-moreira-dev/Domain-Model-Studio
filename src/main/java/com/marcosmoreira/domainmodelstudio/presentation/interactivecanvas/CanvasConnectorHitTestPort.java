package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/**
 * Permite optar explícitamente por líneas de conectores que interceptan mouse.
 *
 * <p>El lienzo especializado dibuja conectores por encima de nodos y contenedores
 * para preservar legibilidad. Por defecto las líneas no deben bloquear clic ni
 * arrastre de tarjetas; la selección de relaciones se mantiene por hit testing de
 * fondo, etiquetas y handles. Solo un adaptador con una razón UX explícita debe
 * devolver {@code true}.</p>
 */
public interface CanvasConnectorHitTestPort {

    default boolean connectorLineHitTestingEnabled() {
        return false;
    }
}
