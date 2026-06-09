package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/** Puerto opcional para mover etiquetas asociadas a conectores. */
public interface CanvasConnectorLabelPort {

    /** Mueve la etiqueta del conector usando desplazamiento relativo en coordenadas de canvas. */
    void moveConnectorLabelBy(String connectorId, double deltaX, double deltaY);

    default boolean supportsConnectorLabels() {
        return true;
    }
}
