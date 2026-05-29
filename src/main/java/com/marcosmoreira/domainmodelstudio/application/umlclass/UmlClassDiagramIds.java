package com.marcosmoreira.domainmodelstudio.application.umlclass;

import java.text.Normalizer;
import java.util.Locale;

/** Utilidades internas para generar IDs estables legibles por humanos. */
final class UmlClassDiagramIds {
    private UmlClassDiagramIds() { }

    static String slug(String value, String fallback) {
        String normalized = Normalizer.normalize(value == null ? "" : value.strip(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_+|_+$", "");
        return normalized.isBlank() ? fallback : normalized;
    }
}
