package com.marcosmoreira.domainmodelstudio.domain.behavior;

/** Relación semántica entre elementos de un diagrama de comportamiento. */
public record BehaviorEdge(
        String id,
        String sourceNodeId,
        String targetNodeId,
        BehaviorEdgeKind kind,
        String label,
        String condition,
        String notes
) {
    public BehaviorEdge {
        id = normalize(id);
        if (id.isBlank()) throw new IllegalArgumentException("El ID de la relación no puede estar vacío.");
        sourceNodeId = normalize(sourceNodeId);
        targetNodeId = normalize(targetNodeId);
        kind = kind == null ? BehaviorEdgeKind.FLOW : kind;
        label = normalize(label);
        condition = normalize(condition);
        notes = normalize(notes);
    }

    public BehaviorEdge withValues(String updatedSource, String updatedTarget, BehaviorEdgeKind updatedKind,
                                   String updatedLabel, String updatedCondition, String updatedNotes) {
        return new BehaviorEdge(id, updatedSource, updatedTarget, updatedKind, updatedLabel, updatedCondition, updatedNotes);
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
