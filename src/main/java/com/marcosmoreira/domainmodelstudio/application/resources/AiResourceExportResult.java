package com.marcosmoreira.domainmodelstudio.application.resources;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/** Resultado de exportar recursos IA a una carpeta destino. */
public record AiResourceExportResult(Path destinationFolder, List<String> exportedFiles) {

    public AiResourceExportResult {
        Objects.requireNonNull(destinationFolder, "destinationFolder");
        exportedFiles = List.copyOf(Objects.requireNonNull(exportedFiles, "exportedFiles"));
    }
}
