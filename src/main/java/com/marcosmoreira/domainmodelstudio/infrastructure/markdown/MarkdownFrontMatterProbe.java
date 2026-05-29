package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Lector reutilizable de frontmatter YAML limitado.
 *
 * <p>Solo considera frontmatter real cuando el primer renglón del archivo es {@code ---}.
 * Los bloques YAML internos de gramáticas, prompts o documentación auxiliar no se tratan
 * como encabezado importable.</p>
 */
public final class MarkdownFrontMatterProbe {

    public MarkdownFrontMatterSnapshot read(Path markdownFile) throws IOException {
        Objects.requireNonNull(markdownFile, "markdownFile");
        return read(Files.readString(markdownFile, StandardCharsets.UTF_8));
    }

    public MarkdownFrontMatterSnapshot read(String markdownContent) {
        Objects.requireNonNull(markdownContent, "markdownContent");
        String[] lines = markdownContent.split("\\R", -1);
        if (lines.length == 0 || !"---".equals(lines[0].strip())) {
            return MarkdownFrontMatterSnapshot.none();
        }
        Map<String, String> values = new LinkedHashMap<>();
        for (int index = 1; index < lines.length; index++) {
            String line = lines[index].strip();
            if ("---".equals(line)) {
                return MarkdownFrontMatterSnapshot.of(values);
            }
            if (MarkdownTextUtils.isPropertyLine(line)) {
                values.put(MarkdownTextUtils.keyBeforeColon(line), unquote(MarkdownTextUtils.valueAfterColon(line)));
            }
        }
        return MarkdownFrontMatterSnapshot.none();
    }

    private String unquote(String value) {
        String cleaned = value == null ? "" : value.strip();
        if (cleaned.length() >= 2 && cleaned.startsWith("\"") && cleaned.endsWith("\"")) {
            return cleaned.substring(1, cleaned.length() - 1);
        }
        return cleaned;
    }
}
