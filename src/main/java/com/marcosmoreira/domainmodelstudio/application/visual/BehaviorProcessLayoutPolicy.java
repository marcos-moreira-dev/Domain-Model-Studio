package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import java.util.Objects;

/**
 * Tamaños iniciales especializados para BPMN básico y flujo operativo.
 *
 * <p>No ubica nodos dentro de carriles todavía; solo evita que carriles,
 * responsables y documentos nazcan como cajas genéricas idénticas.</p>
 */
public final class BehaviorProcessLayoutPolicy {

    private final VisualTextFitPolicy textFitPolicy = new VisualTextFitPolicy();

    public VisualNodeReference visualReference(BehaviorDiagramKind diagramKind, BehaviorNode node, int fallbackIndex) {
        Objects.requireNonNull(node, "node");
        DiagramSize base = sizeFor(diagramKind, node.kind());
        VisualTextFitPolicy.BoxSize fitted = node.kind() == BehaviorNodeKind.DECISION
                ? textFitPolicy.fitDiamond(new VisualTextFitPolicy.BoxSize(base.width(), base.height()), node.displayName(), node.description())
                : textFitPolicy.fitCard(new VisualTextFitPolicy.BoxSize(base.width(), base.height()), node.displayName(), node.description());
        return new VisualNodeReference(
                VisualElementLayoutIds.behaviorNode(node.id()),
                fitted.width(),
                fitted.height(),
                orderIndex(node, fallbackIndex));
    }

    private static DiagramSize sizeFor(BehaviorDiagramKind diagramKind, BehaviorNodeKind nodeKind) {
        if (diagramKind == BehaviorDiagramKind.BPMN_BASIC) {
            return switch (nodeKind) {
                case START_EVENT, END_EVENT -> new DiagramSize(96.0, 76.0);
                case DECISION -> new DiagramSize(128.0, 104.0);
                case LANE -> new DiagramSize(360.0, 96.0);
                default -> new DiagramSize(190.0, 82.0);
            };
        }
        if (diagramKind == BehaviorDiagramKind.OPERATIONAL_FLOW) {
            return switch (nodeKind) {
                case START_EVENT, END_EVENT -> new DiagramSize(120.0, 72.0);
                case DECISION -> new DiagramSize(150.0, 104.0);
                case LANE -> new DiagramSize(340.0, 88.0);
                case NOTE -> new DiagramSize(170.0, 86.0);
                default -> new DiagramSize(210.0, 86.0);
            };
        }
        return new DiagramSize(180.0, 70.0);
    }

    private static int orderIndex(BehaviorNode node, int fallbackIndex) {
        return node.orderIndex() > 0 ? node.orderIndex() : Math.max(0, fallbackIndex);
    }

    private record DiagramSize(double width, double height) { }
}
