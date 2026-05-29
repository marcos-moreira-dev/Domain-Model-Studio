package com.marcosmoreira.domainmodelstudio.presentation.statusbar;

import java.util.Objects;

/** Resumen contextual del proyecto activo para la barra de estado inferior. */
public record ProjectStatusSummary(String viewLabel, String elementSummary) {

    public ProjectStatusSummary {
        viewLabel = required(viewLabel, "viewLabel");
        elementSummary = required(elementSummary, "elementSummary");
    }

    private static String required(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        String normalized = value.trim();
        if (normalized.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return normalized;
    }
}
