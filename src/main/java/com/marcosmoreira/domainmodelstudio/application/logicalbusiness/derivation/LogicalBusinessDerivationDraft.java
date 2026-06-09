package com.marcosmoreira.domainmodelstudio.application.logicalbusiness.derivation;

import java.util.List;
import java.util.Objects;

/** Borrador Markdown compatible y revisable preparado desde el levantamiento lógico. */
public record LogicalBusinessDerivationDraft(
        LogicalBusinessDerivationTarget target,
        String title,
        String fileName,
        String markdown,
        List<String> warnings
) {
    public LogicalBusinessDerivationDraft {
        target = Objects.requireNonNull(target, "target");
        title = normalized(title, target.displayName());
        fileName = normalized(fileName, target.fileName());
        markdown = normalized(markdown, "");
        if (markdown.isBlank()) {
            throw new IllegalArgumentException("El borrador compatible no puede estar vacío.");
        }
        warnings = List.copyOf(warnings == null ? List.of() : warnings.stream()
                .filter(value -> value != null && !value.isBlank())
                .map(String::strip)
                .toList());
    }

    private static String normalized(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.strip();
    }
}
