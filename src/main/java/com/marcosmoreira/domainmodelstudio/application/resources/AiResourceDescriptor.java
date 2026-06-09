package com.marcosmoreira.domainmodelstudio.application.resources;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Objects;

/** Recurso Markdown exportable para trabajo con IA. */
public record AiResourceDescriptor(
        String id,
        String fileName,
        DiagramTypeId diagramTypeId,
        String classpathLocation,
        boolean exportable,
        boolean importableByApplication,
        String description
) {

    public AiResourceDescriptor {
        id = requireText(id, "id");
        fileName = requireText(fileName, "fileName");
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        classpathLocation = requireText(classpathLocation, "classpathLocation");
        description = description == null ? "" : description.strip();
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return value;
    }
}
