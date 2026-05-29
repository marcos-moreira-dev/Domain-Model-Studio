package com.marcosmoreira.domainmodelstudio.application.services;

import com.marcosmoreira.domainmodelstudio.application.screenflow.AddScreenTransitionUseCase;
import com.marcosmoreira.domainmodelstudio.application.screenflow.AddScreenUseCase;
import com.marcosmoreira.domainmodelstudio.application.screenflow.CreateScreenFlowUseCase;
import com.marcosmoreira.domainmodelstudio.application.screenflow.RemoveScreenFlowItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.screenflow.UpdateScreenTransitionUseCase;
import com.marcosmoreira.domainmodelstudio.application.screenflow.UpdateScreenUseCase;
import com.marcosmoreira.domainmodelstudio.application.screenflow.ValidateScreenFlowUseCase;
import java.util.Objects;

/** Fachada de edición y validación del Flujo de pantallas. */
public final class ScreenFlowApplicationServices {

    private final CreateScreenFlowUseCase createScreenFlowUseCase;
    private final AddScreenUseCase addScreenUseCase;
    private final AddScreenTransitionUseCase addScreenTransitionUseCase;
    private final UpdateScreenUseCase updateScreenUseCase;
    private final UpdateScreenTransitionUseCase updateScreenTransitionUseCase;
    private final RemoveScreenFlowItemUseCase removeScreenFlowItemUseCase;
    private final ValidateScreenFlowUseCase validateScreenFlowUseCase;

    public ScreenFlowApplicationServices(
            CreateScreenFlowUseCase createScreenFlowUseCase,
            AddScreenUseCase addScreenUseCase,
            AddScreenTransitionUseCase addScreenTransitionUseCase,
            UpdateScreenUseCase updateScreenUseCase,
            UpdateScreenTransitionUseCase updateScreenTransitionUseCase,
            RemoveScreenFlowItemUseCase removeScreenFlowItemUseCase,
            ValidateScreenFlowUseCase validateScreenFlowUseCase
    ) {
        this.createScreenFlowUseCase = Objects.requireNonNull(createScreenFlowUseCase, "createScreenFlowUseCase");
        this.addScreenUseCase = Objects.requireNonNull(addScreenUseCase, "addScreenUseCase");
        this.addScreenTransitionUseCase = Objects.requireNonNull(addScreenTransitionUseCase, "addScreenTransitionUseCase");
        this.updateScreenUseCase = Objects.requireNonNull(updateScreenUseCase, "updateScreenUseCase");
        this.updateScreenTransitionUseCase = Objects.requireNonNull(updateScreenTransitionUseCase, "updateScreenTransitionUseCase");
        this.removeScreenFlowItemUseCase = Objects.requireNonNull(removeScreenFlowItemUseCase, "removeScreenFlowItemUseCase");
        this.validateScreenFlowUseCase = Objects.requireNonNull(validateScreenFlowUseCase, "validateScreenFlowUseCase");
    }

    public CreateScreenFlowUseCase createScreenFlowUseCase() {
        return createScreenFlowUseCase;
    }

    public AddScreenUseCase addScreenUseCase() {
        return addScreenUseCase;
    }

    public AddScreenTransitionUseCase addScreenTransitionUseCase() {
        return addScreenTransitionUseCase;
    }

    public UpdateScreenUseCase updateScreenUseCase() {
        return updateScreenUseCase;
    }

    public UpdateScreenTransitionUseCase updateScreenTransitionUseCase() {
        return updateScreenTransitionUseCase;
    }

    public RemoveScreenFlowItemUseCase removeScreenFlowItemUseCase() {
        return removeScreenFlowItemUseCase;
    }

    public ValidateScreenFlowUseCase validateScreenFlowUseCase() {
        return validateScreenFlowUseCase;
    }

}
