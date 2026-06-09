package com.marcosmoreira.domainmodelstudio.application.importbatch;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/** Resultado consolidado de abrir una carpeta raíz con Markdown de proyectos. */
public record MarkdownBatchImportResult(
        Path sourceRoot,
        int scannedFiles,
        int candidateFiles,
        int importedCount,
        int skippedCount,
        int rejectedCount,
        List<MarkdownBatchImportItemResult> items,
        List<String> notes
) {

    public MarkdownBatchImportResult {
        Objects.requireNonNull(sourceRoot, "sourceRoot");
        if (scannedFiles < 0 || candidateFiles < 0 || importedCount < 0 || skippedCount < 0 || rejectedCount < 0) {
            throw new IllegalArgumentException("Los contadores de importación Markdown no pueden ser negativos.");
        }
        items = List.copyOf(items == null ? List.of() : items);
        notes = List.copyOf(notes == null ? List.of() : notes);
    }

    public List<MarkdownBatchImportItemResult> importedItems() {
        return items.stream().filter(MarkdownBatchImportItemResult::imported).toList();
    }
}
