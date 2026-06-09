package com.marcosmoreira.domainmodelstudio.presentation.conceptual.bridge;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.validation.DiagramModelValidator;
import com.marcosmoreira.domainmodelstudio.domain.validation.ValidationResult;
import com.marcosmoreira.domainmodelstudio.presentation.canvas.DiagramCanvasViewModel;
import java.util.Objects;

/** Valida el modelo conceptual activo sin mutarlo ni crear una ruta paralela de dominio. */
public final class ConceptualValidationBridge {

    private final DiagramCanvasViewModel canvasViewModel;
    private final DiagramModelValidator validator;

    public ConceptualValidationBridge(DiagramCanvasViewModel canvasViewModel) {
        this(canvasViewModel, new DiagramModelValidator());
    }

    ConceptualValidationBridge(DiagramCanvasViewModel canvasViewModel, DiagramModelValidator validator) {
        this.canvasViewModel = Objects.requireNonNull(canvasViewModel, "canvasViewModel");
        this.validator = Objects.requireNonNull(validator, "validator");
    }

    public ValidationResult validateActiveProject() {
        DiagramProject project = canvasViewModel.currentProject();
        if (project == null) {
            return ValidationResult.ok();
        }
        return validator.validate(project.model());
    }
}
