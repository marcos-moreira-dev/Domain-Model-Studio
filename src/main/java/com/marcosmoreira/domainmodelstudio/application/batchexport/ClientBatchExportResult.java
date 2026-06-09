package com.marcosmoreira.domainmodelstudio.application.batchexport;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/** Resultado de una exportación por lote de proyectos abiertos. */
public record ClientBatchExportResult(
        Path rootFolder,
        Path manifestFile,
        List<Path> createdFolders,
        List<Path> exportedFiles,
        List<ClientBatchExportItemResult> itemResults,
        List<String> notes
) {

    public ClientBatchExportResult {
        Objects.requireNonNull(rootFolder, "rootFolder");
        Objects.requireNonNull(manifestFile, "manifestFile");
        createdFolders = List.copyOf(Objects.requireNonNull(createdFolders, "createdFolders"));
        exportedFiles = List.copyOf(Objects.requireNonNull(exportedFiles, "exportedFiles"));
        itemResults = List.copyOf(Objects.requireNonNull(itemResults, "itemResults"));
        notes = List.copyOf(Objects.requireNonNull(notes, "notes"));
    }
}
