package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNode;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNodeKind;
import java.util.Objects;

/**
 * Tamaños iniciales especializados para C4 y despliegue técnico.
 *
 * <p>El layout definitivo sigue siendo editable por el usuario, pero los nodos
 * no deben nacer como cajas idénticas: un boundary, un ambiente o una red son
 * zonas; una base de datos y una API necesitan cajas legibles; una persona es
 * un actor externo más compacto.</p>
 */
public final class ArchitectureLayoutPolicy {

    private final VisualTextFitPolicy textFitPolicy = new VisualTextFitPolicy();

    public VisualNodeReference visualReference(ArchitectureDiagramKind diagramKind, ArchitectureNode node, int fallbackIndex) {
        Objects.requireNonNull(node, "node");
        DiagramSize base = sizeFor(diagramKind, node.kind());
        VisualTextFitPolicy.BoxSize fitted = textFitPolicy.fitCard(
                new VisualTextFitPolicy.BoxSize(base.width(), base.height()),
                node.displayName(),
                node.description());
        return new VisualNodeReference(
                VisualElementLayoutIds.architectureNode(node.id()),
                fitted.width(),
                fitted.height(),
                orderIndex(node, fallbackIndex));
    }

    private static DiagramSize sizeFor(ArchitectureDiagramKind diagramKind, ArchitectureNodeKind nodeKind) {
        return switch (diagramKind) {
            case C4_CONTEXT -> contextSize(nodeKind);
            case C4_CONTAINERS -> containersSize(nodeKind);
            case TECHNICAL_DEPLOYMENT -> deploymentSize(nodeKind);
        };
    }

    private static DiagramSize contextSize(ArchitectureNodeKind kind) {
        return switch (kind) {
            case BOUNDARY -> new DiagramSize(520.0, 320.0);
            case PERSON -> new DiagramSize(178.0, 104.0);
            case SOFTWARE_SYSTEM -> new DiagramSize(245.0, 112.0);
            case EXTERNAL_SYSTEM -> new DiagramSize(220.0, 104.0);
            default -> new DiagramSize(210.0, 92.0);
        };
    }

    private static DiagramSize containersSize(ArchitectureNodeKind kind) {
        return switch (kind) {
            case BOUNDARY -> new DiagramSize(560.0, 340.0);
            case DATABASE -> new DiagramSize(210.0, 105.0);
            case API -> new DiagramSize(218.0, 100.0);
            case APPLICATION, CONTAINER -> new DiagramSize(220.0, 102.0);
            case EXTERNAL_SERVICE -> new DiagramSize(220.0, 98.0);
            default -> new DiagramSize(210.0, 92.0);
        };
    }

    private static DiagramSize deploymentSize(ArchitectureNodeKind kind) {
        return switch (kind) {
            case ENVIRONMENT -> new DiagramSize(570.0, 350.0);
            case NETWORK -> new DiagramSize(520.0, 230.0);
            case SERVER -> new DiagramSize(220.0, 112.0);
            case CLIENT -> new DiagramSize(190.0, 96.0);
            case SERVICE -> new DiagramSize(212.0, 96.0);
            case DATABASE -> new DiagramSize(210.0, 105.0);
            case ARTIFACT -> new DiagramSize(198.0, 82.0);
            default -> new DiagramSize(210.0, 92.0);
        };
    }

    private static int orderIndex(ArchitectureNode node, int fallbackIndex) {
        return node.orderIndex() > 0 ? node.orderIndex() : Math.max(0, fallbackIndex);
    }

    private record DiagramSize(double width, double height) { }
}
