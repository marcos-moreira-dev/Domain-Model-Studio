package com.marcosmoreira.domainmodelstudio.application.modulemap;

import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleKind;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleStatus;
import java.util.List;
import java.util.Objects;

/** Actualiza datos descriptivos de un módulo. */
public final class UpdateModuleMapModuleUseCase {

    public ModuleMapDocument update(
            ModuleMapDocument document,
            String moduleId,
            String displayName,
            String parentId,
            ModuleKind kind,
            ModuleStatus status,
            String responsibility,
            String description,
            List<String> tags,
            String notes
    ) {
        Objects.requireNonNull(document, "document");
        ModuleNode current = document.moduleById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("No existe módulo para actualizar: " + moduleId));
        String normalizedParent = parentId == null ? "" : parentId.strip();
        if (!normalizedParent.isBlank() && normalizedParent.equals(current.id())) {
            throw new IllegalArgumentException("Un módulo no puede ser padre de sí mismo.");
        }
        if (!normalizedParent.isBlank()) {
            document.moduleById(normalizedParent)
                    .orElseThrow(() -> new IllegalArgumentException("No existe el módulo padre: " + normalizedParent));
        }
        ModuleNode updated = current.withDetails(displayName, normalizedParent, kind, status,
                responsibility, description, tags, notes);
        return document.withUpdatedModule(updated);
    }
}
