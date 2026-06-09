package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNode;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.List;
import java.util.Objects;

/**
 * Ajustes de contención para límites C4, ambientes y redes de despliegue técnico.
 *
 * <p>En arquitectura un rectángulo translúcido no es decoración: representa un
 * límite, ambiente o zona de red que debe gobernar visualmente a sus elementos.
 * La pertenencia sigue siendo geométrica —centro del hijo dentro del contenedor—,
 * pero el comportamiento de movimiento y autoajuste debe ser consistente.</p>
 */
public final class ArchitectureContainerLayoutSupport {

    private static final ContainerPadding ARCHITECTURE_PADDING = ContainerPadding.of(56.0, 90.0, 56.0, 64.0);
    private static final double NESTED_CONTAINER_MARGIN = 18.0;
    private final ContainerAutoExpansionPolicy containerPolicy = new ContainerAutoExpansionPolicy();
    private final VisualLayoutService visualLayoutService = new VisualLayoutService();

    public DiagramProject moveNode(
            DiagramProject project,
            String nodeId,
            double x,
            double y,
            List<ArchitectureNode> nodes
    ) {
        Objects.requireNonNull(project, "project");
        ArchitectureNode movedNode = nodeById(nodeId, nodes);
        DiagramElementId layoutId = VisualElementLayoutIds.architectureNode(nodeId);
        NodeLayout current = visualLayoutService.nodeLayout(project, layoutId)
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para el nodo de arquitectura: " + nodeId));
        double deltaX = x - current.x();
        double deltaY = y - current.y();
        DiagramProject moved = visualLayoutService.moveNodeTo(project, layoutId, x, y);
        if (movedNode != null && isVisualContainer(movedNode.kind())) {
            moved = containerPolicy.moveNodesBy(moved, containedNodeIds(project, movedNode.id(), nodes, true), deltaX, deltaY);
            moved = keepMovedContainerInsideAdoptedParent(moved, movedNode, nodes);
        }
        return expandContainers(moved, nodes);
    }

    public DiagramProject expandContainers(DiagramProject project, List<ArchitectureNode> nodes) {
        DiagramProject updated = project;
        if (nodes == null || nodes.isEmpty()) {
            return updated;
        }
        for (ArchitectureNode container : visualContainersByArea(updated, nodes)) {
            List<DiagramElementId> children = containedNodeIds(updated, container.id(), nodes, true);
            if (!children.isEmpty()) {
                updated = containerPolicy.fitContainerToChildren(
                        updated,
                        VisualElementLayoutIds.architectureNode(container.id()),
                        children,
                        ARCHITECTURE_PADDING,
                        minimumWidth(container.kind()),
                        minimumHeight(container.kind()));
            }
        }
        return updated;
    }


    private DiagramProject keepMovedContainerInsideAdoptedParent(
            DiagramProject project,
            ArchitectureNode movedNode,
            List<ArchitectureNode> nodes
    ) {
        if (movedNode == null || nodes == null || !isVisualContainer(movedNode.kind())) {
            return project;
        }
        DiagramElementId movedLayoutId = VisualElementLayoutIds.architectureNode(movedNode.id());
        NodeLayout child = visualLayoutService.nodeLayout(project, movedLayoutId).orElse(null);
        if (child == null || !child.visible()) {
            return project;
        }
        NodeLayout parent = visualContainersByArea(project, nodes).stream()
                .filter(candidate -> !candidate.id().equals(movedNode.id()))
                .map(candidate -> visualLayoutService.nodeLayout(project,
                        VisualElementLayoutIds.architectureNode(candidate.id())).orElse(null))
                .filter(candidate -> candidate != null && candidate.visible())
                .filter(candidate -> smallerThan(child, candidate))
                .filter(candidate -> containerPolicy.centerInside(child, candidate))
                .findFirst()
                .orElse(null);
        if (parent == null) {
            return project;
        }
        double clampedX = clamp(child.x(), parent.x() + NESTED_CONTAINER_MARGIN,
                parent.x() + parent.width() - child.width() - NESTED_CONTAINER_MARGIN);
        double clampedY = clamp(child.y(), parent.y() + NESTED_CONTAINER_MARGIN,
                parent.y() + parent.height() - child.height() - NESTED_CONTAINER_MARGIN);
        double deltaX = clampedX - child.x();
        double deltaY = clampedY - child.y();
        if (Math.abs(deltaX) < 0.001 && Math.abs(deltaY) < 0.001) {
            return project;
        }
        DiagramProject updated = visualLayoutService.moveNodeTo(project, movedLayoutId, clampedX, clampedY);
        return containerPolicy.moveNodesBy(updated, containedNodeIds(project, movedNode.id(), nodes, true), deltaX, deltaY);
    }

