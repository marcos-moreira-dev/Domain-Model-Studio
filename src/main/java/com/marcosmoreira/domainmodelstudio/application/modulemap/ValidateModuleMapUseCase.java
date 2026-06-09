package com.marcosmoreira.domainmodelstudio.application.modulemap;

import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/** Revisa consistencia básica del mapa de módulos. */
public final class ValidateModuleMapUseCase {

    public ModuleMapValidationResult validate(ModuleMapDocument document) {
        Objects.requireNonNull(document, "document");
        ArrayList<String> warnings = new ArrayList<>();
        if (document.modules().isEmpty()) {
            warnings.add("El mapa todavía no tiene módulos.");
        }
        Set<String> names = new HashSet<>();
        for (ModuleNode module : document.modules()) {
            if (!names.add(module.displayName().toLowerCase(java.util.Locale.ROOT))) {
                warnings.add("Nombre de módulo repetido: " + module.displayName());
            }
            if (module.responsibility().isBlank() && module.description().isBlank()) {
                warnings.add("El módulo '" + module.displayName() + "' no tiene responsabilidad ni descripción.");
            }
            if (!module.parentId().isBlank() && document.moduleById(module.parentId()).isEmpty()) {
                warnings.add("El módulo '" + module.displayName() + "' apunta a un módulo padre inexistente.");
            }
        }
        for (ModuleDependency dependency : document.dependencies()) {
            if (document.moduleById(dependency.sourceModuleId()).isEmpty()) {
                warnings.add("Dependencia con origen inexistente: " + dependency.sourceModuleId());
            }
            if (document.moduleById(dependency.targetModuleId()).isEmpty()) {
                warnings.add("Dependencia con destino inexistente: " + dependency.targetModuleId());
            }
        }
        return new ModuleMapValidationResult(warnings);
    }
}
