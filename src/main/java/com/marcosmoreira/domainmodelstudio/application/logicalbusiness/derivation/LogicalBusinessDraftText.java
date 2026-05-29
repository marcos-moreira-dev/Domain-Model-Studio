package com.marcosmoreira.domainmodelstudio.application.logicalbusiness.derivation;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import java.text.Normalizer;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/** Utilidades textuales para borradores Markdown compatibles. */
final class LogicalBusinessDraftText {

    private LogicalBusinessDraftText() {
    }

    static String yamlHeader(LogicalBusinessDerivationTarget target, String name, String domain, String intendedOutput) {
        return "---\n"
                + "dms_version: \"1\"\n"
                + "diagram_type: \"" + target.diagramType() + "\"\n"
                + "name: \"" + escapeYaml(name) + "\"\n"
                + "sample_kind: \"compatible-draft\"\n"
                + "domain: \"" + escapeYaml(domain) + "\"\n"
                + "status: \"borrador compatible revisable\"\n"
                + "importable: true\n"
                + "source_mode: \"levantamiento-logico-como-fuente\"\n"
                + "auto_import: false\n"
                + "intended_output: \"" + escapeYaml(intendedOutput) + "\"\n"
                + "---\n";
    }

    static String slug(String value) {
        String camelSeparated = (value == null ? "item" : value).replaceAll("([a-z0-9])([A-Z])", "$1_$2");
        String normalized = Normalizer.normalize(camelSeparated, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_+|_+$", "");
        return normalized.isBlank() ? "item" : normalized;
    }

    static String itemNodeId(LogicalBusinessItem item) {
        return slug(item.id());
    }

    static String itemTitle(LogicalBusinessItem item) {
        return item.id() + " — " + item.title();
    }

    static String itemSummary(LogicalBusinessItem item) {
        if (!item.description().isBlank()) {
            return item.description();
        }
        if (!item.humanReading().isBlank()) {
            return item.humanReading();
        }
        if (!item.content().isBlank()) {
            return firstLine(item.content());
        }
        return item.title();
    }

    static String firstLine(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.strip().lines().findFirst().orElse("");
    }

    static String commaList(Collection<String> values) {
        List<String> clean = values == null ? List.of() : values.stream()
                .filter(value -> value != null && !value.isBlank())
                .map(String::strip)
                .toList();
        return clean.isEmpty() ? "—" : String.join(", ", clean);
    }

    static String md(String value) {
        return value == null || value.isBlank() ? "—" : value.strip();
    }

    static String escapeTable(String value) {
        return md(value).replace("|", "\\|").replace("\n", " ");
    }

    private static String escapeYaml(String value) {
        return md(value).replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
