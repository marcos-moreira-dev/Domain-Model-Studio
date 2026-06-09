package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import java.util.Objects;

/**
 * Coordina operaciones comunes sobre puntos intermedios de conectores.
 *
 * <p>El controlador no decide cómo una familia guarda sus bendpoints; solo consulta si el
 * adaptador los soporta y delega la creación, selección, movimiento o eliminación. Esto permite
 * que UML, arquitectura, grafo libre u otros workspaces compartan el mismo gesto visual sin
 * mezclar la persistencia específica de cada tipo.</p>
 *
 * <p>Cuando un punto se agrega por doble clic, el adaptador debe insertarlo en el segmento más cercano para conservar el orden geométrico de la ruta y evitar deformaciones visuales.</p>
 
 *
 * <p><strong>Ejemplo pedagógico:</strong> al hacer doble clic en una relación, el
 * controlador no decide dónde se almacena el punto. Delegar en el adaptador permite
 * que cada diagrama inserte el bendpoint en el segmento correcto sin deformar la ruta.</p>
 */
public final class CanvasBendPointController {

    private final CanvasBendPointPort bendPointPort;
    private final CanvasDirtyPort dirtyPort;

    public CanvasBendPointController(InteractiveCanvasAdapter adapter) {
        this(adapter, adapter);
    }

    public CanvasBendPointController(CanvasBendPointPort bendPointPort, CanvasDirtyPort dirtyPort) {
        this.bendPointPort = Objects.requireNonNull(bendPointPort, "El puerto de puntos intermedios no puede ser null");
        this.dirtyPort = Objects.requireNonNull(dirtyPort, "El puerto de cambios no puede ser null");
    }

    public void add(String connectorId, double canvasX, double canvasY) {
        if (!bendPointPort.supportsBendPoints()) {
            return;
        }
        bendPointPort.addBendPoint(connectorId, canvasX, canvasY);
        dirtyPort.markDirty();
    }

    public void select(String connectorId, int index) {
        if (!bendPointPort.supportsBendPoints()) {
            return;
        }
        bendPointPort.selectBendPoint(connectorId, index);
    }

    public void move(String connectorId, int index, double canvasX, double canvasY) {
        if (!bendPointPort.supportsBendPoints()) {
            return;
        }
        bendPointPort.moveBendPoint(connectorId, index, canvasX, canvasY);
        dirtyPort.markDirty();
    }

    public void removeSelected() {
        if (!bendPointPort.supportsBendPoints()) {
            return;
        }
        bendPointPort.removeSelectedBendPoint();
        dirtyPort.markDirty();
    }
}
