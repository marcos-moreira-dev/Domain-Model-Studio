package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import java.util.ArrayList;
import java.util.List;

final class LogicalBusinessMarkdownTables {

    private LogicalBusinessMarkdownTables() {
    }

    static boolean looksLikeDataRow(String line) {
        String stripped = line == null ? "" : line.strip();
        if (!stripped.startsWith("|") || !stripped.endsWith("|")) {
            return false;
        }
        if (stripped.replace("|", "").replace("-", "").replace(":", "").isBlank()) {
            return false;
        }
        return LogicalBusinessMarkdownIds.firstId(stripped).isPresent();
    }

    static List<String> cells(String line) {
        String stripped = line == null ? "" : line.strip();
        if (stripped.startsWith("|")) {
            stripped = stripped.substring(1);
        }
        if (stripped.endsWith("|")) {
            stripped = stripped.substring(0, stripped.length() - 1);
        }
        List<String> cells = new ArrayList<>();
        for (String cell : stripped.split("\\|")) {
            cells.add(clean(cell));
        }
        return List.copyOf(cells);
    }

    private static String clean(String value) {
        return value == null ? "" : value.strip().replace("`", "");
    }
}
