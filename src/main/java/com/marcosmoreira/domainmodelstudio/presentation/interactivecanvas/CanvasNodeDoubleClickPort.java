package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/** Puerto opcional para edicion por doble clic sobre una zona de un nodo. */
public interface CanvasNodeDoubleClickPort {

    boolean handleNodeDoubleClick(String nodeId, String role);
}
