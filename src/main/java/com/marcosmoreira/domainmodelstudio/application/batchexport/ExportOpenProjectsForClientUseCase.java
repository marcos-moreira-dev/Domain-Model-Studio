package com.marcosmoreira.domainmodelstudio.application.batchexport;

import java.io.IOException;
import java.util.Objects;

/** Caso de uso para exportar todos los proyectos abiertos. */
public final class ExportOpenProjectsForClientUseCase {

    private final ClientBatchExportService clientBatchExportService;

    public ExportOpenProjectsForClientUseCase(ClientBatchExportService clientBatchExportService) {
        this.clientBatchExportService = Objects.requireNonNull(clientBatchExportService, "clientBatchExportService");
    }

    public ClientBatchExportResult export(ClientBatchExportRequest request) throws IOException {
        Objects.requireNonNull(request, "request");
        return clientBatchExportService.export(request);
    }
}
