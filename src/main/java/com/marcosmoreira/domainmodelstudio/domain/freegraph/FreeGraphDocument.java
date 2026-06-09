package com.marcosmoreira.domainmodelstudio.domain.freegraph;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Documento semántico de un grafo libre, sin dependencias de JavaFX ni persistencia. */
public final class FreeGraphDocument {

    private final String projectName;
    private final String version;
    private final LocalDate documentDate;
    private final FreeGraphKind graphKind;
    private final List<FreeGraphNode> nodes;
    private final List<FreeGraphEdge> edges;
    private final String notes;
    private final Map<String, FreeGraphNode> nodesById;
    private final Map<String, FreeGraphEdge> edgesById;

    public FreeGraphDocument(
            String projectName,
            String version,
            LocalDate documentDate,
            FreeGraphKind graphKind,
            List<FreeGraphNode> nodes,
            List<FreeGraphEdge> edges,
            String notes
    ) {
        this.projectName = normalize(projectName).isBlank() ? "Grafo libre" : normalize(projectName);
        this.version = normalize(version).isBlank() ? "borrador" : normalize(version);
        this.documentDate = documentDate == null ? LocalDate.now() : documentDate;
        this.graphKind = graphKind == null ? FreeGraphKind.MIXED : graphKind;
        this.nodes = List.copyOf(nodes == null ? List.of() : nodes);
        this.edges = List.copyOf(normalizeEdges(edges == null ? List.of() : edges, this.graphKind));
        this.notes = normalize(notes);
        this.nodesById = indexNodes(this.nodes);
        this.edgesById = indexEdges(this.edges);
        validateReferences();
    }

    public static FreeGraphDocument blank(String projectName) {
        return new FreeGraphDocument(projectName, "borrador", LocalDate.now(), FreeGraphKind.MIXED,
                List.of(), List.of(), "");
    }

    public static FreeGraphDocument directed(String projectName, List<FreeGraphNode> nodes, List<FreeGraphEdge> edges) {
        return new FreeGraphDocument(projectName, "borrador", LocalDate.now(), FreeGraphKind.DIRECTED,
                nodes, edges, "");
    }

    public static FreeGraphDocument undirected(String projectName, List<FreeGraphNode> nodes, List<FreeGraphEdge> edges) {
        return new FreeGraphDocument(projectName, "borrador", LocalDate.now(), FreeGraphKind.UNDIRECTED,
                nodes, edges, "");
    }

    public static FreeGraphDocument mixed(String projectName, List<FreeGraphNode> nodes, List<FreeGraphEdge> edges) {
        return new FreeGraphDocument(projectName, "borrador", LocalDate.now(), FreeGraphKind.MIXED,
                nodes, edges, "");
    }

    public String projectName() {
        return projectName;
    }

    public String version() {
        return version;
    }

    public LocalDate documentDate() {
        return documentDate;
    }

    public FreeGraphKind graphKind() {
        return graphKind;
    }

    public List<FreeGraphNode> nodes() {
        return nodes;
    }

    public List<FreeGraphEdge> edges() {
        return edges;
    }

    public String notes() {
        return notes;
    }

    public boolean isEmpty() {
        return nodes.isEmpty() && edges.isEmpty();
    }

    public boolean empty() {
        return isEmpty();
    }

    public int nodeCount() {
        return nodes.size();
    }

    public int edgeCount() {
        return edges.size();
    }

    public Optional<FreeGraphNode> nodeById(String id) {
        return Optional.ofNullable(nodesById.get(normalize(id)));
    }

    public Optional<FreeGraphEdge> edgeById(String id) {
        return Optional.ofNullable(edgesById.get(normalize(id)));
    }

    public List<FreeGraphEdge> outgoingEdgesOf(String nodeId) {
        String normalizedNodeId = normalize(nodeId);
        return edges.stream()
                .filter(edge -> edge.sourceNodeId().equals(normalizedNodeId))
                .toList();
    }

    public List<FreeGraphEdge> incomingEdgesOf(String nodeId) {
        String normalizedNodeId = normalize(nodeId);
        return edges.stream()
                .filter(edge -> edge.targetNodeId().equals(normalizedNodeId))
                .toList();
    }

    public List<FreeGraphEdge> incidentEdgesOf(String nodeId) {
        String normalizedNodeId = normalize(nodeId);
        return edges.stream()
                .filter(edge -> edge.sourceNodeId().equals(normalizedNodeId)
                        || edge.targetNodeId().equals(normalizedNodeId))
                .toList();
    }

    public FreeGraphDocument withDocumentDetails(
            String updatedProjectName,
            String updatedVersion,
            LocalDate updatedDocumentDate,
            FreeGraphKind updatedGraphKind,
            String updatedNotes
    ) {
        return new FreeGraphDocument(updatedProjectName, updatedVersion, updatedDocumentDate,
                updatedGraphKind, nodes, edges, updatedNotes);
    }

    public FreeGraphDocument withGraphKind(FreeGraphKind updatedGraphKind) {
        return new FreeGraphDocument(projectName, version, documentDate, updatedGraphKind, nodes, edges, notes);
    }

    public FreeGraphDocument withNode(FreeGraphNode node) {
        Objects.requireNonNull(node, "node");
        if (nodesById.containsKey(node.id())) {
            throw new IllegalArgumentException("Ya existe un nodo con ID: " + node.id());
        }
        List<FreeGraphNode> updated = new ArrayList<>(nodes);
        updated.add(node);
        return new FreeGraphDocument(projectName, version, documentDate, graphKind, updated, edges, notes);
    }

