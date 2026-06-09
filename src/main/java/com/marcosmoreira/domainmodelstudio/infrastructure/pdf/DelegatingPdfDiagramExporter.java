package com.marcosmoreira.domainmodelstudio.infrastructure.pdf;

import com.marcosmoreira.domainmodelstudio.application.export.PdfDiagramExporter;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

/** Exportador PDF por proyecto que despacha al adaptador registrado para cada tipo. */
public final class DelegatingPdfDiagramExporter implements PdfDiagramExporter {

    private final Map<DiagramTypeId, PdfDiagramExporter> exporters;

    public DelegatingPdfDiagramExporter(Map<DiagramTypeId, PdfDiagramExporter> exporters) {
        this.exporters = Map.copyOf(Objects.requireNonNull(exporters, "exporters"));
    }

    @Override
    public Path export(DiagramProject project, Path destinationFile) throws IOException {
        Objects.requireNonNull(project, "project");
        DiagramTypeId typeId = project.metadata().diagramTypeId();
        PdfDiagramExporter exporter = exporters.get(typeId);
        if (exporter == null) {
            throw new IllegalArgumentException("El tipo activo no tiene exportador PDF registrado: "
                    + (typeId == null ? "sin tipo" : typeId.value()));
        }
        return exporter.export(project, destinationFile);
    }
}
