package com.marcosmoreira.domainmodelstudio.domain.wireframe;

/** Componente funcional de una pantalla administrativa. */
public record WireframeComponent(
        String id,
        String screenId,
        WireframeComponentKind kind,
        String displayName,
        int orderIndex,
        String dataBinding,
        String behavior,
        String notes
) {
    public WireframeComponent {
        id = required(id, "id");
        screenId = required(screenId, "pantalla");
        kind = kind == null ? WireframeComponentKind.OTHER : kind;
        displayName = defaultText(displayName, id);
        orderIndex = Math.max(0, orderIndex);
        dataBinding = normalize(dataBinding);
        behavior = normalize(behavior);
        notes = normalize(notes);
    }

    public WireframeComponent withDetails(
            String screenId,
            WireframeComponentKind kind,
            String displayName,
            int orderIndex,
            String dataBinding,
            String behavior,
            String notes
    ) {
        return new WireframeComponent(id, screenId, kind, displayName, orderIndex, dataBinding, behavior, notes);
    }

    private static String required(String value, String label) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El campo " + label + " del componente no puede estar vacío.");
        }
        return normalized;
    }

    private static String defaultText(String value, String fallback) {
        String normalized = normalize(value);
        return normalized.isBlank() ? fallback : normalized;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
