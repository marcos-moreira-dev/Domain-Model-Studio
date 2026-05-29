package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.application.ApplicationServices;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.diagram.ProjectType;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutService;
import java.util.Objects;

/**
 * Crea proyectos vacíos para los tipos soportados por el shell.
 *
 * <p>La fábrica concentra la relación entre el catálogo visible y los documentos
 * especializados iniciales. El shell decide cuándo abrir pestañas; esta clase solo
 * construye el proyecto inicial correspondiente.</p>
 */
final class NewProjectFactory {

    private final ApplicationServices applicationServices;
    private final ProjectIdPolicy projectIdPolicy;

    NewProjectFactory(ApplicationServices applicationServices) {
        this(applicationServices, new ProjectIdPolicy());
    }

    NewProjectFactory(ApplicationServices applicationServices, ProjectIdPolicy projectIdPolicy) {
        this.applicationServices = Objects.requireNonNull(applicationServices, "applicationServices");
        this.projectIdPolicy = Objects.requireNonNull(projectIdPolicy, "projectIdPolicy");
    }

    DiagramProject createDataDictionary(DiagramTypeDescriptor descriptor, int sequence) {
        String title = titleFor(descriptor);
        DataDictionaryDocument document = applicationServices.createDataDictionaryUseCase().createBlank(title);
        return blank(descriptor, title, sequence).withDataDictionary(document);
    }

    DiagramProject createModuleMap(DiagramTypeDescriptor descriptor, int sequence) {
        String title = titleFor(descriptor);
        ModuleMapDocument document = applicationServices.createModuleMapUseCase().createBlank(title);
        return DiagramProject.blank(newProjectId(descriptor, sequence), title, descriptor.id()).withModuleMap(document);
    }

    DiagramProject createUmlClassDiagram(DiagramTypeDescriptor descriptor, int sequence) {
        String title = titleFor(descriptor);
        UmlClassDiagramDocument document = applicationServices.createUmlClassDiagramUseCase().createBlank(title);
        return blank(descriptor, title, sequence).withUmlClassDiagram(document);
    }

    DiagramProject createRolesPermissions(DiagramTypeDescriptor descriptor, int sequence) {
        String title = titleFor(descriptor);
        RolesPermissionsDocument document = applicationServices.createRolesPermissionsUseCase().createBlank(title);
        return blank(descriptor, title, sequence).withRolesPermissions(document);
    }

    DiagramProject createScreenFlow(DiagramTypeDescriptor descriptor, int sequence) {
        String title = titleFor(descriptor);
        ScreenFlowDocument document = applicationServices.createScreenFlowUseCase().createBlank(title);
        return blank(descriptor, title, sequence).withScreenFlow(document);
    }

    DiagramProject createWireframe(DiagramTypeDescriptor descriptor, int sequence) {
        String title = titleFor(descriptor);
        WireframeDocument document = applicationServices.createWireframeUseCase().createBlank(title);
        return blank(descriptor, title, sequence).withWireframe(document);
    }

    DiagramProject createBehaviorDiagram(DiagramTypeDescriptor descriptor, int sequence) {
        String title = titleFor(descriptor);
        BehaviorDiagramKind kind = BehaviorDiagramKind.fromDiagramTypeId(descriptor.id());
        BehaviorDiagramDocument document = applicationServices.createBehaviorDiagramUseCase().createBlank(title, kind);
        return blank(descriptor, title, sequence).withBehaviorDiagram(document);
    }

    DiagramProject createArchitectureDiagram(DiagramTypeDescriptor descriptor, int sequence) {
        String title = titleFor(descriptor);
        ArchitectureDiagramKind kind = ArchitectureDiagramKind.fromDiagramTypeId(descriptor.id());
        ArchitectureDiagramDocument document = applicationServices.createArchitectureDiagramUseCase().createBlank(title, kind);
        return blank(descriptor, title, sequence).withArchitectureDiagram(document);
    }

    DiagramProject createFreeGraph(DiagramTypeDescriptor descriptor, int sequence) {
        String title = titleFor(descriptor);
        FreeGraphDocument document = FreeGraphDocument.blank(title);
        return new VisualLayoutService().ensureVisualLayout(
                DiagramProject.blank(newProjectId(descriptor, sequence), title, descriptor.id()).withFreeGraph(document));
    }

    DiagramProject createLogicalBusinessIntake(DiagramTypeDescriptor descriptor, int sequence) {
        String title = titleFor(descriptor);
        LogicalBusinessDocument document = LogicalBusinessDocument.blank(title);
        return DiagramProject.blank(newProjectId(descriptor, sequence), title, descriptor.id())
                .withLogicalBusinessDocument(document);
    }

    DiagramProject createLogicalBusinessGraph(DiagramTypeDescriptor descriptor, int sequence) {
        String title = titleFor(descriptor);
        LogicalBusinessGraphDocument document = LogicalBusinessGraphDocument.blank(title);
        return new VisualLayoutService().ensureVisualLayout(
                DiagramProject.blank(newProjectId(descriptor, sequence), title, descriptor.id())
                        .withLogicalBusinessGraphDocument(document));
    }

    DiagramProject createPlaceholderProject(DiagramTypeDescriptor descriptor, int sequence) {
        return DiagramProject.blank(newProjectId(descriptor, sequence), titleFor(descriptor), descriptor.id());
    }

    DiagramProject createConceptualFallback(DiagramTypeDescriptor descriptor, int sequence) {
        return DiagramProject.blank(
                newProjectId(descriptor, sequence),
                titleFor(descriptor),
                ProjectType.CONCEPTUAL_MODEL);
    }

    boolean supportsBehaviorDiagram(DiagramTypeId diagramTypeId) {
        for (BehaviorDiagramKind kind : BehaviorDiagramKind.values()) {
            if (kind.diagramTypeId().equals(diagramTypeId)) {
                return true;
            }
        }
        return false;
    }

    boolean supportsArchitectureDiagram(DiagramTypeId diagramTypeId) {
        return ArchitectureDiagramKind.supports(diagramTypeId);
    }

    private DiagramProject blank(DiagramTypeDescriptor descriptor, String title, int sequence) {
        return DiagramProject.blank(newProjectId(descriptor, sequence), title, ProjectType.CONCEPTUAL_MODEL, descriptor.id());
    }

    private String newProjectId(DiagramTypeDescriptor descriptor, int sequence) {
        return projectIdPolicy.newProjectId(descriptor.id(), sequence);
    }

    private String titleFor(DiagramTypeDescriptor descriptor) {
        return descriptor.displayName() + " nuevo";
    }
}
