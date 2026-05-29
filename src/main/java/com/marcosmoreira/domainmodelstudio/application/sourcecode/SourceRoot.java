package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.nio.file.Path;
import java.util.List;

/** Raíz lógica detectada dentro de un proyecto de código fuente. */
public record SourceRoot(
        String id,
        String displayName,
        Path path,
        SourceRootKind kind,
        List<SourceLanguageVersion> languageVersions
) {
    public SourceRoot {
        id = required(id, "id");
        displayName = normalizeOrDefault(displayName, id);
        if (path == null) {
            throw new IllegalArgumentException("La ruta de la raíz de código no puede ser nula.");
        }
        kind = kind == null ? SourceRootKind.UNKNOWN : kind;
        languageVersions = List.copyOf(languageVersions == null ? List.of() : languageVersions);
    }

    public boolean supports(SourceLanguage language) {
        return languageVersions.stream().anyMatch(version -> version.language() == language);
    }

    private static String required(String value, String label) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El " + label + " de la raíz de código no puede estar vacío.");
        }
        return normalized;
    }

    private static String normalizeOrDefault(String value, String fallback) {
        String normalized = normalize(value);
        return normalized.isBlank() ? fallback : normalized;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