    private List<DiagramElementId> containedNodeIds(
            DiagramProject project,
            String containerId,
            List<ArchitectureNode> nodes,
            boolean includeNestedContainers
    ) {
        DiagramElementId containerLayoutId = VisualElementLayoutIds.architectureNode(containerId);
        NodeLayout container = visualLayoutService.nodeLayout(project, containerLayoutId).orElse(null);
        if (container == null || nodes == null) {
            return List.of();
        }
        return nodes.stream()
                .filter(node -> !node.id().equals(containerId))
                .filter(node -> includeNestedContainers || !isVisualContainer(node.kind()))
                .map(node -> new Candidate(node, visualLayoutService.nodeLayout(project,
                        VisualElementLayoutIds.architectureNode(node.id())).orElse(null)))
                .filter(candidate -> candidate.layout() != null && candidate.layout().visible())
                .filter(candidate -> containerPolicy.centerInside(candidate.layout(), container))
                .filter(candidate -> !isVisualContainer(candidate.node().kind()) || smallerThan(candidate.layout(), container))
                .map(candidate -> VisualElementLayoutIds.architectureNode(candidate.node().id()))
                .toList();
    }

    private List<ArchitectureNode> visualContainersByArea(DiagramProject project, List<ArchitectureNode> nodes) {
        return nodes.stream()
                .filter(node -> isVisualContainer(node.kind()))
                .sorted(java.util.Comparator.comparingDouble(node -> areaOf(project, node.id())))
                .toList();
    }

    public static boolean isVisualContainer(ArchitectureNodeKind kind) {
        return kind == ArchitectureNodeKind.BOUNDARY
                || kind == ArchitectureNodeKind.ENVIRONMENT
                || kind == ArchitectureNodeKind.NETWORK;
    }

    private double areaOf(DiagramProject project, String nodeId) {
        return visualLayoutService.nodeLayout(project, VisualElementLayoutIds.architectureNode(nodeId))
                .map(ArchitectureContainerLayoutSupport::areaOf)
                .orElse(Double.MAX_VALUE);
    }

    private static boolean smallerThan(NodeLayout candidate, NodeLayout container) {
        return areaOf(candidate) < areaOf(container);
    }

    private static double areaOf(NodeLayout layout) {
        return layout.width() * layout.height();
    }

    private static double clamp(double value, double min, double max) {
        if (max < min) {
            return min;
        }
        return Math.max(min, Math.min(max, value));
    }

    private static double minimumWidth(ArchitectureNodeKind kind) {
        return switch (kind) {
            case NETWORK -> 300.0;
            case ENVIRONMENT -> 360.0;
            default -> 420.0;
        };
    }

    private static double minimumHeight(ArchitectureNodeKind kind) {
        return switch (kind) {
            case NETWORK -> 190.0;
            case ENVIRONMENT -> 240.0;
            default -> 260.0;
        };
    }

    private record Candidate(ArchitectureNode node, NodeLayout layout) { }


    private static ArchitectureNode nodeById(String nodeId, List<ArchitectureNode> nodes) {
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
