package com.marcosmoreira.domainmodelstudio.domain.freegraph;

import java.util.Objects;

/** Relación semántica entre dos nodos de un grafo libre. */
public final class FreeGraphEdge {

    private final String id;
    private final String sourceNodeId;
    private final String targetNodeId;
    private final FreeGraphEdgeDirection direction;
    private final String label;
    private final String notes;

    public FreeGraphEdge(
            String id,
            String sourceNodeId,
            String targetNodeId,
            FreeGraphEdgeDirection direction,
            String label,
            String notes
    ) {
        this.id = required(id, "id");
        this.sourceNodeId = required(sourceNodeId, "sourceNodeId");
        this.targetNodeId = required(targetNodeId, "targetNodeId");
        this.direction = direction == null ? FreeGraphEdgeDirection.DIRECTED : direction;
        this.label = normalize(label);
        this.notes = normalize(notes);
    }

    public static FreeGraphEdge directed(String id, String sourceNodeId, String targetNodeId, String label) {
        return new FreeGraphEdge(id, sourceNodeId, targetNodeId, FreeGraphEdgeDirection.DIRECTED, label, "");
    }

    public static FreeGraphEdge undirected(String id, String sourceNodeId, String targetNodeId, String label) {
        return new FreeGraphEdge(id, sourceNodeId, targetNodeId, FreeGraphEdgeDirection.UNDIRECTED, label, "");
    }

    public String id() {
        return id;
    }

    public String sourceNodeId() {
        return sourceNodeId;
    }

    public String targetNodeId() {
        return targetNodeId;
    }

    public FreeGraphEdgeDirection direction() {
        return direction;
    }

    public String label() {
        return label;
    }

    public String notes() {
        return notes;
    }

    public boolean loop() {
        return sourceNodeId.equals(targetNodeId);
    }

    public FreeGraphEdge withDirection(FreeGraphEdgeDirection updatedDirection) {
        return new FreeGraphEdge(id, sourceNodeId, targetNodeId, updatedDirection, label, notes);
    }

    public FreeGraphEdge withDetails(
            String updatedSourceNodeId,
            String updatedTargetNodeId,
            FreeGraphEdgeDirection updatedDirection,
            String updatedLabel,
            String updatedNotes
    ) {
        return new FreeGraphEdge(id, updatedSourceNodeId, updatedTargetNodeId, updatedDirection,
                updatedLabel, updatedNotes);
    }

    private static String required(String value, String fieldName) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El campo " + fieldName + " no puede estar vacío.");
        }
        return normalized;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof FreeGraphEdge edge && edge.id.equals(id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
