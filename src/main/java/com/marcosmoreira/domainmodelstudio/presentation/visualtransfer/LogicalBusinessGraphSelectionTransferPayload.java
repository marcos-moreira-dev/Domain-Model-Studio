package com.marcosmoreira.domainmodelstudio.presentation.visualtransfer;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNode;
import java.util.List;

/** Selección copiada del Grafo lógico del negocio con semántica y layout visual. */
public record LogicalBusinessGraphSelectionTransferPayload(
        String sourceProjectTitle,
        List<LogicalBusinessGraphNode> nodes,
        List<LogicalBusinessGraphEdge> edges,
        List<NodeLayout> nodeLayouts,
        List<ConnectorLayout> connectorLayouts
) implements VisualSelectionTransferPayload {

    public LogicalBusinessGraphSelectionTransferPayload {
        sourceProjectTitle = sourceProjectTitle == null || sourceProjectTitle.isBlank()
                ? "Grafo lógico del negocio"
                : sourceProjectTitle.strip();
        nodes = List.copyOf(nodes == null ? List.of() : nodes);
        edges = List.copyOf(edges == null ? List.of() : edges);
        nodeLayouts = List.copyOf(nodeLayouts == null ? List.of() : nodeLayouts);
        connectorLayouts = List.copyOf(connectorLayouts == null ? List.of() : connectorLayouts);
    }

    @Override
    public DiagramTypeId diagramTypeId() {
        return DiagramTypeId.LOGICAL_BUSINESS_GRAPH;
    }

    @Override
    public int nodeCount() {
        return nodes.size();
    }

    @Override
    public int connectorCount() {
        return edges.size();
    }
}
