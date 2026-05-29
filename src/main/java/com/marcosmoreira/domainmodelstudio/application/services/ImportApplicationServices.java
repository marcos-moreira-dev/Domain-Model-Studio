package com.marcosmoreira.domainmodelstudio.application.services;

import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportUseCase;
import com.marcosmoreira.domainmodelstudio.application.importmodel.ImportMarkdownModelUseCase;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.PreviewSourceCodeImportUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.GenerateUmlClassDiagramFromSourceCodeUseCase;
import java.util.Objects;

/**
 * Fachada de casos de uso de entrada/importación.
 *
 * <p>Agrupa importación Markdown individual, importación por carpeta y entrada
 * desde código fuente. La selección de archivos/carpetas sigue viviendo en
 * presentación.</p>
 */
public final class ImportApplicationServices {

    private final ImportMarkdownModelUseCase importMarkdownModelUseCase;
    private final MarkdownBatchImportUseCase markdownBatchImportUseCase;
    private final GenerateUmlClassDiagramFromSourceCodeUseCase generateUmlClassDiagramFromSourceCodeUseCase;
    private final PreviewSourceCodeImportUseCase previewSourceCodeImportUseCase;

    public ImportApplicationServices(
            ImportMarkdownModelUseCase importMarkdownModelUseCase,
            MarkdownBatchImportUseCase markdownBatchImportUseCase,
            GenerateUmlClassDiagramFromSourceCodeUseCase generateUmlClassDiagramFromSourceCodeUseCase,
            PreviewSourceCodeImportUseCase previewSourceCodeImportUseCase
    ) {
        this.importMarkdownModelUseCase = Objects.requireNonNull(importMarkdownModelUseCase, "importMarkdownModelUseCase");
        this.markdownBatchImportUseCase = Objects.requireNonNull(markdownBatchImportUseCase, "markdownBatchImportUseCase");
        this.generateUmlClassDiagramFromSourceCodeUseCase = Objects.requireNonNull(
                generateUmlClassDiagramFromSourceCodeUseCase, "generateUmlClassDiagramFromSourceCodeUseCase");
        this.previewSourceCodeImportUseCase = Objects.requireNonNull(
                previewSourceCodeImportUseCase, "previewSourceCodeImportUseCase");
    }

    public ImportMarkdownModelUseCase importMarkdownModelUseCase() {
        return importMarkdownModelUseCase;
    }

    public MarkdownBatchImportUseCase markdownBatchImportUseCase() {
        return markdownBatchImportUseCase;
    }

    public GenerateUmlClassDiagramFromSourceCodeUseCase generateUmlClassDiagramFromSourceCodeUseCase() {
        return generateUmlClassDiagramFromSourceCodeUseCase;
    }

    public PreviewSourceCodeImportUseCase previewSourceCodeImportUseCase() {
        return previewSourceCodeImportUseCase;
    }
}
