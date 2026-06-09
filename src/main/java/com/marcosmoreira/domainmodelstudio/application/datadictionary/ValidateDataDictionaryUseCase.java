package com.marcosmoreira.domainmodelstudio.application.datadictionary;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryEntity;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryField;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.LogicalDataType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Revisa consistencia mínima del diccionario de datos. */
public final class ValidateDataDictionaryUseCase {

    public DataDictionaryValidationResult validate(DataDictionaryDocument document) {
        Objects.requireNonNull(document, "document");
        List<String> warnings = new ArrayList<>();
        if (document.entities().isEmpty()) {
            warnings.add("El diccionario aún no tiene entidades documentadas.");
        }
        for (DataDictionaryEntity entity : document.entities()) {
            validateEntity(entity, warnings);
        }
        return new DataDictionaryValidationResult(warnings);
    }

    private static void validateEntity(DataDictionaryEntity entity, List<String> warnings) {
        if (entity.description().isBlank()) {
            warnings.add("Entidad sin descripción: " + entity.displayName());
        }
        if (entity.fields().isEmpty()) {
            warnings.add("Entidad sin campos: " + entity.displayName());
        }
        for (DataDictionaryField field : entity.fields()) {
            validateField(entity, field, warnings);
        }
    }

    private static void validateField(DataDictionaryEntity entity, DataDictionaryField field, List<String> warnings) {
        if (field.logicalType() == LogicalDataType.UNKNOWN) {
            warnings.add("Campo con tipo lógico desconocido: " + entity.displayName() + "." + field.displayName());
        }
        if (field.description().isBlank()) {
            warnings.add("Campo sin descripción: " + entity.displayName() + "." + field.displayName());
        }
        if (field.isRequired() && field.example().isBlank()) {
            warnings.add("Campo obligatorio sin ejemplo: " + entity.displayName() + "." + field.displayName());
        }
    }
}
