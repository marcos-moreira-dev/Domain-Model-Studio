package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/** Puerto opcional para acciones globales sobre notas visuales seleccionadas. */
public interface CanvasVisualCommentPort {

    void activateCommentPlacement();

    boolean deleteSelectedVisualComment();

    boolean hasSelectedVisualComment();
}
