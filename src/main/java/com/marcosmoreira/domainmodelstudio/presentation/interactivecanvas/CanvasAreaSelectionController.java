package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import java.util.Objects;

/**
 * Selección rectangular con clic izquierdo sobre fondo.
 */
public final class CanvasAreaSelectionController {

    private static final double MIN_SELECTION_SIZE = 4.0;

    private final CanvasSelectionPort selectionPort;
    private boolean active;
    private boolean additive;
    private double startX;
    private double startY;
    private CanvasBounds currentBounds;

    public CanvasAreaSelectionController(InteractiveCanvasAdapter adapter) {
        this((CanvasSelectionPort) adapter);
    }

    public CanvasAreaSelectionController(CanvasSelectionPort selectionPort) {
        this.selectionPort = Objects.requireNonNull(selectionPort, "El puerto de selección no puede ser null");
    }

    public void begin(double canvasX, double canvasY, boolean additive) {
        this.active = true;
        this.additive = additive;
        this.startX = canvasX;
        this.startY = canvasY;
        this.currentBounds = CanvasBounds.between(canvasX, canvasY, canvasX, canvasY);
    }

    public CanvasBounds dragTo(double canvasX, double canvasY) {
        if (!active) {
            return currentBounds;
        }
        currentBounds = CanvasBounds.between(startX, startY, canvasX, canvasY);
        return currentBounds;
    }

    public boolean end() {
        boolean applied = false;
        if (active && currentBounds != null && selectionGesture()) {
            if (!additive) {
                selectionPort.clearSelection();
            }
            selectionPort.selectElementsInside(currentBounds, additive);
            applied = true;
        }
        active = false;
        currentBounds = null;
        return applied;
    }

    public boolean selectionGesture() {
        return currentBounds != null
                && (currentBounds.width() >= MIN_SELECTION_SIZE || currentBounds.height() >= MIN_SELECTION_SIZE);
    }

    public boolean active() {
        return active;
    }

    public CanvasBounds currentBounds() {
        return currentBounds;
    }
}
