package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphNode;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** Política visual mínima para convertir nodos semánticos de Grafo libre en cajas ubicables. */
public final class FreeGraphLayoutPolicy {

    public static final double NODE_WIDTH = 184.0;
    public static final double NODE_HEIGHT = 118.0;

    private final VisualTextFitPolicy textFitPolicy = new VisualTextFitPolicy();

    public List<VisualNodeReference> visualReferences(FreeGraphDocument document, int startingIndex) {
        Objects.requireNonNull(document, "document");
        int base = Math.max(0, startingIndex);
        List<FreeGraphNode> orderedNodes = document.nodes().stream()
                .sorted(Comparator
                        .comparingInt(FreeGraphNode::orderIndex)
                        .thenComparing(FreeGraphNode::title)
                        .thenComparing(FreeGraphNode::id))
                .toList();
        java.util.ArrayList<VisualNodeReference> references = new java.util.ArrayList<>();
        for (int i = 0; i < orderedNodes.size(); i++) {
            FreeGraphNode node = orderedNodes.get(i);
            VisualTextFitPolicy.BoxSize size = textFitPolicy.fitCard(
                    new VisualTextFitPolicy.BoxSize(NODE_WIDTH, NODE_HEIGHT),
                    node.title(),
                    node.content());
            references.add(new VisualNodeReference(
                    VisualElementLayoutIds.freeGraphNode(node.id()),
                    size.width(),
                    size.height(),
                    base + i));
        }
        return List.copyOf(references);
    }
}
