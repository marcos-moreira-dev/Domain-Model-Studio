package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/**
 * Referencia estable al punto intermedio seleccionado de una línea.
 */
public record SelectedBendPoint(String connectorId, int index) {

    public SelectedBendPoint {
        if (connectorId == null || connectorId.isBlank()) {
            throw new IllegalArgumentException("El conector del punto intermedio no puede estar vacío");
        }
        connectorId = connectorId.strip();
        if (index < 0) {
            throw new IllegalArgumentException("El índice del punto intermedio no puede ser negativo");
        }
    }
}
