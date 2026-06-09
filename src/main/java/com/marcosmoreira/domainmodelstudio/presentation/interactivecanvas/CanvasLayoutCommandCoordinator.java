package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import java.util.Objects;

/**
 * Fachada mínima para comandos visuales de layout.
 *
 * <p>Ayuda a que las futuras superficies canónicas muevan nodos sin depender de un
 * adapter monolítico ni modificar posiciones dentro de nodos JavaFX.</p>
 */
public final class CanvasLayoutCommandCoordinator {

    private final CanvasLayoutCommandPort layoutPort;
    private final CanvasDirtyPort dirtyPort;

    public CanvasLayoutCommandCoordinator(CanvasLayoutCommandPort layoutPort, CanvasDirtyPort dirtyPort) {
        this.layoutPort = Objects.requireNonNull(layoutPort, "El puerto de layout no puede ser null");
        this.dirtyPort = Objects.requireNonNull(dirtyPort, "El puerto de cambios no puede ser null");
    }

    public void moveNodeTo(String nodeId, double x, double y) {
        layoutPort.moveNode(nodeId, x, y);
        dirtyPort.markDirty();
    }

    public void moveSelectedBy(double deltaX, double deltaY) {
        layoutPort.moveSelectedNodesBy(deltaX, deltaY);
        dirtyPort.markDirty();
    }
}
