package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.util.Map;

/** Relación normalizada entre tipos o nombres de tipo detectados en código fuente. */
public record ParsedCodeRelation(
        String id,
        String sourceTypeId,
        String targetTypeName,
        ParsedCodeRelationKind kind,
        String description,
        Map<String, String> metadata
) {
    public ParsedCodeRelation {
        id = required(id, "id");
        sourceTypeId = required(sourceTypeId, "sourceTypeId");
        targetTypeName = required(targetTypeName, "targetTypeName");
        kind = kind == null ? ParsedCodeRelationKind.UNKNOWN : kind;
        description = normalize(description);
        metadata = Map.copyOf(metadata == null ? Map.of() : metadata);
    }

    private static String required(String value, String label) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El " + label + " de la relación parseada no puede estar vacío.");
        }
        return normalized;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
