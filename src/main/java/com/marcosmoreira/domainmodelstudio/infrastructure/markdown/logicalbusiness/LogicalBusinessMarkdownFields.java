package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

final class LogicalBusinessMarkdownFields {

    private LogicalBusinessMarkdownFields() {
    }

    static Optional<String> field(String body, String... labels) {
        List<String> lines = List.of((body == null ? "" : body).split("\\R", -1));
        for (int index = 0; index < lines.size(); index++) {
            String line = lines.get(index).strip();
            if (!line.startsWith("- ") || !line.contains(":")) {
                continue;
            }
            String key = normalize(line.substring(2, line.indexOf(':')));
            if (!matches(key, labels)) {
                continue;
            }
            String inlineValue = clean(line.substring(line.indexOf(':') + 1));
            String collected = inlineValue.isBlank() ? collectNested(lines, index + 1) : inlineValue;
            if (!collected.isBlank()) {
                return Optional.of(collected);
            }
        }
        return Optional.empty();
    }

    static String descriptionFor(ParsedLogicalBusinessItem item) {
        if (item.fromTable()) {
            if (item.id().startsWith("SUP-")) {
                return cell(item, 1).orElse("");
            }
            return cell(item, 2).orElse("");
        }
        return field(item.body(), "descripcion humana", "descripción humana", "descripcion", "descripción",
                "justificacion logica", "justificación lógica", "razon operativa", "razón operativa",
                "pregunta que responde", "objetivo", "significado")
                .orElse(firstParagraph(item.body()));
    }

    static String sourceFor(ParsedLogicalBusinessItem item) {
        if (item.fromTable()) {
            if (item.id().startsWith("SUP-") && item.tableCells().size() >= 3) {
                return cell(item, 2).orElse("");
            }
            return cell(item, item.tableCells().size() - 1).orElse("");
        }
        return field(item.body(), "fuente", "fuente logica", "fuente lógica", "sustento logico", "sustento lógico",
                "justificada por", "justificado por", "derivada de", "derivado de",
                "fuente de derivacion", "fuente de derivación").orElse("");
    }

    static String statusFor(ParsedLogicalBusinessItem item) {
        if (item.fromTable() && item.id().startsWith("SUP-") && item.tableCells().size() >= 4) {
            return cell(item, 3).orElse("");
        }
        if (item.fromTable() && item.tableCells().size() >= 5) {
            return cell(item, 4).orElse("");
        }
        return field(item.body(), "estado", "estado del documento").orElse("");
    }

    static String humanReadingFor(ParsedLogicalBusinessItem item) {
        return field(item.body(), "lectura", "lectura de la forma logica", "lectura de la forma lógica")
                .orElse("");
    }

    static List<String> referencesFor(ParsedLogicalBusinessItem item) {
        String text = item.fromTable() ? String.join(" ", item.tableCells()) : item.body();
        return LogicalBusinessMarkdownIds.idsInExcept(text, item.id());
    }

    static Optional<String> cell(ParsedLogicalBusinessItem item, int index) {
        if (index < 0 || index >= item.tableCells().size()) {
            return Optional.empty();
        }
        String value = clean(item.tableCells().get(index));
        return value.isBlank() ? Optional.empty() : Optional.of(value);
    }

    private static String collectNested(List<String> lines, int startIndex) {
        List<String> collected = new ArrayList<>();
        boolean insideFence = false;
        for (int index = startIndex; index < lines.size(); index++) {
            String raw = lines.get(index);
            String stripped = raw.strip();
            if (stripped.startsWith("```") && collected.isEmpty()) {
                insideFence = true;
                continue;
            }
            if (insideFence && stripped.startsWith("```")) {
                break;
            }
            if (!insideFence && stripped.startsWith("- ") && stripped.contains(":")) {
                break;
            }
            if (!insideFence && stripped.startsWith("### ")) {
                break;
            }
            if (!stripped.isBlank()) {
                collected.add(stripped.replaceFirst("^-\\s*", ""));
            }
        }
        return String.join("\n", collected).strip();
    }

    private static boolean matches(String key, String... labels) {
        Set<String> normalizedLabels = Set.of(labels).stream().map(LogicalBusinessMarkdownFields::normalize)
                .collect(java.util.stream.Collectors.toSet());
        return normalizedLabels.contains(key);
    }

    private static String firstParagraph(String body) {
        for (String rawLine : (body == null ? "" : body).split("\\R")) {
            String line = rawLine.strip();
            if (!line.isBlank() && !line.startsWith("-") && !line.startsWith("```") && !line.startsWith("|")) {
                return line;
            }
        }
        return "";
    }

    private static String normalize(String value) {
        return LogicalBusinessMarkdownStatusMapper.stable(value).replace('_', ' ').strip().toLowerCase(Locale.ROOT);
    }

    private static String clean(String value) {
        String cleaned = value == null ? "" : value.strip();
        if (cleaned.endsWith(".")) {
            cleaned = cleaned.substring(0, cleaned.length() - 1).strip();
        }
        return cleaned.replace("`", "").strip();
    }
}
