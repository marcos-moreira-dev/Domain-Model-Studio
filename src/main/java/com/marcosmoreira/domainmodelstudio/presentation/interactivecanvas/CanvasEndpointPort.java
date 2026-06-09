package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/** Puerto opcional para reconectar extremos de una relación cuando el tipo activo lo permite. */
public interface CanvasEndpointPort {

    void reconnectEndpoint(String connectorId, CanvasConnectorEndpoint endpoint, String targetNodeId);

    default boolean supportsEndpointDragging() {
        return false;
    }
}
