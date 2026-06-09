package com.marcosmoreira.domainmodelstudio.application.services;

import com.marcosmoreira.domainmodelstudio.application.behavior.AddBehaviorEdgeUseCase;
import com.marcosmoreira.domainmodelstudio.application.behavior.AddBehaviorNodeUseCase;
import com.marcosmoreira.domainmodelstudio.application.behavior.CreateBehaviorDiagramUseCase;
import com.marcosmoreira.domainmodelstudio.application.behavior.RemoveBehaviorItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.behavior.UpdateBehaviorEdgeUseCase;
import com.marcosmoreira.domainmodelstudio.application.behavior.UpdateBehaviorNodeUseCase;
import com.marcosmoreira.domainmodelstudio.application.behavior.ValidateBehaviorDiagramUseCase;
import java.util.Objects;

/** Fachada de edición y validación de diagramas de comportamiento. */
public final class BehaviorApplicationServices {

    private final CreateBehaviorDiagramUseCase createBehaviorDiagramUseCase;
    private final AddBehaviorNodeUseCase addBehaviorNodeUseCase;
    private final AddBehaviorEdgeUseCase addBehaviorEdgeUseCase;
    private final UpdateBehaviorNodeUseCase updateBehaviorNodeUseCase;
    private final UpdateBehaviorEdgeUseCase updateBehaviorEdgeUseCase;
    private final RemoveBehaviorItemUseCase removeBehaviorItemUseCase;
    private final ValidateBehaviorDiagramUseCase validateBehaviorDiagramUseCase;

    public BehaviorApplicationServices(
            CreateBehaviorDiagramUseCase createBehaviorDiagramUseCase,
            AddBehaviorNodeUseCase addBehaviorNodeUseCase,
            AddBehaviorEdgeUseCase addBehaviorEdgeUseCase,
            UpdateBehaviorNodeUseCase updateBehaviorNodeUseCase,
            UpdateBehaviorEdgeUseCase updateBehaviorEdgeUseCase,
            RemoveBehaviorItemUseCase removeBehaviorItemUseCase,
            ValidateBehaviorDiagramUseCase validateBehaviorDiagramUseCase
    ) {
        this.createBehaviorDiagramUseCase = Objects.requireNonNull(createBehaviorDiagramUseCase, "createBehaviorDiagramUseCase");
        this.addBehaviorNodeUseCase = Objects.requireNonNull(addBehaviorNodeUseCase, "addBehaviorNodeUseCase");
        this.addBehaviorEdgeUseCase = Objects.requireNonNull(addBehaviorEdgeUseCase, "addBehaviorEdgeUseCase");
        this.updateBehaviorNodeUseCase = Objects.requireNonNull(updateBehaviorNodeUseCase, "updateBehaviorNodeUseCase");
        this.updateBehaviorEdgeUseCase = Objects.requireNonNull(updateBehaviorEdgeUseCase, "updateBehaviorEdgeUseCase");
        this.removeBehaviorItemUseCase = Objects.requireNonNull(removeBehaviorItemUseCase, "removeBehaviorItemUseCase");
        this.validateBehaviorDiagramUseCase = Objects.requireNonNull(validateBehaviorDiagramUseCase, "validateBehaviorDiagramUseCase");
    }

    public CreateBehaviorDiagramUseCase createBehaviorDiagramUseCase() {
        return createBehaviorDiagramUseCase;
    }

    public AddBehaviorNodeUseCase addBehaviorNodeUseCase() {
        return addBehaviorNodeUseCase;
    }

    public AddBehaviorEdgeUseCase addBehaviorEdgeUseCase() {
        return addBehaviorEdgeUseCase;
    }

    public UpdateBehaviorNodeUseCase updateBehaviorNodeUseCase() {
        return updateBehaviorNodeUseCase;
    }

    public UpdateBehaviorEdgeUseCase updateBehaviorEdgeUseCase() {
        return updateBehaviorEdgeUseCase;
    }

    public RemoveBehaviorItemUseCase removeBehaviorItemUseCase() {
        return removeBehaviorItemUseCase;
    }

    public ValidateBehaviorDiagramUseCase validateBehaviorDiagramUseCase() {
        return validateBehaviorDiagramUseCase;
    }

}
