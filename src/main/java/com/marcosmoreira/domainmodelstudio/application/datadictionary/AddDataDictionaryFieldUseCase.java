package com.marcosmoreira.domainmodelstudio.application.datadictionary;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryEntity;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryField;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldConstraint;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldVisibility;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.LogicalDataType;
import java.util.Objects;
import java.util.Set;

/** Agrega un campo lógico a una entidad del diccionario de datos. */
public final class AddDataDictionaryFieldUseCase {

    public DataDictionaryDocument add(DataDictionaryDocument document, String entityId, String baseName) {
        Objects.requireNonNull(document, "document");
        DataDictionaryEntity entity = document.entityById(entityId)
                .orElseThrow(() -> new IllegalArgumentException("Selecciona una entidad para agregar campo."));
        String name = uniqueFieldName(entity, normalizeBaseName(baseName));
        DataDictionaryField field = new DataDictionaryField(
                stableTechnicalName(name),
                name,
                stableTechnicalName(name),
                LogicalDataType.SHORT_TEXT,
                "",
                Set.of(FieldConstraint.VISIBLE_IN_FORM),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                Set.of(FieldVisibility.FORM, FieldVisibility.TABLE),
                true,
                "");
        return document.withUpdatedEntity(entity.withField(field));
    }

    private static String uniqueFieldName(DataDictionaryEntity entity, String baseName) {
        String candidate = baseName;
        int suffix = 2;
        while (containsField(entity, candidate)) {
            candidate = baseName + " " + suffix++;
        }
        return candidate;
    }

    private static boolean containsField(DataDictionaryEntity entity, String displayName) {
        String technical = stableTechnicalName(displayName);
        return entity.fields().stream()
                .anyMatch(field -> field.displayName().equalsIgnoreCase(displayName)
                        || field.name().equalsIgnoreCase(technical)
                        || field.technicalName().equalsIgnoreCase(technical));
    }

    private static String normalizeBaseName(String baseName) {
        String normalized = baseName == null ? "" : baseName.strip();
        return normalized.isBlank() ? "campo" : normalized;
    }

    private static String stableTechnicalName(String value) {
        String normalized = value == null ? "" : value.toLowerCase(java.util.Locale.ROOT)
                .replaceAll("[^a-z0-9áéíóúñ]+", "_")
                .replaceAll("^_+|_+$", "");
        return normalized.isBlank() ? "campo" : normalized;
    }
}
