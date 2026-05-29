package com.marcosmoreira.domainmodelstudio.application.importbatch;

import com.marcosmoreira.domainmodelstudio.application.importmodel.ImportMarkdownModelResult;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Resultado individual de un archivo revisado dentro de una carpeta Markdown. */
public record MarkdownBatchImportItemResult(
        Path sourceFile,
        String displayName,
        Optional<DiagramTypeId> declaredDiagramType,
        Optional<DiagramTypeId> suffixDiagramType,
        MarkdownBatchImportItemStatus status,
        Optional<ImportMarkdownModelResult> importResult,
        List<String> warnings,
        Optional<String> errorMessage
) {

    public MarkdownBatchImportItemResult {
        Objects.requireNonNull(sourceFile, "sourceFile");
        displayName = displayName == null || displayName.isBlank()
                ? sourceFile.getFileName().toString()
                : displayName.strip();
        declaredDiagramType = declaredDiagramType == null ? Optional.empty() : declaredDiagramType;
        suffixDiagramType = suffixDiagramType == null ? Optional.empty() : suffixDiagramType;
        Objects.requireNonNull(status, "status");
        importResult = importResult == null ? Optional.empty() : importResult;
        warnings = List.copyOf(warnings == null ? List.of() : warnings);
        errorMessage = errorMessage == null ? Optional.empty() : errorMessage;
    }

    public boolean imported() {
        return status.imported();
    }

    public boolean skipped() {
        return status.skipped();
    }

    public boolean rejected() {
        return status.rejected();
    }

    public static MarkdownBatchImportItemResult skipped(
            MarkdownImportCandidate candidate,
            MarkdownBatchImportItemStatus status
    ) {
        return new MarkdownBatchImportItemResult(
                candidate.sourceFile(),
                candidate.displayName(),
                Optional.empty(),
                candidate.suffixDiagramType(),
                status,
                Optional.empty(),
                candidate.notes(),
                Optional.empty());
    }
}
