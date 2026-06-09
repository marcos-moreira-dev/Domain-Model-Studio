package com.marcosmoreira.domainmodelstudio.domain.architecture;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Documento semántico común para C4 Contexto, C4 Contenedores y Despliegue técnico. */
public final class ArchitectureDiagramDocument {

    private final String projectName;
    private final String version;
    private final LocalDate documentDate;
    private final ArchitectureDiagramKind diagramKind;
    private final List<ArchitectureNode> nodes;
    private final List<ArchitectureEdge> edges;
    private final String notes;
    private final Map<String, ArchitectureNode> nodesById;

    public ArchitectureDiagramDocument(
            String projectName,
            String version,
            LocalDate documentDate,
            ArchitectureDiagramKind diagramKind,
            List<ArchitectureNode> nodes,
            List<ArchitectureEdge> edges,
            String notes
    ) {
        this.projectName = normalize(projectName).isBlank() ? "Diagrama de arquitectura" : normalize(projectName);
        this.version = normalize(version).isBlank() ? "borrador" : normalize(version);
        this.documentDate = documentDate == null ? LocalDate.now() : documentDate;
        this.diagramKind = diagramKind == null ? ArchitectureDiagramKind.C4_CONTEXT : diagramKind;
        this.nodes = List.copyOf(nodes == null ? List.of() : nodes);
        this.edges = List.copyOf(edges == null ? List.of() : edges);
        this.notes = normalize(notes);
        this.nodesById = indexNodes(this.nodes);
        validateReferences();
    }

    public static ArchitectureDiagramDocument blank(String projectName, ArchitectureDiagramKind diagramKind) {
        return new ArchitectureDiagramDocument(projectName, "borrador", LocalDate.now(), diagramKind, List.of(), List.of(), "");
    }

    public String projectName() { return projectName; }
    public String version() { return version; }
    public LocalDate documentDate() { return documentDate; }
    public ArchitectureDiagramKind diagramKind() { return diagramKind; }
    public List<ArchitectureNode> nodes() { return nodes; }
    public List<ArchitectureEdge> edges() { return edges; }
    public String notes() { return notes; }
    public Optional<ArchitectureNode> nodeById(String id) { return Optional.ofNullable(nodesById.get(normalize(id))); }

    public ArchitectureDiagramDocument withNode(ArchitectureNode node) {
        Objects.requireNonNull(node, "node");
        if (nodesById.containsKey(node.id())) throw new IllegalArgumentException("Ya existe un elemento con ID: " + node.id());
        ArrayList<ArchitectureNode> updated = new ArrayList<>(nodes);
        updated.add(node);
        return new ArchitectureDiagramDocument(projectName, version, documentDate, diagramKind, updated, edges, notes);
    }

    public ArchitectureDiagramDocument withUpdatedNode(ArchitectureNode updatedNode) {
        Objects.requireNonNull(updatedNode, "updatedNode");
        ArrayList<ArchitectureNode> updated = new ArrayList<>();
        boolean replaced = false;
        for (ArchitectureNode node : nodes) {
            if (node.id().equals(updatedNode.id())) {
                updated.add(updatedNode);
                replaced = true;
            } else {
                updated.add(node);
            }
        }
        if (!replaced) throw new IllegalArgumentException("No existe elemento para actualizar: " + updatedNode.id());
        return new ArchitectureDiagramDocument(projectName, version, documentDate, diagramKind, updated, edges, notes);
    }

    public ArchitectureDiagramDocument withoutNode(String nodeId) {
        String normalized = normalize(nodeId);
        if (!nodesById.containsKey(normalized)) throw new IllegalArgumentException("No existe elemento para eliminar: " + normalized);
        List<ArchitectureNode> updatedNodes = nodes.stream().filter(node -> !node.id().equals(normalized)).toList();
        List<ArchitectureEdge> updatedEdges = edges.stream()
                .filter(edge -> !edge.sourceNodeId().equals(normalized))
                .filter(edge -> !edge.targetNodeId().equals(normalized))
                .toList();
        return new ArchitectureDiagramDocument(projectName, version, documentDate, diagramKind, updatedNodes, updatedEdges, notes);
    }

    public ArchitectureDiagramDocument withEdge(ArchitectureEdge edge) {
        Objects.requireNonNull(edge, "edge");
        if (edges.stream().anyMatch(existing -> existing.id().equals(edge.id()))) {
            throw new IllegalArgumentException("Ya existe una relación con ID: " + edge.id());
        }
        ArrayList<ArchitectureEdge> updated = new ArrayList<>(edges);
        updated.add(edge);
        return new ArchitectureDiagramDocument(projectName, version, documentDate, diagramKind, nodes, updated, notes);
    }

    public ArchitectureDiagramDocument withUpdatedEdge(ArchitectureEdge updatedEdge) {
        Objects.requireNonNull(updatedEdge, "updatedEdge");
        ArrayList<ArchitectureEdge> updated = new ArrayList<>();
        boolean replaced = false;
        for (ArchitectureEdge edge : edges) {
            if (edge.id().equals(updatedEdge.id())) {
                updated.add(updatedEdge);
                replaced = true;
            } else {
                updated.add(edge);
            }
        }
        if (!replaced) throw new IllegalArgumentException("No existe relación para actualizar: " + updatedEdge.id());
        return new ArchitectureDiagramDocument(projectName, version, documentDate, diagramKind, nodes, updated, notes);
    }

    public ArchitectureDiagramDocument withoutEdge(String edgeId) {
        String normalized = normalize(edgeId);
        List<ArchitectureEdge> updated = edges.stream().filter(edge -> !edge.id().equals(normalized)).toList();
        if (updated.size() == edges.size()) throw new IllegalArgumentException("No existe relación para eliminar: " + normalized);
        return new ArchitectureDiagramDocument(projectName, version, documentDate, diagramKind, nodes, updated, notes);
    }

    private void validateReferences() {
        for (ArchitectureEdge edge : edges) {
            if (!nodesById.containsKey(edge.sourceNodeId())) {
                throw new IllegalArgumentException("Relación con origen inexistente: " + edge.sourceNodeId());
            }
            if (!nodesById.containsKey(edge.targetNodeId())) {
                throw new IllegalArgumentException("Relación con destino inexistente: " + edge.targetNodeId());
            }
        }
    }

    private static Map<String, ArchitectureNode> indexNodes(List<ArchitectureNode> nodes) {
        LinkedHashMap<String, ArchitectureNode> indexed = new LinkedHashMap<>();
        for (ArchitectureNode node : nodes) {
            ArchitectureNode previous = indexed.put(node.id(), node);
            if (previous != null) throw new IllegalArgumentException("Elemento duplicado: " + node.id());
        }
        return Map.copyOf(indexed);
    }

    private static String normalize(String value) { return value == null ? "" : value.strip(); }
}
