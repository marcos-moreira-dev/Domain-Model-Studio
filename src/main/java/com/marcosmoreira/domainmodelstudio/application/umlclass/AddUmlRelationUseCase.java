package com.marcosmoreira.domainmodelstudio.application.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlRelationKind;
import java.util.Objects;

/** Agrega relaciones estructurales entre clases UML. */
public final class AddUmlRelationUseCase {
    public UmlClassDiagramDocument add(UmlClassDiagramDocument document, String sourceClassId, String targetClassId, UmlRelationKind kind) {
        Objects.requireNonNull(document, "document");
        String source = sourceClassId == null ? "" : sourceClassId.strip();
        String target = targetClassId == null ? "" : targetClassId.strip();
        if (source.isBlank() || target.isBlank()) {
            throw new IllegalArgumentException("Selecciona clase origen y destino para crear relación UML.");
        }
        document.classById(source).orElseThrow(() -> new IllegalArgumentException("No existe clase origen: " + source));
        document.classById(target).orElseThrow(() -> new IllegalArgumentException("No existe clase destino: " + target));
        String id = uniqueRelationId(document, source + "_to_" + target);
        return document.withRelation(new UmlClassRelation(id, source, target,
                kind == null ? UmlRelationKind.DEPENDENCY : kind, "", "", ""));
    }

    private String uniqueRelationId(UmlClassDiagramDocument document, String base) {
        String candidate = base;
        int counter = 2;
        while (document.relationById(candidate).isPresent()) {
            candidate = base + "_" + counter++;
        }
        return candidate;
    }
}
