package com.marcosmoreira.domainmodelstudio.application.behavior;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdgeKind;
import java.util.Objects;

/** Actualiza relaciones de un diagrama de comportamiento. */
public final class UpdateBehaviorEdgeUseCase {
    public BehaviorDiagramDocument update(BehaviorDiagramDocument document, String edgeId, String sourceNodeId,
                                          String targetNodeId, BehaviorEdgeKind kind, String label, String condition,
                                          String notes) {
        Objects.requireNonNull(document, "document");
        BehaviorEdge edge = document.edges().stream().filter(item -> item.id().equals(edgeId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No existe relación: " + edgeId));
        return document.withUpdatedEdge(edge.withValues(sourceNodeId, targetNodeId, kind, label, condition, notes));
    }
}
