package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import java.util.Objects;

/**
 * Drag de nodos seleccionados. No decide semántica ni crea elementos.
 *
 * <p>El controlador conserva el identificador del nodo que inició el gesto para que
 * un refresco externo o una sincronización panel-canvas no deje el siguiente
 * arrastre sin objetivo real. Si durante {@code MOUSE_DRAGGED} la selección
 * semántica ya no contiene ese nodo, se reasegura antes de aplicar el delta.</p>
 */
public final class CanvasNodeDragController {

    private final CanvasSelectionPort selectionPort;
    private final CanvasLayoutCommandPort layoutPort;
    private final CanvasDirtyPort dirtyPort;
    private String draggedNodeId;
    private boolean active;
    private double lastCanvasX;
    private double lastCanvasY;

    public CanvasNodeDragController(InteractiveCanvasAdapter adapter) {
        this(adapter, adapter, adapter);
    }

    public CanvasNodeDragController(
            CanvasSelectionPort selectionPort,
            CanvasLayoutCommandPort layoutPort,
            CanvasDirtyPort dirtyPort
    ) {
        this.selectionPort = Objects.requireNonNull(selectionPort, "El puerto de selección no puede ser null");
        this.layoutPort = Objects.requireNonNull(layoutPort, "El puerto de layout no puede ser null");
        this.dirtyPort = Objects.requireNonNull(dirtyPort, "El puerto de cambios no puede ser null");
    }

    public void begin(String nodeId, double canvasX, double canvasY, boolean additive) {
        draggedNodeId = Objects.requireNonNull(nodeId, "El nodo arrastrado no puede ser null");
        if (!selectionPort.selection().isNodeSelected(draggedNodeId)) {
            selectionPort.selectNode(draggedNodeId, additive);
        }
        active = true;
        lastCanvasX = canvasX;
        lastCanvasY = canvasY;
    }

    public void dragTo(double canvasX, double canvasY) {
        if (!active) {
            return;
        }
        ensureDraggedNodeStillSelected();
        double deltaX = canvasX - lastCanvasX;
        double deltaY = canvasY - lastCanvasY;
        layoutPort.moveSelectedNodesBy(deltaX, deltaY);
        layoutPort.moveSelectedConnectorBendPointsBy(deltaX, deltaY);
        dirtyPort.markDirty();
        lastCanvasX = canvasX;
        lastCanvasY = canvasY;
    }

    public void end() {
        active = false;
        draggedNodeId = null;
    }

    private void ensureDraggedNodeStillSelected() {
        if (draggedNodeId != null && !selectionPort.selection().isNodeSelected(draggedNodeId)) {
            selectionPort.selectNode(draggedNodeId, false);
        }
    }

    public boolean active() {
        return active;
    }
}
