package com.marcosmoreira.domainmodelstudio.domain.behavior;

import java.util.Objects;

/** Elemento semántico de un diagrama de comportamiento. */
public record BehaviorNode(
        String id,
        BehaviorNodeKind kind,
        String displayName,
        String owner,
        String description,
        String notes,
        int orderIndex
) {
    public BehaviorNode {
        id = normalize(id);
        if (id.isBlank()) throw new IllegalArgumentException("El ID del elemento no puede estar vacío.");
        kind = kind == null ? BehaviorNodeKind.ACTIVITY : kind;
        displayName = normalize(displayName).isBlank() ? kind.displayName() : normalize(displayName);
        owner = normalize(owner);
        description = normalize(description);
        notes = normalize(notes);
        orderIndex = Math.max(0, orderIndex);
    }

    public BehaviorNode withValues(BehaviorNodeKind updatedKind, String updatedName, String updatedOwner,
                                   String updatedDescription, String updatedNotes, int updatedOrderIndex) {
        return new BehaviorNode(id, updatedKind, updatedName, updatedOwner, updatedDescription, updatedNotes, updatedOrderIndex);
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
