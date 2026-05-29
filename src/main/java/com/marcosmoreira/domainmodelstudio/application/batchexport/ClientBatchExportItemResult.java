package com.marcosmoreira.domainmodelstudio.application.batchexport;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Resultado por pestaña/proyecto dentro de una exportación por lote. */
public record ClientBatchExportItemResult(
        String tabId,
        String displayName,
        Path projectFolder,
        Path inputFolder,
        Path editableFolder,
        Path outputFolder,
        String baseFileName,
        List<Path> exportedFiles,
        Optional<Path> pendingPngTarget
) {

    public ClientBatchExportItemResult {
        tabId = requireText(tabId, "tabId");
        displayName = requireText(displayName, "displayName");
        Objects.requireNonNull(projectFolder, "projectFolder");
        Objects.requireNonNull(inputFolder, "inputFolder");
        Objects.requireNonNull(editableFolder, "editableFolder");
        Objects.requireNonNull(outputFolder, "outputFolder");
        baseFileName = requireText(baseFileName, "baseFileName");
        exportedFiles = List.copyOf(Objects.requireNonNull(exportedFiles, "exportedFiles"));
        pendingPngTarget = pendingPngTarget == null ? Optional.empty() : pendingPngTarget;
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        String normalized = value.strip();
        if (normalized.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return normalized;
    }
}
