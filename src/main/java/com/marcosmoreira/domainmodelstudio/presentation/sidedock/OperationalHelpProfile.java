package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import java.util.List;
import java.util.Objects;

/** Perfil textual de ayuda de herramienta; no reemplaza la guía académica. */
record OperationalHelpProfile(
        String title,
        String subtitle,
        List<OperationalHelpSection> sections
) {

    OperationalHelpProfile {
        title = cleanRequired(title, "title");
        subtitle = subtitle == null ? "" : subtitle.strip();
        sections = List.copyOf(Objects.requireNonNull(sections, "sections"));
        if (sections.isEmpty()) {
            throw new IllegalArgumentException("La ayuda operativa debe declarar secciones.");
        }
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
