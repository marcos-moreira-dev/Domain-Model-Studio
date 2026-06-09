package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** Preserva layouts de notas visuales al reconciliar o regenerar layouts semanticos. */
final class VisualCommentLayoutPreserver {

    List<NodeLayout> appendFrom(DiagramLayout activeLayout, List<NodeLayout> semanticNodes) {
        List<NodeLayout> updated = new ArrayList<>(semanticNodes == null ? List.of() : semanticNodes);
        Set<DiagramElementId> existing = updated.stream()
                .map(NodeLayout::elementId)
                .collect(java.util.stream.Collectors.toSet());
        for (NodeLayout node : activeLayout.nodes()) {
            if (VisualElementLayoutIds.isVisualComment(node.elementId()) && existing.add(node.elementId())) {
                updated.add(node);
            }
        }
        return updated;
    }
}
