package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import java.util.List;

record ParsedLogicalBusinessItem(
        String id,
        String title,
        String sectionId,
        String body,
        List<String> tableCells
) {
    ParsedLogicalBusinessItem {
        id = clean(id);
        title = clean(title);
        sectionId = clean(sectionId);
        body = clean(body);
        tableCells = List.copyOf(tableCells == null ? List.of() : tableCells);
    }

    boolean fromTable() {
        return !tableCells.isEmpty();
    }

    private static String clean(String value) {
        return value == null ? "" : value.strip();
    }
}
