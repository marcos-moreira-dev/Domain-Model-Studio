package com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** Utilidades pequeñas para normalizar nombres de tipo Java sin acoplar el dominio al parser concreto. */
final class JavaTypeNamePolicy {
    private static final Set<String> NON_ASSOCIATION_TYPES = Set.of(
            "byte", "short", "int", "long", "float", "double", "boolean", "char", "void",
            "Byte", "Short", "Integer", "Long", "Float", "Double", "Boolean", "Character", "Void",
            "String", "Object", "BigDecimal", "BigInteger", "LocalDate", "LocalDateTime", "Instant",
            "Date", "UUID");
    private static final Set<String> COLLECTION_TYPES = Set.of(
            "Collection", "List", "Set", "Map", "Optional", "Iterable", "Stream");

    private JavaTypeNamePolicy() {
    }

    static List<String> associationTargets(String typeExpression) {
        String normalized = clean(typeExpression);
        if (normalized.isBlank()) {
            return List.of();
        }
        List<String> candidates = new ArrayList<>();
        collectCandidates(normalized, candidates);
        return candidates.stream()
                .map(JavaTypeNamePolicy::simpleName)
                .filter(name -> !name.isBlank())
                .filter(name -> !NON_ASSOCIATION_TYPES.contains(name))
                .distinct()
                .toList();
    }

    static String simpleName(String qualifiedOrGenericName) {
        String normalized = clean(qualifiedOrGenericName);
        int genericIndex = normalized.indexOf('<');
        if (genericIndex >= 0) {
            normalized = normalized.substring(0, genericIndex);
        }
        int dotIndex = normalized.lastIndexOf('.');
        return dotIndex >= 0 ? normalized.substring(dotIndex + 1) : normalized;
    }

    static String clean(String value) {
        String normalized = value == null ? "" : value.strip();
        normalized = normalized.replace("...", "");
        while (normalized.endsWith("[]")) {
            normalized = normalized.substring(0, normalized.length() - 2).strip();
        }
        if (normalized.startsWith("? extends ")) {
            normalized = normalized.substring("? extends ".length()).strip();
        }
        if (normalized.startsWith("? super ")) {
            normalized = normalized.substring("? super ".length()).strip();
        }
        return normalized;
    }

    private static void collectCandidates(String expression, List<String> candidates) {
        int genericStart = expression.indexOf('<');
        if (genericStart < 0) {
            String simple = simpleName(expression);
            if (!COLLECTION_TYPES.contains(simple)) {
                candidates.add(expression);
            }
            return;
        }

        String outer = expression.substring(0, genericStart).strip();
        String outerSimple = simpleName(outer);
        String inside = insideGeneric(expression, genericStart);
        if (!COLLECTION_TYPES.contains(outerSimple)) {
            candidates.add(outer);
        }
        for (String part : splitTopLevelGenericArguments(inside)) {
            collectCandidates(part, candidates);
        }
    }

    private static String insideGeneric(String expression, int genericStart) {
        int depth = 0;
        for (int index = genericStart; index < expression.length(); index++) {
            char current = expression.charAt(index);
            if (current == '<') {
                depth++;
            } else if (current == '>') {
                depth--;
                if (depth == 0) {
                    return expression.substring(genericStart + 1, index);
                }
            }
        }
        return expression.substring(genericStart + 1);
    }

    private static List<String> splitTopLevelGenericArguments(String value) {
        List<String> result = new ArrayList<>();
        int depth = 0;
        int start = 0;
        for (int index = 0; index < value.length(); index++) {
            char current = value.charAt(index);
            if (current == '<') {
                depth++;
            } else if (current == '>') {
                depth--;
            } else if (current == ',' && depth == 0) {
                addPart(value, start, index, result);
                start = index + 1;
            }
        }
        addPart(value, start, value.length(), result);
        return result;
    }

    private static void addPart(String value, int start, int end, List<String> result) {
        String part = value.substring(start, end).strip();
        if (!part.isBlank() && !part.toLowerCase(Locale.ROOT).equals("?")) {
            result.add(part);
        }
    }
}
