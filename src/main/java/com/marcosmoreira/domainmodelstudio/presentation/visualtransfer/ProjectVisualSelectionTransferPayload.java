package com.marcosmoreira.domainmodelstudio.presentation.visualtransfer;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Payload genérico de una selección visual de un proyecto DMS.
 *
 * <p>Está pensado para diagramas visuales del mismo tipo: modelo conceptual,
 * arquitectura, comportamiento, mapa de módulos, flujo de pantallas, wireframes
 * y UML clases. Conserva el proyecto fuente para poder reconstruir la semántica
 * de dominio y no solo la geometría del canvas.</p>
 */
public record ProjectVisualSelectionTransferPayload(
        DiagramTypeId diagramTypeId,
        String sourceProjectTitle,
        DiagramProject sourceProject,
        Set<String> selectedNodeIds,
        Set<String> selectedConnectorIds,
        List<NodeLayout> nodeLayouts,
        List<ConnectorLayout> connectorLayouts
) implements VisualSelectionTransferPayload {

    public ProjectVisualSelectionTransferPayload {
        if (diagramTypeId == null) {
            throw new IllegalArgumentException("El tipo de diagrama no puede estar vacío.");
        }
        if (sourceProject == null) {
            throw new IllegalArgumentException("El proyecto fuente no puede estar vacío.");
        }
        sourceProjectTitle = sourceProjectTitle == null || sourceProjectTitle.isBlank()
                ? diagramTypeId.value()
                : sourceProjectTitle.strip();
        selectedNodeIds = Set.copyOf(selectedNodeIds == null ? Set.of() : new LinkedHashSet<>(selectedNodeIds));
        selectedConnectorIds = Set.copyOf(selectedConnectorIds == null ? Set.of() : new LinkedHashSet<>(selectedConnectorIds));
        nodeLayouts = List.copyOf(nodeLayouts == null ? List.of() : nodeLayouts);
        connectorLayouts = List.copyOf(connectorLayouts == null ? List.of() : connectorLayouts);
    }

    @Override
    public int nodeCount() {
        return selectedNodeIds.size();
    }

    @Override
    public int connectorCount() {
        return selectedConnectorIds.size();
    }
}
