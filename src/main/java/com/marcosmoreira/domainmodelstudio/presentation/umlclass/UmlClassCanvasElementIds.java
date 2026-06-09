package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;

/** IDs visuales canónicos usados por el canvas UML Clases. */
final class UmlClassCanvasElementIds {

    static final String MODULE_PREFIX = "uml-module:";
    static final String CLASS_PREFIX = "uml-class:";
    static final String RELATION_PREFIX = "uml-relation:";

    private UmlClassCanvasElementIds() {
    }

    static String moduleLayoutId(String moduleId) {
        return VisualElementLayoutIds.umlModule(moduleId).value();
    }

    static String classLayoutId(String classId) {
        return VisualElementLayoutIds.umlClass(classId).value();
    }

    static String relationLayoutId(String relationId) {
        return VisualElementLayoutIds.umlRelation(relationId).value();
    }

    static String rawIdAfterPrefix(String value, String prefix) {
        String normalized = normalize(value);
        return normalized.startsWith(prefix) ? normalized.substring(prefix.length()) : "";
    }

    static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
