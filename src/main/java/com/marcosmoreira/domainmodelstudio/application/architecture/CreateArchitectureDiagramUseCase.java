package com.marcosmoreira.domainmodelstudio.application.architecture;

import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramKind;

/** Crea documentos base de arquitectura. */
public final class CreateArchitectureDiagramUseCase {
    public ArchitectureDiagramDocument createBlank(String projectName, ArchitectureDiagramKind diagramKind) {
        return ArchitectureDiagramDocument.blank(projectName, diagramKind);
    }
}
