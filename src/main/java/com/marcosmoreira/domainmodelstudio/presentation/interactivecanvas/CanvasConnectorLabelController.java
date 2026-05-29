package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import java.util.Objects;

/**
 * Controlador común para mover etiquetas de conectores desde el lienzo interactivo.
 *
 * <p>El controlador solo conoce el puerto de edición de etiquetas y el estado sucio del
 * editor. La decisión de cómo persistir el desplazamiento vive en el adapter concreto.</p>
 */
public final class CanvasConnectorLabelController {

    private final CanvasConnectorLabelPort labelPort;
    private final CanvasDirtyPort dirtyPort;

    public CanvasConnectorLabelController(CanvasConnectorLabelPort labelPort, CanvasDirtyPort dirtyPort) {
        this.labelPort = Objects.requireNonNull(labelPort, "El puerto de etiquetas no puede ser null");
        this.dirtyPort = Objects.requireNonNull(dirtyPort, "El puerto de cambios no puede ser null");
    }

    public void moveBy(String connectorId, double deltaX, double deltaY) {
        if (!labelPort.supportsConnectorLabels()) {
            return;
        }
        labelPort.moveConnectorLabelBy(connectorId, deltaX, deltaY);
        dirtyPort.markDirty();
    }
}
