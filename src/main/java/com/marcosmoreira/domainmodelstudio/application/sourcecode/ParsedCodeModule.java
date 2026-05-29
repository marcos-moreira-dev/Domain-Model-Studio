package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.nio.file.Path;

/** Módulo, package o carpeta funcional detectada en el código fuente. */
public record ParsedCodeModule(
        String id,
        String sourceRootId,
        String qualifiedName,
        String displayName,
        Path relativePath
) {
    public ParsedCodeModule {
        id = required(id, "id");
        sourceRootId = required(sourceRootId, "sourceRootId");
        qualifiedName = normalize(qualifiedName);
        displayName = normalizeOrDefault(displayName, qualifiedName.isBlank() ? id : qualifiedName);
    }

    private static String required(String value, String label) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El " + label + " del módulo fuente no puede estar vacío.");
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
