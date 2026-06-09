package com.marcosmoreira.domainmodelstudio.application.examples;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Objects;

/** Descriptor de un ejemplo oficial incluido con la aplicación. */
public record OfficialExampleDescriptor(
        String id,
        String title,
        DiagramTypeId diagramTypeId,
        String diagramTypeName,
        String classpathLocation,
        String summary,
        boolean importable
) {

    public OfficialExampleDescriptor {
        id = requireText(id, "id");
        title = requireText(title, "title");
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        diagramTypeName = requireText(diagramTypeName, "diagramTypeName");
        classpathLocation = requireText(classpathLocation, "classpathLocation");
        summary = summary == null ? "" : summary.strip();
    }

    public String sourceName() {
        int slash = classpathLocation.lastIndexOf('/');
        return slash < 0 ? classpathLocation : classpathLocation.substring(slash + 1);
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        String normalized = value.strip();
        if (normalized.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return normalized;
    }
}
