package com.marcosmoreira.domainmodelstudio.application.behavior;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdgeKind;
import java.util.List;
import java.util.Objects;

/**
 * Orden temporal estable para UML Secuencia.
 *
 * <p>En secuencia, el eje vertical no es libre: el orden de la lista de mensajes
 * representa el tiempo. Esta política concentra esa decisión para que render,
 * validación, paneles y exportación no inventen órdenes distintos.</p>
 */
public final class SequenceMessageOrderPolicy {

    public List<BehaviorEdge> orderedMessages(BehaviorDiagramDocument document) {
        if (document == null || document.diagramKind() != BehaviorDiagramKind.UML_SEQUENCE) {
            return List.of();
        }
        return document.edges().stream()
                .filter(SequenceMessageOrderPolicy::isSequenceMessage)
                .toList();
    }

    public int temporalIndex(BehaviorDiagramDocument document, String edgeId) {
        String normalized = normalize(edgeId);
        List<BehaviorEdge> messages = orderedMessages(document);
        for (int index = 0; index < messages.size(); index++) {
            if (messages.get(index).id().equals(normalized)) {
                return index;
            }
        }
        return -1;
    }

    public int displayNumber(BehaviorDiagramDocument document, String edgeId) {
        int index = temporalIndex(document, edgeId);
        return index < 0 ? 0 : index + 1;
    }

    public boolean isTemporalMessage(BehaviorEdge edge) {
        return isSequenceMessage(edge);
    }

    public static boolean isSequenceMessage(BehaviorEdge edge) {
        if (edge == null) {
            return false;
        }
        return edge.kind() == BehaviorEdgeKind.MESSAGE
                || edge.kind() == BehaviorEdgeKind.ASYNC_MESSAGE
                || edge.kind() == BehaviorEdgeKind.RETURN_MESSAGE;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
