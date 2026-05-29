package com.marcosmoreira.domainmodelstudio.presentation.canvas.interaction;

import java.util.Objects;
import java.util.function.DoubleSupplier;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.control.ScrollPane;

/**
 * Controlador de paneo del canvas conceptual.
 *
 * <p>Reserva el clic derecho para desplazar el área de trabajo. No abre menús
 * contextuales ni decide semántica del diagrama.</p>
 */
public final class DiagramCanvasPanController {

    private final ScrollPane scrollPane;
    private final Pane cursorTarget;
    private final Pane zoomContainer;
    private final DoubleSupplier zoomFactorSupplier;
    private final double workspaceWidth;
    private final double workspaceHeight;

    private boolean active;
    private double startSceneX;
    private double startSceneY;
    private double startHvalue;
    private double startVvalue;

    public DiagramCanvasPanController(
            ScrollPane scrollPane,
            Pane cursorTarget,
            Pane zoomContainer,
            DoubleSupplier zoomFactorSupplier,
            double workspaceWidth,
            double workspaceHeight
    ) {
        this.scrollPane = Objects.requireNonNull(scrollPane, "scrollPane");
        this.cursorTarget = Objects.requireNonNull(cursorTarget, "cursorTarget");
        this.zoomContainer = Objects.requireNonNull(zoomContainer, "zoomContainer");
        this.zoomFactorSupplier = Objects.requireNonNull(zoomFactorSupplier, "zoomFactorSupplier");
        this.workspaceWidth = workspaceWidth;
        this.workspaceHeight = workspaceHeight;
    }

    public void begin(MouseEvent event) {
        active = true;
        startSceneX = event.getSceneX();
        startSceneY = event.getSceneY();
        startHvalue = scrollPane.getHvalue();
        startVvalue = scrollPane.getVvalue();
        cursorTarget.setCursor(Cursor.CLOSED_HAND);
        event.consume();
    }

    public void drag(MouseEvent event) {
        if (!active) {
            return;
        }
        double viewportWidth = Math.max(1.0, scrollPane.getViewportBounds().getWidth());
        double viewportHeight = Math.max(1.0, scrollPane.getViewportBounds().getHeight());
        double hMax = horizontalScrollRange(viewportWidth);
        double vMax = verticalScrollRange(viewportHeight);
        double deltaX = event.getSceneX() - startSceneX;
        double deltaY = event.getSceneY() - startSceneY;
        scrollPane.setHvalue(clamp01(startHvalue - deltaX / hMax));
        scrollPane.setVvalue(clamp01(startVvalue - deltaY / vMax));
        event.consume();
    }

    public void end(MouseEvent event) {
        active = false;
        cursorTarget.setCursor(Cursor.DEFAULT);
        event.consume();
    }

    public boolean active() {
        return active;
    }

    private double horizontalScrollRange(double viewportWidth) {
        double zoomFactor = zoomFactorSupplier.getAsDouble();
        double contentWidth = Math.max(workspaceWidth * zoomFactor, zoomContainer.getLayoutBounds().getWidth());
        return Math.max(1.0, contentWidth - viewportWidth);
    }

    private double verticalScrollRange(double viewportHeight) {
        double zoomFactor = zoomFactorSupplier.getAsDouble();
        double contentHeight = Math.max(workspaceHeight * zoomFactor, zoomContainer.getLayoutBounds().getHeight());
        return Math.max(1.0, contentHeight - viewportHeight);
    }

    private static double clamp01(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }
}
