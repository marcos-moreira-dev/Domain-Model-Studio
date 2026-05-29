package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.nio.file.Path;
import java.util.List;

/** Resultado de detectar raíces lógicas de código dentro de una carpeta de proyecto. */
public record SourceRootDetectionResult(
        Path projectRoot,
        List<SourceRoot> sourceRoots,
        List<String> warnings
) {
    public SourceRootDetectionResult {
        if (projectRoot == null) {
            throw new IllegalArgumentException("La carpeta raíz detectada no puede ser nula.");
        }
        sourceRoots = List.copyOf(sourceRoots == null ? List.of() : sourceRoots);
        warnings = List.copyOf(warnings == null ? List.of() : warnings);
    }

    public List<SourceRoot> rootsFor(SourceLanguage language) {
        SourceLanguage normalized = language == null ? SourceLanguage.UNKNOWN : language;
        return sourceRoots.stream()
                .filter(root -> root.supports(normalized))
                .toList();
    }

    public List<SourceRoot> rootsForKind(SourceRootKind kind) {
        SourceRootKind normalized = kind == null ? SourceRootKind.UNKNOWN : kind;
        return sourceRoots.stream()
                .filter(root -> root.kind() == normalized)
                .toList();
    }

    public boolean hasMultipleSourceRoots() {
        return sourceRoots.size() > 1;
    }
}
