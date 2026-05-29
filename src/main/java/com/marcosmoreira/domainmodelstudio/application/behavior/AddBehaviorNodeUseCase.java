package com.marcosmoreira.domainmodelstudio.application.behavior;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import java.util.Locale;
import java.util.Objects;

/** Agrega elementos a un diagrama de comportamiento. */
public final class AddBehaviorNodeUseCase {
    public BehaviorDiagramDocument add(BehaviorDiagramDocument document, BehaviorNodeKind kind, String displayName) {
        Objects.requireNonNull(document, "document");
        BehaviorNodeKind nodeKind = kind == null ? document.diagramKind().defaultNodeKind() : kind;
        String id = uniqueId(document, nodeKind.name().toLowerCase(Locale.ROOT).replace('_', '-'));
        BehaviorNode node = new BehaviorNode(id, nodeKind, displayName, "", "", "", document.nodes().size());
        return document.withNode(node);
    }

    private String uniqueId(BehaviorDiagramDocument document, String prefix) {
        String base = prefix == null || prefix.isBlank() ? "elemento" : prefix;
        int index = document.nodes().size() + 1;
        String candidate;
        do {
            candidate = base + "-" + index++;
        } while (document.nodeById(candidate).isPresent());
        return candidate;
    }
}
