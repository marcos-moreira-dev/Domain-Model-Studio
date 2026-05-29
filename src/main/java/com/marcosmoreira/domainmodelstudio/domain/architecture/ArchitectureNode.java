package com.marcosmoreira.domainmodelstudio.domain.architecture;

/** Elemento semántico de C4 o despliegue técnico. */
public record ArchitectureNode(
        String id,
        ArchitectureNodeKind kind,
        String displayName,
        String technology,
        String owner,
        String environment,
        String description,
        String notes,
        int orderIndex
) {
    public ArchitectureNode {
        id = normalize(id);
        if (id.isBlank()) throw new IllegalArgumentException("El ID del elemento no puede estar vacío.");
        kind = kind == null ? ArchitectureNodeKind.SOFTWARE_SYSTEM : kind;
        displayName = normalize(displayName).isBlank() ? kind.displayName() : normalize(displayName);
        technology = normalize(technology);
        owner = normalize(owner);
        environment = normalize(environment);
        description = normalize(description);
        notes = normalize(notes);
        orderIndex = Math.max(0, orderIndex);
    }

    public ArchitectureNode withValues(ArchitectureNodeKind updatedKind, String updatedName, String updatedTechnology,
                                       String updatedOwner, String updatedEnvironment, String updatedDescription,
                                       String updatedNotes, int updatedOrderIndex) {
        return new ArchitectureNode(id, updatedKind, updatedName, updatedTechnology, updatedOwner,
                updatedEnvironment, updatedDescription, updatedNotes, updatedOrderIndex);
    }

    private static String normalize(String value) { return value == null ? "" : value.strip(); }
}
