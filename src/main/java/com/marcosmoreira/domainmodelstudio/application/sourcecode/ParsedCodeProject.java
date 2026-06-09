package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.util.List;
import java.util.Optional;

/** Modelo intermedio neutral generado desde uno o varios lenguajes de código fuente. */
public record ParsedCodeProject(
        String projectName,
        List<ParsedCodeSourceRoot> sourceRoots,
        List<ParsedCodeModule> modules,
        List<ParsedCodeType> types,
        List<ParsedCodeRelation> relations,
        List<String> warnings
) {
    public ParsedCodeProject {
        projectName = normalizeOrDefault(projectName, "Proyecto de código fuente");
        sourceRoots = List.copyOf(sourceRoots == null ? List.of() : sourceRoots);
        modules = List.copyOf(modules == null ? List.of() : modules);
        types = List.copyOf(types == null ? List.of() : types);
        relations = List.copyOf(relations == null ? List.of() : relations);
        warnings = List.copyOf(warnings == null ? List.of() : warnings);
    }

    public List<ParsedCodeType> typesForRoot(String sourceRootId) {
        String normalized = sourceRootId == null ? "" : sourceRootId.strip();
        return types.stream().filter(type -> type.sourceRootId().equals(normalized)).toList();
    }

    public Optional<ParsedCodeType> typeById(String typeId) {
        String normalized = typeId == null ? "" : typeId.strip();
        return types.stream().filter(type -> type.id().equals(normalized)).findFirst();
    }

    public List<ParsedCodeModule> modulesForRoot(String sourceRootId) {
        String normalized = sourceRootId == null ? "" : sourceRootId.strip();
        return modules.stream().filter(module -> module.sourceRootId().equals(normalized)).toList();
    }

    private static String normalizeOrDefault(String value, String fallback) {
        String normalized = value == null ? "" : value.strip();
        return normalized.isBlank() ? fallback : normalized;
    }
}
