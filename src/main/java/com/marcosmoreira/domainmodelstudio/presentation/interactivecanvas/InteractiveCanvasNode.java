package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import java.util.Objects;

/**
 * Nodo visual normalizado para un lienzo de diagramas.
 */
public record InteractiveCanvasNode(
        String id,
        String title,
        String subtitle,
        String kind,
        boolean visible,
        boolean locked
) {

    public InteractiveCanvasNode {
        id = requireText(id, "id");
        title = normalize(title);
        subtitle = normalize(subtitle);
        kind = normalize(kind).isBlank() ? "node" : normalize(kind);
    }

    public static InteractiveCanvasNode of(String id, String title, String kind) {
        return new InteractiveCanvasNode(id, title, "", kind, true, false);
    }

    public InteractiveCanvasNode withLocked(boolean updatedLocked) {
        return new InteractiveCanvasNode(id, title, subtitle, kind, visible, updatedLocked);
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
