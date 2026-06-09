package com.marcosmoreira.domainmodelstudio.infrastructure.pdf;

import com.marcosmoreira.domainmodelstudio.application.datadictionary.ExportDataDictionaryPdfUseCase;
import com.marcosmoreira.domainmodelstudio.application.export.ExportTargetPathPolicy;
import com.marcosmoreira.domainmodelstudio.application.export.PdfDiagramExporter;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Exporta el diccionario de datos como PDF formal y acotado al contenido tabular.
 *
 * <p>Normaliza la extensión de salida, delega la composición del reporte y devuelve la
 * ruta final. El PDF es una publicación de lectura; el formato editable sigue siendo
 * el proyecto {@code .dms} o el Markdown oficial cuando aplique.</p>
 */
public final class DataDictionaryPdfExporter implements ExportDataDictionaryPdfUseCase, PdfDiagramExporter {

    private final DataDictionaryPdfReportBuilder reportBuilder;
    private final ExportTargetPathPolicy targetPathPolicy;

    public DataDictionaryPdfExporter() {
        this(new DataDictionaryPdfReportBuilder(), new ExportTargetPathPolicy());
    }

    DataDictionaryPdfExporter(DataDictionaryPdfReportBuilder reportBuilder) {
        this(reportBuilder, new ExportTargetPathPolicy());
    }

    DataDictionaryPdfExporter(DataDictionaryPdfReportBuilder reportBuilder, ExportTargetPathPolicy targetPathPolicy) {
        this.reportBuilder = Objects.requireNonNull(reportBuilder, "reportBuilder");
        this.targetPathPolicy = Objects.requireNonNull(targetPathPolicy, "targetPathPolicy");
    }

    @Override
    public Path export(DataDictionaryDocument document, Path destinationFile) throws IOException {
        Objects.requireNonNull(document, "document");
        Objects.requireNonNull(destinationFile, "destinationFile");
        Path normalizedDestination = targetPathPolicy.ensurePdfExtension(destinationFile)
                .toAbsolutePath().normalize();
        reportBuilder.build(document).writeTo(normalizedDestination);
        return normalizedDestination;
    }

    @Override
    public Path export(DiagramProject project, Path destinationFile) throws IOException {
        Objects.requireNonNull(project, "project");
        DataDictionaryDocument document = project.dataDictionary()
                .orElseThrow(() -> new IllegalArgumentException("El proyecto no contiene diccionario PDF exportable."));
        return export(document, destinationFile);
    }

}
