package com.marcosmoreira.domainmodelstudio.application.importmodel;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.validation.ValidationResult;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

/** Resultado de importar un Markdown estructurado. */
public final class ImportMarkdownModelResult {

    private final Path sourceFile;
    private final DiagramProject project;
    private final ValidationResult validationResult;

    public ImportMarkdownModelResult(Path sourceFile, DiagramProject project, ValidationResult validationResult) {
        this.sourceFile = sourceFile;
        this.project = Objects.requireNonNull(project, "El proyecto importado no puede ser null");
        this.validationResult = Objects.requireNonNull(validationResult, "El resultado de validación no puede ser null");
    }

    public Optional<Path> sourceFile() {
        return Optional.ofNullable(sourceFile);
    }

    public DiagramProject project() {
        return project;
    }

    public ValidationResult validationResult() {
        return validationResult;
    }

    public boolean hasErrors() {
        return !validationResult.isValid();
    }

    public String summary() {
        return new ImportedProjectSummaryFormatter().summarize(sourceFile, project);
    }
}