    public FreeGraphDocument withUpdatedNode(FreeGraphNode updatedNode) {
        Objects.requireNonNull(updatedNode, "updatedNode");
        List<FreeGraphNode> updated = new ArrayList<>();
        boolean replaced = false;
        for (FreeGraphNode node : nodes) {
            if (node.id().equals(updatedNode.id())) {
                updated.add(updatedNode);
                replaced = true;
            } else {
                updated.add(node);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe nodo para actualizar: " + updatedNode.id());
        }
        return new FreeGraphDocument(projectName, version, documentDate, graphKind, updated, edges, notes);
    }

    public FreeGraphDocument withoutNode(String nodeId) {
        String normalizedId = normalize(nodeId);
        if (!nodesById.containsKey(normalizedId)) {
            throw new IllegalArgumentException("No existe nodo para eliminar: " + normalizedId);
        }
        List<FreeGraphNode> updatedNodes = nodes.stream()
                .filter(node -> !node.id().equals(normalizedId))
                .toList();
        List<FreeGraphEdge> updatedEdges = edges.stream()
                .filter(edge -> !edge.sourceNodeId().equals(normalizedId))
                .filter(edge -> !edge.targetNodeId().equals(normalizedId))
                .toList();
        return new FreeGraphDocument(projectName, version, documentDate, graphKind, updatedNodes, updatedEdges, notes);
    }

    public FreeGraphDocument withEdge(FreeGraphEdge edge) {
        Objects.requireNonNull(edge, "edge");
        if (edgesById.containsKey(edge.id())) {
            throw new IllegalArgumentException("Ya existe una relación con ID: " + edge.id());
        }
        List<FreeGraphEdge> updated = new ArrayList<>(edges);
        updated.add(edge);
        return new FreeGraphDocument(projectName, version, documentDate, graphKind, nodes, updated, notes);
    }

    public FreeGraphDocument withUpdatedEdge(FreeGraphEdge updatedEdge) {
        Objects.requireNonNull(updatedEdge, "updatedEdge");
        List<FreeGraphEdge> updated = new ArrayList<>();
        boolean replaced = false;
        for (FreeGraphEdge edge : edges) {
            if (edge.id().equals(updatedEdge.id())) {
                updated.add(updatedEdge);
                replaced = true;
            } else {
                updated.add(edge);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe relación para actualizar: " + updatedEdge.id());
        }
        return new FreeGraphDocument(projectName, version, documentDate, graphKind, nodes, updated, notes);
    }

    public FreeGraphDocument withoutEdge(String edgeId) {
        String normalizedId = normalize(edgeId);
        List<FreeGraphEdge> updated = edges.stream()
                .filter(edge -> !edge.id().equals(normalizedId))
                .toList();
        if (updated.size() == edges.size()) {
            throw new IllegalArgumentException("No existe relación para eliminar: " + normalizedId);
        }
        return new FreeGraphDocument(projectName, version, documentDate, graphKind, nodes, updated, notes);
    }

    public FreeGraphDocument withNotes(String updatedNotes) {
        return new FreeGraphDocument(projectName, version, documentDate, graphKind, nodes, edges, updatedNotes);
    }

    private void validateReferences() {
        for (FreeGraphEdge edge : edges) {
            if (!nodesById.containsKey(edge.sourceNodeId())) {
                throw new IllegalArgumentException("Relación con origen inexistente: " + edge.sourceNodeId());
            }
            if (!nodesById.containsKey(edge.targetNodeId())) {
                throw new IllegalArgumentException("Relación con destino inexistente: " + edge.targetNodeId());
            }
        }
    }

    private static List<FreeGraphEdge> normalizeEdges(List<FreeGraphEdge> rawEdges, FreeGraphKind graphKind) {
        return rawEdges.stream()
                .map(edge -> normalizeEdgeDirection(edge, graphKind))
                .toList();
    }

    private static FreeGraphEdge normalizeEdgeDirection(FreeGraphEdge edge, FreeGraphKind graphKind) {
        Objects.requireNonNull(edge, "edge");
        if (graphKind == FreeGraphKind.DIRECTED && edge.direction() != FreeGraphEdgeDirection.DIRECTED) {
            return edge.withDirection(FreeGraphEdgeDirection.DIRECTED);
        }
        if (graphKind == FreeGraphKind.UNDIRECTED && edge.direction() != FreeGraphEdgeDirection.UNDIRECTED) {
            return edge.withDirection(FreeGraphEdgeDirection.UNDIRECTED);
        }
        return edge;
    }

    private static Map<String, FreeGraphNode> indexNodes(List<FreeGraphNode> nodes) {
        Map<String, FreeGraphNode> indexed = new LinkedHashMap<>();
        for (FreeGraphNode node : nodes) {
            FreeGraphNode previous = indexed.put(node.id(), node);
            if (previous != null) {
                throw new IllegalArgumentException("Nodo duplicado: " + node.id());
            }
        }
        return Map.copyOf(indexed);
    }

    private static Map<String, FreeGraphEdge> indexEdges(List<FreeGraphEdge> edges) {
        Map<String, FreeGraphEdge> indexed = new LinkedHashMap<>();
        for (FreeGraphEdge edge : edges) {
            FreeGraphEdge previous = indexed.put(edge.id(), edge);
            if (previous != null) {
                throw new IllegalArgumentException("Relación duplicada: " + edge.id());
            }
        }
        return Map.copyOf(indexed);
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
