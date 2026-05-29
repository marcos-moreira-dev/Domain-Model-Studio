package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/** Puerto opcional para operaciones de previsualización viva durante edición visual. */
public interface CanvasLivePreviewPort {

    void beginPreview(String elementId, double canvasX, double canvasY);

    void updatePreview(String elementId, double canvasX, double canvasY);

    void commitPreview();

    void cancelPreview();

    default boolean supportsLivePreview() {
        return true;
    }
}
