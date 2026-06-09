package com.marcosmoreira.domainmodelstudio.application.architecture;

import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramDocument;
import java.util.Objects;

/** Elimina elementos o relaciones de arquitectura. */
public final class RemoveArchitectureItemUseCase {
    public ArchitectureDiagramDocument removeNode(ArchitectureDiagramDocument document, String nodeId) {
        Objects.requireNonNull(document, "document");
        return document.withoutNode(nodeId);
    }

    public ArchitectureDiagramDocument removeEdge(ArchitectureDiagramDocument document, String edgeId) {
        Objects.requireNonNull(document, "document");
        return document.withoutEdge(edgeId);
    }
}
