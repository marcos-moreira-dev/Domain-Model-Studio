package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.Collection;

/**
 * Calcula límites de contenido para encuadre y exportación sin duplicar esa lógica
 * en cada adaptador especializado.
 */
public final class CanvasContentBoundsCalculator {

    private final double padding;
    private final double minimumWidth;
    private final double minimumHeight;

    public CanvasContentBoundsCalculator(double padding, double minimumWidth, double minimumHeight) {
        this.padding = finiteOrDefault(padding, 0.0);
        this.minimumWidth = finiteOrDefault(minimumWidth, 880.0);
        this.minimumHeight = finiteOrDefault(minimumHeight, 600.0);
    }

    public CanvasBounds fromNodeLayouts(Collection<NodeLayout> layouts) {
        CanvasBounds bounds = null;
        for (NodeLayout layout : layouts == null ? java.util.List.<NodeLayout>of() : layouts) {
            if (layout == null || !layout.visible()) {
                continue;
            }
            bounds = CanvasBounds.union(bounds, CanvasBounds.from(layout));
        }
        if (bounds == null) {
            return CanvasBounds.of(0.0, 0.0, minimumWidth, minimumHeight);
        }
        return withPaddingAndMinimums(bounds);
    }

    public CanvasBounds fromReadPort(CanvasReadPort readPort) {
        CanvasBounds bounds = null;
        for (InteractiveCanvasNode node : readPort.nodes()) {
            if (!node.visible()) {
                continue;
            }
            bounds = CanvasBounds.union(bounds, readPort.layoutForNode(node.id())
                    .filter(NodeLayout::visible)
                    .map(CanvasBounds::from)
                    .orElse(null));
        }
        if (bounds == null) {
            return CanvasBounds.of(0.0, 0.0, minimumWidth, minimumHeight);
        }
        return withPaddingAndMinimums(bounds);
    }

    private CanvasBounds withPaddingAndMinimums(CanvasBounds bounds) {
        double safePadding = Math.max(0.0, padding);
        double x = Math.max(0.0, bounds.x() - safePadding);
        double y = Math.max(0.0, bounds.y() - safePadding);
        double width = Math.max(minimumWidth, bounds.width() + safePadding * 2.0);
        double height = Math.max(minimumHeight, bounds.height() + safePadding * 2.0);
        return CanvasBounds.of(x, y, width, height);
    }

    private static double finiteOrDefault(double value, double fallback) {
        return Double.isFinite(value) && value >= 0.0 ? value : fallback;
    }
}
