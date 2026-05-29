package com.marcosmoreira.domainmodelstudio.application.datadictionary;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryEntity;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryStatus;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataEntityKind;
import java.util.Objects;

/** Actualiza los datos documentales de una entidad del diccionario. */
public final class UpdateDataDictionaryEntityUseCase {

    public DataDictionaryDocument update(
            DataDictionaryDocument document,
            String entityId,
            String displayName,
            String technicalName,
            String moduleName,
            DataEntityKind kind,
            DataDictionaryStatus status,
            String description,
            String notes
    ) {
        Objects.requireNonNull(document, "document");
        DataDictionaryEntity entity = document.entityById(entityId)
                .orElseThrow(() -> new IllegalArgumentException("No existe entidad para actualizar: " + entityId));
        DataDictionaryEntity updated = new DataDictionaryEntity(
                entity.id(),
                displayName,
                technicalName,
                description,
                moduleName,
                kind,
                entity.origin(),
                status,
                entity.fields(),
                notes);
        return document.withUpdatedEntity(updated);
    }
}
