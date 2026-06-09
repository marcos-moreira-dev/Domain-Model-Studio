package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas.ZoomableDiagramSurface;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.DiagramInteractionProfile;
import java.util.Objects;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 * Instala los gestos de fondo del lienzo interactivo.
 *
 * <p>La selección por área se comporta como el canvas conceptual: un movimiento
 * menor al umbral cuenta como clic de fondo; con Shift se preserva la selección
 * existente, y sin Shift se limpia. Después de una selección rectangular se
 * ignora el {@code MOUSE_CLICKED} sintético que JavaFX emite al soltar, porque
 * si no se borraría inmediatamente la selección recién calculada.</p>
 */
final class CanvasBackgroundGestureInstaller {

    @FunctionalInterface
    interface CanvasPointMapper {
        Point2D toCanvasPoint(double sceneX, double sceneY);
    }

    private CanvasBackgroundGestureInstaller() {
    }

    static void install(
            ZoomableDiagramSurface surface,
            InteractiveCanvasAdapter adapter,
            DiagramInteractionProfile interactionProfile,
            CanvasAreaSelectionController areaSelectionController,
            Rectangle selectionRectangle,
            CanvasPointMapper canvasPointMapper,
            Runnable refresh
    ) {
        Objects.requireNonNull(surface, "surface");
        Objects.requireNonNull(adapter, "adapter");
        Objects.requireNonNull(interactionProfile, "interactionProfile");
        Objects.requireNonNull(areaSelectionController, "areaSelectionController");
        Objects.requireNonNull(selectionRectangle, "selectionRectangle");
        Objects.requireNonNull(canvasPointMapper, "canvasPointMapper");
        Objects.requireNonNull(refresh, "refresh");

        Parent root = surface.workspaceRoot();
        CanvasInteractiveTargetPolicy targetPolicy = new CanvasInteractiveTargetPolicy(surface);
        boolean[] suppressNextBackgroundClick = {false};

        root.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.isConsumed()) {
                return;
            }
            surface.root().requestFocus();
            if (event.getButton() != MouseButton.PRIMARY || !isBackgroundGestureTarget(targetPolicy, event)) {
                return;
            }
            if (suppressNextBackgroundClick[0]) {
                suppressNextBackgroundClick[0] = false;
                event.consume();
                return;
            }
            Point2D canvasPoint = canvasPointMapper.toCanvasPoint(event.getSceneX(), event.getSceneY());
            if (adapter instanceof CanvasBackgroundClickPort clickPort
                    && clickPort.handleBackgroundClick(
                            canvasPoint.getX(),
                            canvasPoint.getY(),
                            event.isShiftDown(),
                            event.getClickCount())) {
                refresh.run();
                event.consume();
                return;
            }
            if (!event.isShiftDown()) {
                adapter.clearSelection();
                refresh.run();
            }
            event.consume();
        });

        root.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.isConsumed()) {
                return;
            }
            surface.root().requestFocus();
            suppressNextBackgroundClick[0] = false;
            if (event.getButton() != MouseButton.PRIMARY || !isBackgroundGestureTarget(targetPolicy, event)) {
                return;
            }
            if (!interactionProfile.supportsAreaSelection()) {
                if (!event.isShiftDown()) {
                    adapter.clearSelection();
                    refresh.run();
                }
                event.consume();
                return;
            }
            Point2D canvasPoint = canvasPointMapper.toCanvasPoint(event.getSceneX(), event.getSceneY());
            areaSelectionController.begin(canvasPoint.getX(), canvasPoint.getY(), event.isShiftDown());
            selectionRectangle.setVisible(false);
            event.consume();
        });
        root.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (event.isConsumed()) {
                return;
            }
            if (!interactionProfile.supportsAreaSelection() || !areaSelectionController.active()) {
                return;
            }
            Point2D canvasPoint = canvasPointMapper.toCanvasPoint(event.getSceneX(), event.getSceneY());
            CanvasBounds bounds = areaSelectionController.dragTo(canvasPoint.getX(), canvasPoint.getY());
            updateSelectionRectangle(selectionRectangle, bounds, areaSelectionController.selectionGesture());
            event.consume();
        });
        root.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            if (event.isConsumed()) {
                return;
            }
            if (!interactionProfile.supportsAreaSelection() || !areaSelectionController.active()) {
                return;
            }
            boolean wasSelectionGesture = areaSelectionController.selectionGesture();
            boolean selectedByArea = areaSelectionController.end();
            suppressNextBackgroundClick[0] = wasSelectionGesture;
            selectionRectangle.setVisible(false);
            if (!selectedByArea && !event.isShiftDown()) {
                adapter.clearSelection();
            }
            refresh.run();
            event.consume();
        });
    }

    private static boolean isBackgroundGestureTarget(CanvasInteractiveTargetPolicy targetPolicy, MouseEvent event) {
        Object target = event.getTarget();
        Object picked = event.getPickResult() == null ? null : event.getPickResult().getIntersectedNode();
        return targetPolicy.isBackground(target) && targetPolicy.isBackground(picked);
    }

    private static void updateSelectionRectangle(Rectangle rectangle, CanvasBounds bounds, boolean visible) {
        if (bounds == null) {
            return;
        }
        rectangle.setX(bounds.x());
        rectangle.setY(bounds.y());
        rectangle.setWidth(bounds.width());
        rectangle.setHeight(bounds.height());
        rectangle.setVisible(visible);
    }
}
