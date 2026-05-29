package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Lectura limitada y segura del frontmatter real al inicio de un Markdown. */
public record MarkdownFrontMatterSnapshot(
        boolean hasFrontMatter,
        Map<String, String> values
) {

    public MarkdownFrontMatterSnapshot {
        values = Map.copyOf(values == null ? Map.of() : values);
    }

    public static MarkdownFrontMatterSnapshot none() {
        return new MarkdownFrontMatterSnapshot(false, Map.of());
    }

    public static MarkdownFrontMatterSnapshot of(Map<String, String> values) {
        return new MarkdownFrontMatterSnapshot(true, new LinkedHashMap<>(Objects.requireNonNull(values, "values")));
    }

    public Optional<String> value(String key) {
        if (key == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(values.get(key.strip().toLowerCase()));
    }

    public Optional<Boolean> booleanValue(String key) {
        return value(key).map(String::strip).map(value -> {
            if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("si") || value.equalsIgnoreCase("sí")) {
                return true;
            }
            if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no")) {
                return false;
            }
            return null;
        });
    }
}
