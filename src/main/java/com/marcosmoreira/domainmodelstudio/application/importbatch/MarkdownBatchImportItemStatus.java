package com.marcosmoreira.domainmodelstudio.application.importbatch;

/** Estado individual dentro de una importación masiva de carpeta Markdown. */
public enum MarkdownBatchImportItemStatus {
    IMPORTED,
    IMPORTED_WITH_WARNINGS,
    IMPORTED_WITH_VALIDATION_ERRORS,

    SKIPPED_NOT_MARKDOWN,
    SKIPPED_README,
    SKIPPED_MISSING_FRONTMATTER,
    SKIPPED_MISSING_DIAGRAM_TYPE,
    SKIPPED_IMPORTABLE_FALSE,
    SKIPPED_TEMPLATE,
    SKIPPED_PROMPT,
    SKIPPED_GRAMMAR,

    REJECTED_UNKNOWN_DIAGRAM_TYPE,
    REJECTED_UNSUPPORTED_DIAGRAM_TYPE,
    REJECTED_SUFFIX_MISMATCH,
    REJECTED_PARSE_ERROR,
    REJECTED_IO_ERROR,
    REJECTED_LIMIT_EXCEEDED;

    public boolean imported() {
        return this == IMPORTED || this == IMPORTED_WITH_WARNINGS || this == IMPORTED_WITH_VALIDATION_ERRORS;
    }

    public boolean skipped() {
        return name().startsWith("SKIPPED_");
    }

    public boolean rejected() {
        return name().startsWith("REJECTED_");
    }
}
