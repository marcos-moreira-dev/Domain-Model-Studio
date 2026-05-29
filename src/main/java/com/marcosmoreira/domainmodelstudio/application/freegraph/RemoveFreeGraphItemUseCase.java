package com.marcosmoreira.domainmodelstudio.application.freegraph;

import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import java.util.Objects;

/** Elimina nodos o relaciones del Grafo libre. */
public final class RemoveFreeGraphItemUseCase {

    public FreeGraphDocument removeNode(FreeGraphDocument document, String nodeId) {
        Objects.requireNonNull(document, "document");
        return document.withoutNode(nodeId);
    }

    public FreeGraphDocument removeEdge(FreeGraphDocument document, String edgeId) {
        Objects.requireNonNull(document, "document");
        return document.withoutEdge(edgeId);
    }
}
