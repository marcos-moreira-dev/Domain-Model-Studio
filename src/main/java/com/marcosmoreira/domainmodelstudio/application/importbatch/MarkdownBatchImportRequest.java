package com.marcosmoreira.domainmodelstudio.application.importbatch;

import java.nio.file.Path;
import java.util.Objects;

/** Solicitud para abrir una carpeta raíz con artefactos Markdown de proyecto. */
public record MarkdownBatchImportRequest(
        Path sourceRoot,
        MarkdownBatchImportPolicy policy
) {

    public MarkdownBatchImportRequest {
        Objects.requireNonNull(sourceRoot, "La carpeta raíz Markdown no puede ser null");
        policy = policy == null ? MarkdownBatchImportPolicy.defaultPolicy() : policy;
    }
}
