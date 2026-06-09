package com.marcosmoreira.domainmodelstudio.application.modulemap;

import com.marcosmoreira.domainmodelstudio.domain.modulemap.DependencyKind;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import java.util.Objects;

/** Actualiza una dependencia funcional entre módulos. */
public final class UpdateModuleMapDependencyUseCase {

    public ModuleMapDocument update(
            ModuleMapDocument document,
            String dependencyId,
            String sourceModuleId,
            String targetModuleId,
            DependencyKind kind,
            String description,
            String notes
    ) {
        Objects.requireNonNull(document, "document");
        ModuleDependency current = document.dependencies().stream()
                .filter(dependency -> dependency.id().equals(dependencyId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No existe dependencia: " + dependencyId));
        document.moduleById(sourceModuleId)
                .orElseThrow(() -> new IllegalArgumentException("No existe módulo origen: " + sourceModuleId));
        document.moduleById(targetModuleId)
                .orElseThrow(() -> new IllegalArgumentException("No existe módulo destino: " + targetModuleId));
        return document.withUpdatedDependency(current.withDetails(sourceModuleId, targetModuleId, kind, description, notes));
    }
}
