package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import java.io.IOException;
import java.nio.file.Path;

/** Acción concreta para escribir PNG desde la salida visual activa. */
@FunctionalInterface
public interface ExportPngAction {
    void export(Path targetFile) throws IOException;
}
