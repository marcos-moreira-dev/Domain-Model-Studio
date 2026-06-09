package com.marcosmoreira.domainmodelstudio.presentation.canvas;

import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 * Construye el fondo visual del lienzo infinito.
 *
 * <p>El fondo queda aislado del canvas para que futuras familias de diagramas puedan
 * reutilizar o reemplazar la grilla sin mezclarla con selección, paneo o renderizado.</p>
 */
final class CanvasWorkspaceBackground {

    private static final double MINOR_GRID_STEP = 80.0;
    private static final double MAJOR_GRID_STEP = 400.0;

    private CanvasWorkspaceBackground() {
    }

    static void rebuild(Group workspaceBackground, Rectangle workspaceFill, double width, double height) {
        workspaceBackground.getChildren().clear();
        workspaceFill.getStyleClass().add("workspace-fill");
        workspaceBackground.getChildren().add(workspaceFill);

        for (double x = 0; x <= width; x += MINOR_GRID_STEP) {
            Line line = new Line(x, 0, x, height);
            line.getStyleClass().add(isMajorLine(x) ? "workspace-grid-major-line" : "workspace-grid-line");
            workspaceBackground.getChildren().add(line);
        }
        for (double y = 0; y <= height; y += MINOR_GRID_STEP) {
            Line line = new Line(0, y, width, y);
            line.getStyleClass().add(isMajorLine(y) ? "workspace-grid-major-line" : "workspace-grid-line");
            workspaceBackground.getChildren().add(line);
        }
        workspaceBackground.setMouseTransparent(true);
    }

    private static boolean isMajorLine(double coordinate) {
        return Math.abs(coordinate % MAJOR_GRID_STEP) < 0.01;
    }
}
