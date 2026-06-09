package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/** Fondo de workspace compartido: relleno sobrio y grilla menor/mayor. */
public final class DiagramSurfaceBackground {

    private static final double MINOR_GRID_STEP = 80.0;
    private static final double MAJOR_GRID_STEP = 400.0;

    private DiagramSurfaceBackground() {
    }

    public static void rebuild(Group backgroundLayer, Rectangle workspaceFill, DiagramSurfaceConfig config) {
        rebuild(backgroundLayer, workspaceFill, config, config.workspaceWidth(), config.workspaceHeight());
    }

    public static void rebuild(
            Group backgroundLayer,
            Rectangle workspaceFill,
            DiagramSurfaceConfig config,
            double workspaceWidth,
            double workspaceHeight
    ) {
        backgroundLayer.getChildren().clear();
        workspaceFill.setWidth(workspaceWidth);
        workspaceFill.setHeight(workspaceHeight);
        workspaceFill.getStyleClass().setAll("workspace-fill", "diagram-surface-fill");
        backgroundLayer.getChildren().add(workspaceFill);

        if (config.showGrid()) {
            addGrid(backgroundLayer, workspaceWidth, workspaceHeight);
        }
        backgroundLayer.setMouseTransparent(true);
    }

    private static void addGrid(Group backgroundLayer, double width, double height) {
        for (double x = 0; x <= width; x += MINOR_GRID_STEP) {
            Line line = new Line(x, 0, x, height);
            line.getStyleClass().setAll(gridClass(x), surfaceGridClass(x));
            backgroundLayer.getChildren().add(line);
        }
        for (double y = 0; y <= height; y += MINOR_GRID_STEP) {
            Line line = new Line(0, y, width, y);
            line.getStyleClass().setAll(gridClass(y), surfaceGridClass(y));
            backgroundLayer.getChildren().add(line);
        }
    }

    private static String gridClass(double coordinate) {
        return isMajorLine(coordinate) ? "workspace-grid-major-line" : "workspace-grid-line";
    }

    private static String surfaceGridClass(double coordinate) {
        return isMajorLine(coordinate) ? "diagram-surface-grid-major-line" : "diagram-surface-grid-line";
    }

    private static boolean isMajorLine(double coordinate) {
        return Math.abs(coordinate % MAJOR_GRID_STEP) < 0.01;
    }
}
