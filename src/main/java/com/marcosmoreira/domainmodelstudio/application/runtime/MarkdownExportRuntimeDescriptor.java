package com.marcosmoreira.domainmodelstudio.application.runtime;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Objects;

/** Contrato runtime declarativo para exportación Markdown de un tipo de proyecto. */
public record MarkdownExportRuntimeDescriptor(
        DiagramTypeId diagramTypeId,
        String exporterContract
) {

    public MarkdownExportRuntimeDescriptor {
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        exporterContract = requireText(exporterContract, "exporterContract");
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return value.strip();
    }
}
