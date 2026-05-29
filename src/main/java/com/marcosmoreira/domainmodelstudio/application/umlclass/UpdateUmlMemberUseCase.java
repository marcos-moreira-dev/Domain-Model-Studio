package com.marcosmoreira.domainmodelstudio.application.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.util.Objects;

/** Actualiza atributos, métodos o constructores de clases UML. */
public final class UpdateUmlMemberUseCase {
    public UmlClassDiagramDocument update(
            UmlClassDiagramDocument document,
            String memberId,
            UmlMemberKind kind,
            String name,
            String type,
            String signature,
            UmlVisibility visibility,
            boolean staticMember,
            String description
    ) {
        Objects.requireNonNull(document, "document");
        UmlClassNode owner = document.classOwningMember(memberId)
                .orElseThrow(() -> new IllegalArgumentException("No existe miembro UML para actualizar: " + memberId));
        UmlClassMember current = owner.memberById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("No existe miembro UML: " + memberId));
        return document.withUpdatedClass(owner.withUpdatedMember(current.withDetails(kind, name, type, signature,
                visibility, staticMember, description)));
    }
}
