package com.marcosmoreira.domainmodelstudio.presentation.visualtransfer;

import static com.marcosmoreira.domainmodelstudio.presentation.visualtransfer.ProjectVisualSelectionTransferSupport.*;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenNode;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransition;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponent;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreen;
import com.marcosmoreira.domainmodelstudio.presentation.visualtransfer.ProjectVisualSelectionTransferService.PasteResult;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/** Pegado de selecciones para diagramas estructurados visuales. */
final class ProjectVisualSelectionPasteStructured {

    private ProjectVisualSelectionPasteStructured() {
    }

static PasteResult pasteModuleMap(DiagramProject targetProject, ProjectVisualSelectionTransferPayload payload) {
        ModuleMapDocument source = payload.sourceProject().moduleMap().orElse(null);
        ModuleMapDocument document = targetProject.moduleMap().orElse(null);
        if (source == null || document == null) return PasteResult.empty(targetProject);
        Set<String> selectedModuleIds = rawIds(payload.selectedNodeIds(), "module:");
        Set<String> selectedDependencyIds = rawIds(payload.selectedConnectorIds(), "dependency:");
        for (ModuleDependency dependency : source.dependencies()) if (selectedDependencyIds.contains(dependency.id())) selectedModuleIds = with(selectedModuleIds, dependency.sourceModuleId(), dependency.targetModuleId());
        for (ModuleDependency dependency : source.dependencies()) if (selectedModuleIds.contains(dependency.sourceModuleId()) && selectedModuleIds.contains(dependency.targetModuleId())) selectedDependencyIds = with(selectedDependencyIds, dependency.id());
        Map<String, String> moduleIdMap = new LinkedHashMap<>();
        Set<String> occupiedModuleIds = idsOfModules(document);
        Set<String> occupiedDependencyIds = idsOfDependencies(document);
        Set<String> pending = new LinkedHashSet<>();
        for (ModuleNode module : source.modules()) {
            if (!selectedModuleIds.contains(module.id())) continue;
            String newId = uniqueRawId(module.id(), occupiedModuleIds, pending);
            pending.add(newId);
            occupiedModuleIds.add(newId);
            moduleIdMap.put(module.id(), newId);
            String parentId = moduleIdMap.getOrDefault(module.parentId(), document.moduleById(module.parentId()).isPresent() ? module.parentId() : "");
            document = document.withModule(new ModuleNode(newId, copyName(module.displayName()), parentId, module.kind(), module.status(), module.responsibility(), module.description(), module.tags(), module.notes()));
        }
        Map<String, String> dependencyIdMap = new LinkedHashMap<>();
        for (ModuleDependency dependency : source.dependencies()) {
            if (!selectedDependencyIds.contains(dependency.id())) continue;
            String sourceId = moduleIdMap.get(dependency.sourceModuleId());
            String targetId = moduleIdMap.get(dependency.targetModuleId());
            if (sourceId == null || targetId == null) continue;
            String newId = uniqueRawId(dependency.id(), occupiedDependencyIds, pending);
            pending.add(newId);
            occupiedDependencyIds.add(newId);
            dependencyIdMap.put(dependency.id(), newId);
            document = document.withDependency(new ModuleDependency(newId, sourceId, targetId, dependency.kind(), dependency.description(), dependency.notes()));
        }
        DiagramLayout layout = targetProject.layouts().activeLayout();
        layout = copyNodeLayouts(layout, payload, id -> moduleIdMap.get(rawAfter(id, "module:")), id -> VisualElementLayoutIds.module(id).value());
        layout = copyConnectorLayouts(layout, payload, id -> dependencyIdMap.get(rawAfter(id, "dependency:")), id -> moduleIdMap.get(rawAfter(id, "module:")), id -> VisualElementLayoutIds.dependency(id).value(), id -> VisualElementLayoutIds.module(id).value());
        DiagramProject updated = targetProject.withModuleMap(document).withLayouts(targetProject.layouts().withLayout(layout));
        return new PasteResult(updated, moduleIdMap.size(), dependencyIdMap.size(), "Selección pegada en mapa de módulos.");
    }

static PasteResult pasteScreenFlow(DiagramProject targetProject, ProjectVisualSelectionTransferPayload payload) {
        ScreenFlowDocument source = payload.sourceProject().screenFlow().orElse(null);
        ScreenFlowDocument document = targetProject.screenFlow().orElse(null);
        if (source == null || document == null) return PasteResult.empty(targetProject);
        Set<String> selectedScreenIds = rawIds(payload.selectedNodeIds(), "screen:");
        Set<String> selectedTransitionIds = rawIds(payload.selectedConnectorIds(), "transition:");
        for (ScreenTransition transition : source.transitions()) if (selectedTransitionIds.contains(transition.id())) selectedScreenIds = with(selectedScreenIds, transition.sourceScreenId(), transition.targetScreenId());
        for (ScreenTransition transition : source.transitions()) if (selectedScreenIds.contains(transition.sourceScreenId()) && selectedScreenIds.contains(transition.targetScreenId())) selectedTransitionIds = with(selectedTransitionIds, transition.id());
        Map<String, String> screenIdMap = new LinkedHashMap<>();
        Set<String> occupiedScreenIds = idsOfScreens(document);
        Set<String> occupiedTransitionIds = idsOfTransitions(document);
        Set<String> pending = new LinkedHashSet<>();
        for (ScreenNode screen : source.screens()) {
            if (!selectedScreenIds.contains(screen.id())) continue;
            String newId = uniqueRawId(screen.id(), occupiedScreenIds, pending);
            pending.add(newId);
            occupiedScreenIds.add(newId);
            screenIdMap.put(screen.id(), newId);
            document = document.withScreen(new ScreenNode(newId, copyName(screen.displayName()), screen.kind(), screen.moduleName(), screen.route(), screen.purpose(), screen.notes()));
        }
        Map<String, String> transitionIdMap = new LinkedHashMap<>();
        for (ScreenTransition transition : source.transitions()) {
            if (!selectedTransitionIds.contains(transition.id())) continue;
            String sourceId = screenIdMap.get(transition.sourceScreenId());
            String targetId = screenIdMap.get(transition.targetScreenId());
            if (sourceId == null || targetId == null || sourceId.equals(targetId)) continue;
            String newId = uniqueRawId(transition.id(), occupiedTransitionIds, pending);
            pending.add(newId);
            occupiedTransitionIds.add(newId);
            transitionIdMap.put(transition.id(), newId);
            document = document.withTransition(new ScreenTransition(newId, sourceId, targetId, transition.kind(), transition.trigger(), transition.condition(), transition.notes()));
        }
        DiagramLayout layout = targetProject.layouts().activeLayout();
        layout = copyNodeLayouts(layout, payload, id -> screenIdMap.get(rawAfter(id, "screen:")), id -> VisualElementLayoutIds.screen(id).value());
        layout = copyConnectorLayouts(layout, payload, id -> transitionIdMap.get(rawAfter(id, "transition:")), id -> screenIdMap.get(rawAfter(id, "screen:")), id -> VisualElementLayoutIds.transition(id).value(), id -> VisualElementLayoutIds.screen(id).value());
        DiagramProject updated = targetProject.withScreenFlow(document).withLayouts(targetProject.layouts().withLayout(layout));
        return new PasteResult(updated, screenIdMap.size(), transitionIdMap.size(), "Selección pegada en flujo de pantallas.");
    }

static PasteResult pasteWireframe(DiagramProject targetProject, ProjectVisualSelectionTransferPayload payload) {
        WireframeDocument source = payload.sourceProject().wireframe().orElse(null);
        WireframeDocument document = targetProject.wireframe().orElse(null);
        if (source == null || document == null) return PasteResult.empty(targetProject);
        Set<String> selectedScreenIds = rawIds(payload.selectedNodeIds(), "wireframe-screen:");
        Set<String> selectedComponentIds = rawIds(payload.selectedNodeIds(), "wireframe-component:");
        for (WireframeComponent component : source.components()) if (selectedComponentIds.contains(component.id())) selectedScreenIds = with(selectedScreenIds, component.screenId());
        for (WireframeComponent component : source.components()) if (selectedScreenIds.contains(component.screenId())) selectedComponentIds = with(selectedComponentIds, component.id());
        Map<String, String> screenIdMap = new LinkedHashMap<>();
        Map<String, String> componentIdMap = new LinkedHashMap<>();
        Set<String> occupiedScreenIds = idsOfWireframeScreens(document);
        Set<String> occupiedComponentIds = idsOfWireframeComponents(document);
        Set<String> pending = new LinkedHashSet<>();
        for (WireframeScreen screen : source.screens()) {
            if (!selectedScreenIds.contains(screen.id())) continue;
            String newId = uniqueRawId(screen.id(), occupiedScreenIds, pending);
            pending.add(newId);
            occupiedScreenIds.add(newId);
            screenIdMap.put(screen.id(), newId);
            document = document.withScreen(new WireframeScreen(newId, copyName(screen.displayName()), screen.moduleName(), screen.purpose(), screen.notes()));
        }
        for (WireframeComponent component : source.components()) {
            if (!selectedComponentIds.contains(component.id())) continue;
            String screenId = screenIdMap.get(component.screenId());
            if (screenId == null) continue;
            String newId = uniqueRawId(component.id(), occupiedComponentIds, pending);
            pending.add(newId);
            occupiedComponentIds.add(newId);
            componentIdMap.put(component.id(), newId);
            document = document.withComponent(new WireframeComponent(newId, screenId, component.kind(), copyName(component.displayName()), component.orderIndex(), component.dataBinding(), component.behavior(), component.notes()));
        }
        DiagramLayout layout = targetProject.layouts().activeLayout();
        layout = copyNodeLayouts(layout, payload, id -> {
            String screen = screenIdMap.get(rawAfter(id, "wireframe-screen:"));
            if (screen != null) return screen;
            return componentIdMap.get(rawAfter(id, "wireframe-component:"));
        }, id -> id.startsWith("wireframe-screen:")
                ? VisualElementLayoutIds.wireframeScreen(rawAfter(id, "wireframe-screen:")).value()
                : VisualElementLayoutIds.wireframeComponent(rawAfter(id, "wireframe-component:")).value());
        DiagramProject updated = targetProject.withWireframe(document).withLayouts(targetProject.layouts().withLayout(layout));
        return new PasteResult(updated, screenIdMap.size() + componentIdMap.size(), 0, "Selección pegada en wireframes.");
    }

static PasteResult pasteUmlClass(DiagramProject targetProject, ProjectVisualSelectionTransferPayload payload) {
        UmlClassDiagramDocument source = payload.sourceProject().umlClassDiagram().orElse(null);
        UmlClassDiagramDocument document = targetProject.umlClassDiagram().orElse(null);
        if (source == null || document == null) return PasteResult.empty(targetProject);
        Set<String> selectedModuleIds = rawIds(payload.selectedNodeIds(), "uml-module:");
        Set<String> selectedClassIds = rawIds(payload.selectedNodeIds(), "uml-class:");
        Set<String> selectedRelationIds = rawIds(payload.selectedConnectorIds(), "uml-relation:");
        for (UmlClassRelation relation : source.relations()) if (selectedRelationIds.contains(relation.id())) selectedClassIds = with(selectedClassIds, relation.sourceClassId(), relation.targetClassId());
        for (UmlClassRelation relation : source.relations()) if (selectedClassIds.contains(relation.sourceClassId()) && selectedClassIds.contains(relation.targetClassId())) selectedRelationIds = with(selectedRelationIds, relation.id());
        for (UmlClassNode node : source.classes()) if (selectedClassIds.contains(node.id()) && !node.moduleId().isBlank()) selectedModuleIds = with(selectedModuleIds, node.moduleId());
        Map<String, String> moduleIdMap = new LinkedHashMap<>();
        Map<String, String> classIdMap = new LinkedHashMap<>();
        Set<String> occupiedModuleIds = idsOfUmlModules(document);
        Set<String> occupiedClassIds = idsOfUmlClasses(document);
        Set<String> occupiedRelationIds = idsOfUmlRelations(document);
        Set<String> pending = new LinkedHashSet<>();
        for (UmlModuleGroup module : source.modules()) {
            if (!selectedModuleIds.contains(module.id())) continue;
            String newId = uniqueRawId(module.id(), occupiedModuleIds, pending);
            pending.add(newId);
            occupiedModuleIds.add(newId);
            moduleIdMap.put(module.id(), newId);
            document = document.withModule(new UmlModuleGroup(newId, copyName(module.displayName()), module.path(), module.description(), module.notes()));
        }
        for (UmlClassNode node : source.classes()) {
            if (!selectedClassIds.contains(node.id())) continue;
            String newId = uniqueRawId(node.id(), occupiedClassIds, pending);
            pending.add(newId);
            occupiedClassIds.add(newId);
            classIdMap.put(node.id(), newId);
            String moduleId = moduleIdMap.getOrDefault(node.moduleId(), document.moduleById(node.moduleId()).isPresent() ? node.moduleId() : "");
            document = document.withClass(new UmlClassNode(newId, moduleId, copyName(node.displayName()), node.packageName(), node.kind(), node.visibility(), node.responsibility(), node.description(), copyMembers(node.members(), pending), node.notes()));
        }
        Map<String, String> relationIdMap = new LinkedHashMap<>();
        for (UmlClassRelation relation : source.relations()) {
            if (!selectedRelationIds.contains(relation.id())) continue;
            String sourceId = classIdMap.get(relation.sourceClassId());
            String targetId = classIdMap.get(relation.targetClassId());
            if (sourceId == null || targetId == null) continue;
            String newId = uniqueRawId(relation.id(), occupiedRelationIds, pending);
            pending.add(newId);
            occupiedRelationIds.add(newId);
            relationIdMap.put(relation.id(), newId);
            document = document.withRelation(new UmlClassRelation(newId, sourceId, targetId, relation.kind(), relation.label(), relation.description(), relation.notes()));
        }
        DiagramLayout layout = targetProject.layouts().activeLayout();
        layout = copyNodeLayouts(layout, payload, id -> {
            String module = moduleIdMap.get(rawAfter(id, "uml-module:"));
            if (module != null) return module;
            return classIdMap.get(rawAfter(id, "uml-class:"));
        }, id -> id.startsWith("uml-module:")
                ? VisualElementLayoutIds.umlModule(rawAfter(id, "uml-module:")).value()
                : VisualElementLayoutIds.umlClass(rawAfter(id, "uml-class:")).value());
        layout = copyConnectorLayouts(layout, payload, id -> relationIdMap.get(rawAfter(id, "uml-relation:")), id -> classIdMap.get(rawAfter(id, "uml-class:")), id -> VisualElementLayoutIds.umlRelation(id).value(), id -> VisualElementLayoutIds.umlClass(id).value());
        DiagramProject updated = targetProject.withUmlClassDiagram(document).withLayouts(targetProject.layouts().withLayout(layout));
        return new PasteResult(updated, moduleIdMap.size() + classIdMap.size(), relationIdMap.size(), "Selección pegada en UML Clases.");
    }
}
