package com.marcosmoreira.domainmodelstudio.application.services;

import com.marcosmoreira.domainmodelstudio.application.wireframe.AddWireframeComponentUseCase;
import com.marcosmoreira.domainmodelstudio.application.wireframe.AddWireframeScreenUseCase;
import com.marcosmoreira.domainmodelstudio.application.wireframe.CreateWireframeUseCase;
import com.marcosmoreira.domainmodelstudio.application.wireframe.RemoveWireframeItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.wireframe.UpdateWireframeComponentUseCase;
import com.marcosmoreira.domainmodelstudio.application.wireframe.UpdateWireframeScreenUseCase;
import com.marcosmoreira.domainmodelstudio.application.wireframe.ValidateWireframeUseCase;
import java.util.Objects;

/** Fachada de edición y validación de Wireframes administrativos. */
public final class WireframeApplicationServices {

    private final CreateWireframeUseCase createWireframeUseCase;
    private final AddWireframeScreenUseCase addWireframeScreenUseCase;
    private final AddWireframeComponentUseCase addWireframeComponentUseCase;
    private final UpdateWireframeScreenUseCase updateWireframeScreenUseCase;
    private final UpdateWireframeComponentUseCase updateWireframeComponentUseCase;
    private final RemoveWireframeItemUseCase removeWireframeItemUseCase;
    private final ValidateWireframeUseCase validateWireframeUseCase;

    public WireframeApplicationServices(
            CreateWireframeUseCase createWireframeUseCase,
            AddWireframeScreenUseCase addWireframeScreenUseCase,
            AddWireframeComponentUseCase addWireframeComponentUseCase,
            UpdateWireframeScreenUseCase updateWireframeScreenUseCase,
            UpdateWireframeComponentUseCase updateWireframeComponentUseCase,
            RemoveWireframeItemUseCase removeWireframeItemUseCase,
            ValidateWireframeUseCase validateWireframeUseCase
    ) {
        this.createWireframeUseCase = Objects.requireNonNull(createWireframeUseCase, "createWireframeUseCase");
        this.addWireframeScreenUseCase = Objects.requireNonNull(addWireframeScreenUseCase, "addWireframeScreenUseCase");
        this.addWireframeComponentUseCase = Objects.requireNonNull(addWireframeComponentUseCase, "addWireframeComponentUseCase");
        this.updateWireframeScreenUseCase = Objects.requireNonNull(updateWireframeScreenUseCase, "updateWireframeScreenUseCase");
        this.updateWireframeComponentUseCase = Objects.requireNonNull(updateWireframeComponentUseCase, "updateWireframeComponentUseCase");
        this.removeWireframeItemUseCase = Objects.requireNonNull(removeWireframeItemUseCase, "removeWireframeItemUseCase");
        this.validateWireframeUseCase = Objects.requireNonNull(validateWireframeUseCase, "validateWireframeUseCase");
    }

    public CreateWireframeUseCase createWireframeUseCase() {
        return createWireframeUseCase;
    }

    public AddWireframeScreenUseCase addWireframeScreenUseCase() {
        return addWireframeScreenUseCase;
    }

    public AddWireframeComponentUseCase addWireframeComponentUseCase() {
        return addWireframeComponentUseCase;
    }

    public UpdateWireframeScreenUseCase updateWireframeScreenUseCase() {
        return updateWireframeScreenUseCase;
    }

    public UpdateWireframeComponentUseCase updateWireframeComponentUseCase() {
        return updateWireframeComponentUseCase;
    }

    public RemoveWireframeItemUseCase removeWireframeItemUseCase() {
        return removeWireframeItemUseCase;
    }

    public ValidateWireframeUseCase validateWireframeUseCase() {
        return validateWireframeUseCase;
    }

}
