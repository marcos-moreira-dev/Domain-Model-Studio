package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.List;
import java.util.Objects;

/** Contención visual mínima para el límite del sistema en UML Casos de uso. */
public final class UseCaseBoundaryLayoutSupport {

    private static final ContainerPadding BOUNDARY_PADDING = ContainerPadding.of(56.0, 84.0, 56.0, 56.0);
    private final ContainerAutoExpansionPolicy containerPolicy = new ContainerAutoExpansionPolicy();
    private final VisualLayoutService visualLayoutService = new VisualLayoutService();

    public DiagramProject moveNode(
            DiagramProject project,
            BehaviorDiagramKind diagramKind,
            List<BehaviorNode> nodes,
            String nodeId,
            double x,
            double y
    ) {
        Objects.requireNonNull(project, "project");
        if (diagramKind != BehaviorDiagramKind.UML_USE_CASE) {
            return visualLayoutService.moveNodeTo(project, VisualElementLayoutIds.behaviorNode(nodeId), x, y);
        }
        BehaviorNode movedNode = nodeById(nodeId, nodes);
        if (movedNode == null) {
            return visualLayoutService.moveNodeTo(project, VisualElementLayoutIds.behaviorNode(nodeId), x, y);
        }
        if (movedNode.kind() == BehaviorNodeKind.SYSTEM_BOUNDARY) {
            return moveBoundaryWithContainedUseCases(project, movedNode.id(), x, y, nodes);
        }
        DiagramProject moved = visualLayoutService.moveNodeTo(project, VisualElementLayoutIds.behaviorNode(nodeId), x, y);
        return movedNode.kind() == BehaviorNodeKind.USE_CASE ? expandBoundaries(moved, nodes) : moved;
    }

    private DiagramProject moveBoundaryWithContainedUseCases(
            DiagramProject project,
            String boundaryId,
            double x,
            double y,
            List<BehaviorNode> nodes
    ) {
        DiagramElementId boundaryLayoutId = VisualElementLayoutIds.behaviorNode(boundaryId);
        NodeLayout boundary = visualLayoutService.nodeLayout(project, boundaryLayoutId)
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para el límite del sistema: " + boundaryId));
        double deltaX = x - boundary.x();
        double deltaY = y - boundary.y();
        List<DiagramElementId> containedUseCases = useCasesInsideBoundary(project, boundary, nodes);
        DiagramProject moved = visualLayoutService.moveNodeTo(project, boundaryLayoutId, x, y);
        return containerPolicy.moveNodesBy(moved, containedUseCases, deltaX, deltaY);
    }

    private DiagramProject expandBoundaries(DiagramProject project, List<BehaviorNode> nodes) {
        DiagramProject updated = project;
        List<DiagramElementId> useCaseIds = nodes.stream()
                .filter(node -> node.kind() == BehaviorNodeKind.USE_CASE)
                .map(node -> VisualElementLayoutIds.behaviorNode(node.id()))
                .toList();
        for (BehaviorNode boundary : nodes.stream().filter(node -> node.kind() == BehaviorNodeKind.SYSTEM_BOUNDARY).toList()) {
            updated = containerPolicy.fitContainerToChildren(
                    updated,
                    VisualElementLayoutIds.behaviorNode(boundary.id()),
                    useCaseIds,
                    BOUNDARY_PADDING,
                    420.0,
                    320.0);
        }
        return updated;
    }

    private List<DiagramElementId> useCasesInsideBoundary(DiagramProject project, NodeLayout boundary, List<BehaviorNode> nodes) {
        return nodes.stream()
                .filter(node -> node.kind() == BehaviorNodeKind.USE_CASE)
                .map(node -> visualLayoutService.nodeLayout(project, VisualElementLayoutIds.behaviorNode(node.id()))
                        .filter(layout -> containerPolicy.centerInside(layout, boundary))
                        .map(ignored -> VisualElementLayoutIds.behaviorNode(node.id())))
                .flatMap(java.util.Optional::stream)
                .toList();
    }

    private static BehaviorNode nodeById(String nodeId, List<BehaviorNode> nodes) {
        String normalized = normalize(nodeId);
        if (nodes == null || normalized.isBlank()) {
            return null;
        }
        return nodes.stream().filter(node -> node.id().equals(normalized)).findFirst().orElse(null);
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
