package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/** Resumen legible de una raíz detectada antes de generar UML Clases. */
public record SourceRootImportPreview(
        String sourceRootId,
        String displayName,
        Path path,
        SourceRootKind kind,
        List<SourceLanguageVersion> languageVersions,
        Map<SourceLanguage, Long> fileCountByLanguage
) {
    public SourceRootImportPreview {
        sourceRootId = required(sourceRootId, "sourceRootId");
        displayName = normalizeOrDefault(displayName, sourceRootId);
        if (path == null) {
            throw new IllegalArgumentException("La ruta de la raíz previa no puede ser nula.");
        }
        kind = kind == null ? SourceRootKind.UNKNOWN : kind;
        languageVersions = List.copyOf(languageVersions == null ? List.of() : languageVersions);
        fileCountByLanguage = Map.copyOf(fileCountByLanguage == null ? Map.of() : fileCountByLanguage);
    }

    public long totalFiles() {
        return fileCountByLanguage.values().stream().mapToLong(Long::longValue).sum();
    }

    public boolean hasFiles() {
        return totalFiles() > 0;
    }

    private static String required(String value, String label) {
        String normalized = value == null ? "" : value.strip();
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El " + label + " de la vista previa no puede estar vacío.");
        }
        return normalized;
    }

    private static String normalizeOrDefault(String value, String fallback) {
        String normalized = value == null ? "" : value.strip();
        return normalized.isBlank() ? fallback : normalized;
    }
}
