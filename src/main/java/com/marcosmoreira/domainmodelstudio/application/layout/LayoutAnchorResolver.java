package com.marcosmoreira.domainmodelstudio.application.layout;

import com.marcosmoreira.domainmodelstudio.domain.layout.AnchorSide;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.Objects;

/** Utilidades geométricas para resolver anclas de conexión sin depender de JavaFX. */
public final class LayoutAnchorResolver {

    private LayoutAnchorResolver() {
        // Utilidad pura.
    }

    public static AnchorSide sideFacing(NodeLayout source, NodeLayout target) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(target, "target");
        double dx = centerX(target) - centerX(source);
        double dy = centerY(target) - centerY(source);
        if (Math.abs(dx) >= Math.abs(dy)) {
            return dx >= 0.0 ? AnchorSide.RIGHT : AnchorSide.LEFT;
        }
        return dy >= 0.0 ? AnchorSide.BOTTOM : AnchorSide.TOP;
    }

    public static DiagramPoint anchorPoint(NodeLayout node, AnchorSide anchorSide, NodeLayout opposite) {
        Objects.requireNonNull(node, "node");
        AnchorSide resolved = anchorSide == null || anchorSide == AnchorSide.AUTO
                ? sideFacing(node, opposite == null ? node : opposite)
                : anchorSide;
        return switch (resolved) {
            case TOP -> DiagramPoint.of(centerX(node), node.y());
            case RIGHT -> DiagramPoint.of(node.x() + node.width(), centerY(node));
            case BOTTOM -> DiagramPoint.of(centerX(node), node.y() + node.height());
            case LEFT -> DiagramPoint.of(node.x(), centerY(node));
            case AUTO, CENTER -> DiagramPoint.of(centerX(node), centerY(node));
        };
    }

    private static double centerX(NodeLayout node) {
        return node.x() + node.width() / 2.0;
    }

    private static double centerY(NodeLayout node) {
        return node.y() + node.height() / 2.0;
    }
}
