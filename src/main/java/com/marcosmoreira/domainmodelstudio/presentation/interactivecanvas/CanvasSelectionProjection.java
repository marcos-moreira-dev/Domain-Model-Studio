package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/** Proyecta la selección de conectores sobre sus nodos extremos para feedback visual. */
final class CanvasSelectionProjection {

    private CanvasSelectionProjection() {
    }

    static boolean isNodeSelectedOrEndpointOfSelectedConnector(InteractiveCanvasModel model, String nodeId) {
        if (model.selection().isNodeSelected(nodeId)) {
            return true;
        }
        for (InteractiveCanvasConnector connector : model.visibleConnectors()) {
            if (model.selection().isConnectorSelected(connector.id())
                    && (nodeId.equals(connector.sourceNodeId()) || nodeId.equals(connector.targetNodeId()))) {
                return true;
            }
        }
        return false;
    }
}
