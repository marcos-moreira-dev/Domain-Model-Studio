package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import java.util.Objects;

/**
 * Soporte reutilizable para persistir desplazamientos de etiquetas de conectores.
 *
 * <p>Los adapters concretos pueden usar esta pieza cuando deleguen la escritura de
 * offsets a un {@link CanvasConnectorLabelPort} y quieran mantener un estado sucio
 * homogéneo con el resto del canvas común.</p>
 */
public final class CanvasConnectorLabelEditingSupport {

    private final CanvasConnectorLabelPort labelPort;
    private final CanvasDirtyState dirtyState;

    public CanvasConnectorLabelEditingSupport(CanvasConnectorLabelPort labelPort, CanvasDirtyState dirtyState) {
        this.labelPort = Objects.requireNonNull(labelPort, "labelPort");
        this.dirtyState = Objects.requireNonNull(dirtyState, "dirtyState");
    }

    public void moveLabelBy(String connectorId, double deltaX, double deltaY) {
        labelPort.moveConnectorLabelBy(connectorId, deltaX, deltaY);
        dirtyState.markDirty();
    }
}
