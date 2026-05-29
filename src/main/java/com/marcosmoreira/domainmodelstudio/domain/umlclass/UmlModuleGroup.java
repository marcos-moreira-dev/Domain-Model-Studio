package com.marcosmoreira.domainmodelstudio.domain.umlclass;

import java.util.Objects;

/** Agrupador visual/arquitectónico de clases por carpeta, paquete o módulo. */
public record UmlModuleGroup(
        String id,
        String displayName,
        String path,
        String description,
        String notes
) {
    public UmlModuleGroup {
        id = normalizeRequired(id, "id");
        displayName = normalizeDisplay(displayName, id);
        path = normalize(path);
        description = normalize(description);
        notes = normalize(notes);
    }

    public UmlModuleGroup withDetails(String displayName, String path, String description, String notes) {
        return new UmlModuleGroup(id, displayName, path, description, notes);
    }

    private static String normalizeRequired(String value, String label) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El " + label + " del módulo UML no puede estar vacío.");
        }
        return normalized;
    }

    private static String normalizeDisplay(String value, String fallback) {
        String normalized = normalize(value);
        return normalized.isBlank() ? Objects.requireNonNull(fallback, "fallback") : normalized;
    }

    private static String normalize(String value) { return value == null ? "" : value.strip(); }
}
