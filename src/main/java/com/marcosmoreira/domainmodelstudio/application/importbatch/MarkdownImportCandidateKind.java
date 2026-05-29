package com.marcosmoreira.domainmodelstudio.application.importbatch;

/** Clasificación previa de un archivo hallado en una carpeta raíz Markdown. */
public enum MarkdownImportCandidateKind {
    PROJECT_CANDIDATE,
    SKIPPED_NOT_MARKDOWN,
    SKIPPED_README,
    SKIPPED_MISSING_FRONTMATTER,
    SKIPPED_MISSING_DIAGRAM_TYPE,
    SKIPPED_IMPORTABLE_FALSE,
    SKIPPED_TEMPLATE,
    SKIPPED_PROMPT,
    SKIPPED_GRAMMAR;

    public boolean projectCandidate() {
        return this == PROJECT_CANDIDATE;
    }
}
