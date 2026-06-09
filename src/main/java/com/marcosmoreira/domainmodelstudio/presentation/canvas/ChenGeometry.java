package com.marcosmoreira.domainmodelstudio.presentation.canvas;

import com.marcosmoreira.domainmodelstudio.domain.layout.AnchorSide;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import javafx.geometry.Point2D;

/** Utilidades geométricas de presentación para conectores Chen. */
final class ChenGeometry {

    private ChenGeometry() {
    }

    static Point2D anchorPoint(NodeLayout node, AnchorSide requestedSide, NodeLayout oppositeNode) {
        AnchorSide side = requestedSide == null || requestedSide == AnchorSide.AUTO
                ? inferSide(node, oppositeNode)
                : requestedSide;

        double centerX = node.x() + node.width() / 2.0;
        double centerY = node.y() + node.height() / 2.0;

        return switch (side) {
            case LEFT -> new Point2D(node.x(), centerY);
            case RIGHT -> new Point2D(node.x() + node.width(), centerY);
            case TOP -> new Point2D(centerX, node.y());
            case BOTTOM -> new Point2D(centerX, node.y() + node.height());
            case CENTER, AUTO -> new Point2D(centerX, centerY);
        };
    }

    private static AnchorSide inferSide(NodeLayout node, NodeLayout oppositeNode) {
        double centerX = node.x() + node.width() / 2.0;
        double centerY = node.y() + node.height() / 2.0;
        double oppositeX = oppositeNode.x() + oppositeNode.width() / 2.0;
        double oppositeY = oppositeNode.y() + oppositeNode.height() / 2.0;
        double dx = oppositeX - centerX;
        double dy = oppositeY - centerY;

        if (Math.abs(dx) >= Math.abs(dy)) {
            return dx >= 0 ? AnchorSide.RIGHT : AnchorSide.LEFT;
        }
        return dy >= 0 ? AnchorSide.BOTTOM : AnchorSide.TOP;
    }

    static double centerX(NodeLayout node) {
        return node.x() + node.width() / 2.0;
    }

    static double centerY(NodeLayout node) {
        return node.y() + node.height() / 2.0;
    }
}
