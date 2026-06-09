package com.marcosmoreira.domainmodelstudio.application.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;

/** Crea un documento UML Clases vacío. */
public final class CreateUmlClassDiagramUseCase {
    public UmlClassDiagramDocument createBlank(String projectName) {
        return UmlClassDiagramDocument.blank(projectName);
    }
}
