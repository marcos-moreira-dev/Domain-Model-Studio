package com.marcosmoreira.domainmodelstudio.application.modulemap;

import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import java.util.Objects;

/** Elimina módulos o dependencias del mapa de módulos. */
public final class RemoveModuleMapItemUseCase {

    public ModuleMapDocument removeModule(ModuleMapDocument document, String moduleId) {
        Objects.requireNonNull(document, "document");
        return document.withoutModule(moduleId);
    }

    public ModuleMapDocument removeDependency(ModuleMapDocument document, String dependencyId) {
        Objects.requireNonNull(document, "document");
        return document.withoutDependency(dependencyId);
    }
}
