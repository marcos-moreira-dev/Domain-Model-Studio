package com.marcosmoreira.domainmodelstudio.application.project;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.validation.DiagramProjectValidator;
import com.marcosmoreira.domainmodelstudio.domain.validation.ValidationResult;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

/** Caso de uso para abrir un proyecto editable .dms desde disco. */
public final class OpenProjectUseCase {

    private final ProjectRepository projectRepository;
    private final DiagramProjectValidator validator;

    public OpenProjectUseCase(ProjectRepository projectRepository, DiagramProjectValidator validator) {
        this.projectRepository = Objects.requireNonNull(projectRepository, "projectRepository");
        this.validator = Objects.requireNonNull(validator, "validator");
    }

    public DiagramProject open(Path sourceFile) throws IOException {
        Objects.requireNonNull(sourceFile, "El archivo origen no puede ser null");
        DiagramProject project = projectRepository.open(sourceFile);
        ValidationResult validationResult = validator.validate(project);
        if (validationResult.hasErrors()) {
            throw new IllegalArgumentException("El archivo .dms contiene errores: "
                    + validationResult.errors().get(0).message());
        }
        return project;
    }
}
