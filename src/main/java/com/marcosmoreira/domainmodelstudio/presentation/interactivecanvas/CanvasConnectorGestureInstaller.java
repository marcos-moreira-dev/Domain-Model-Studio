package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas.ZoomableDiagramSurface;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.DiagramInteractionProfile;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Gestos transversales para relaciones dibujadas sobre el lienzo.
 *
 * <p>Cuando una familia desactiva el hit testing directo de líneas para no bloquear
 * el drag de nodos —por ejemplo UML Clases— esta instalación sigue permitiendo
 * seleccionar una relación y agregar puntos intermedios haciendo doble clic sobre
 * un segmento libre del canvas. Si el evento ya pertenece a un nodo, etiqueta o
 * handle, no intercepta nada.</p>
 */
final class CanvasConnectorGestureInstaller {

    private static final double CONNECTOR_HIT_TOLERANCE = 10.0;

    private CanvasConnectorGestureInstaller() {
    }

    static void install(
            ZoomableDiagramSurface surface,
            InteractiveCanvasAdapter adapter,
            DiagramInteractionProfile interactionProfile,
            CanvasPointMapper canvasPointMapper,
            Supplier<InteractiveCanvasModel> modelSupplier,
            DiagramDrawingFacade drawingFacade,
            Runnable refresh
    ) {
        Objects.requireNonNull(surface, "surface");
        Objects.requireNonNull(adapter, "adapter");
        Objects.requireNonNull(interactionProfile, "interactionProfile");
        Objects.requireNonNull(canvasPointMapper, "canvasPointMapper");
        Objects.requireNonNull(modelSupplier, "modelSupplier");
        Objects.requireNonNull(refresh, "refresh");

        Parent root = surface.workspaceRoot();
        CanvasInteractiveTargetPolicy targetPolicy = new CanvasInteractiveTargetPolicy(surface);
        root.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> handlePressed(
                surface, adapter, interactionProfile, canvasPointMapper, modelSupplier, drawingFacade, refresh, targetPolicy, event));
        root.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> handleClicked(
                surface, adapter, interactionProfile, canvasPointMapper, modelSupplier, drawingFacade, refresh, targetPolicy, event));
    }

    private static void handlePressed(
            ZoomableDiagramSurface surface,
            InteractiveCanvasAdapter adapter,
            DiagramInteractionProfile interactionProfile,
            CanvasPointMapper canvasPointMapper,
            Supplier<InteractiveCanvasModel> modelSupplier,
            DiagramDrawingFacade drawingFacade,
            Runnable refresh,
            CanvasInteractiveTargetPolicy targetPolicy,
            MouseEvent event
    ) {
        if (!canHandleConnectorGesture(interactionProfile, event) || !isBackgroundTarget(targetPolicy, event)) {
            return;
        }
        Point2D canvasPoint = canvasPointMapper.toCanvasPoint(event.getSceneX(), event.getSceneY());
        nearestConnectorAt(surface, modelSupplier.get(), canvasPoint, drawingFacade).ifPresent(connector -> {
            if (interactionProfile.supportsConnectorSelection()) {
                adapter.selectConnector(connector.id(), event.isShiftDown());
                surface.root().requestFocus();
                refresh.run();
            }
            event.consume();
        });
    }

    private static void handleClicked(
            ZoomableDiagramSurface surface,
            InteractiveCanvasAdapter adapter,
            DiagramInteractionProfile interactionProfile,
            CanvasPointMapper canvasPointMapper,
            Supplier<InteractiveCanvasModel> modelSupplier,
            DiagramDrawingFacade drawingFacade,
            Runnable refresh,
            CanvasInteractiveTargetPolicy targetPolicy,
            MouseEvent event
    ) {
        if (!canHandleConnectorGesture(interactionProfile, event) || !isBackgroundTarget(targetPolicy, event)) {
            return;
        }
        Point2D canvasPoint = canvasPointMapper.toCanvasPoint(event.getSceneX(), event.getSceneY());
        nearestConnectorAt(surface, modelSupplier.get(), canvasPoint, drawingFacade).ifPresent(connector -> {
            if (event.getClickCount() >= 2 && interactionProfile.supportsBendPoints() && adapter.supportsBendPoints()) {
                adapter.addBendPoint(connector.id(), canvasPoint.getX(), canvasPoint.getY());
            } else if (interactionProfile.supportsConnectorSelection()) {
                adapter.selectConnector(connector.id(), event.isShiftDown());
            }
            surface.root().requestFocus();
            refresh.run();
            event.consume();
        });
    }

    private static boolean canHandleConnectorGesture(DiagramInteractionProfile profile, MouseEvent event) {
        return event.getButton() == MouseButton.PRIMARY
                && (profile.supportsConnectorSelection() || profile.supportsBendPoints());
    }

    private static boolean isBackgroundTarget(CanvasInteractiveTargetPolicy targetPolicy, MouseEvent event) {
        Object target = event.getTarget();
        Object picked = event.getPickResult() == null ? null : event.getPickResult().getIntersectedNode();
        return targetPolicy.isBackground(target) && targetPolicy.isBackground(picked);
    }

    private static Optional<InteractiveCanvasConnector> nearestConnectorAt(
            ZoomableDiagramSurface surface,
            InteractiveCanvasModel model,
            Point2D canvasPoint,
            DiagramDrawingFacade drawingFacade
    ) {
        if (model == null || canvasPoint == null) {
            return Optional.empty();
        }
        double tolerance = CONNECTOR_HIT_TOLERANCE / Math.max(0.25, surface.zoomFactor());
        InteractiveCanvasConnector nearest = null;
        double bestDistance = Double.MAX_VALUE;
        for (InteractiveCanvasConnector connector : model.visibleConnectors()) {
            Optional<NodeLayout> source = model.layoutForNode(connector.sourceNodeId());
            Optional<NodeLayout> target = model.layoutForNode(connector.targetNodeId());
            if (source.isEmpty() || target.isEmpty()) {
                continue;
            }
            List<Point2D> route = CanvasConnectorGeometry.edgeToEdgePoints(
                    source.get(),
                    target.get(),
                    model.layoutForConnector(connector.id()),
                    drawingFacade);
            double distance = distanceToRoute(canvasPoint, route);
            if (distance <= tolerance && distance < bestDistance) {
                nearest = connector;
                bestDistance = distance;
            }
        }
        return Optional.ofNullable(nearest);
    }

    private static double distanceToRoute(Point2D point, List<Point2D> route) {
        if (route == null || route.size() < 2) {
            return Double.MAX_VALUE;
        }
        double best = Double.MAX_VALUE;
        for (int index = 0; index < route.size() - 1; index++) {
            best = Math.min(best, distanceToSegment(point, route.get(index), route.get(index + 1)));
        }
        return best;
    }

    private static double distanceToSegment(Point2D point, Point2D a, Point2D b) {
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        double lengthSquared = dx * dx + dy * dy;
        if (lengthSquared <= 0.0001) {
            return point.distance(a);
        }
        double t = ((point.getX() - a.getX()) * dx + (point.getY() - a.getY()) * dy) / lengthSquared;
        double clamped = Math.max(0.0, Math.min(1.0, t));
        return point.distance(a.getX() + clamped * dx, a.getY() + clamped * dy);
    }
}
