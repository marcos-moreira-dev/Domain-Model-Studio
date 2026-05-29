package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import java.util.ArrayList;
import java.util.List;

/** Limpieza de textos visibles del levantamiento lógico sin alterar el Markdown canónico. */
final class LogicalBusinessDisplayText {

    private LogicalBusinessDisplayText() {
    }

    static String clean(String value) {
        if (value == null || value.isBlank()) {
            return "—";
        }
        List<String> lines = new ArrayList<>();
        for (String line : value.replace("\r\n", "\n").replace('\r', '\n').split("\n", -1)) {
            String trimmed = line.strip();
            if (trimmed.equals("```") || trimmed.matches("```[A-Za-z0-9_-]*")) {
                continue;
            }
            lines.add(line.stripTrailing());
        }
        String cleaned = String.join("\n", lines).strip();
        return cleaned.isBlank() ? "—" : cleaned;
    }
}
