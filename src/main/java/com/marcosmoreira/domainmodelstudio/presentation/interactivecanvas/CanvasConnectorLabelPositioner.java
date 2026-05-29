package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import java.util.List;
import java.util.Objects;
import javafx.geometry.Point2D;

/** Calcula un punto de anclaje para una etiqueta sobre la ruta de un conector. */
public final class CanvasConnectorLabelPositioner {

    private CanvasConnectorLabelPositioner() {
    }

    public static Point2D labelPoint(List<Point2D> route, ConnectorLayout layout) {
        Objects.requireNonNull(route, "La ruta del conector no puede ser null");
        if (route.isEmpty()) {
            return new Point2D(offsetX(layout), offsetY(layout));
        }
        Point2D base = looksLikeSelfLoop(route)
                ? cubicMidpoint(route.get(0), route.get(1), route.get(2), route.get(3))
                : midpointAlongRoute(route);
        return new Point2D(base.getX() + offsetX(layout), base.getY() + offsetY(layout));
    }

    private static Point2D midpointAlongRoute(List<Point2D> route) {
        if (route.size() == 1) {
            return route.get(0);
        }
        double totalLength = 0.0;
        for (int index = 1; index < route.size(); index++) {
            totalLength += route.get(index - 1).distance(route.get(index));
        }
        if (totalLength <= 0.0) {
            return route.get(route.size() / 2);
        }
        double targetLength = totalLength / 2.0;
        double traversed = 0.0;
        for (int index = 1; index < route.size(); index++) {
            Point2D previous = route.get(index - 1);
            Point2D current = route.get(index);
            double segmentLength = previous.distance(current);
            if (segmentLength <= 0.0) {
                continue;
            }
            if (traversed + segmentLength >= targetLength) {
                double ratio = (targetLength - traversed) / segmentLength;
                return previous.interpolate(current, ratio);
            }
            traversed += segmentLength;
        }
        return route.get(route.size() - 1);
    }

    private static boolean looksLikeSelfLoop(List<Point2D> route) {
        if (route.size() != 4) {
            return false;
        }
        Point2D start = route.get(0);
        Point2D control1 = route.get(1);
        Point2D control2 = route.get(2);
        Point2D end = route.get(3);
        double topY = Math.min(start.getY(), end.getY());
        return Math.abs(start.getY() - end.getY()) <= 16.0
                && control1.getY() < topY
                && control2.getY() < topY
                && control1.getX() > Math.max(start.getX(), end.getX())
                && control2.getX() < Math.min(start.getX(), end.getX());
    }

    private static Point2D cubicMidpoint(Point2D start, Point2D control1, Point2D control2, Point2D end) {
        double t = 0.5;
        double oneMinusT = 1.0 - t;
        double x = Math.pow(oneMinusT, 3) * start.getX()
                + 3.0 * Math.pow(oneMinusT, 2) * t * control1.getX()
                + 3.0 * oneMinusT * Math.pow(t, 2) * control2.getX()
                + Math.pow(t, 3) * end.getX();
        double y = Math.pow(oneMinusT, 3) * start.getY()
                + 3.0 * Math.pow(oneMinusT, 2) * t * control1.getY()
                + 3.0 * oneMinusT * Math.pow(t, 2) * control2.getY()
                + Math.pow(t, 3) * end.getY();
        return new Point2D(x, y);
    }

    private static double offsetX(ConnectorLayout layout) {
        return layout == null ? 0.0 : layout.labelOffsetX();
    }

    private static double offsetY(ConnectorLayout layout) {
        return layout == null ? 0.0 : layout.labelOffsetY();
    }
}
