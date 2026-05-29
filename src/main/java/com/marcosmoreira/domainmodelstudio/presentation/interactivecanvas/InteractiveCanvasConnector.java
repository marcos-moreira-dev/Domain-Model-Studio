package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import java.util.Objects;

/**
 * Conector visual normalizado entre dos nodos del lienzo.
 */
public record InteractiveCanvasConnector(
        String id,
        String sourceNodeId,
        String targetNodeId,
        String label,
        String kind,
        boolean visible
) {

    public InteractiveCanvasConnector {
        id = requireText(id, "id");
        sourceNodeId = requireText(sourceNodeId, "sourceNodeId");
        targetNodeId = requireText(targetNodeId, "targetNodeId");
        label = normalize(label);
        kind = normalize(kind).isBlank() ? "connector" : normalize(kind);
    }

    public static InteractiveCanvasConnector between(String id, String sourceNodeId, String targetNodeId, String label) {
        return new InteractiveCanvasConnector(id, sourceNodeId, targetNodeId, label, "connector", true);
    }

    private static String requireText(String value, String name) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El campo " + name + " no puede estar vacío");
        }
        return normalized;
    }

    private static String normalize(String value) {
        return Objects.toString(value, "").strip();
    }
}
