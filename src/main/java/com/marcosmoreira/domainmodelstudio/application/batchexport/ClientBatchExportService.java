package com.marcosmoreira.domainmodelstudio.application.batchexport;

import java.io.IOException;

/** Contrato para exportar todos los proyectos abiertos a una carpeta base. */
public interface ClientBatchExportService {

    ClientBatchExportResult export(ClientBatchExportRequest request) throws IOException;
}
