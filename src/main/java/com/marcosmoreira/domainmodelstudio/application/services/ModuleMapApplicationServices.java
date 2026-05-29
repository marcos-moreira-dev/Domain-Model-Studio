package com.marcosmoreira.domainmodelstudio.application.services;

import com.marcosmoreira.domainmodelstudio.application.modulemap.AddModuleMapDependencyUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.AddModuleMapModuleUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.CreateModuleMapUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.RemoveModuleMapItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.UpdateModuleMapDependencyUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.UpdateModuleMapModuleUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.ValidateModuleMapUseCase;
import java.util.Objects;

/** Fachada de casos de uso del Mapa de módulos.
 *
 * <p>Separa el dominio modular de los servicios visuales y de exportación.</p> */
public final class ModuleMapApplicationServices {

    private final CreateModuleMapUseCase createModuleMapUseCase;
    private final AddModuleMapModuleUseCase addModuleMapModuleUseCase;
    private final AddModuleMapDependencyUseCase addModuleMapDependencyUseCase;
    private final UpdateModuleMapModuleUseCase updateModuleMapModuleUseCase;
    private final UpdateModuleMapDependencyUseCase updateModuleMapDependencyUseCase;
    private final RemoveModuleMapItemUseCase removeModuleMapItemUseCase;
    private final ValidateModuleMapUseCase validateModuleMapUseCase;

    public ModuleMapApplicationServices(
            CreateModuleMapUseCase createModuleMapUseCase,
            AddModuleMapModuleUseCase addModuleMapModuleUseCase,
            AddModuleMapDependencyUseCase addModuleMapDependencyUseCase,
            UpdateModuleMapModuleUseCase updateModuleMapModuleUseCase,
            UpdateModuleMapDependencyUseCase updateModuleMapDependencyUseCase,
            RemoveModuleMapItemUseCase removeModuleMapItemUseCase,
            ValidateModuleMapUseCase validateModuleMapUseCase
    ) {
        this.createModuleMapUseCase = Objects.requireNonNull(createModuleMapUseCase, "createModuleMapUseCase");
        this.addModuleMapModuleUseCase = Objects.requireNonNull(addModuleMapModuleUseCase, "addModuleMapModuleUseCase");
        this.addModuleMapDependencyUseCase = Objects.requireNonNull(addModuleMapDependencyUseCase, "addModuleMapDependencyUseCase");
        this.updateModuleMapModuleUseCase = Objects.requireNonNull(updateModuleMapModuleUseCase, "updateModuleMapModuleUseCase");
        this.updateModuleMapDependencyUseCase = Objects.requireNonNull(updateModuleMapDependencyUseCase, "updateModuleMapDependencyUseCase");
        this.removeModuleMapItemUseCase = Objects.requireNonNull(removeModuleMapItemUseCase, "removeModuleMapItemUseCase");
        this.validateModuleMapUseCase = Objects.requireNonNull(validateModuleMapUseCase, "validateModuleMapUseCase");
    }

    public CreateModuleMapUseCase createModuleMapUseCase() {
        return createModuleMapUseCase;
    }

    public AddModuleMapModuleUseCase addModuleMapModuleUseCase() {
        return addModuleMapModuleUseCase;
    }

    public AddModuleMapDependencyUseCase addModuleMapDependencyUseCase() {
        return addModuleMapDependencyUseCase;
    }

    public UpdateModuleMapModuleUseCase updateModuleMapModuleUseCase() {
        return updateModuleMapModuleUseCase;
    }

    public UpdateModuleMapDependencyUseCase updateModuleMapDependencyUseCase() {
        return updateModuleMapDependencyUseCase;
    }

    public RemoveModuleMapItemUseCase removeModuleMapItemUseCase() {
        return removeModuleMapItemUseCase;
    }

    public ValidateModuleMapUseCase validateModuleMapUseCase() {
        return validateModuleMapUseCase;
    }

}
