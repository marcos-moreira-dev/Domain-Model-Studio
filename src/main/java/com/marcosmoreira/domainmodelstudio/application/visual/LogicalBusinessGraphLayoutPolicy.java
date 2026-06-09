package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNode;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** Política visual para ubicar tarjetas semánticas del Grafo lógico del negocio. */
public final class LogicalBusinessGraphLayoutPolicy {

    public static final double NODE_WIDTH = 226.0;
    public static final double NODE_HEIGHT = 124.0;

    private final VisualTextFitPolicy textFitPolicy = new VisualTextFitPolicy();

    public List<VisualNodeReference> visualReferences(LogicalBusinessGraphDocument document, int startingIndex) {
        Objects.requireNonNull(document, "document");
        int base = Math.max(0, startingIndex);
        List<LogicalBusinessGraphNode> orderedNodes = document.nodes().stream()
                .sorted(Comparator
                        .comparing((LogicalBusinessGraphNode node) -> node.kind().ordinal())
                        .thenComparing(LogicalBusinessGraphNode::code))
                .toList();
        java.util.ArrayList<VisualNodeReference> references = new java.util.ArrayList<>();
        for (int i = 0; i < orderedNodes.size(); i++) {
            LogicalBusinessGraphNode node = orderedNodes.get(i);
            VisualTextFitPolicy.BoxSize size = textFitPolicy.fitLargeCard(
                    new VisualTextFitPolicy.BoxSize(NODE_WIDTH, NODE_HEIGHT),
                    node.title(),
                    node.description());
            references.add(new VisualNodeReference(
                    VisualElementLayoutIds.logicalBusinessGraphNode(node.code()),
                    size.width(),
                    size.height(),
                    base + i));
        }
        return List.copyOf(references);
    }
}
