package com.marcosmoreira.domainmodelstudio.application.batchexport;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/** Solicitud de exportación por lote de los proyectos abiertos. */
public record ClientBatchExportRequest(String clientName, Path destinationRoot, List<OpenProjectExportItem> items) {

    public ClientBatchExportRequest {
        clientName = clientName == null || clientName.isBlank() ? "proyectos_abiertos" : clientName.strip();
        Objects.requireNonNull(destinationRoot, "destinationRoot");
        items = List.copyOf(Objects.requireNonNull(items, "items"));
        if (items.isEmpty()) {
            throw new IllegalArgumentException("La exportación por lote requiere al menos un proyecto abierto.");
        }
    }
}
