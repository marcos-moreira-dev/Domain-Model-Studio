package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionAssignment;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransition;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** Construye especificaciones de layout visual a partir de documentos especializados. */
public final class VisualLayoutSpecificationFactory {

    private final BehaviorProcessLayoutPolicy behaviorProcessLayoutPolicy = new BehaviorProcessLayoutPolicy();
    private final BusinessProcessAutoLayoutPolicy businessProcessAutoLayoutPolicy = new BusinessProcessAutoLayoutPolicy();
    private final UmlFlowAutoLayoutPolicy umlFlowAutoLayoutPolicy = new UmlFlowAutoLayoutPolicy();
    private final UmlBehaviorLayoutPolicy umlBehaviorLayoutPolicy = new UmlBehaviorLayoutPolicy();
    private final UmlUseCaseAutoLayoutPolicy umlUseCaseAutoLayoutPolicy = new UmlUseCaseAutoLayoutPolicy();
    private final SequenceTimelineLayoutPolicy sequenceTimelineLayoutPolicy = new SequenceTimelineLayoutPolicy();
    private final ArchitectureLayoutPolicy architectureLayoutPolicy = new ArchitectureLayoutPolicy();
    private final C4ArchitectureAutoLayoutPolicy c4ArchitectureAutoLayoutPolicy = new C4ArchitectureAutoLayoutPolicy();
    private final UmlClassLayoutPolicy umlClassLayoutPolicy;
    private final AdminApplicationsLayoutPolicy adminApplicationsLayoutPolicy = new AdminApplicationsLayoutPolicy();
    private final FreeGraphLayoutPolicy freeGraphLayoutPolicy = new FreeGraphLayoutPolicy();
    private final LogicalBusinessGraphLayoutPolicy logicalBusinessGraphLayoutPolicy = new LogicalBusinessGraphLayoutPolicy();

    public VisualLayoutSpecificationFactory() {
        this(new UmlClassLayoutPolicy());
    }

    public VisualLayoutSpecificationFactory(UmlClassLayoutPolicy umlClassLayoutPolicy) {
        this.umlClassLayoutPolicy = umlClassLayoutPolicy == null ? new UmlClassLayoutPolicy() : umlClassLayoutPolicy;
    }

    public VisualLayoutSpecification fromProject(DiagramProject project) {
        if (project == null) {
            return VisualLayoutSpecification.empty();
        }
        List<VisualNodeReference> nodes = new ArrayList<>();
        List<VisualConnectorReference> connectors = new ArrayList<>();
        project.moduleMap().ifPresent(document -> addModuleMap(document, nodes, connectors));
        project.screenFlow().ifPresent(document -> addScreenFlow(document, nodes, connectors));
        project.wireframe().ifPresent(document -> addWireframe(document, nodes));
        project.umlClassDiagram().ifPresent(document -> addUmlClass(document, nodes, connectors));
        project.behaviorDiagram().ifPresent(document -> addBehavior(document, nodes, connectors));
        project.architectureDiagram().ifPresent(document -> addArchitecture(document, nodes, connectors));
        project.rolesPermissions().ifPresent(document -> addRolesPermissions(document, nodes, connectors));
        project.freeGraph().ifPresent(document -> addFreeGraph(document, nodes, connectors));
        project.logicalBusinessGraphDocument().ifPresent(document -> addLogicalBusinessGraph(document, nodes, connectors));
        return new VisualLayoutSpecification(nodes, connectors.stream()
                .filter(connector -> hasNode(nodes, connector.sourceLayoutId()))
                .filter(connector -> hasNode(nodes, connector.targetLayoutId()))
                .toList());
    }

    private void addModuleMap(ModuleMapDocument document, List<VisualNodeReference> nodes,
                              List<VisualConnectorReference> connectors) {
        nodes.addAll(adminApplicationsLayoutPolicy.moduleMapReferences(document, nodes.size()));
        for (var module : document.modules()) {
            if (!module.parentId().isBlank()) {
                connectors.add(new VisualConnectorReference(
                        VisualElementLayoutIds.moduleContainment(module.parentId(), module.id()),
                        VisualElementLayoutIds.module(module.parentId()),
                        VisualElementLayoutIds.module(module.id())));
            }
        }
        for (ModuleDependency dependency : document.dependencies()) {
            connectors.add(new VisualConnectorReference(
                    VisualElementLayoutIds.dependency(dependency.id()),
                    VisualElementLayoutIds.module(dependency.sourceModuleId()),
                    VisualElementLayoutIds.module(dependency.targetModuleId())));
        }
    }

    private void addScreenFlow(ScreenFlowDocument document, List<VisualNodeReference> nodes,
                               List<VisualConnectorReference> connectors) {
        nodes.addAll(adminApplicationsLayoutPolicy.screenFlowReferences(document, nodes.size()));
        for (ScreenTransition transition : document.transitions()) {
            connectors.add(new VisualConnectorReference(
                    VisualElementLayoutIds.transition(transition.id()),
                    VisualElementLayoutIds.screen(transition.sourceScreenId()),
                    VisualElementLayoutIds.screen(transition.targetScreenId())));
        }
    }

    private void addWireframe(WireframeDocument document, List<VisualNodeReference> nodes) {
        nodes.addAll(adminApplicationsLayoutPolicy.wireframeReferences(document, nodes.size()));
    }

    private void addUmlClass(UmlClassDiagramDocument document, List<VisualNodeReference> nodes,
                             List<VisualConnectorReference> connectors) {
        nodes.addAll(umlClassLayoutPolicy.visualReferences(document, nodes.size()));
        for (UmlClassRelation relation : document.relations()) {
            connectors.add(new VisualConnectorReference(
                    VisualElementLayoutIds.umlRelation(relation.id()),
                    VisualElementLayoutIds.umlClass(relation.sourceClassId()),
                    VisualElementLayoutIds.umlClass(relation.targetClassId())));
        }
    }

