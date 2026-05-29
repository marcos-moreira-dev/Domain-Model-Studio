package com.marcosmoreira.domainmodelstudio.application.validation;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.validation.DiagramProjectValidator;
import com.marcosmoreira.domainmodelstudio.domain.validation.ValidationResult;
import java.util.Objects;

/**
 * Caso de uso para validar el proyecto actualmente cargado.
 *
 * <p>Mantiene la validación fuera de la UI: el shell solo solicita validar y muestra
 * un resumen humano en la barra de estado.</p>
 */
public final class ValidateProjectUseCase {

    private final DiagramProjectValidator validator;

    public ValidateProjectUseCase(DiagramProjectValidator validator) {
        this.validator = Objects.requireNonNull(validator, "validator");
    }

    public ValidationResult validate(DiagramProject project) {
        return validator.validate(Objects.requireNonNull(project, "project"));
    }
}
