package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import java.util.Objects;

/** Advertencia estructural del snapshot visual del canvas. */
public record CanvasModelWarning(String elementId, String message) {

    public CanvasModelWarning {
        elementId = normalize(elementId);
        message = normalize(message);
        if (message.isBlank()) {
            throw new IllegalArgumentException("El mensaje de advertencia no puede estar vacío");
        }
    }

    public static CanvasModelWarning missingNodeLayout(String nodeId) {
        return new CanvasModelWarning(nodeId, "Nodo sin layout visual asociado");
    }

    public static CanvasModelWarning missingConnectorLayout(String connectorId) {
        return new CanvasModelWarning(connectorId, "Conector sin layout visual asociado");
    }

    private static String normalize(String value) {
        return Objects.toString(value, "").strip();
    }
}
