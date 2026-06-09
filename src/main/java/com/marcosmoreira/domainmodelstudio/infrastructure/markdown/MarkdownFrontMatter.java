package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/** Metadatos simples leídos del front matter YAML limitado del Markdown. */
final class MarkdownFrontMatter {

    private final Map<String, String> values = new LinkedHashMap<>();

    void put(String key, String value) {
        if (key != null && !key.isBlank()) {
            values.put(key.trim().toLowerCase(), unquote(value == null ? "" : value.trim()));
        }
    }

    private String unquote(String value) {
        String cleaned = value == null ? "" : value.strip();
        if (cleaned.length() >= 2 && cleaned.startsWith("\"") && cleaned.endsWith("\"")) {
            return cleaned.substring(1, cleaned.length() - 1);
        }
        return cleaned;
    }

    Optional<String> value(String key) {
        return Optional.ofNullable(values.get(key.toLowerCase()));
    }

    String valueOrDefault(String key, String defaultValue) {
        return value(key).filter(text -> !text.isBlank()).orElse(defaultValue);
    }
}
