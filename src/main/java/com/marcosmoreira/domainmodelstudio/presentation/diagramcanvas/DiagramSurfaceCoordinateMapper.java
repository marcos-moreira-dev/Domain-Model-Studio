package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import java.util.Collection;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

/** Conversor único entre escena, viewport y coordenadas de workspace. */
public final class DiagramSurfaceCoordinateMapper {

    private final ScrollPane scrollPane;
    private final Pane workspaceRoot;

    DiagramSurfaceCoordinateMapper(ScrollPane scrollPane, Pane workspaceRoot) {
        this.scrollPane = scrollPane;
        this.workspaceRoot = workspaceRoot;
    }

    public Point2D sceneToWorkspace(double sceneX, double sceneY) {
        return workspaceRoot.sceneToLocal(sceneX, sceneY);
    }

    public Point2D viewportToWorkspace(double viewportX, double viewportY) {
        Point2D scenePoint = scrollPane.localToScene(viewportX, viewportY);
        return sceneToWorkspace(scenePoint.getX(), scenePoint.getY());
    }

    public Point2D workspaceToScene(double workspaceX, double workspaceY) {
        return workspaceRoot.localToScene(workspaceX, workspaceY);
    }

    public Bounds workspaceBoundsOf(Node node) {
        Bounds sceneBounds = node.localToScene(node.getBoundsInLocal());
        return workspaceRoot.sceneToLocal(sceneBounds);
    }

    public Bounds contentBoundsOf(Collection<? extends Node> nodes) {
        Bounds result = null;
        if (nodes == null) {
            return null;
        }
        for (Node node : nodes) {
            if (node == null || !node.isVisible()) {
                continue;
            }
            Bounds current = workspaceBoundsOf(node);
            result = result == null ? current : union(result, current);
        }
        return result;
    }

    public static Bounds union(Bounds first, Bounds second) {
        double minX = Math.min(first.getMinX(), second.getMinX());
        double minY = Math.min(first.getMinY(), second.getMinY());
        double maxX = Math.max(first.getMaxX(), second.getMaxX());
        double maxY = Math.max(first.getMaxY(), second.getMaxY());
        return new BoundingBox(minX, minY, maxX - minX, maxY - minY);
    }
}
