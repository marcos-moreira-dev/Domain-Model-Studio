package com.marcosmoreira.domainmodelstudio.domain.architecture;

/** Relación semántica entre elementos de arquitectura. */
public record ArchitectureEdge(
        String id,
        String sourceNodeId,
        String targetNodeId,
        ArchitectureEdgeKind kind,
        String label,
        String protocol,
        String notes
) {
    public ArchitectureEdge {
        id = normalize(id);
        if (id.isBlank()) throw new IllegalArgumentException("El ID de la relación no puede estar vacío.");
        sourceNodeId = normalize(sourceNodeId);
        targetNodeId = normalize(targetNodeId);
        kind = kind == null ? ArchitectureEdgeKind.DEPENDS_ON : kind;
        label = normalize(label);
        protocol = normalize(protocol);
        notes = normalize(notes);
    }

    public ArchitectureEdge withValues(String updatedSource, String updatedTarget, ArchitectureEdgeKind updatedKind,
                                       String updatedLabel, String updatedProtocol, String updatedNotes) {
        return new ArchitectureEdge(id, updatedSource, updatedTarget, updatedKind, updatedLabel, updatedProtocol, updatedNotes);
    }

    private static String normalize(String value) { return value == null ? "" : value.strip(); }
}
