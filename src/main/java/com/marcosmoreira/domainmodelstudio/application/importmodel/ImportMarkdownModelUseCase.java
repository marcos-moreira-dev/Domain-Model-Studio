package com.marcosmoreira.domainmodelstudio.application.importmodel;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.validation.DiagramProjectValidator;
import com.marcosmoreira.domainmodelstudio.domain.validation.ValidationResult;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Caso de uso para importar Markdown estructurado.
 *
 * <p>Coordina parser y validación, pero no abre diálogos de archivo ni actualiza JavaFX.
 * La UI decide cuándo llamarlo y cómo presentar los resultados.</p>
 */
public final class ImportMarkdownModelUseCase {

    private final MarkdownModelParser parser;
    private final DiagramProjectValidator projectValidator;

    public ImportMarkdownModelUseCase(MarkdownModelParser parser, DiagramProjectValidator projectValidator) {
        this.parser = Objects.requireNonNull(parser, "El lector Markdown no puede ser null");
        this.projectValidator = Objects.requireNonNull(projectValidator, "El validador de proyecto no puede ser null");
    }

    public ImportMarkdownModelResult importFile(Path markdownFile)
            throws IOException, MarkdownModelParsingException {
        Objects.requireNonNull(markdownFile, "El archivo Markdown no puede ser null");
        DiagramProject project = parser.parse(markdownFile);
        ValidationResult validationResult = projectValidator.validate(project);
        return new ImportMarkdownModelResult(markdownFile, project, validationResult);
    }

    public ImportMarkdownModelResult importContent(String markdownContent, String sourceName)
            throws MarkdownModelParsingException {
        DiagramProject project = parser.parse(markdownContent, sourceName);
        ValidationResult validationResult = projectValidator.validate(project);
        return new ImportMarkdownModelResult(null, project, validationResult);
    }
}
