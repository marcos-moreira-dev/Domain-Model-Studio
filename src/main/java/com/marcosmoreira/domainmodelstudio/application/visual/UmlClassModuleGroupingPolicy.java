package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Ordena módulos UML importados desde código por raíz, carpeta/package y dependencias locales.
 *
 * <p>La prioridad es que un sistema full stack sea legible: backend, frontend, shared/librerías
 * y otros grupos no deben mezclarse en una nube plana aunque existan relaciones cruzadas.</p>
 */
final class UmlClassModuleGroupingPolicy {

    private final DependencyDrivenOrderingPolicy dependencyOrdering = new DependencyDrivenOrderingPolicy();

    List<UmlModuleGroup> orderModules(UmlClassDiagramDocument document) {
        Objects.requireNonNull(document, "document");
        Map<String, UmlModuleGroup> byId = modulesById(document.modules());
        Map<String, List<String>> idsByRoot = new LinkedHashMap<>();
        document.modules().stream()
                .sorted(Comparator
                        .comparingInt(this::rootRank)
                        .thenComparing(this::rootKey)
                        .thenComparing(this::normalizedPath)
                        .thenComparing(UmlModuleGroup::displayName))
                .forEach(module -> idsByRoot
                        .computeIfAbsent(rootKey(module), ignored -> new ArrayList<>())
                        .add(module.id()));

        List<DependencyDrivenOrderingPolicy.DirectedDependency> dependencies = document.relations().stream()
                .map(relation -> new DependencyDrivenOrderingPolicy.DirectedDependency(
                        moduleIdForClass(document, relation.sourceClassId()),
                        moduleIdForClass(document, relation.targetClassId())))
                .toList();

        ArrayList<UmlModuleGroup> ordered = new ArrayList<>();
        for (List<String> ids : idsByRoot.values()) {
            dependencyOrdering.order(ids, dependencies).stream()
                    .map(byId::get)
                    .filter(Objects::nonNull)
                    .forEach(ordered::add);
        }
        return ordered;
    }

    private int rootRank(UmlModuleGroup module) {
        String key = rootKey(module);
        if (key.contains("backend") || key.contains("java") || key.contains("api")) return 10;
        if (key.contains("frontend") || key.contains("front") || key.contains("typescript") || key.contains("angular")) return 20;
        if (key.contains("shared") || key.contains("common")) return 30;
        if (key.contains("library") || key.contains("lib")) return 40;
        return 90;
    }

    private String rootKey(UmlModuleGroup module) {
        String notes = normalize(module.notes()).toLowerCase(Locale.ROOT);
        int index = notes.indexOf("source root:");
        if (index >= 0) {
            String value = notes.substring(index + "source root:".length());
            int separator = value.indexOf('|');
            return normalize(separator >= 0 ? value.substring(0, separator) : value).toLowerCase(Locale.ROOT);
        }
        String id = normalize(module.id()).toLowerCase(Locale.ROOT);
        int separator = id.indexOf('-');
        return separator > 0 ? id.substring(0, separator) : id;
    }

    private String normalizedPath(UmlModuleGroup module) {
        String path = normalize(module.path()).toLowerCase(Locale.ROOT);
        return path.isBlank() ? normalize(module.displayName()).toLowerCase(Locale.ROOT) : path;
    }

    private static Map<String, UmlModuleGroup> modulesById(List<UmlModuleGroup> modules) {
        Map<String, UmlModuleGroup> byId = new HashMap<>();
        for (UmlModuleGroup module : modules) {
            byId.put(module.id(), module);
        }
        return byId;
    }

    private static String moduleIdForClass(UmlClassDiagramDocument document, String classId) {
        return document.classes().stream()
                .filter(node -> node.id().equals(classId))
                .map(node -> normalize(node.moduleId()))
                .findFirst()
                .orElse("");
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
