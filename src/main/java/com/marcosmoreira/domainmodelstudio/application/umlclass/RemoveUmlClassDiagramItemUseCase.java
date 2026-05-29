package com.marcosmoreira.domainmodelstudio.application.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import java.util.Objects;

/** Elimina módulos, clases, miembros o relaciones del diagrama UML Clases. */
public final class RemoveUmlClassDiagramItemUseCase {
    public UmlClassDiagramDocument removeModule(UmlClassDiagramDocument document, String moduleId) {
        Objects.requireNonNull(document, "document");
        return document.withoutModule(moduleId);
    }

    public UmlClassDiagramDocument removeClass(UmlClassDiagramDocument document, String classId) {
        Objects.requireNonNull(document, "document");
        return document.withoutClass(classId);
    }

    public UmlClassDiagramDocument removeMember(UmlClassDiagramDocument document, String memberId) {
        Objects.requireNonNull(document, "document");
        UmlClassNode owner = document.classOwningMember(memberId)
                .orElseThrow(() -> new IllegalArgumentException("No existe miembro UML para eliminar: " + memberId));
        return document.withUpdatedClass(owner.withoutMember(memberId));
    }

    public UmlClassDiagramDocument removeRelation(UmlClassDiagramDocument document, String relationId) {
        Objects.requireNonNull(document, "document");
        return document.withoutRelation(relationId);
    }
}
