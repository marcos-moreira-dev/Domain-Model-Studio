package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/** Índice de texto precomputado para búsqueda y navegación en diagramas UML Clases grandes. */
final class UmlClassSearchIndex {

    private static final UmlClassSearchIndex EMPTY = new UmlClassSearchIndex(Map.of(), Map.of(), Map.of());

    private final Map<String, String> moduleTextById;
    private final Map<String, String> classTextById;
    private final Map<String, String> relationTextById;

    private UmlClassSearchIndex(
            Map<String, String> moduleTextById,
            Map<String, String> classTextById,
            Map<String, String> relationTextById
    ) {
        this.moduleTextById = Map.copyOf(moduleTextById);
        this.classTextById = Map.copyOf(classTextById);
        this.relationTextById = Map.copyOf(relationTextById);
    }

    static UmlClassSearchIndex empty() {
        return EMPTY;
    }

    static UmlClassSearchIndex from(UmlClassDiagramDocument document) {
        if (document == null) {
            return EMPTY;
        }
        Map<String, String> modules = new LinkedHashMap<>();
        for (UmlModuleGroup module : document.modules()) {
            modules.put(module.id(), normalize(moduleSearchText(module)));
        }

        Map<String, String> classHeaders = new LinkedHashMap<>();
        for (UmlClassNode node : document.classes()) {
            classHeaders.put(node.id(), normalize(classHeaderText(node, modules.get(node.moduleId()))));
        }

        Map<String, String> classes = new LinkedHashMap<>();
        for (UmlClassNode node : document.classes()) {
            classes.put(node.id(), normalize(classSearchText(node, modules.get(node.moduleId()))));
        }

        Map<String, String> relations = new LinkedHashMap<>();
        for (UmlClassRelation relation : document.relations()) {
            relations.put(relation.id(), normalize(relationSearchText(relation,
                    classHeaders.get(relation.sourceClassId()), classHeaders.get(relation.targetClassId()))));
        }
        return new UmlClassSearchIndex(modules, classes, relations);
    }

    boolean matchesModule(String query, UmlModuleGroup module) {
        return contains(moduleTextById.get(module == null ? "" : module.id()), query);
    }

    boolean matchesClass(String query, UmlClassNode node) {
        return contains(classTextById.get(node == null ? "" : node.id()), query);
    }

    boolean matchesRelation(String query, UmlClassRelation relation) {
        return contains(relationTextById.get(relation == null ? "" : relation.id()), query);
    }

    int moduleEntryCount() {
        return moduleTextById.size();
    }

    int classEntryCount() {
        return classTextById.size();
    }

    int relationEntryCount() {
        return relationTextById.size();
    }

    private static String moduleSearchText(UmlModuleGroup module) {
        StringBuilder text = new StringBuilder();
        append(text, module.id());
        append(text, module.displayName());
        append(text, module.path());
        append(text, module.description());
        append(text, module.notes());
        return text.toString();
    }

    private static String classHeaderText(UmlClassNode node, String moduleText) {
        StringBuilder text = new StringBuilder();
        append(text, node.id());
        append(text, node.moduleId());
        append(text, node.displayName());
        append(text, node.packageName());
        append(text, node.kind().displayName());
        append(text, node.visibility().displayName());
        append(text, node.responsibility());
        append(text, node.description());
        append(text, node.notes());
        append(text, moduleText);
        return text.toString();
    }

    private static String classSearchText(UmlClassNode node, String moduleText) {
        StringBuilder text = new StringBuilder(classHeaderText(node, moduleText));
        for (UmlClassMember member : node.members()) {
            append(text, member.id());
            append(text, member.name());
            append(text, member.type());
            append(text, member.signature());
            append(text, member.kind().displayName());
            append(text, member.visibility().displayName());
            append(text, member.description());
        }
        return text.toString();
    }

    private static String relationSearchText(UmlClassRelation relation, String sourceClassText, String targetClassText) {
        StringBuilder text = new StringBuilder();
        append(text, relation.id());
        append(text, relation.sourceClassId());
        append(text, relation.targetClassId());
        append(text, relation.kind().displayName());
        append(text, relation.label());
        append(text, relation.description());
        append(text, relation.notes());
        append(text, sourceClassText);
        append(text, targetClassText);
        return text.toString();
    }

    private static boolean contains(String text, String query) {
        String normalizedQuery = normalize(query);
        return normalizedQuery.isBlank() || (text != null && text.contains(normalizedQuery));
    }

    private static String normalize(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT).strip();
    }

    private static void append(StringBuilder text, String value) {
        if (value != null && !value.isBlank()) {
            if (!text.isEmpty()) {
                text.append(' ');
            }
            text.append(value);
        }
    }
}
