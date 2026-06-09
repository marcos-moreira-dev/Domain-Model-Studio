package com.marcosmoreira.domainmodelstudio.application.datadictionary;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryEntity;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryField;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldConstraint;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldVisibility;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.LogicalDataType;
import java.util.Objects;
import java.util.Set;

/** Actualiza un campo lógico documentado en una entidad del diccionario. */
public final class UpdateDataDictionaryFieldUseCase {

    public DataDictionaryDocument update(
            DataDictionaryDocument document,
            String entityId,
            String originalFieldName,
            String displayName,
            String technicalName,
            LogicalDataType logicalType,
            String physicalTypeSuggestion,
            Set<FieldConstraint> constraints,
            String foreignKeyReference,
            String defaultValue,
            String expectedFormat,
            String description,
            String businessRule,
            String validationRule,
            String example,
            Set<FieldVisibility> visibility,
            boolean userEditable,
            String notes
    ) {
        Objects.requireNonNull(document, "document");
        DataDictionaryEntity entity = document.entityById(entityId)
                .orElseThrow(() -> new IllegalArgumentException("No existe entidad para actualizar campo: " + entityId));
        DataDictionaryField updated = new DataDictionaryField(
                normalizeTechnicalName(technicalName, displayName),
                displayName,
                normalizeTechnicalName(technicalName, displayName),
                logicalType,
                physicalTypeSuggestion,
                constraints,
                foreignKeyReference,
                defaultValue,
                expectedFormat,
                description,
                businessRule,
                validationRule,
                example,
                visibility,
                userEditable,
                notes);
        return document.withUpdatedEntity(entity.withUpdatedField(originalFieldName, updated));
    }

    private static String normalizeTechnicalName(String technicalName, String displayName) {
        String normalized = technicalName == null ? "" : technicalName.strip();
        if (!normalized.isBlank()) {
            return normalized;
        }
        String fallback = displayName == null ? "" : displayName.toLowerCase(java.util.Locale.ROOT)
                .replaceAll("[^a-z0-9áéíóúñ]+", "_")
                .replaceAll("^_+|_+$", "");
        return fallback.isBlank() ? "campo" : fallback;
    }
}
