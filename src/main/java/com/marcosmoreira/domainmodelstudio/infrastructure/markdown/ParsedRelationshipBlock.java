package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/** Bloque temporal de relación mientras se parsea el Markdown. */
final class ParsedRelationshipBlock {

    private final String headerName;
    private final Map<String, String> properties = new LinkedHashMap<>();

    ParsedRelationshipBlock(String headerName) {
        this.headerName = headerName == null ? "" : headerName.trim();
        properties.put("id", MarkdownTextUtils.toStableId(headerName));
    }

    String headerName() {
        return headerName;
    }

    void applyProperty(String key, String value) {
        if (key != null && !key.isBlank()) {
            properties.put(key.trim().toLowerCase(), value == null ? "" : value.trim());
        }
    }

    String valueOrDefault(String key, String defaultValue) {
        return Optional.ofNullable(properties.get(key.toLowerCase()))
                .filter(value -> !value.isBlank())
                .orElse(defaultValue);
    }
}
