package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/**
 * Soporte pequeño para mantener consistente la selección de puntos intermedios.
 *
 * <p>La persistencia de cada punto sigue perteneciendo al adaptador y a su ViewModel;
 * este soporte evita repetir la mecánica de selección transversal.</p>
 */
public final class CanvasBendPointEditingSupport {

    private final CanvasSelectionSupport selectionSupport;
    private final CanvasDirtyState dirtyState;

    public CanvasBendPointEditingSupport(CanvasSelectionSupport selectionSupport, CanvasDirtyState dirtyState) {
        this.selectionSupport = java.util.Objects.requireNonNull(selectionSupport, "selectionSupport");
        this.dirtyState = java.util.Objects.requireNonNull(dirtyState, "dirtyState");
    }

    public void selectBendPoint(String connectorId, int index) {
        selectionSupport.selectBendPoint(connectorId, index);
    }

    public void markEditedBendPoint(String connectorId, int index) {
        selectBendPoint(connectorId, index);
        dirtyState.markDirty();
    }

    public void clearAfterRemoval() {
        selectionSupport.clear();
        dirtyState.markDirty();
    }
}
