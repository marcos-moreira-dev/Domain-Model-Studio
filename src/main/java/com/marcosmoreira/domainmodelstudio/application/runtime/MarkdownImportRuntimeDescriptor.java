package com.marcosmoreira.domainmodelstudio.application.runtime;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Objects;

/** Contrato runtime declarativo para importación Markdown de un tipo de proyecto. */
public record MarkdownImportRuntimeDescriptor(
        DiagramTypeId diagramTypeId,
        String grammarResourceId,
        String dispatcherContract
) {

    public MarkdownImportRuntimeDescriptor {
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        grammarResourceId = normalize(grammarResourceId);
        dispatcherContract = requireText(dispatcherContract, "dispatcherContract");
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return value.strip();
    }
}
