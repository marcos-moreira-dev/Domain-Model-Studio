package com.marcosmoreira.domainmodelstudio.presentation.canvas.interaction;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.BooleanSupplier;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

/**
 * Selección rectangular del canvas conceptual.
 *
 * <p>Esta clase solo dibuja y finaliza la región de selección. La decisión de
 * qué elementos caen dentro sigue delegada al canvas conceptual.</p>
 */
public final class DiagramCanvasAreaSelectionController {

    private final Group contentLayer;
    private final BooleanSupplier hasActiveProject;
    private final Runnable clearSelection;
    private final BiConsumer<Set<DiagramElementId>, Boolean> selectElements;
    private final Function<Rectangle2D, Set<DiagramElementId>> selectableNodesInside;

    private boolean active;
    private double startX;
    private double startY;
    private Rectangle selectionArea;

    public DiagramCanvasAreaSelectionController(
            Group contentLayer,
            BooleanSupplier hasActiveProject,
            Runnable clearSelection,
            BiConsumer<Set<DiagramElementId>, Boolean> selectElements,
            Function<Rectangle2D, Set<DiagramElementId>> selectableNodesInside
    ) {
        this.contentLayer = Objects.requireNonNull(contentLayer, "contentLayer");
        this.hasActiveProject = Objects.requireNonNull(hasActiveProject, "hasActiveProject");
        this.clearSelection = Objects.requireNonNull(clearSelection, "clearSelection");
        this.selectElements = Objects.requireNonNull(selectElements, "selectElements");
        this.selectableNodesInside = Objects.requireNonNull(selectableNodesInside, "selectableNodesInside");
    }

    public void begin(MouseEvent event) {
        if (!hasActiveProject.getAsBoolean()) {
            clearSelection.run();
            return;
        }
        Point2D start = contentLayer.sceneToLocal(event.getSceneX(), event.getSceneY());
        active = true;
        startX = start.getX();
        startY = start.getY();
        selectionArea = new Rectangle(startX, startY, 0, 0);
        selectionArea.getStyleClass().add("diagram-selection-area");
        selectionArea.setMouseTransparent(true);
        contentLayer.getChildren().add(selectionArea);
        event.consume();
    }

    public void drag(MouseEvent event) {
        if (selectionArea == null) {
            return;
        }
        Point2D current = contentLayer.sceneToLocal(event.getSceneX(), event.getSceneY());
        double x = Math.min(startX, current.getX());
        double y = Math.min(startY, current.getY());
        double width = Math.abs(current.getX() - startX);
        double height = Math.abs(current.getY() - startY);
        selectionArea.setX(x);
        selectionArea.setY(y);
        selectionArea.setWidth(width);
        selectionArea.setHeight(height);
        event.consume();
    }

    public void end(MouseEvent event) {
        if (selectionArea == null) {
            active = false;
            return;
        }
        Rectangle2D selectionBounds = new Rectangle2D(
                selectionArea.getX(),
                selectionArea.getY(),
                selectionArea.getWidth(),
                selectionArea.getHeight()
        );
        contentLayer.getChildren().remove(selectionArea);
        selectionArea = null;
        active = false;

        if (selectionBounds.getWidth() < 4.0 && selectionBounds.getHeight() < 4.0) {
            if (!event.isShiftDown()) {
                clearSelection.run();
            }
            event.consume();
            return;
        }

        Set<DiagramElementId> selectedIds = selectableNodesInside.apply(selectionBounds);
        if (selectedIds.isEmpty() && !event.isShiftDown()) {
            clearSelection.run();
        } else if (!selectedIds.isEmpty()) {
            selectElements.accept(selectedIds, event.isShiftDown());
        }
        event.consume();
    }

    public boolean active() {
        return active;
    }
}
