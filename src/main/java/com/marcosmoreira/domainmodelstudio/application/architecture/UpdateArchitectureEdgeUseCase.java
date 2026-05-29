package com.marcosmoreira.domainmodelstudio.application.architecture;

import com.marcosmoreira.domainmodelstudio.domain.architecture.*;
import java.util.Objects;

/** Actualiza una relación de arquitectura. */
public final class UpdateArchitectureEdgeUseCase {
    public ArchitectureDiagramDocument update(ArchitectureDiagramDocument document, String edgeId, String sourceNodeId,
                                              String targetNodeId, ArchitectureEdgeKind kind, String label,
                                              String protocol, String notes) {
        Objects.requireNonNull(document, "document");
        for (ArchitectureEdge edge : document.edges()) {
            if (edge.id().equals(edgeId)) {
                return document.withUpdatedEdge(edge.withValues(sourceNodeId, targetNodeId, kind, label, protocol, notes));
            }
        }
        throw new IllegalArgumentException("No existe relación: " + edgeId);
    }
}
