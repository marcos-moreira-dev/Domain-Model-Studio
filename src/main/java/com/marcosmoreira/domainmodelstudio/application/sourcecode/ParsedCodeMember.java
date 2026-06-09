package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.util.List;
import java.util.Map;

/** Miembro normalizado de una clase o interfaz de código fuente. */
public record ParsedCodeMember(
        String id,
        ParsedCodeMemberKind kind,
        String name,
        String type,
        String signature,
        ParsedCodeVisibility visibility,
        List<String> annotations,
        Map<String, String> metadata
) {
    public ParsedCodeMember {
        id = required(id, "id");
        kind = kind == null ? ParsedCodeMemberKind.UNKNOWN : kind;
        name = normalizeOrDefault(name, id);
        type = normalize(type);
        signature = normalize(signature);
        visibility = visibility == null ? ParsedCodeVisibility.UNKNOWN : visibility;
        annotations = List.copyOf(annotations == null ? List.of() : annotations);
        metadata = Map.copyOf(metadata == null ? Map.of() : metadata);
    }

    private static String required(String value, String label) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El " + label + " del miembro parseado no puede estar vacío.");
        }
        return normalized;
    }

    private static String normalizeOrDefault(String value, String fallback) {
        String normalized = normalize(value);
        return normalized.isBlank() ? fallback : normalized;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
