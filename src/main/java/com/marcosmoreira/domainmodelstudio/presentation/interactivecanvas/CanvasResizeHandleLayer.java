package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.DiagramInteractionProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/** Capa de agarradores para redimensionar nodos seleccionados en el canvas común. */
final class CanvasResizeHandleLayer {

    private static final double HANDLE_SIZE = 12.0;
    private static final double HIT_SIZE = 24.0;

    private final CanvasResizePort resizePort;
    private final DiagramInteractionProfile interactionProfile;
    private final CanvasPointMapper pointMapper;
    private final Runnable refreshPreservingViewport;
    private final Group livePreviewLayer;
    private String activeNodeId;
    private double startCanvasX;
    private double startCanvasY;
    private double startNodeX;
    private double startNodeY;
    private double startWidth;
    private double startHeight;

    CanvasResizeHandleLayer(
            CanvasResizePort resizePort,
            DiagramInteractionProfile interactionProfile,
            CanvasPointMapper pointMapper,
            Runnable refreshPreservingViewport,
            Group livePreviewLayer
    ) {
        this.resizePort = resizePort;
        this.interactionProfile = Objects.requireNonNull(interactionProfile, "interactionProfile");
        this.pointMapper = Objects.requireNonNull(pointMapper, "pointMapper");
        this.refreshPreservingViewport = Objects.requireNonNull(refreshPreservingViewport, "refreshPreservingViewport");
        this.livePreviewLayer = Objects.requireNonNull(livePreviewLayer, "livePreviewLayer");
    }

    List<Node> renderHandles(InteractiveCanvasModel model) {
        if (!interactionProfile.supportsNodeResize() || resizePort == null || !resizePort.supportsNodeResize()) {
            return List.of();
        }
        List<Node> handles = new ArrayList<>();
        for (String nodeId : model.selection().selectedNodeIds()) {
            if (resizePort.supportsNodeResize(nodeId)) {
                model.layoutForNode(nodeId).ifPresent(layout -> handles.add(renderHandle(nodeId, layout)));
            }
        }
        return handles;
    }

    private Node renderHandle(String nodeId, NodeLayout layout) {
        double handleX = layout.x() + layout.width() - HANDLE_SIZE / 2.0;
        double handleY = layout.y() + layout.height() - HANDLE_SIZE / 2.0;
        double hitOffset = (HIT_SIZE - HANDLE_SIZE) / 2.0;
        Rectangle hitTarget = new Rectangle(handleX - hitOffset, handleY - hitOffset, HIT_SIZE, HIT_SIZE);
        hitTarget.getStyleClass().add("interactive-canvas-resize-handle-hitbox");
        hitTarget.setFill(Color.TRANSPARENT);
        hitTarget.setManaged(false);
        hitTarget.setCursor(Cursor.SE_RESIZE);

        Rectangle handle = new Rectangle(handleX, handleY, HANDLE_SIZE, HANDLE_SIZE);
        handle.getStyleClass().add("interactive-canvas-resize-handle");
        handle.setManaged(false);
        handle.setCursor(Cursor.SE_RESIZE);

        Group group = new Group(hitTarget, handle);
        group.setManaged(false);
        group.setCursor(Cursor.SE_RESIZE);
        group.setUserData(nodeId + ":resize-handle");
        installGestures(group, nodeId, layout);
        installGestures(hitTarget, nodeId, layout);
        installGestures(handle, nodeId, layout);
        return group;
    }

    private void installGestures(Node target, String nodeId, NodeLayout layout) {
        target.setOnMousePressed(event -> begin(event, nodeId, layout));
        target.setOnMouseDragged(this::dragPreview);
        target.setOnMouseReleased(this::finish);
    }

    private void begin(MouseEvent event, String nodeId, NodeLayout layout) {
        if (event.getButton() != MouseButton.PRIMARY) {
            return;
        }
        Point2D canvasPoint = pointMapper.toCanvas(event.getSceneX(), event.getSceneY());
        activeNodeId = nodeId;
        startCanvasX = canvasPoint.getX();
        startCanvasY = canvasPoint.getY();
        startNodeX = layout.x();
        startNodeY = layout.y();
        startWidth = layout.width();
        startHeight = layout.height();
        showPreview(startWidth, startHeight);
        event.consume();
    }

    private void dragPreview(MouseEvent event) {
        if (!event.isPrimaryButtonDown() || activeNodeId == null) {
            return;
        }
        showPreview(liveWidth(event), liveHeight(event));
        event.consume();
    }

    private void finish(MouseEvent event) {
        if (activeNodeId != null) {
            resizePort.resizeNode(activeNodeId, liveWidth(event), liveHeight(event));
        }
        activeNodeId = null;
        clearPreview();
        refreshPreservingViewport.run();
        event.consume();
    }

    private void showPreview(double width, double height) {
        double safeWidth = Math.max(1.0, width);
        double safeHeight = Math.max(1.0, height);
        Rectangle bounds = new Rectangle(startNodeX, startNodeY, safeWidth, safeHeight);
        bounds.getStyleClass().add("interactive-canvas-resize-preview");
        bounds.setManaged(false);
        bounds.setMouseTransparent(true);

        Rectangle corner = new Rectangle(
                startNodeX + safeWidth - HANDLE_SIZE / 2.0,
                startNodeY + safeHeight - HANDLE_SIZE / 2.0,
                HANDLE_SIZE,
                HANDLE_SIZE
        );
        corner.getStyleClass().add("interactive-canvas-resize-preview-handle");
        corner.setManaged(false);
        corner.setMouseTransparent(true);
        livePreviewLayer.getChildren().setAll(bounds, corner);
    }

    private void clearPreview() {
        livePreviewLayer.getChildren().clear();
    }

    private double liveWidth(MouseEvent event) {
        return Math.max(1.0, startWidth + pointMapper.toCanvas(event.getSceneX(), event.getSceneY()).getX() - startCanvasX);
    }

    private double liveHeight(MouseEvent event) {
        return Math.max(1.0, startHeight + pointMapper.toCanvas(event.getSceneX(), event.getSceneY()).getY() - startCanvasY);
    }

    @FunctionalInterface
    interface CanvasPointMapper {
        Point2D toCanvas(double sceneX, double sceneY);
    }
}
