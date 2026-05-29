package com.marcosmoreira.domainmodelstudio.presentation.visualtransfer;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphNode;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.List;

/** Selección copiada de un Grafo libre con semántica y layout visual. */
public record FreeGraphSelectionTransferPayload(
        String sourceProjectTitle,
        List<FreeGraphNode> nodes,
        List<FreeGraphEdge> edges,
        List<NodeLayout> nodeLayouts,
        List<ConnectorLayout> connectorLayouts
) implements VisualSelectionTransferPayload {

    public FreeGraphSelectionTransferPayload {
        sourceProjectTitle = sourceProjectTitle == null || sourceProjectTitle.isBlank()
                ? "Grafo libre"
                : sourceProjectTitle.strip();
        nodes = List.copyOf(nodes == null ? List.of() : nodes);
        edges = List.copyOf(edges == null ? List.of() : edges);
        nodeLayouts = List.copyOf(nodeLayouts == null ? List.of() : nodeLayouts);
        connectorLayouts = List.copyOf(connectorLayouts == null ? List.of() : connectorLayouts);
    }

    @Override
    public DiagramTypeId diagramTypeId() {
        return DiagramTypeId.FREE_GRAPH;
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
