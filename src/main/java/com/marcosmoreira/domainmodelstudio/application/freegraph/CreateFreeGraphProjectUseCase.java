package com.marcosmoreira.domainmodelstudio.application.freegraph;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphKind;
import java.util.Objects;

/** Crea el agregado de proyecto .dms para un Grafo libre sin activar todavía la UI visual. */
public final class CreateFreeGraphProjectUseCase {

    public DiagramProject createBlankProject(String projectId, String projectTitle) {
        FreeGraphDocument document = new CreateFreeGraphUseCase().createBlank(projectTitle, FreeGraphKind.MIXED);
        return createProject(projectId, projectTitle, document);
    }

    public DiagramProject createProject(String projectId, String projectTitle, FreeGraphDocument document) {
        Objects.requireNonNull(document, "document");
        return DiagramProject.blank(projectId, projectTitle, DiagramTypeId.FREE_GRAPH)
                .withFreeGraph(document);
    }
}
