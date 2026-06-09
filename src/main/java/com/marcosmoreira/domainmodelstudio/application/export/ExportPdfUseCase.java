package com.marcosmoreira.domainmodelstudio.application.export;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

/** Caso de uso para exportar el proyecto activo como PDF formal. */
public final class ExportPdfUseCase {

    private final PdfDiagramExporter pdfDiagramExporter;

    public ExportPdfUseCase(PdfDiagramExporter pdfDiagramExporter) {
        this.pdfDiagramExporter = Objects.requireNonNull(pdfDiagramExporter, "pdfDiagramExporter");
    }

    public Path export(DiagramProject project, Path destinationFile) throws IOException {
        Objects.requireNonNull(project, "project");
        Objects.requireNonNull(destinationFile, "destinationFile");
        return pdfDiagramExporter.export(project, destinationFile);
    }
}