    private void addBehavior(BehaviorDiagramDocument document, List<VisualNodeReference> nodes,
                             List<VisualConnectorReference> connectors) {
        if (umlUseCaseAutoLayoutPolicy.supports(document)) {
            nodes.addAll(umlUseCaseAutoLayoutPolicy.visualReferences(document));
        } else if (businessProcessAutoLayoutPolicy.supports(document)) {
            nodes.addAll(businessProcessAutoLayoutPolicy.visualReferences(document));
        } else if (umlFlowAutoLayoutPolicy.supports(document)) {
            nodes.addAll(umlFlowAutoLayoutPolicy.visualReferences(document));
        } else if (sequenceTimelineLayoutPolicy.supports(document)) {
            addSequenceNodes(document, nodes);
        } else {
            int index = nodes.size();
            for (var node : document.nodes()) {
                if (umlBehaviorLayoutPolicy.supports(document.diagramKind())) {
                    nodes.add(umlBehaviorLayoutPolicy.visualReference(document.diagramKind(), node, index++));
                } else {
                    nodes.add(behaviorProcessLayoutPolicy.visualReference(document.diagramKind(), node, index++));
                }
            }
        }
        for (BehaviorEdge edge : document.edges()) {
            connectors.add(new VisualConnectorReference(
                    VisualElementLayoutIds.behaviorEdge(edge.id()),
                    VisualElementLayoutIds.behaviorNode(edge.sourceNodeId()),
                    VisualElementLayoutIds.behaviorNode(edge.targetNodeId())));
        }
    }


    private void addSequenceNodes(BehaviorDiagramDocument document, List<VisualNodeReference> nodes) {
        int index = nodes.size();
        Set<String> addedIds = new LinkedHashSet<>();
        for (var participant : sequenceTimelineLayoutPolicy.participants(document)) {
            nodes.add(sequenceTimelineLayoutPolicy.visualReference(participant, index++));
            addedIds.add(participant.id());
        }
        for (var node : document.nodes()) {
            if (addedIds.add(node.id())) {
                nodes.add(sequenceTimelineLayoutPolicy.visualReference(node, index++));
            }
        }
    }

    private void addArchitecture(ArchitectureDiagramDocument document, List<VisualNodeReference> nodes,
                                 List<VisualConnectorReference> connectors) {
        if (c4ArchitectureAutoLayoutPolicy.supports(document)) {
            nodes.addAll(c4ArchitectureAutoLayoutPolicy.visualReferences(document));
        } else {
            int index = nodes.size();
            for (var node : document.nodes()) {
                nodes.add(architectureLayoutPolicy.visualReference(document.diagramKind(), node, index++));
            }
        }
        for (ArchitectureEdge edge : document.edges()) {
            connectors.add(new VisualConnectorReference(
                    VisualElementLayoutIds.architectureEdge(edge.id()),
                    VisualElementLayoutIds.architectureNode(edge.sourceNodeId()),
                    VisualElementLayoutIds.architectureNode(edge.targetNodeId())));
        }
    }

    private void addRolesPermissions(RolesPermissionsDocument document, List<VisualNodeReference> nodes,
                                     List<VisualConnectorReference> connectors) {
        int index = nodes.size();
        for (var role : document.roles()) {
            nodes.add(new VisualNodeReference(VisualElementLayoutIds.role(role.id()), 180.0, 70.0, index++));
        }
        for (var permission : document.permissions()) {
            nodes.add(new VisualNodeReference(VisualElementLayoutIds.permission(permission.id()), 210.0, 58.0, index++));
        }
        for (PermissionAssignment assignment : document.assignments()) {
            connectors.add(new VisualConnectorReference(
                    VisualElementLayoutIds.assignment(assignment.id()),
                    VisualElementLayoutIds.role(assignment.roleId()),
                    VisualElementLayoutIds.permission(assignment.permissionId())));
        }
    }

    private void addFreeGraph(FreeGraphDocument document, List<VisualNodeReference> nodes,
                              List<VisualConnectorReference> connectors) {
        nodes.addAll(freeGraphLayoutPolicy.visualReferences(document, nodes.size()));
        for (FreeGraphEdge edge : document.edges()) {
            connectors.add(new VisualConnectorReference(
                    VisualElementLayoutIds.freeGraphEdge(edge.id()),
                    VisualElementLayoutIds.freeGraphNode(edge.sourceNodeId()),
                    VisualElementLayoutIds.freeGraphNode(edge.targetNodeId())));
        }
    }

    private void addLogicalBusinessGraph(LogicalBusinessGraphDocument document, List<VisualNodeReference> nodes,
                                         List<VisualConnectorReference> connectors) {
        nodes.addAll(logicalBusinessGraphLayoutPolicy.visualReferences(document, nodes.size()));
        for (LogicalBusinessGraphEdge edge : document.edges()) {
            connectors.add(new VisualConnectorReference(
                    VisualElementLayoutIds.logicalBusinessGraphEdge(edge.id()),
                    VisualElementLayoutIds.logicalBusinessGraphNode(edge.sourceCode()),
                    VisualElementLayoutIds.logicalBusinessGraphNode(edge.targetCode())));
        }
    }

    private static boolean hasNode(List<VisualNodeReference> nodes, DiagramElementId id) {
        return nodes.stream().anyMatch(node -> node.layoutId().equals(id));
    }
}
