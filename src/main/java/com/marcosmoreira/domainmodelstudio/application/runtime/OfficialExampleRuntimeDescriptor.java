package com.marcosmoreira.domainmodelstudio.application.runtime;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Objects;

/** Descriptor runtime del ejemplo oficial principal de un tipo de proyecto. */
public record OfficialExampleRuntimeDescriptor(
        DiagramTypeId diagramTypeId,
        String exampleId,
        String title,
        String resource,
        boolean importable
) {

    public OfficialExampleRuntimeDescriptor {
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        exampleId = requireText(exampleId, "exampleId");
        title = requireText(title, "title");
        resource = requireText(resource, "resource");
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return value.strip();
    }
}
