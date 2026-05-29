package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/** Marca que un editor especializado tiene cambios visuales pendientes de guardar. */
public interface CanvasDirtyPort {

    void markDirty();
}
