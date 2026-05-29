package com.marcosmoreira.domainmodelstudio.application.modulemap;

import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;

/** Crea un mapa de módulos vacío. */
public final class CreateModuleMapUseCase {

    public ModuleMapDocument createBlank(String projectName) {
        return ModuleMapDocument.blank(projectName);
    }
}
