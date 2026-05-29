package com.marcosmoreira.domainmodelstudio.presentation.visualtransfer;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;

/**
 * Payload común del portapapeles visual entre pestañas del mismo tipo.
 *
 * <p>Transporta una selección semántica con nodos y relaciones. No usa el
 * portapapeles del sistema porque los objetos deben conservar identidad lógica,
 * propiedades y layout DMS.</p>
 */
public interface VisualSelectionTransferPayload {

    DiagramTypeId diagramTypeId();

    String sourceProjectTitle();

    int nodeCount();

    int connectorCount();

    default boolean empty() {
        return nodeCount() == 0 && connectorCount() == 0;
    }
}
