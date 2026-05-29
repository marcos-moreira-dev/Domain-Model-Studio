package com.marcosmoreira.domainmodelstudio.domain.logicalbusiness;

import java.util.List;

final class LogicalBusinessText {

    private LogicalBusinessText() {
    }

    static String normalize(String value) {
        return value == null ? "" : value.strip();
    }

    static String require(String value, String fieldName) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El campo " + fieldName + " no puede estar vacío.");
        }
        return normalized;
    }

    static List<String> normalizedList(List<String> values) {
        if (values == null) {
            return List.of();
        }
        return values.stream()
                .map(LogicalBusinessText::normalize)
                .filter(value -> !value.isBlank())
                .distinct()
                .toList();
    }
}
