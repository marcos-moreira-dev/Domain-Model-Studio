package com.marcosmoreira.domainmodelstudio.application.behavior;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;

/** Crea documentos vacíos de procesos y UML de comportamiento. */
public final class CreateBehaviorDiagramUseCase {
    public BehaviorDiagramDocument createBlank(String projectName, BehaviorDiagramKind diagramKind) {
        return BehaviorDiagramDocument.blank(projectName, diagramKind);
    }
}
