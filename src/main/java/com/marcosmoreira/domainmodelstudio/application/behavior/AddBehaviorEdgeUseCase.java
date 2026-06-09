package com.marcosmoreira.domainmodelstudio.application.behavior;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import java.util.Locale;
import java.util.Objects;

/** Agrega relaciones entre elementos de un diagrama de comportamiento. */
public final class AddBehaviorEdgeUseCase {
    public BehaviorDiagramDocument add(BehaviorDiagramDocument document, String sourceNodeId, String targetNodeId, BehaviorEdgeKind kind) {
        Objects.requireNonNull(document, "document");
        if (document.nodes().size() < 2) {
            throw new IllegalArgumentException("Crea al menos dos elementos antes de agregar una relación.");
        }
        String source = normalize(sourceNodeId).isBlank() ? document.nodes().get(0).id() : normalize(sourceNodeId);
        String target = normalize(targetNodeId).isBlank() ? firstDifferentNode(document, source).id() : normalize(targetNodeId);
        BehaviorEdgeKind edgeKind = kind == null ? document.diagramKind().defaultEdgeKind() : kind;
        String id = uniqueId(document, edgeKind.name().toLowerCase(Locale.ROOT).replace('_', '-'));
        return document.withEdge(new BehaviorEdge(id, source, target, edgeKind, edgeKind.displayName(), "", ""));
    }

    private BehaviorNode firstDifferentNode(BehaviorDiagramDocument document, String source) {
        return document.nodes().stream().filter(node -> !node.id().equals(source)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Crea al menos dos elementos antes de agregar una relación."));
    }

    private String uniqueId(BehaviorDiagramDocument document, String prefix) {
        int index = document.edges().size() + 1;
        String candidate;
        do {
            candidate = prefix + "-" + index++;
            String current = candidate;
            if (document.edges().stream().noneMatch(edge -> edge.id().equals(current))) {
                break;
            }
        } while (true);
        return candidate;
    }

    private String normalize(String value) { return value == null ? "" : value.strip(); }
}
