package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/**
 * Acciones comunes que la toolbar contextual puede enviar al canvas activo.
 */
public enum InteractiveCanvasCommand {
    SELECT_MODE,
    CONNECT_MODE,
    ZOOM_IN,
    ZOOM_OUT,
    RESET_ZOOM,
    FIT_TO_CONTENT,
    CENTER_SELECTION,
    DELETE_SELECTED,
    DELETE_SELECTED_BEND_POINT,
    ADD_BEND_POINT_MODE
}
