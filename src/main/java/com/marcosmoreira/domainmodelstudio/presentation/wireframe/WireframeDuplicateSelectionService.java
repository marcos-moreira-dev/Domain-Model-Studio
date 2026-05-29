package com.marcosmoreira.domainmodelstudio.presentation.wireframe;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutService;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponent;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreen;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Duplica pantallas o componentes sin ensuciar el ViewModel del editor. */
final class WireframeDuplicateSelectionService {

    private static final double COMPONENT_OFFSET = 36.0;
    private static final double SCREEN_OFFSET = 72.0;

    private final VisualLayoutService visualLayoutService;

    WireframeDuplicateSelectionService(VisualLayoutService visualLayoutService) {
        this.visualLayoutService = Objects.requireNonNull(visualLayoutService, "visualLayoutService");
    }

    Result duplicate(
            DiagramProject project,
            WireframeDocument document,
            WireframeScreen selectedScreen,
            WireframeComponent selectedComponent
    ) {
        Objects.requireNonNull(project, "project");
        Objects.requireNonNull(document, "document");
        if (selectedComponent != null) {
            return duplicateComponent(project, document, selectedComponent);
        }
        if (selectedScreen != null) {
            return duplicateScreen(project, document, selectedScreen);
        }
        throw new IllegalArgumentException("Selecciona pantalla o componente para duplicar.");
    }

    private Result duplicateComponent(DiagramProject project, WireframeDocument document, WireframeComponent original) {
        String newId = document.nextComponentId();
        WireframeComponent copy = new WireframeComponent(
                newId,
                original.screenId(),
                original.kind(),
                original.displayName() + " copia",
                original.orderIndex() + 1,
                original.dataBinding(),
                original.behavior(),
                original.notes());
        NodeLayout sourceLayout = componentLayout(project, original.id());
        WireframeDocument updated = document.withComponent(copy);
        DiagramProject updatedProject = visualLayoutService.ensureVisualLayout(project.withWireframe(updated));
        updatedProject = visualLayoutService.moveNodeTo(
                updatedProject,
                VisualElementLayoutIds.wireframeComponent(newId),
                sourceLayout.x() + COMPONENT_OFFSET,
                sourceLayout.y() + COMPONENT_OFFSET);
        return new Result(updatedProject, updated, null, newId, "Componente duplicado.");
    }

    private Result duplicateScreen(DiagramProject project, WireframeDocument document, WireframeScreen original) {
        String newScreenId = document.nextScreenId();
        NodeLayout screenLayout = screenLayout(project, original.id());
        WireframeDocument updated = document.withScreen(new WireframeScreen(
                newScreenId,
                original.displayName() + " copia",
                original.moduleName(),
                original.purpose(),
                original.notes()));
        List<ComponentCopy> copies = new ArrayList<>();
        for (WireframeComponent component : document.components()) {
            if (component.screenId().equals(original.id())) {
                ComponentCopy copy = copyComponent(updated, newScreenId, component);
                updated = updated.withComponent(copy.component());
                copies.add(copy);
            }
        }
        DiagramProject updatedProject = visualLayoutService.ensureVisualLayout(project.withWireframe(updated));
        updatedProject = visualLayoutService.moveNodeTo(
                updatedProject,
                VisualElementLayoutIds.wireframeScreen(newScreenId),
                screenLayout.x() + SCREEN_OFFSET,
                screenLayout.y() + SCREEN_OFFSET);
        for (ComponentCopy copy : copies) {
            NodeLayout sourceLayout = componentLayout(project, copy.sourceComponentId());
            updatedProject = visualLayoutService.moveNodeTo(
                    updatedProject,
                    VisualElementLayoutIds.wireframeComponent(copy.component().id()),
                    sourceLayout.x() + SCREEN_OFFSET,
                    sourceLayout.y() + SCREEN_OFFSET);
        }
        return new Result(updatedProject, updated, newScreenId, null, "Pantalla duplicada con sus componentes.");
    }

    private static ComponentCopy copyComponent(WireframeDocument document, String newScreenId, WireframeComponent source) {
        WireframeComponent copy = new WireframeComponent(
                document.nextComponentId(),
                newScreenId,
                source.kind(),
                source.displayName(),
                source.orderIndex(),
                source.dataBinding(),
                source.behavior(),
                source.notes());
        return new ComponentCopy(source.id(), copy);
    }

    private NodeLayout screenLayout(DiagramProject project, String screenId) {
        return visualLayoutService.nodeLayout(project, VisualElementLayoutIds.wireframeScreen(screenId))
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para la pantalla: " + screenId));
    }

    private NodeLayout componentLayout(DiagramProject project, String componentId) {
        return visualLayoutService.nodeLayout(project, VisualElementLayoutIds.wireframeComponent(componentId))
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para el componente: " + componentId));
    }

    record Result(
            DiagramProject project,
            WireframeDocument document,
            String selectedScreenId,
            String selectedComponentId,
            String status
    ) { }

    private record ComponentCopy(String sourceComponentId, WireframeComponent component) { }
}
