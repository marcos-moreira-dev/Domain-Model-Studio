package com.marcosmoreira.domainmodelstudio.application.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlRelationKind;
import java.util.Objects;

/** Actualiza una relación entre clases UML. */
public final class UpdateUmlRelationUseCase {
    public UmlClassDiagramDocument update(
            UmlClassDiagramDocument document,
            String relationId,
            String sourceClassId,
            String targetClassId,
            UmlRelationKind kind,
            String label,
            String description,
            String notes
    ) {
        Objects.requireNonNull(document, "document");
        UmlClassRelation current = document.relationById(relationId)
                .orElseThrow(() -> new IllegalArgumentException("No existe relación UML para actualizar: " + relationId));
        document.classById(sourceClassId).orElseThrow(() -> new IllegalArgumentException("No existe clase origen: " + sourceClassId));
        document.classById(targetClassId).orElseThrow(() -> new IllegalArgumentException("No existe clase destino: " + targetClassId));
        return document.withUpdatedRelation(current.withDetails(sourceClassId, targetClassId, kind, label, description, notes));
    }
}
