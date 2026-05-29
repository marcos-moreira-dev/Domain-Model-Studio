package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import java.util.List;
import java.util.Objects;

/** Ajustes de contención para el mapa de módulos administrativos. */
public final class ModuleMapContainerLayoutSupport {

    private static final ContainerPadding MODULE_PADDING = ContainerPadding.of(48.0, 86.0, 48.0, 52.0);
    private final ContainerAutoExpansionPolicy containerPolicy = new ContainerAutoExpansionPolicy();

    public DiagramProject expandAncestors(DiagramProject project, String movedModuleId, List<ModuleNode> modules) {
        Objects.requireNonNull(project, "project");
        String currentId = normalize(movedModuleId);
        DiagramProject updated = project;
        while (!currentId.isBlank()) {
            ModuleNode current = moduleById(currentId, modules);
            if (current == null || current.parentId().isBlank()) {
                return updated;
            }
            String parentId = current.parentId();
            List<DiagramElementId> siblingIds = modules.stream()
                    .filter(module -> module.parentId().equals(parentId))
                    .map(module -> VisualElementLayoutIds.module(module.id()))
                    .toList();
            updated = containerPolicy.fitContainerToChildren(
                    updated,
                    VisualElementLayoutIds.module(parentId),
                    siblingIds,
                    MODULE_PADDING,
                    340.0,
                    210.0);
            currentId = parentId;
        }
        return updated;
    }

    private static ModuleNode moduleById(String moduleId, List<ModuleNode> modules) {
        if (modules == null) {
            return null;
        }
        return modules.stream().filter(module -> module.id().equals(moduleId)).findFirst().orElse(null);
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
