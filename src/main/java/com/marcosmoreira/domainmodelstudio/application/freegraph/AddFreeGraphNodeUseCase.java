package com.marcosmoreira.domainmodelstudio.application.freegraph;

import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphNode;
import java.util.Objects;

/** Agrega nodos semánticos al Grafo libre. */
public final class AddFreeGraphNodeUseCase {

    public FreeGraphDocument addNode(FreeGraphDocument document, String title) {
        return addNode(document, title, "");
    }

    public FreeGraphDocument addNode(FreeGraphDocument document, String title, String content) {
        Objects.requireNonNull(document, "document");
        String normalizedTitle = normalizeTitle(title);
        String id = FreeGraphIds.uniqueNodeId(document, normalizedTitle);
        int orderIndex = document.nodeCount();
        return document.withNode(new FreeGraphNode(id, normalizedTitle, normalize(content), orderIndex));
    }

    private String normalizeTitle(String title) {
        String normalized = normalize(title);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El nodo necesita un título.");
        }
        return normalized;
    }

    private String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
