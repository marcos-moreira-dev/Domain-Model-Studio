package com.marcosmoreira.domainmodelstudio.presentation.canvas;

import com.marcosmoreira.domainmodelstudio.domain.layout.AnchorSide;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.Objects;

/**
 * Política geométrica para resolver el anclaje más cercano en el canvas conceptual legacy.
 *
 * <p>La extracción mantiene la matemática fuera del ViewModel y facilita probar el criterio
 * sin instanciar JavaFX ni la vista completa. El criterio sigue siendo puramente visual: no
 * altera semántica ER, layouts persistidos ni exportaciones. Por eso puede evolucionar
 * como política geométrica aislada sin reabrir el canvas conceptual completo.</p>
 */
final class ConceptualAnchorResolver {

    private ConceptualAnchorResolver() {
    }

    static AnchorSide nearestAnchor(NodeLayout node, double x, double y) {
        Objects.requireNonNull(node, "node");
        double leftX = node.x();
        double rightX = node.x() + node.width();
        double topY = node.y();
        double bottomY = node.y() + node.height();
        double centerX = node.x() + node.width() / 2.0;
        double centerY = node.y() + node.height() / 2.0;

        AnchorSide best = AnchorSide.LEFT;
        double bestDistance = squaredDistance(x, y, leftX, centerY);

        double rightDistance = squaredDistance(x, y, rightX, centerY);
        if (rightDistance < bestDistance) {
            best = AnchorSide.RIGHT;
            bestDistance = rightDistance;
        }

        double topDistance = squaredDistance(x, y, centerX, topY);
        if (topDistance < bestDistance) {
            best = AnchorSide.TOP;
            bestDistance = topDistance;
        }

        double bottomDistance = squaredDistance(x, y, centerX, bottomY);
        if (bottomDistance < bestDistance) {
            best = AnchorSide.BOTTOM;
        }
        return best;
    }

    private static double squaredDistance(double x1, double y1, double x2, double y2) {
        double deltaX = x1 - x2;
        double deltaY = y1 - y2;
        return deltaX * deltaX + deltaY * deltaY;
    }
}
