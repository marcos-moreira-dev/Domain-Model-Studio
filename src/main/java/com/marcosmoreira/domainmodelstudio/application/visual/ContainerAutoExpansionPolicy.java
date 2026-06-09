package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.List;
import java.util.Objects;

/**
 * Política geométrica mínima para que módulos, pantallas, carriles y límites
 * funcionen como contenedores visuales y no solo como decoración de fondo.
 */
public final class ContainerAutoExpansionPolicy {

    public DiagramProject expandContainerToFit(
            DiagramProject project,
            DiagramElementId containerId,
            List<DiagramElementId> childIds,
            ContainerPadding padding
    ) {
        Objects.requireNonNull(project, "project");
        Objects.requireNonNull(containerId, "containerId");
        Objects.requireNonNull(padding, "padding");
        DiagramLayout layout = project.layouts().activeLayout();
        NodeLayout container = layout.nodeFor(containerId).orElse(null);
        if (container == null || childIds == null || childIds.isEmpty()) {
            return project;
        }
        List<NodeLayout> children = childIds.stream()
                .map(layout::nodeFor)
                .flatMap(java.util.Optional::stream)
                .filter(NodeLayout::visible)
                .toList();
        if (children.isEmpty()) {
            return project;
        }
        double minChildX = children.stream().mapToDouble(NodeLayout::x).min().orElse(container.x());
        double minChildY = children.stream().mapToDouble(NodeLayout::y).min().orElse(container.y());
        double maxChildX = children.stream().mapToDouble(child -> child.x() + child.width()).max().orElse(container.x() + container.width());
        double maxChildY = children.stream().mapToDouble(child -> child.y() + child.height()).max().orElse(container.y() + container.height());
        double x = Math.min(container.x(), minChildX - padding.left());
        double y = Math.min(container.y(), minChildY - padding.top());
        double right = Math.max(container.x() + container.width(), maxChildX + padding.right());
        double bottom = Math.max(container.y() + container.height(), maxChildY + padding.bottom());
        NodeLayout expanded = container.movedTo(x, y).resizedTo(right - x, bottom - y);
        return project.withLayouts(project.layouts().withLayout(layout.withNode(expanded)));
    }


    /**
     * Ajusta un contenedor al contenido visible de sus hijos.
     *
     * <p>A diferencia de {@link #expandContainerToFit(DiagramProject, DiagramElementId, List, ContainerPadding)},
     * esta operación también permite encoger el contenedor cuando el usuario reagrupa sus hijos.
     * Se conserva un tamaño mínimo para que el encabezado, la pestaña y el margen visual sigan siendo legibles.</p>
     */
    public DiagramProject fitContainerToChildren(
            DiagramProject project,
            DiagramElementId containerId,
            List<DiagramElementId> childIds,
            ContainerPadding padding,
            double minimumWidth,
            double minimumHeight
    ) {
        Objects.requireNonNull(project, "project");
        Objects.requireNonNull(containerId, "containerId");
        Objects.requireNonNull(padding, "padding");
        DiagramLayout layout = project.layouts().activeLayout();
        NodeLayout container = layout.nodeFor(containerId).orElse(null);
        if (container == null || childIds == null || childIds.isEmpty()) {
            return project;
        }
        List<NodeLayout> children = childIds.stream()
                .map(layout::nodeFor)
                .flatMap(java.util.Optional::stream)
                .filter(NodeLayout::visible)
                .toList();
        if (children.isEmpty()) {
            return project;
        }
        double minChildX = children.stream().mapToDouble(NodeLayout::x).min().orElse(container.x());
        double minChildY = children.stream().mapToDouble(NodeLayout::y).min().orElse(container.y());
        double maxChildX = children.stream().mapToDouble(child -> child.x() + child.width()).max().orElse(container.x() + container.width());
        double maxChildY = children.stream().mapToDouble(child -> child.y() + child.height()).max().orElse(container.y() + container.height());
        double x = minChildX - padding.left();
        double y = minChildY - padding.top();
        double width = Math.max(safeMinimum(minimumWidth, 180.0), maxChildX + padding.right() - x);
        double height = Math.max(safeMinimum(minimumHeight, 140.0), maxChildY + padding.bottom() - y);
        NodeLayout fitted = container.movedTo(x, y).resizedTo(width, height);
        return project.withLayouts(project.layouts().withLayout(layout.withNode(fitted)));
    }

    private static double safeMinimum(double value, double fallback) {
        return Double.isFinite(value) && value > 0.0 ? value : fallback;
    }

    public DiagramProject moveNodesBy(
            DiagramProject project,
            List<DiagramElementId> nodeIds,
            double deltaX,
            double deltaY
    ) {
        Objects.requireNonNull(project, "project");
        if (nodeIds == null || nodeIds.isEmpty()) {
            return project;
        }
        DiagramLayout updated = project.layouts().activeLayout();
        for (DiagramElementId nodeId : nodeIds) {
            NodeLayout current = updated.nodeFor(nodeId).orElse(null);
            if (current != null && current.visible()) {
                updated = updated.withNode(current.translatedBy(deltaX, deltaY));
            }
        }
        return project.withLayouts(project.layouts().withLayout(updated));
    }

    public boolean centerInside(NodeLayout child, NodeLayout container) {
        Objects.requireNonNull(child, "child");
        Objects.requireNonNull(container, "container");
        double centerX = child.x() + child.width() / 2.0;
        double centerY = child.y() + child.height() / 2.0;
        return centerX >= container.x()
                && centerX <= container.x() + container.width()
                && centerY >= container.y()
                && centerY <= container.y() + container.height();
    }
}
