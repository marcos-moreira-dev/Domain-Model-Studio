package com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode.typescript;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** Política de nombres TypeScript para asociaciones tentativas y etiquetas UML. */
final class TypeScriptTypeNamePolicy {
    private static final Set<String> IGNORED = Set.of(
            "string", "number", "boolean", "bigint", "symbol", "object", "void", "never", "unknown", "any", "null",
            "undefined", "Array", "ReadonlyArray", "Promise", "Observable", "Record", "Map", "Set", "Date", "Error");

    private TypeScriptTypeNamePolicy() {
    }

    static List<String> associationTargets(String typeExpression) {
        LinkedHashSet<String> names = new LinkedHashSet<>();
        String normalized = normalize(typeExpression);
        StringBuilder token = new StringBuilder();
        for (int i = 0; i <= normalized.length(); i++) {
            char current = i < normalized.length() ? normalized.charAt(i) : ' ';
            if (Character.isJavaIdentifierPart(current) || current == '_' || current == '$') {
                token.append(current);
            } else {
                addCandidate(names, token.toString());
                token.setLength(0);
            }
        }
        return List.copyOf(names);
    }

    static String simpleName(String rawName) {
        String normalized = normalize(rawName);
        int dot = normalized.lastIndexOf('.');
        return dot >= 0 ? normalized.substring(dot + 1) : normalized;
    }

    private static void addCandidate(Set<String> names, String raw) {
        String candidate = raw == null ? "" : raw.strip();
        if (candidate.isBlank() || IGNORED.contains(candidate) || IGNORED.contains(candidate.toLowerCase(Locale.ROOT))) {
            return;
        }
        if (candidate.length() > 1 && Character.isUpperCase(candidate.charAt(0))) {
            names.add(simpleName(candidate));
        }
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip()
                .replace("[]", " ")
                .replace("<", " ")
                .replace(">", " ")
                .replace("|", " ")
                .replace("&", " ")
                .replace("?", " ");
    }
}
