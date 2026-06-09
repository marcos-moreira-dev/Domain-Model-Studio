package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import java.util.Set;

/**
 * Permite que un diagrama declare nodos que deben moverse visualmente juntos durante
 * el arrastre preliminar, sin acoplar el canvas común a un tipo de diagrama concreto.
 */
public interface CanvasDragPreviewPort {

    /**
     * Devuelve los ids visuales que deben acompañar al nodo arrastrado durante la
     * previsualización. La posición real sigue persistida por CanvasLayoutCommandPort.
     */
    Set<String> previewNodeIdsForDraggedNode(String draggedNodeId, Set<String> selectedNodeIds);
}
