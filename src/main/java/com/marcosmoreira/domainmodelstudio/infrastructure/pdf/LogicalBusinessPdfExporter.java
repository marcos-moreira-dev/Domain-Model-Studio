package com.marcosmoreira.domainmodelstudio.infrastructure.pdf;

import com.marcosmoreira.domainmodelstudio.application.export.ExportTargetPathPolicy;
import com.marcosmoreira.domainmodelstudio.application.export.PdfDiagramExporter;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

/** Exporta el levantamiento logico como PDF formal de lectura. */
public final class LogicalBusinessPdfExporter implements PdfDiagramExporter {

    private final LogicalBusinessPdfReportBuilder reportBuilder;
    private final ExportTargetPathPolicy targetPathPolicy;

    public LogicalBusinessPdfExporter() {
        this(new LogicalBusinessPdfReportBuilder(), new ExportTargetPathPolicy());
    }

    LogicalBusinessPdfExporter(LogicalBusinessPdfReportBuilder reportBuilder, ExportTargetPathPolicy targetPathPolicy) {
        this.reportBuilder = Objects.requireNonNull(reportBuilder, "reportBuilder");
        this.targetPathPolicy = Objects.requireNonNull(targetPathPolicy, "targetPathPolicy");
    }

    @Override
    public Path export(DiagramProject project, Path destinationFile) throws IOException {
        Objects.requireNonNull(project, "project");
        LogicalBusinessDocument document = project.logicalBusinessDocument()
                .orElseThrow(() -> new IllegalArgumentException(
                        "El proyecto no contiene levantamiento logico PDF exportable."));
        return export(document, destinationFile);
    }

    public Path export(LogicalBusinessDocument document, Path destinationFile) throws IOException {
        Objects.requireNonNull(document, "document");
        Objects.requireNonNull(destinationFile, "destinationFile");
        Path normalizedDestination = targetPathPolicy.ensurePdfExtension(destinationFile)
                .toAbsolutePath().normalize();
        reportBuilder.build(document).writeTo(normalizedDestination);
        return normalizedDestination;
    }

}
