package com.marcosmoreira.domainmodelstudio.application.modulemap;

import com.marcosmoreira.domainmodelstudio.domain.modulemap.DependencyKind;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import java.util.Objects;

/** Agrega dependencias funcionales entre módulos. */
public final class AddModuleMapDependencyUseCase {

    public ModuleMapDocument add(ModuleMapDocument document, String sourceModuleId, String targetModuleId) {
        Objects.requireNonNull(document, "document");
        String source = normalize(sourceModuleId);
        String target = normalize(targetModuleId);
        if (source.isBlank() || target.isBlank()) {
            throw new IllegalArgumentException("Selecciona módulo origen y destino para crear dependencia.");
        }
        document.moduleById(source).orElseThrow(() -> new IllegalArgumentException("No existe módulo origen: " + source));
        document.moduleById(target).orElseThrow(() -> new IllegalArgumentException("No existe módulo destino: " + target));
        String id = uniqueDependencyId(document, source + "_to_" + target);
        return document.withDependency(new ModuleDependency(id, source, target, DependencyKind.USES, "", ""));
    }

    private String uniqueDependencyId(ModuleMapDocument document, String base) {
        String normalizedBase = base == null || base.isBlank() ? "dependencia" : base;
        String candidate = normalizedBase;
        int counter = 2;
        while (containsDependencyId(document, candidate)) {
            candidate = normalizedBase + "_" + counter++;
        }
        return candidate;
    }

    private boolean containsDependencyId(ModuleMapDocument document, String candidate) {
        for (ModuleDependency dependency : document.dependencies()) {
            if (dependency.id().equals(candidate)) {
                return true;
            }
        }
        return false;
    }

    private String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
