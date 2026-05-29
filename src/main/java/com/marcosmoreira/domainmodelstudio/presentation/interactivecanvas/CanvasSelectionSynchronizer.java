package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import java.util.Objects;

/**
 * Operaciones explícitas de sincronización panel ↔ canvas.
 *
 * <p>No sabe qué es un módulo, una clase UML o una pantalla. Solo aplica selección
 * normalizada sobre el puerto del canvas con una guardia anti-recursión.</p>
 */
public final class CanvasSelectionSynchronizer {

    private final CanvasSelectionPort selectionPort;
    private final CanvasSelectionSynchronizationGuard guard;

    public CanvasSelectionSynchronizer(CanvasSelectionPort selectionPort) {
        this(selectionPort, new CanvasSelectionSynchronizationGuard());
    }

    public CanvasSelectionSynchronizer(
            CanvasSelectionPort selectionPort,
            CanvasSelectionSynchronizationGuard guard
    ) {
        this.selectionPort = Objects.requireNonNull(selectionPort, "El puerto de selección no puede ser null");
        this.guard = Objects.requireNonNull(guard, "La guardia de sincronización no puede ser null");
    }

    public void selectNodeFromPanel(String nodeId) {
        guard.runGuarded(() -> selectionPort.selectNode(nodeId, false));
    }

    public void selectConnectorFromPanel(String connectorId) {
        guard.runGuarded(() -> selectionPort.selectConnector(connectorId, false));
    }

    public void selectNodeFromCanvas(String nodeId, boolean additive) {
        guard.runGuarded(() -> selectionPort.selectNode(nodeId, additive));
    }

    public void selectConnectorFromCanvas(String connectorId, boolean additive) {
        guard.runGuarded(() -> selectionPort.selectConnector(connectorId, additive));
    }

    public void clearFromCanvas() {
        guard.runGuarded(selectionPort::clearSelection);
    }

    public boolean synchronizing() {
        return guard.active();
    }
}
