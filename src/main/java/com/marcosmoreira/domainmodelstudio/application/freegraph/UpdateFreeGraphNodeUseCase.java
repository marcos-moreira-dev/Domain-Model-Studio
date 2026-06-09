package com.marcosmoreira.domainmodelstudio.application.freegraph;

import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphNode;
import java.util.Objects;

/** Actualiza título y contenido de un nodo de Grafo libre. */
public final class UpdateFreeGraphNodeUseCase {

    public FreeGraphDocument updateNode(FreeGraphDocument document, String nodeId, String title, String content) {
        Objects.requireNonNull(document, "document");
        String id = normalize(nodeId);
        FreeGraphNode current = document.nodeById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe nodo para actualizar: " + id));
        String normalizedTitle = normalize(title);
        if (normalizedTitle.isBlank()) {
            throw new IllegalArgumentException("El nodo necesita un título.");
        }
        return document.withUpdatedNode(current.withDetails(normalizedTitle, normalize(content), current.orderIndex()));
    }

    public FreeGraphDocument reorderNode(FreeGraphDocument document, String nodeId, int orderIndex) {
        Objects.requireNonNull(document, "document");
        String id = normalize(nodeId);
        FreeGraphNode current = document.nodeById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe nodo para reordenar: " + id));
        return document.withUpdatedNode(current.withOrderIndex(orderIndex));
    }

    private String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
