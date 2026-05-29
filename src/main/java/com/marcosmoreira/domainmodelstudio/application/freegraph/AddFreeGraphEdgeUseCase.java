package com.marcosmoreira.domainmodelstudio.application.freegraph;

import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdgeDirection;
import java.util.Objects;

/** Crea relaciones entre nodos existentes del Grafo libre. */
public final class AddFreeGraphEdgeUseCase {

    public FreeGraphDocument addEdge(
            FreeGraphDocument document,
            String sourceNodeId,
            String targetNodeId,
            FreeGraphEdgeDirection direction,
            String label
    ) {
        return addEdge(document, sourceNodeId, targetNodeId, direction, label, "");
    }

    public FreeGraphDocument addEdge(
            FreeGraphDocument document,
            String sourceNodeId,
            String targetNodeId,
            FreeGraphEdgeDirection direction,
            String label,
            String notes
    ) {
        Objects.requireNonNull(document, "document");
        String source = normalize(sourceNodeId);
        String target = normalize(targetNodeId);
        if (source.isBlank() || target.isBlank()) {
            throw new IllegalArgumentException("Selecciona nodo origen y nodo destino para crear la relación.");
        }
        document.nodeById(source).orElseThrow(() -> new IllegalArgumentException("No existe nodo origen: " + source));
        document.nodeById(target).orElseThrow(() -> new IllegalArgumentException("No existe nodo destino: " + target));
        String id = FreeGraphIds.uniqueEdgeId(document, source, target, label);
        return document.withEdge(new FreeGraphEdge(id, source, target,
                direction == null ? FreeGraphEdgeDirection.DIRECTED : direction, label, notes));
    }

    private String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
