package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/** Tipo normalizado de código fuente listo para convertirse a UML Clases. */
public record ParsedCodeType(
        String id,
        String sourceRootId,
        String moduleId,
        String qualifiedName,
        String simpleName,
        ParsedCodeTypeKind kind,
        Path sourcePath,
        String packageName,
        List<ParsedCodeMember> members,
        List<String> annotations,
        Map<String, String> metadata
) {
    public ParsedCodeType {
        id = required(id, "id");
        sourceRootId = required(sourceRootId, "sourceRootId");
        moduleId = normalize(moduleId);
        qualifiedName = normalizeOrDefault(qualifiedName, id);
        simpleName = normalizeOrDefault(simpleName, qualifiedName);
        kind = kind == null ? ParsedCodeTypeKind.UNKNOWN : kind;
        packageName = normalize(packageName);
        members = List.copyOf(members == null ? List.of() : members);
        annotations = List.copyOf(annotations == null ? List.of() : annotations);
        metadata = Map.copyOf(metadata == null ? Map.of() : metadata);
    }

    public String displayName(int maxLength) {
        if (maxLength < 4 || simpleName.length() <= maxLength) {
            return simpleName;
        }
        return simpleName.substring(0, Math.max(1, maxLength - 2)) + "...";
    }

    private static String required(String value, String label) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El " + label + " del tipo parseado no puede estar vacío.");
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
