package com.marcosmoreira.domainmodelstudio.application.services;

import com.marcosmoreira.domainmodelstudio.application.batchexport.ExportOpenProjectsForClientUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.ExportDataDictionaryMarkdownUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.ExportDataDictionaryPdfUseCase;
import com.marcosmoreira.domainmodelstudio.application.export.ExportMarkdownUseCase;
import com.marcosmoreira.domainmodelstudio.application.export.ExportSvgUseCase;
import java.util.Objects;

/**
 * Fachada de casos de uso de salida/exportación.
 *
 * <p>Los coordinadores de presentación deben depender de esta familia para no
 * acoplarse a toda la fachada de aplicación.</p>
 */
public final class ExportApplicationServices {

    private final ExportSvgUseCase exportSvgUseCase;
    private final ExportMarkdownUseCase exportMarkdownUseCase;
    private final ExportDataDictionaryPdfUseCase exportDataDictionaryPdfUseCase;
    private final ExportDataDictionaryMarkdownUseCase exportDataDictionaryMarkdownUseCase;
    private final ExportOpenProjectsForClientUseCase exportOpenProjectsForClientUseCase;

    public ExportApplicationServices(
            ExportSvgUseCase exportSvgUseCase,
            ExportMarkdownUseCase exportMarkdownUseCase,
            ExportDataDictionaryPdfUseCase exportDataDictionaryPdfUseCase,
            ExportDataDictionaryMarkdownUseCase exportDataDictionaryMarkdownUseCase,
            ExportOpenProjectsForClientUseCase exportOpenProjectsForClientUseCase
    ) {
        this.exportSvgUseCase = Objects.requireNonNull(exportSvgUseCase, "exportSvgUseCase");
        this.exportMarkdownUseCase = Objects.requireNonNull(exportMarkdownUseCase, "exportMarkdownUseCase");
        this.exportDataDictionaryPdfUseCase = Objects.requireNonNull(
                exportDataDictionaryPdfUseCase, "exportDataDictionaryPdfUseCase");
        this.exportDataDictionaryMarkdownUseCase = Objects.requireNonNull(
                exportDataDictionaryMarkdownUseCase, "exportDataDictionaryMarkdownUseCase");
        this.exportOpenProjectsForClientUseCase = Objects.requireNonNull(
                exportOpenProjectsForClientUseCase, "exportOpenProjectsForClientUseCase");
    }

    public ExportSvgUseCase exportSvgUseCase() {
        return exportSvgUseCase;
    }

    public ExportMarkdownUseCase exportMarkdownUseCase() {
        return exportMarkdownUseCase;
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
