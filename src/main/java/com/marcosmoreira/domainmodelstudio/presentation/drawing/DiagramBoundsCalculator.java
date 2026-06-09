package com.marcosmoreira.domainmodelstudio.presentation.drawing;

import javafx.geometry.Point2D;

/** Utilidades geométricas compartidas por render kits de nodos y conectores. */
public final class DiagramBoundsCalculator {

    public Point2D center(double x, double y, double width, double height) {
        return new Point2D(x + width / 2.0, y + height / 2.0);
    }

    public Point2D edgePoint(
            double x,
            double y,
            double width,
            double height,
            Point2D target
    ) {
        Point2D center = center(x, y, width, height);
        if (target == null) {
            return center;
        }
        double dx = target.getX() - center.getX();
        double dy = target.getY() - center.getY();
        if (Math.abs(dx) < 0.001 && Math.abs(dy) < 0.001) {
            return center;
        }
        double halfWidth = width / 2.0;
        double halfHeight = height / 2.0;
        double scale = Math.min(
                halfWidth / Math.max(0.001, Math.abs(dx)),
                halfHeight / Math.max(0.001, Math.abs(dy))
        );
        return new Point2D(center.getX() + dx * scale, center.getY() + dy * scale);
    }

    public Point2D middlePoint(Point2D start, Point2D end) {
        if (start == null || end == null) {
            return new Point2D(0.0, 0.0);
        }
        return new Point2D((start.getX() + end.getX()) / 2.0, (start.getY() + end.getY()) / 2.0);
    }
}
