package com.marcosmoreira.domainmodelstudio.domain.catalog;

import java.util.Objects;

/** Categoría visible y trazable para agrupar tipos de diagrama. */
public record DiagramCategory(
        DiagramCategoryId id,
        String displayName,
        String purpose,
        int order
) {

    public DiagramCategory {
        Objects.requireNonNull(id, "id");
        displayName = requireText(displayName, "displayName");
        purpose = requireText(purpose, "purpose");
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return value;
    }
}
