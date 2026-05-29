package com.marcosmoreira.domainmodelstudio.domain.screenflow;

/** Pantalla navegable de una aplicación administrativa. */
public record ScreenNode(
        String id,
        String displayName,
        ScreenKind kind,
        String moduleName,
        String route,
        String purpose,
        String notes
) {
    public ScreenNode {
        id = required(id, "id");
        displayName = defaultText(displayName, id);
        kind = kind == null ? ScreenKind.OTHER : kind;
        moduleName = normalize(moduleName);
        route = normalize(route);
        purpose = normalize(purpose);
        notes = normalize(notes);
    }

    public ScreenNode withDetails(
            String displayName,
            ScreenKind kind,
            String moduleName,
            String route,
            String purpose,
            String notes
    ) {
        return new ScreenNode(id, displayName, kind, moduleName, route, purpose, notes);
    }

    private static String required(String value, String label) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El " + label + " de la pantalla no puede estar vacío.");
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
