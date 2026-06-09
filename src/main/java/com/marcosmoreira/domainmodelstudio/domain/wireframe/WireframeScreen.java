package com.marcosmoreira.domainmodelstudio.domain.wireframe;

/** Pantalla base de un wireframe administrativo. */
public record WireframeScreen(
        String id,
        String displayName,
        String moduleName,
        String purpose,
        String notes
) {
    public WireframeScreen {
        id = required(id, "id");
        displayName = defaultText(displayName, id);
        moduleName = normalize(moduleName);
        purpose = normalize(purpose);
        notes = normalize(notes);
    }

    public WireframeScreen withDetails(String displayName, String moduleName, String purpose, String notes) {
        return new WireframeScreen(id, displayName, moduleName, purpose, notes);
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
