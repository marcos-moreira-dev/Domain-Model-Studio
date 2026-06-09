package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import java.util.List;

record ParsedLogicalBusinessMarkdown(
        LogicalBusinessMarkdownMetadata metadata,
        List<ParsedLogicalBusinessSection> sections,
        List<ParsedLogicalBusinessItem> items,
        String notes
) {
    ParsedLogicalBusinessMarkdown {
        metadata = metadata == null ? LogicalBusinessMarkdownMetadata.empty() : metadata;
        sections = List.copyOf(sections == null ? List.of() : sections);
        items = List.copyOf(items == null ? List.of() : items);
        notes = notes == null ? "" : notes.strip();
    }
}
