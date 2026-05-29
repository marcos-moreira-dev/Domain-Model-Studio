package com.marcosmoreira.domainmodelstudio.application.behavior;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import java.util.Objects;

/** Elimina elementos o relaciones del diagrama de comportamiento. */
public final class RemoveBehaviorItemUseCase {
    public BehaviorDiagramDocument removeNode(BehaviorDiagramDocument document, String nodeId) {
        Objects.requireNonNull(document, "document");
        return document.withoutNode(nodeId);
    }

    public BehaviorDiagramDocument removeEdge(BehaviorDiagramDocument document, String edgeId) {
        Objects.requireNonNull(document, "document");
        return document.withoutEdge(edgeId);
    }
}
