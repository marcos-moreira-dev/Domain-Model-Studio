package com.marcosmoreira.domainmodelstudio.application.freegraph;

import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdgeDirection;
import java.util.Objects;

/** Actualiza extremos, dirección, etiqueta y notas de una relación del Grafo libre. */
public final class UpdateFreeGraphEdgeUseCase {

    public FreeGraphDocument updateEdge(
            FreeGraphDocument document,
            String edgeId,
            String sourceNodeId,
            String targetNodeId,
            FreeGraphEdgeDirection direction,
            String label,
            String notes
    ) {
        Objects.requireNonNull(document, "document");
        String id = normalize(edgeId);
        FreeGraphEdge current = document.edgeById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe relación para actualizar: " + id));
        String source = normalize(sourceNodeId);
        String target = normalize(targetNodeId);
        document.nodeById(source).orElseThrow(() -> new IllegalArgumentException("No existe nodo origen: " + source));
        document.nodeById(target).orElseThrow(() -> new IllegalArgumentException("No existe nodo destino: " + target));
        return document.withUpdatedEdge(current.withDetails(source, target,
                direction == null ? current.direction() : direction, label, notes));
    }

    private String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
