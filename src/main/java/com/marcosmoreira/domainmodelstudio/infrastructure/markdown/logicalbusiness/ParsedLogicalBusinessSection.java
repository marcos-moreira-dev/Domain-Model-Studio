package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import java.util.List;

record ParsedLogicalBusinessSection(
        String id,
        String title,
        String purpose,
        List<String> itemIds
) {
    ParsedLogicalBusinessSection {
        id = clean(id);
        title = clean(title);
        purpose = clean(purpose);
        itemIds = List.copyOf(itemIds == null ? List.of() : itemIds);
    }

    private static String clean(String value) {
        return value == null ? "" : value.strip();
    }
}
