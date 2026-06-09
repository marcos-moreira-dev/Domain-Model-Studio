package com.marcosmoreira.domainmodelstudio.application.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import java.util.Objects;

/** Actualiza el agrupador de un módulo/carpeta UML. */
public final class UpdateUmlModuleUseCase {
    public UmlClassDiagramDocument update(UmlClassDiagramDocument document, String moduleId, String displayName, String path, String description, String notes) {
        Objects.requireNonNull(document, "document");
        UmlModuleGroup current = document.moduleById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("No existe módulo UML para actualizar: " + moduleId));
        return document.withUpdatedModule(current.withDetails(displayName, path, description, notes));
    }
}
