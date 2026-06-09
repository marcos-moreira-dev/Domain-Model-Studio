package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import java.util.List;
import java.util.Objects;

/** Bloque de ayuda operativa mostrado dentro del SideDock del módulo activo. */
record OperationalHelpSection(String title, List<String> items) {

    OperationalHelpSection {
        title = cleanRequired(title, "title");
        items = List.copyOf(Objects.requireNonNull(items, "items"));
        if (items.isEmpty()) {
            throw new IllegalArgumentException("La sección de ayuda debe tener al menos una indicación.");
        }
    }

    static OperationalHelpSection of(String title, String... items) {
        return new OperationalHelpSection(title, List.of(items));
    }

    private static String cleanRequired(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        String clean = value.strip();
        if (clean.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return clean;
    }
}
