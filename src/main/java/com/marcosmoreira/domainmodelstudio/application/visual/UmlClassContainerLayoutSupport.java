package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import java.util.List;
import java.util.Objects;

/** Ajustes de contención para paquetes/módulos de UML Clases. */
public final class UmlClassContainerLayoutSupport {

    private static final ContainerPadding MODULE_PADDING = ContainerPadding.of(48.0, 96.0, 52.0, 52.0);
    private final ContainerAutoExpansionPolicy containerPolicy = new ContainerAutoExpansionPolicy();


    /** Ajusta todos los módulos UML para envolver sus clases visibles con margen consistente. */
    public DiagramProject fitAllModules(DiagramProject project, List<UmlClassNode> classes) {
        Objects.requireNonNull(project, "project");
        DiagramProject updated = project;
        for (String moduleId : moduleIds(classes)) {
            updated = expandModule(updated, moduleId, classes);
        }
        return updated;
    }

    private static List<String> moduleIds(List<UmlClassNode> classes) {
        if (classes == null) {
            return List.of();
        }
        return classes.stream()
                .map(UmlClassNode::moduleId)
                .map(UmlClassContainerLayoutSupport::normalize)
                .filter(id -> !id.isBlank())
                .distinct()
                .toList();
    }

    public DiagramProject expandModuleForClass(DiagramProject project, String classId, List<UmlClassNode> classes) {
        Objects.requireNonNull(project, "project");
        UmlClassNode movedClass = classById(classId, classes);
        if (movedClass == null || movedClass.moduleId().isBlank()) {
            return project;
        }
        return expandModule(project, movedClass.moduleId(), classes);
    }

    public DiagramProject expandModule(DiagramProject project, String moduleId, List<UmlClassNode> classes) {
        String normalizedModuleId = normalize(moduleId);
        if (normalizedModuleId.isBlank()) {
            return project;
        }
        return containerPolicy.fitContainerToChildren(
                project,
                VisualElementLayoutIds.umlModule(normalizedModuleId),
                classes.stream()
                        .filter(node -> node.moduleId().equals(normalizedModuleId))
                        .map(node -> VisualElementLayoutIds.umlClass(node.id()))
                        .toList(),
                MODULE_PADDING,
                320.0,
                210.0);
    }

    private static UmlClassNode classById(String classId, List<UmlClassNode> classes) {
        String normalized = normalize(classId);
        if (classes == null || normalized.isBlank()) {
            return null;
        }
        return classes.stream().filter(node -> node.id().equals(normalized)).findFirst().orElse(null);
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
