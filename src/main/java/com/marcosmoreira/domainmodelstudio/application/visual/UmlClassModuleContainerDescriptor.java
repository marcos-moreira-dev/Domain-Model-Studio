package com.marcosmoreira.domainmodelstudio.application.visual;

/** Descriptor de lectura visual para un contenedor UML generado por package, carpeta o módulo. */
public record UmlClassModuleContainerDescriptor(
        String moduleId,
        String title,
        String sourceRootLabel,
        String pathLabel,
        String classCountLabel,
        String roleStyleClass
) {
    public UmlClassModuleContainerDescriptor {
        moduleId = normalize(moduleId);
        title = normalizeOrDefault(title, moduleId.isBlank() ? "Módulo" : moduleId);
        sourceRootLabel = normalize(sourceRootLabel);
        pathLabel = normalize(pathLabel);
        classCountLabel = normalizeOrDefault(classCountLabel, "0 clases");
        roleStyleClass = normalizeOrDefault(roleStyleClass, "uml-class-canvas-module-source");
    }

    public String subtitle() {
        if (!sourceRootLabel.isBlank() && !pathLabel.isBlank()) {
            return sourceRootLabel + " · " + pathLabel;
        }
        if (!sourceRootLabel.isBlank()) {
            return sourceRootLabel;
        }
        return pathLabel;
    }

    private static String normalizeOrDefault(String value, String fallback) {
        String normalized = normalize(value);
        return normalized.isBlank() ? normalize(fallback) : normalized;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
