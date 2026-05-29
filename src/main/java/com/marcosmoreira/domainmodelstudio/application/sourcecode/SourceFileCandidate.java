package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.nio.file.Path;

/** Archivo candidato a ser leído por un adaptador de lenguaje. */
public record SourceFileCandidate(
        Path absolutePath,
        Path relativePath,
        SourceLanguage language,
        String sourceRootId
) {
    public SourceFileCandidate {
        if (absolutePath == null) {
            throw new IllegalArgumentException("La ruta absoluta del archivo fuente no puede ser nula.");
        }
        relativePath = relativePath == null ? absolutePath.getFileName() : relativePath;
        language = language == null ? SourceLanguage.UNKNOWN : language;
        sourceRootId = required(sourceRootId, "sourceRootId");
    }

    private static String required(String value, String label) {
        String normalized = value == null ? "" : value.strip();
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El " + label + " del archivo fuente no puede estar vacío.");
        }
        return normalized;
    }
}
