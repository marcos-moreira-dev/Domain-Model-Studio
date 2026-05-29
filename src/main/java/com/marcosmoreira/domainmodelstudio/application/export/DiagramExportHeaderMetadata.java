package com.marcosmoreira.domainmodelstudio.application.export;

import java.util.Objects;

/**
 * Metadatos textuales mínimos para encabezados de exportaciones visuales.
 *
 * <p>El contrato es deliberadamente pequeño para que PNG y SVG compartan la misma intención
 * sin acoplarse a JavaFX, Markdown ni a un tipo concreto de diagrama. El encabezado describe
 * la salida exportada; no modifica el contrato canónico del proyecto ni sus datos persistidos.</p>
 */
public record DiagramExportHeaderMetadata(
        String title,
        String subtitle,
        String typeLabel
) {

    public DiagramExportHeaderMetadata {
        title = normalize(title, "Diagrama exportado");
        subtitle = normalize(subtitle, "Salida visual de Domain Model Studio");
        typeLabel = normalize(typeLabel, subtitle);
    }

    public static DiagramExportHeaderMetadata of(String title, String subtitle, String typeLabel) {
        return new DiagramExportHeaderMetadata(title, subtitle, typeLabel);
    }

    public String compactSubtitle() {
        if (subtitle.equals(typeLabel) || typeLabel.isBlank()) {
            return subtitle;
        }
        return subtitle + " · " + typeLabel;
    }

    private static String normalize(String value, String fallback) {
        String normalized = value == null ? "" : value.strip();
        if (normalized.isBlank()) {
            return Objects.requireNonNull(fallback, "fallback");
        }
        return normalized.replaceAll("\\s+", " ");
    }
}
