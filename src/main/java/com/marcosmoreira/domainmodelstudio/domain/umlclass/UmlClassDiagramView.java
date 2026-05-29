package com.marcosmoreira.domainmodelstudio.domain.umlclass;

import java.util.List;

/** Vista interna que filtra módulos, clases y relaciones dentro de un mismo diagrama UML Clases. */
public record UmlClassDiagramView(
        String id,
        UmlClassDiagramViewKind kind,
        String displayName,
        String description,
        List<String> sourceRootIds,
        List<String> moduleIds,
        List<String> classIds,
        List<String> relationIds,
        String notes
) {
    public UmlClassDiagramView {
        id = required(id, "id");
        kind = kind == null ? UmlClassDiagramViewKind.CUSTOM : kind;
        displayName = normalizeOrDefault(displayName, id);
        description = normalize(description);
        sourceRootIds = normalizedList(sourceRootIds);
        moduleIds = normalizedList(moduleIds);
        classIds = normalizedList(classIds);
        relationIds = normalizedList(relationIds);
        notes = normalize(notes);
    }

    public boolean includesModule(UmlModuleGroup module) {
        return isGlobal() || moduleIds.contains(module.id());
    }

    public boolean includesClass(UmlClassNode node) {
        return isGlobal() || classIds.contains(node.id()) || moduleIds.contains(node.moduleId());
    }

    public boolean includesRelation(UmlClassRelation relation) {
        return isGlobal() || relationIds.contains(relation.id());
    }

    public boolean isGlobal() {
        return moduleIds.isEmpty() && classIds.isEmpty() && relationIds.isEmpty();
    }

    private static List<String> normalizedList(List<String> values) {
        if (values == null) {
            return List.of();
        }
        return values.stream().map(UmlClassDiagramView::normalize).filter(value -> !value.isBlank()).distinct().toList();
    }

    private static String required(String value, String label) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El " + label + " de la vista UML no puede estar vacío.");
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
