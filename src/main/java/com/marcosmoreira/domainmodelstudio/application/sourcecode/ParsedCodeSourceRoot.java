package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.nio.file.Path;
import java.util.List;

/** Raíz de código ya normalizada dentro del modelo intermedio independiente de lenguaje. */
public record ParsedCodeSourceRoot(
        String id,
        String displayName,
        Path path,
        SourceRootKind kind,
        List<SourceLanguageVersion> languageVersions
) {
    public ParsedCodeSourceRoot {
        id = required(id, "id");
        displayName = normalizeOrDefault(displayName, id);
        if (path == null) {
            throw new IllegalArgumentException("La ruta de la raíz parseada no puede ser nula.");
        }
        kind = kind == null ? SourceRootKind.UNKNOWN : kind;
        languageVersions = List.copyOf(languageVersions == null ? List.of() : languageVersions);
    }

    public static ParsedCodeSourceRoot from(SourceRoot sourceRoot) {
        return new ParsedCodeSourceRoot(sourceRoot.id(), sourceRoot.displayName(), sourceRoot.path(),
                sourceRoot.kind(), sourceRoot.languageVersions());
    }

    private static String required(String value, String label) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El " + label + " de la raíz parseada no puede estar vacío.");
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
