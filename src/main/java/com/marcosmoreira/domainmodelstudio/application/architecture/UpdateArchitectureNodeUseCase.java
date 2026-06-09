package com.marcosmoreira.domainmodelstudio.application.architecture;

import com.marcosmoreira.domainmodelstudio.domain.architecture.*;
import java.util.Objects;

/** Actualiza propiedades editables de un elemento de arquitectura. */
public final class UpdateArchitectureNodeUseCase {
    public ArchitectureDiagramDocument update(ArchitectureDiagramDocument document, String nodeId, ArchitectureNodeKind kind,
                                              String displayName, String technology, String owner, String environment,
                                              String description, String notes, int orderIndex) {
        Objects.requireNonNull(document, "document");
        ArchitectureNode node = document.nodeById(nodeId).orElseThrow(() -> new IllegalArgumentException("No existe elemento: " + nodeId));
        return document.withUpdatedNode(node.withValues(kind, displayName, technology, owner, environment, description, notes, orderIndex));
    }
}
