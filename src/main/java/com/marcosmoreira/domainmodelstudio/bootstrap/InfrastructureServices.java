package com.marcosmoreira.domainmodelstudio.bootstrap;

import com.marcosmoreira.domainmodelstudio.application.batchexport.ExportOpenProjectsForClientUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.ExportDataDictionaryPdfUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.ExportDataDictionaryMarkdownUseCase;
import com.marcosmoreira.domainmodelstudio.application.export.SvgDiagramExporter;
import com.marcosmoreira.domainmodelstudio.application.export.MarkdownDiagramExporter;
import com.marcosmoreira.domainmodelstudio.application.export.PdfDiagramExporter;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParser;
import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchCandidateReader;
import com.marcosmoreira.domainmodelstudio.application.project.ProjectRepository;
import com.marcosmoreira.domainmodelstudio.application.project.SourceMarkdownSynchronizer;
import com.marcosmoreira.domainmodelstudio.application.resources.ExportAiResourcesUseCase;
import java.util.Objects;

/** Contenedor de adaptadores técnicos disponibles para application. */
public final class InfrastructureServices {

    private final MarkdownModelParser markdownModelParser;
    private final MarkdownBatchCandidateReader markdownBatchCandidateReader;
    private final ProjectRepository projectRepository;
    private final SourceMarkdownSynchronizer sourceMarkdownSynchronizer;
    private final SvgDiagramExporter svgDiagramExporter;
    private final MarkdownDiagramExporter markdownDiagramExporter;
    private final PdfDiagramExporter pdfDiagramExporter;
    private final ExportAiResourcesUseCase exportAiResourcesUseCase;
    private final ExportDataDictionaryPdfUseCase exportDataDictionaryPdfUseCase;
    private final ExportDataDictionaryMarkdownUseCase exportDataDictionaryMarkdownUseCase;
    private final ExportOpenProjectsForClientUseCase exportOpenProjectsForClientUseCase;

    public InfrastructureServices(
            MarkdownModelParser markdownModelParser,
            MarkdownBatchCandidateReader markdownBatchCandidateReader,
            ProjectRepository projectRepository,
            SourceMarkdownSynchronizer sourceMarkdownSynchronizer,
            SvgDiagramExporter svgDiagramExporter,
            MarkdownDiagramExporter markdownDiagramExporter,
            PdfDiagramExporter pdfDiagramExporter,
            ExportAiResourcesUseCase exportAiResourcesUseCase,
            ExportDataDictionaryPdfUseCase exportDataDictionaryPdfUseCase,
            ExportDataDictionaryMarkdownUseCase exportDataDictionaryMarkdownUseCase,
            ExportOpenProjectsForClientUseCase exportOpenProjectsForClientUseCase
    ) {
        this.markdownModelParser = Objects.requireNonNull(markdownModelParser, "markdownModelParser");
        this.markdownBatchCandidateReader = Objects.requireNonNull(markdownBatchCandidateReader, "markdownBatchCandidateReader");
        this.projectRepository = Objects.requireNonNull(projectRepository, "projectRepository");
        this.sourceMarkdownSynchronizer = Objects.requireNonNull(sourceMarkdownSynchronizer, "sourceMarkdownSynchronizer");
        this.svgDiagramExporter = Objects.requireNonNull(svgDiagramExporter, "svgDiagramExporter");
        this.markdownDiagramExporter = Objects.requireNonNull(markdownDiagramExporter, "markdownDiagramExporter");
        this.pdfDiagramExporter = Objects.requireNonNull(pdfDiagramExporter, "pdfDiagramExporter");
        this.exportAiResourcesUseCase = Objects.requireNonNull(exportAiResourcesUseCase, "exportAiResourcesUseCase");
        this.exportDataDictionaryPdfUseCase = Objects.requireNonNull(exportDataDictionaryPdfUseCase, "exportDataDictionaryPdfUseCase");
        this.exportDataDictionaryMarkdownUseCase = Objects.requireNonNull(exportDataDictionaryMarkdownUseCase, "exportDataDictionaryMarkdownUseCase");
        this.exportOpenProjectsForClientUseCase = Objects.requireNonNull(exportOpenProjectsForClientUseCase, "exportOpenProjectsForClientUseCase");
    }

    public MarkdownModelParser markdownModelParser() {
        return markdownModelParser;
    }

    public MarkdownBatchCandidateReader markdownBatchCandidateReader() {
        return markdownBatchCandidateReader;
    }

    public ProjectRepository projectRepository() {
        return projectRepository;
    }

    public SourceMarkdownSynchronizer sourceMarkdownSynchronizer() {
        return sourceMarkdownSynchronizer;
    }

    public SvgDiagramExporter svgDiagramExporter() {
        return svgDiagramExporter;
    }

    public MarkdownDiagramExporter markdownDiagramExporter() {
        return markdownDiagramExporter;
    }

    public PdfDiagramExporter pdfDiagramExporter() {
        return pdfDiagramExporter;
    }

    public ExportAiResourcesUseCase exportAiResourcesUseCase() {
        return exportAiResourcesUseCase;
    }

    public ExportDataDictionaryPdfUseCase exportDataDictionaryPdfUseCase() {
        return exportDataDictionaryPdfUseCase;
    }

    public ExportDataDictionaryMarkdownUseCase exportDataDictionaryMarkdownUseCase() {
        return exportDataDictionaryMarkdownUseCase;
    }

    public ExportOpenProjectsForClientUseCase exportOpenProjectsForClientUseCase() {
        return exportOpenProjectsForClientUseCase;
    }
}
