package com.marcosmoreira.domainmodelstudio.application.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.util.Objects;

/** Actualiza datos descriptivos de una clase UML. */
public final class UpdateUmlClassUseCase {
    public UmlClassDiagramDocument update(
            UmlClassDiagramDocument document,
            String classId,
            String moduleId,
            String displayName,
            String packageName,
            UmlClassKind kind,
            UmlVisibility visibility,
            String responsibility,
            String description,
            String notes
    ) {
        Objects.requireNonNull(document, "document");
        UmlClassNode current = document.classById(classId)
                .orElseThrow(() -> new IllegalArgumentException("No existe clase UML para actualizar: " + classId));
        String normalizedModuleId = moduleId == null ? "" : moduleId.strip();
        if (!normalizedModuleId.isBlank()) {
            document.moduleById(normalizedModuleId)
                    .orElseThrow(() -> new IllegalArgumentException("No existe módulo UML: " + normalizedModuleId));
        }
        return document.withUpdatedClass(current.withDetails(normalizedModuleId, displayName, packageName,
                kind, visibility, responsibility, description, notes));
    }
}
