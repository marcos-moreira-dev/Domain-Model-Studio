package com.marcosmoreira.domainmodelstudio.application.importbatch;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Archivo detectado durante el barrido de una carpeta raíz Markdown. */
public record MarkdownImportCandidate(
        Path sourceFile,
        String displayName,
        MarkdownImportCandidateKind kind,
        Optional<String> declaredDiagramType,
        Optional<Boolean> importable,
        Optional<String> sampleKind,
        Optional<DiagramTypeId> suffixDiagramType,
        List<String> notes
) {

    public MarkdownImportCandidate {
        Objects.requireNonNull(sourceFile, "sourceFile");
        displayName = normalizeDisplayName(displayName, sourceFile);
        Objects.requireNonNull(kind, "kind");
        declaredDiagramType = declaredDiagramType == null ? Optional.empty() : declaredDiagramType;
        importable = importable == null ? Optional.empty() : importable;
        sampleKind = sampleKind == null ? Optional.empty() : sampleKind;
        suffixDiagramType = suffixDiagramType == null ? Optional.empty() : suffixDiagramType;
        notes = List.copyOf(notes == null ? List.of() : notes);
    }

    public boolean projectCandidate() {
        return kind.projectCandidate();
    }

    private static String normalizeDisplayName(String displayName, Path sourceFile) {
        if (displayName != null && !displayName.isBlank()) {
            return displayName.strip();
        }
        Path fileName = sourceFile.getFileName();
        return fileName == null ? sourceFile.toString() : fileName.toString();
    }
}
