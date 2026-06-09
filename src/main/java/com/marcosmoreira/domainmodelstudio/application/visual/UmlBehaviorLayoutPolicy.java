package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import java.util.Objects;

/**
 * Tamaños iniciales para UML de comportamiento.
 *
 * <p>Evita que casos de uso, actividades y estados nazcan como una grilla de
 * cajas idénticas. El layout fino puede mantenerse manual o especializarse por
 * tipo de diagrama, pero las dimensiones iniciales ya respetan la notación.</p>
 */
public final class UmlBehaviorLayoutPolicy {

    private final VisualTextFitPolicy textFitPolicy = new VisualTextFitPolicy();

    public VisualNodeReference visualReference(BehaviorDiagramKind diagramKind, BehaviorNode node, int fallbackIndex) {
        Objects.requireNonNull(node, "node");
        DiagramSize base = sizeFor(diagramKind, node.kind());
        VisualTextFitPolicy.BoxSize fitted = fitByShape(base, node);
        return new VisualNodeReference(
                VisualElementLayoutIds.behaviorNode(node.id()),
                fitted.width(),
                fitted.height(),
                orderIndex(node, fallbackIndex));
    }

    public boolean supports(BehaviorDiagramKind diagramKind) {
        return diagramKind == BehaviorDiagramKind.UML_USE_CASE
                || diagramKind == BehaviorDiagramKind.UML_ACTIVITY
                || diagramKind == BehaviorDiagramKind.UML_STATE;
    }

    private VisualTextFitPolicy.BoxSize fitByShape(DiagramSize base, BehaviorNode node) {
        VisualTextFitPolicy.BoxSize box = new VisualTextFitPolicy.BoxSize(base.width(), base.height());
        if (node.kind() == BehaviorNodeKind.DECISION) {
            return textFitPolicy.fitDiamond(box, node.displayName(), node.description());
        }
        return textFitPolicy.fitCard(box, node.displayName(), node.description());
    }

    private static DiagramSize sizeFor(BehaviorDiagramKind diagramKind, BehaviorNodeKind nodeKind) {
        if (diagramKind == BehaviorDiagramKind.UML_USE_CASE) {
            return switch (nodeKind) {
                case ACTOR -> new DiagramSize(105.0, 112.0);
                case USE_CASE -> new DiagramSize(205.0, 86.0);
                case SYSTEM_BOUNDARY -> new DiagramSize(430.0, 310.0);
                default -> new DiagramSize(180.0, 78.0);
            };
        }
        if (diagramKind == BehaviorDiagramKind.UML_ACTIVITY) {
            return switch (nodeKind) {
                case INITIAL_STATE, FINAL_STATE -> new DiagramSize(82.0, 72.0);
                case DECISION -> new DiagramSize(132.0, 104.0);
                case FORK, JOIN -> new DiagramSize(180.0, 56.0);
                case ACTION -> new DiagramSize(200.0, 78.0);
                default -> new DiagramSize(180.0, 74.0);
            };
        }
        if (diagramKind == BehaviorDiagramKind.UML_STATE) {
            return switch (nodeKind) {
                case INITIAL_STATE, FINAL_STATE -> new DiagramSize(82.0, 72.0);
                case STATE -> new DiagramSize(200.0, 82.0);
                case NOTE -> new DiagramSize(180.0, 86.0);
                default -> new DiagramSize(180.0, 74.0);
            };
        }
        return new DiagramSize(180.0, 74.0);
    }

    private static int orderIndex(BehaviorNode node, int fallbackIndex) {
        return node.orderIndex() > 0 ? node.orderIndex() : Math.max(0, fallbackIndex);
    }

    private record DiagramSize(double width, double height) { }
}
