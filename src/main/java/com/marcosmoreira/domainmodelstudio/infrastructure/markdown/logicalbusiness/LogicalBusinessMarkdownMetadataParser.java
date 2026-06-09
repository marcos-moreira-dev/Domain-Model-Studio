package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocumentStatus;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;

final class LogicalBusinessMarkdownMetadataParser {

    LogicalBusinessMarkdownMetadata parse(String markdown) {
        Map<String, String> values = new LinkedHashMap<>();
        for (String rawLine : safe(markdown).split("\\R")) {
            String line = rawLine.strip();
            if (!line.startsWith("- **") || !line.contains(":**")) {
                continue;
            }
            String key = line.substring(4, line.indexOf(":**")).strip();
            String value = clean(line.substring(line.indexOf(":**") + 3));
            values.put(LogicalBusinessMarkdownStatusMapper.stable(key), value);
        }
        LogicalBusinessDocumentStatus status = LogicalBusinessMarkdownStatusMapper.documentStatus(
                first(values, "estado_del_documento", "estado"));
        return new LogicalBusinessMarkdownMetadata(
                first(values, "negocio"),
                first(values, "proyecto_sistema", "proyecto"),
                first(values, "version_del_levantamiento", "version"),
                parseDate(first(values, "fecha")),
                status,
                first(values, "fuente_principal", "fuente"));
    }

    private static String first(Map<String, String> values, String... keys) {
        for (String key : keys) {
            String value = values.get(key);
            if (value != null && !value.isBlank() && !value.contains("<")) {
                return value;
            }
        }
        return "";
    }

    private static LocalDate parseDate(String value) {
        if (value == null || value.isBlank() || value.contains("<")) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(value.replace("`", "").strip());
        } catch (DateTimeParseException exception) {
            return LocalDate.now();
        }
    }

    private static String clean(String value) {
        String cleaned = value == null ? "" : value.strip();
        if (cleaned.startsWith("`") && cleaned.endsWith("`") && cleaned.length() >= 2) {
            cleaned = cleaned.substring(1, cleaned.length() - 1);
        }
        return cleaned.strip();
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }
}
