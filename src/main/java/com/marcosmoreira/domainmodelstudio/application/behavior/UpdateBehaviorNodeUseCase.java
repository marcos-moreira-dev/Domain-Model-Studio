package com.marcosmoreira.domainmodelstudio.application.behavior;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import java.util.Objects;

/** Actualiza propiedades de un elemento de comportamiento. */
public final class UpdateBehaviorNodeUseCase {
    public BehaviorDiagramDocument update(BehaviorDiagramDocument document, String nodeId, BehaviorNodeKind kind,
                                          String displayName, String owner, String description, String notes, int orderIndex) {
        Objects.requireNonNull(document, "document");
        BehaviorNode node = document.nodeById(nodeId)
                .orElseThrow(() -> new IllegalArgumentException("No existe elemento: " + nodeId));
        return document.withUpdatedNode(node.withValues(kind, displayName, owner, description, notes, orderIndex));
    }
}
