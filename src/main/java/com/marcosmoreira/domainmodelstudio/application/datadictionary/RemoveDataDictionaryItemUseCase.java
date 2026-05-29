package com.marcosmoreira.domainmodelstudio.application.datadictionary;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryEntity;
import java.util.Objects;

/** Elimina entidades o campos del diccionario de datos. */
public final class RemoveDataDictionaryItemUseCase {

    public DataDictionaryDocument removeEntity(DataDictionaryDocument document, String entityId) {
        Objects.requireNonNull(document, "document");
        return document.withoutEntity(entityId);
    }

    public DataDictionaryDocument removeField(DataDictionaryDocument document, String entityId, String fieldName) {
        Objects.requireNonNull(document, "document");
        DataDictionaryEntity entity = document.entityById(entityId)
                .orElseThrow(() -> new IllegalArgumentException("No existe entidad para eliminar campo: " + entityId));
        return document.withUpdatedEntity(entity.withoutField(fieldName));
    }
}
