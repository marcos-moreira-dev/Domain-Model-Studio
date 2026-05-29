package com.marcosmoreira.domainmodelstudio.application.behavior;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Reordena mensajes de UML Secuencia sin cambiar IDs ni contenido semántico. */
public final class MoveSequenceMessageUseCase {

    public enum Direction { UP, DOWN }

    private final SequenceMessageOrderPolicy orderPolicy = new SequenceMessageOrderPolicy();

    public BehaviorDiagramDocument move(BehaviorDiagramDocument document, String edgeId, Direction direction) {
        Objects.requireNonNull(document, "document");
        Objects.requireNonNull(direction, "direction");
        if (document.diagramKind() != BehaviorDiagramKind.UML_SEQUENCE) {
            return document;
        }
        String normalized = edgeId == null ? "" : edgeId.strip();
        List<BehaviorEdge> edges = new ArrayList<>(document.edges());
        int index = indexOf(edges, normalized);
        if (index < 0 || !orderPolicy.isTemporalMessage(edges.get(index))) {
            throw new IllegalArgumentException("Selecciona un mensaje de secuencia para reordenarlo.");
        }
        int target = findSwapTarget(edges, index, direction);
        if (target < 0) {
            return document;
        }
        BehaviorEdge current = edges.get(index);
        edges.set(index, edges.get(target));
        edges.set(target, current);
        return document.withEdges(edges);
    }

    private int findSwapTarget(List<BehaviorEdge> edges, int index, Direction direction) {
        int step = direction == Direction.UP ? -1 : 1;
        for (int candidate = index + step; candidate >= 0 && candidate < edges.size(); candidate += step) {
            if (orderPolicy.isTemporalMessage(edges.get(candidate))) {
                return candidate;
            }
        }
        return -1;
    }

    private int indexOf(List<BehaviorEdge> edges, String edgeId) {
        for (int index = 0; index < edges.size(); index++) {
            if (edges.get(index).id().equals(edgeId)) {
                return index;
            }
        }
        return -1;
    }
}
