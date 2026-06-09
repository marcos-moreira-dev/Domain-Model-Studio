package com.marcosmoreira.domainmodelstudio.application.export;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.io.IOException;
import java.nio.file.Path;

/** Puerto de exportacion PDF por proyecto, independiente de JavaFX. */
public interface PdfDiagramExporter {

    Path export(DiagramProject project, Path destinationFile) throws IOException;
}
