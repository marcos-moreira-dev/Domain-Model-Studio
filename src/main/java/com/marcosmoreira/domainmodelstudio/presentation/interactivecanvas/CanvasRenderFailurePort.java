package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/** Puerto opcional para que un editor especializado degrade la vista si el canvas falla. */
public interface CanvasRenderFailurePort {
    void handleCanvasRenderFailure(CanvasRenderFailureReport report);
}
