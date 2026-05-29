package com.marcosmoreira.domainmodelstudio.application.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.util.Objects;

/** Agrega atributos o métodos a una clase UML. */
public final class AddUmlMemberUseCase {
    public UmlClassDiagramDocument add(UmlClassDiagramDocument document, String classId, UmlMemberKind kind) {
        Objects.requireNonNull(document, "document");
        UmlClassNode node = document.classById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Selecciona una clase para agregar miembros."));
        UmlMemberKind resolvedKind = kind == null ? UmlMemberKind.ATTRIBUTE : kind;
        String baseName = resolvedKind == UmlMemberKind.METHOD ? "nuevoMetodo" : "nuevoAtributo";
        String id = uniqueMemberId(document, node.id() + "_" + UmlClassDiagramIds.slug(baseName, "miembro"));
        UmlClassMember member = new UmlClassMember(id, resolvedKind, baseName,
                resolvedKind == UmlMemberKind.METHOD ? "void" : "String",
                resolvedKind == UmlMemberKind.METHOD ? baseName + "(): void" : "",
                UmlVisibility.PUBLIC, false, "");
        return document.withUpdatedClass(node.withMember(member));
    }

    private String uniqueMemberId(UmlClassDiagramDocument document, String base) {
        String candidate = base;
        int counter = 2;
        while (document.classOwningMember(candidate).isPresent()) {
            candidate = base + "_" + counter++;
        }
        return candidate;
    }
}
