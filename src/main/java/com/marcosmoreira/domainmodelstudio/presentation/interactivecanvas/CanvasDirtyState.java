package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/** Estado mínimo de cambios visuales pendientes reutilizable por adaptadores del canvas. */
public final class CanvasDirtyState {

    private boolean dirty;

    public boolean dirty() {
        return dirty;
    }

    public void markDirty() {
        dirty = true;
    }

    public void clearDirty() {
        dirty = false;
    }
}
