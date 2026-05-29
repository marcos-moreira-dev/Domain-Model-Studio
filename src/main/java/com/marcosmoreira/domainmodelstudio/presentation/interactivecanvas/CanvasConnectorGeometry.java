package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.layout.BendPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.geometry.Point2D;

/**
 * Utilidad común para calcular trayectorias de conectores sobre layouts de nodos.
 *
 * <p>Los render kits conservan la teoría de su familia; esta clase solo evita que
 * cada familia replique la geometría básica de centro, borde y puntos intermedios.</p>
 */
public final class CanvasConnectorGeometry {

    private CanvasConnectorGeometry() {
    }

    public static List<Point2D> edgeToEdgePoints(
            NodeLayout source,
            NodeLayout target,
            Optional<ConnectorLayout> layout,
            DiagramDrawingFacade drawingFacade
    ) {
        DiagramDrawingFacade safeDrawingFacade = drawingFacade == null ? DiagramDrawingFacade.defaults() : drawingFacade;
        Optional<ConnectorLayout> safeLayout = layout == null ? Optional.empty() : layout;
        List<BendPoint> bendPoints = safeLayout.map(ConnectorLayout::bendPoints).orElseGet(List::of);
        if (source.elementId().equals(target.elementId()) && bendPoints.isEmpty()) {
            return selfLoopPoints(source);
        }
        List<Point2D> points = new ArrayList<>();
        Point2D sourceCenter = center(source, safeDrawingFacade);
        Point2D targetCenter = center(target, safeDrawingFacade);
        Point2D firstWaypoint = bendPoints.isEmpty() ? targetCenter : toPoint(bendPoints.get(0));
        Point2D lastWaypoint = bendPoints.isEmpty() ? sourceCenter : toPoint(bendPoints.get(bendPoints.size() - 1));
        points.add(edgePoint(source, firstWaypoint, safeDrawingFacade));
        bendPoints.forEach(point -> points.add(toPoint(point)));
        points.add(edgePoint(target, lastWaypoint, safeDrawingFacade));
        return points;
    }

    public static Point2D center(NodeLayout layout, DiagramDrawingFacade drawingFacade) {
        DiagramDrawingFacade safeDrawingFacade = drawingFacade == null ? DiagramDrawingFacade.defaults() : drawingFacade;
        return safeDrawingFacade.bounds().center(layout.x(), layout.y(), layout.width(), layout.height());
    }

    public static Point2D edgePoint(NodeLayout layout, Point2D target, DiagramDrawingFacade drawingFacade) {
        DiagramDrawingFacade safeDrawingFacade = drawingFacade == null ? DiagramDrawingFacade.defaults() : drawingFacade;
        return safeDrawingFacade.bounds().edgePoint(layout.x(), layout.y(), layout.width(), layout.height(), target);
    }

    private static List<Point2D> selfLoopPoints(NodeLayout layout) {
        double x = layout.x();
        double y = layout.y();
        double w = layout.width();
        return List.of(
                new Point2D(x + w * 0.78, y + 2.0),
                new Point2D(x + w + 58.0, y - 64.0),
                new Point2D(x - 24.0, y - 64.0),
                new Point2D(x + w * 0.22, y + 2.0));
    }

    private static Point2D toPoint(BendPoint point) {
        return new Point2D(point.x(), point.y());
    }
}
