package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/**
 * Puerto de selección del lienzo común.
 *
 * <p>Permite sincronizar árbol/lista izquierda, canvas e inspector derecho sin que la
 * infraestructura conozca el ViewModel específico del diagrama activo.</p>
 */
public interface CanvasSelectionPort {

    InteractiveCanvasSelection selection();

    void selectNode(String elementId, boolean additive);

    void selectConnector(String connectorId, boolean additive);

    void selectNodesInside(CanvasBounds selectionBounds, boolean additive);

    default void selectElementsInside(CanvasBounds selectionBounds, boolean additive) {
        selectNodesInside(selectionBounds, additive);
    }

    void clearSelection();
}
