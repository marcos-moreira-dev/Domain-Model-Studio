package com.marcosmoreira.domainmodelstudio.application.services;

import com.marcosmoreira.domainmodelstudio.application.architecture.AddArchitectureEdgeUseCase;
import com.marcosmoreira.domainmodelstudio.application.architecture.AddArchitectureNodeUseCase;
import com.marcosmoreira.domainmodelstudio.application.architecture.CreateArchitectureDiagramUseCase;
import com.marcosmoreira.domainmodelstudio.application.architecture.RemoveArchitectureItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.architecture.UpdateArchitectureEdgeUseCase;
import com.marcosmoreira.domainmodelstudio.application.architecture.UpdateArchitectureNodeUseCase;
import com.marcosmoreira.domainmodelstudio.application.architecture.ValidateArchitectureDiagramUseCase;
import java.util.Objects;

/** Fachada de edición y validación de diagramas de arquitectura C4/despliegue. */
public final class ArchitectureApplicationServices {

    private final CreateArchitectureDiagramUseCase createArchitectureDiagramUseCase;
    private final AddArchitectureNodeUseCase addArchitectureNodeUseCase;
    private final AddArchitectureEdgeUseCase addArchitectureEdgeUseCase;
    private final UpdateArchitectureNodeUseCase updateArchitectureNodeUseCase;
    private final UpdateArchitectureEdgeUseCase updateArchitectureEdgeUseCase;
    private final RemoveArchitectureItemUseCase removeArchitectureItemUseCase;
    private final ValidateArchitectureDiagramUseCase validateArchitectureDiagramUseCase;

    public ArchitectureApplicationServices(
            CreateArchitectureDiagramUseCase createArchitectureDiagramUseCase,
            AddArchitectureNodeUseCase addArchitectureNodeUseCase,
            AddArchitectureEdgeUseCase addArchitectureEdgeUseCase,
            UpdateArchitectureNodeUseCase updateArchitectureNodeUseCase,
            UpdateArchitectureEdgeUseCase updateArchitectureEdgeUseCase,
            RemoveArchitectureItemUseCase removeArchitectureItemUseCase,
            ValidateArchitectureDiagramUseCase validateArchitectureDiagramUseCase
    ) {
        this.createArchitectureDiagramUseCase = Objects.requireNonNull(createArchitectureDiagramUseCase, "createArchitectureDiagramUseCase");
        this.addArchitectureNodeUseCase = Objects.requireNonNull(addArchitectureNodeUseCase, "addArchitectureNodeUseCase");
        this.addArchitectureEdgeUseCase = Objects.requireNonNull(addArchitectureEdgeUseCase, "addArchitectureEdgeUseCase");
        this.updateArchitectureNodeUseCase = Objects.requireNonNull(updateArchitectureNodeUseCase, "updateArchitectureNodeUseCase");
        this.updateArchitectureEdgeUseCase = Objects.requireNonNull(updateArchitectureEdgeUseCase, "updateArchitectureEdgeUseCase");
        this.removeArchitectureItemUseCase = Objects.requireNonNull(removeArchitectureItemUseCase, "removeArchitectureItemUseCase");
        this.validateArchitectureDiagramUseCase = Objects.requireNonNull(validateArchitectureDiagramUseCase, "validateArchitectureDiagramUseCase");
    }

    public CreateArchitectureDiagramUseCase createArchitectureDiagramUseCase() {
        return createArchitectureDiagramUseCase;
    }

    public AddArchitectureNodeUseCase addArchitectureNodeUseCase() {
        return addArchitectureNodeUseCase;
    }

    public AddArchitectureEdgeUseCase addArchitectureEdgeUseCase() {
        return addArchitectureEdgeUseCase;
    }

    public UpdateArchitectureNodeUseCase updateArchitectureNodeUseCase() {
        return updateArchitectureNodeUseCase;
    }

    public UpdateArchitectureEdgeUseCase updateArchitectureEdgeUseCase() {
        return updateArchitectureEdgeUseCase;
    }

    public RemoveArchitectureItemUseCase removeArchitectureItemUseCase() {
        return removeArchitectureItemUseCase;
    }

    public ValidateArchitectureDiagramUseCase validateArchitectureDiagramUseCase() {
        return validateArchitectureDiagramUseCase;
    }

}
