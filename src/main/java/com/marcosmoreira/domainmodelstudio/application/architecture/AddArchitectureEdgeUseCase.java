package com.marcosmoreira.domainmodelstudio.application.architecture;

import com.marcosmoreira.domainmodelstudio.domain.architecture.*;
import java.util.Objects;

/** Agrega una relación de arquitectura entre dos elementos existentes. */
public final class AddArchitectureEdgeUseCase {
    public ArchitectureDiagramDocument add(ArchitectureDiagramDocument document, String sourceNodeId, String targetNodeId, ArchitectureEdgeKind kind) {
        Objects.requireNonNull(document, "document");
        ArchitectureEdgeKind resolvedKind = kind == null ? document.diagramKind().defaultEdgeKind() : kind;
        ArchitectureEdge edge = new ArchitectureEdge(
                ArchitectureDiagramIds.next(resolvedKind.name()),
                sourceNodeId,
                targetNodeId,
                resolvedKind,
                resolvedKind.displayName(),
                "",
                "");
        return document.withEdge(edge);
    }
}
