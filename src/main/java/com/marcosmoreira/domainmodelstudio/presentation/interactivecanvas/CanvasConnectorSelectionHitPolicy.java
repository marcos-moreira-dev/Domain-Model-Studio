package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade;
import java.util.List;
import java.util.Optional;
import javafx.geometry.Point2D;

/**
 * Detecta si una ruta de conector entra en un rectángulo de selección.
 *
 * <p>La selección rectangular debe recoger también relaciones y bendpoints, no solo nodos. Esta política
 * usa la misma geometría borde-a-borde que el render transversal, de modo que el usuario puede encerrar
 * una flecha, un codo o un punto intermedio y obtener una selección coherente.</p>
 */
public final class CanvasConnectorSelectionHitPolicy {

    private CanvasConnectorSelectionHitPolicy() {
    }

    public static boolean routeTouches(
            CanvasBounds selectionBounds,
            Optional<NodeLayout> sourceLayout,
            Optional<NodeLayout> targetLayout,
            Optional<ConnectorLayout> connectorLayout,
            DiagramDrawingFacade drawingFacade
    ) {
        if (selectionBounds == null || sourceLayout == null || targetLayout == null || sourceLayout.isEmpty() || targetLayout.isEmpty()) {
            return false;
        }
        List<Point2D> route = CanvasConnectorGeometry.edgeToEdgePoints(
                sourceLayout.get(),
                targetLayout.get(),
                connectorLayout == null ? Optional.empty() : connectorLayout,
                drawingFacade == null ? DiagramDrawingFacade.defaults() : drawingFacade
        );
        return routeTouches(selectionBounds, route);
    }

    public static boolean routeTouches(CanvasBounds selectionBounds, List<Point2D> route) {
        if (selectionBounds == null || route == null || route.isEmpty()) {
            return false;
        }
        for (Point2D point : route) {
            if (selectionBounds.contains(point.getX(), point.getY())) {
                return true;
            }
        }
        for (int index = 0; index < route.size() - 1; index++) {
            if (segmentIntersectsBounds(route.get(index), route.get(index + 1), selectionBounds)) {
                return true;
            }
        }
        return false;
    }

    private static boolean segmentIntersectsBounds(Point2D a, Point2D b, CanvasBounds bounds) {
        Point2D topLeft = new Point2D(bounds.x(), bounds.y());
        Point2D topRight = new Point2D(bounds.right(), bounds.y());
        Point2D bottomRight = new Point2D(bounds.right(), bounds.bottom());
        Point2D bottomLeft = new Point2D(bounds.x(), bounds.bottom());
        return segmentsIntersect(a, b, topLeft, topRight)
                || segmentsIntersect(a, b, topRight, bottomRight)
                || segmentsIntersect(a, b, bottomRight, bottomLeft)
                || segmentsIntersect(a, b, bottomLeft, topLeft);
    }

    private static boolean segmentsIntersect(Point2D a, Point2D b, Point2D c, Point2D d) {
        double o1 = orientation(a, b, c);
        double o2 = orientation(a, b, d);
        double o3 = orientation(c, d, a);
        double o4 = orientation(c, d, b);
        if (o1 == 0.0 && onSegment(a, c, b)) return true;
        if (o2 == 0.0 && onSegment(a, d, b)) return true;
        if (o3 == 0.0 && onSegment(c, a, d)) return true;
        if (o4 == 0.0 && onSegment(c, b, d)) return true;
        return (o1 > 0) != (o2 > 0) && (o3 > 0) != (o4 > 0);
    }

    private static double orientation(Point2D a, Point2D b, Point2D c) {
        double value = (b.getY() - a.getY()) * (c.getX() - b.getX()) - (b.getX() - a.getX()) * (c.getY() - b.getY());
        return Math.abs(value) < 0.000001 ? 0.0 : value;
    }

    private static boolean onSegment(Point2D a, Point2D b, Point2D c) {
        return b.getX() <= Math.max(a.getX(), c.getX()) + 0.000001
                && b.getX() + 0.000001 >= Math.min(a.getX(), c.getX())
                && b.getY() <= Math.max(a.getY(), c.getY()) + 0.000001
                && b.getY() + 0.000001 >= Math.min(a.getY(), c.getY());
    }
}
