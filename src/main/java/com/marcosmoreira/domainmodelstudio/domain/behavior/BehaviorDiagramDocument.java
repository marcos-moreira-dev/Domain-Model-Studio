package com.marcosmoreira.domainmodelstudio.domain.behavior;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Documento semántico común para BPMN básico y UML de comportamiento. */
public final class BehaviorDiagramDocument {

    private final String projectName;
    private final String version;
    private final LocalDate documentDate;
    private final BehaviorDiagramKind diagramKind;
    private final List<BehaviorNode> nodes;
    private final List<BehaviorEdge> edges;
    private final String notes;
    private final Map<String, BehaviorNode> nodesById;

    public BehaviorDiagramDocument(
            String projectName,
            String version,
            LocalDate documentDate,
            BehaviorDiagramKind diagramKind,
            List<BehaviorNode> nodes,
            List<BehaviorEdge> edges,
            String notes
    ) {
        this.projectName = normalize(projectName).isBlank() ? "Diagrama de comportamiento" : normalize(projectName);
        this.version = normalize(version).isBlank() ? "borrador" : normalize(version);
        this.documentDate = documentDate == null ? LocalDate.now() : documentDate;
        this.diagramKind = diagramKind == null ? BehaviorDiagramKind.BPMN_BASIC : diagramKind;
        this.nodes = List.copyOf(nodes == null ? List.of() : nodes);
        this.edges = List.copyOf(edges == null ? List.of() : edges);
        this.notes = normalize(notes);
        this.nodesById = indexNodes(this.nodes);
        validateReferences();
    }

    public static BehaviorDiagramDocument blank(String projectName, BehaviorDiagramKind diagramKind) {
        return new BehaviorDiagramDocument(projectName, "borrador", LocalDate.now(), diagramKind, List.of(), List.of(), "");
    }

    public String projectName() { return projectName; }
    public String version() { return version; }
    public LocalDate documentDate() { return documentDate; }
    public BehaviorDiagramKind diagramKind() { return diagramKind; }
    public List<BehaviorNode> nodes() { return nodes; }
    public List<BehaviorEdge> edges() { return edges; }
    public String notes() { return notes; }
    public Optional<BehaviorNode> nodeById(String id) { return Optional.ofNullable(nodesById.get(normalize(id))); }

    public BehaviorDiagramDocument withNode(BehaviorNode node) {
        Objects.requireNonNull(node, "node");
        if (nodesById.containsKey(node.id())) throw new IllegalArgumentException("Ya existe un elemento con ID: " + node.id());
        ArrayList<BehaviorNode> updated = new ArrayList<>(nodes);
        updated.add(node);
        return new BehaviorDiagramDocument(projectName, version, documentDate, diagramKind, updated, edges, notes);
    }

    public BehaviorDiagramDocument withUpdatedNode(BehaviorNode updatedNode) {
        Objects.requireNonNull(updatedNode, "updatedNode");
        ArrayList<BehaviorNode> updated = new ArrayList<>();
        boolean replaced = false;
        for (BehaviorNode node : nodes) {
            if (node.id().equals(updatedNode.id())) {
                updated.add(updatedNode);
                replaced = true;
            } else {
                updated.add(node);
            }
        }
        if (!replaced) throw new IllegalArgumentException("No existe elemento para actualizar: " + updatedNode.id());
        return new BehaviorDiagramDocument(projectName, version, documentDate, diagramKind, updated, edges, notes);
    }

    public BehaviorDiagramDocument withoutNode(String nodeId) {
        String normalized = normalize(nodeId);
        if (!nodesById.containsKey(normalized)) throw new IllegalArgumentException("No existe elemento para eliminar: " + normalized);
        List<BehaviorNode> updatedNodes = nodes.stream().filter(node -> !node.id().equals(normalized)).toList();
        List<BehaviorEdge> updatedEdges = edges.stream()
                .filter(edge -> !edge.sourceNodeId().equals(normalized))
                .filter(edge -> !edge.targetNodeId().equals(normalized))
                .toList();
        return new BehaviorDiagramDocument(projectName, version, documentDate, diagramKind, updatedNodes, updatedEdges, notes);
    }

    public BehaviorDiagramDocument withEdge(BehaviorEdge edge) {
        Objects.requireNonNull(edge, "edge");
        if (edges.stream().anyMatch(existing -> existing.id().equals(edge.id()))) {
            throw new IllegalArgumentException("Ya existe una relación con ID: " + edge.id());
        }
        ArrayList<BehaviorEdge> updated = new ArrayList<>(edges);
        updated.add(edge);
        return new BehaviorDiagramDocument(projectName, version, documentDate, diagramKind, nodes, updated, notes);
    }

    public BehaviorDiagramDocument withUpdatedEdge(BehaviorEdge updatedEdge) {
        Objects.requireNonNull(updatedEdge, "updatedEdge");
        ArrayList<BehaviorEdge> updated = new ArrayList<>();
        boolean replaced = false;
        for (BehaviorEdge edge : edges) {
            if (edge.id().equals(updatedEdge.id())) {
                updated.add(updatedEdge);
                replaced = true;
            } else {
                updated.add(edge);
            }
        }
        if (!replaced) throw new IllegalArgumentException("No existe relación para actualizar: " + updatedEdge.id());
        return new BehaviorDiagramDocument(projectName, version, documentDate, diagramKind, nodes, updated, notes);
    }

    /** Reemplaza la lista de relaciones conservando nodos y metadatos; útil para reordenar secuencias. */
    public BehaviorDiagramDocument withEdges(List<BehaviorEdge> updatedEdges) {
        return new BehaviorDiagramDocument(projectName, version, documentDate, diagramKind, nodes, updatedEdges, notes);
    }

    public BehaviorDiagramDocument withoutEdge(String edgeId) {
        String normalized = normalize(edgeId);
        List<BehaviorEdge> updated = edges.stream().filter(edge -> !edge.id().equals(normalized)).toList();
        if (updated.size() == edges.size()) throw new IllegalArgumentException("No existe relación para eliminar: " + normalized);
        return new BehaviorDiagramDocument(projectName, version, documentDate, diagramKind, nodes, updated, notes);
    }

    private void validateReferences() {
        for (BehaviorEdge edge : edges) {
            if (!nodesById.containsKey(edge.sourceNodeId())) {
                throw new IllegalArgumentException("Relación con origen inexistente: " + edge.sourceNodeId());
            }
            if (!nodesById.containsKey(edge.targetNodeId())) {
                throw new IllegalArgumentException("Relación con destino inexistente: " + edge.targetNodeId());
            }
        }
    }

    private static Map<String, BehaviorNode> indexNodes(List<BehaviorNode> nodes) {
        LinkedHashMap<String, BehaviorNode> indexed = new LinkedHashMap<>();
        for (BehaviorNode node : nodes) {
            BehaviorNode previous = indexed.put(node.id(), node);
            if (previous != null) throw new IllegalArgumentException("Elemento duplicado: " + node.id());
        }
        return Map.copyOf(indexed);
    }

    private static String normalize(String value) { return value == null ? "" : value.strip(); }
}
