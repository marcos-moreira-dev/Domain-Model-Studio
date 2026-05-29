package com.marcosmoreira.domainmodelstudio.application.resources;

import java.io.IOException;
import java.nio.file.Path;

/** Contrato para copiar recursos IA registrados a una carpeta elegida por el usuario. */
public interface ExportAiResourcesUseCase {

    AiResourceExportResult exportTo(Path destinationFolder) throws IOException;
}
