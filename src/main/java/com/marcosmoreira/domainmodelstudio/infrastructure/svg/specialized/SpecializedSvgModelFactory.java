package com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import com.marcosmoreira.domainmodelstudio.domain.visualcomment.VisualComment;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/** Crea un modelo SVG común para los documentos visuales que no son ER conceptual. */
public final class SpecializedSvgModelFactory {

    public SpecializedSvgModel fromProject(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        DiagramTypeId typeId = project.metadata().diagramTypeId();
        if (DiagramTypeId.DATA_DICTIONARY.equals(typeId)) {
            throw new IllegalArgumentException("El diccionario de datos se exporta como Markdown/PDF, no como SVG visual.");
        }
        if (DiagramTypeId.CONCEPTUAL_MODEL.equals(typeId)) {
            throw new IllegalArgumentException("El modelo conceptual usa su exportador SVG de notación propio.");
        }
        if (DiagramTypeId.ROLES_PERMISSIONS_MAP.equals(typeId)) {
            throw new IllegalArgumentException("Roles y permisos se exporta como matriz SVG estructurada, no como grafo genérico.");
        }
        Builder builder = new Builder(typeId, project.metadata().title(), readableType(typeId));
        project.moduleMap().ifPresent(document -> addModuleMap(document, builder));
        project.screenFlow().ifPresent(document -> addScreenFlow(document, builder));
        project.wireframe().ifPresent(document -> addWireframe(document, builder));
        project.umlClassDiagram().ifPresent(document -> addUmlClass(document, builder));
        project.behaviorDiagram().ifPresent(document -> addBehavior(document, builder));
        project.architectureDiagram().ifPresent(document -> addArchitecture(document, builder));
        project.freeGraph().ifPresent(document -> addFreeGraph(document, builder));
        project.logicalBusinessGraphDocument().ifPresent(document -> addLogicalBusinessGraph(document, builder));
        addVisualComments(project, builder);
        if (builder.empty()) {
            builder.viewLabel("Sin elementos visuales; completar el artefacto antes de entrega final.");
        }
        return builder.build();
    }

    private void addModuleMap(ModuleMapDocument document, Builder builder) {
        document.modules().forEach(module -> builder.node(new SpecializedSvgNode(
                VisualElementLayoutIds.module(module.id()),
                module.displayName(),
                module.kind().displayName(),
                details(module.status().displayName(), module.responsibility(), module.description()),
                module.rootModule() ? "node-primary" : "node-support")));
        document.modules().stream()
                .filter(module -> !module.parentId().isBlank())
                .forEach(module -> builder.connector(new SpecializedSvgConnector(
                        VisualElementLayoutIds.moduleContainment(module.parentId(), module.id()),
                        VisualElementLayoutIds.module(module.parentId()),
                        VisualElementLayoutIds.module(module.id()),
                        "Contiene",
                        "Contención",
                        "connector-containment")));
        document.dependencies().forEach(dependency -> builder.connector(new SpecializedSvgConnector(
                VisualElementLayoutIds.dependency(dependency.id()),
                VisualElementLayoutIds.module(dependency.sourceModuleId()),
                VisualElementLayoutIds.module(dependency.targetModuleId()),
                firstNonBlank(dependency.description(), dependency.kind().displayName()),
                dependency.kind().displayName(),
                "connector-flow")));
    }

    private void addScreenFlow(ScreenFlowDocument document, Builder builder) {
        document.screens().forEach(screen -> builder.node(new SpecializedSvgNode(
                VisualElementLayoutIds.screen(screen.id()),
                screen.displayName(),
                screen.kind().displayName(),
                details(screen.moduleName(), screen.route(), screen.purpose()),
                "node-screen")));
        document.transitions().forEach(transition -> builder.connector(new SpecializedSvgConnector(
                VisualElementLayoutIds.transition(transition.id()),
                VisualElementLayoutIds.screen(transition.sourceScreenId()),
                VisualElementLayoutIds.screen(transition.targetScreenId()),
                firstNonBlank(transition.trigger(), transition.kind().displayName()),
                transition.kind().displayName(),
                "connector-flow")));
    }

    private void addWireframe(WireframeDocument document, Builder builder) {
        document.screens().forEach(screen -> builder.node(new SpecializedSvgNode(
                VisualElementLayoutIds.wireframeScreen(screen.id()),
                screen.displayName(),
                "Pantalla",
                details(screen.moduleName(), screen.purpose(), screen.notes()),
                "node-wireframe-screen")));
        document.components().forEach(component -> builder.node(new SpecializedSvgNode(
                VisualElementLayoutIds.wireframeComponent(component.id()),
                component.displayName(),
                component.kind().displayName(),
                details("Pantalla: " + component.screenId(), component.dataBinding(), component.behavior()),
                "node-wireframe-component node-wireframe-component-" + component.kind().name().toLowerCase(Locale.ROOT).replace('_', '-'))));
    }

    private void addUmlClass(UmlClassDiagramDocument document, Builder builder) {
        document.views().forEach(view -> builder.viewLabel(view.displayName()));
        document.modules().forEach(module -> builder.node(new SpecializedSvgNode(
                VisualElementLayoutIds.umlModule(module.id()),
                module.displayName(),
                "Paquete / módulo",
                details(module.path(), module.description(), module.notes()),
                "node-group")));
        document.classes().forEach(umlClass -> builder.node(new SpecializedSvgNode(
                VisualElementLayoutIds.umlClass(umlClass.id()),
                umlClass.displayName(),
                umlClass.kind().displayName(),
                details(umlClass.packageName(), umlClass.responsibility(), memberSummary(umlClass.members().size()), umlClass.notes()),
                "node-uml-class")));
        document.relations().forEach(relation -> builder.connector(new SpecializedSvgConnector(
                VisualElementLayoutIds.umlRelation(relation.id()),
                VisualElementLayoutIds.umlClass(relation.sourceClassId()),
                VisualElementLayoutIds.umlClass(relation.targetClassId()),
                firstNonBlank(relation.label(), relation.kind().displayName()),
                relation.kind().displayName(),
                "connector-uml connector-uml-" + relation.kind().name().toLowerCase(Locale.ROOT).replace('_', '-'))));
    }

    private void addBehavior(BehaviorDiagramDocument document, Builder builder) {
        document.nodes().forEach(node -> builder.node(new SpecializedSvgNode(
                VisualElementLayoutIds.behaviorNode(node.id()),
                node.displayName(),
                node.kind().displayName(),
                details(node.owner(), node.description(), node.notes()),
                "node-behavior")));
        document.edges().forEach(edge -> builder.connector(new SpecializedSvgConnector(
                VisualElementLayoutIds.behaviorEdge(edge.id()),
                VisualElementLayoutIds.behaviorNode(edge.sourceNodeId()),
                VisualElementLayoutIds.behaviorNode(edge.targetNodeId()),
                firstNonBlank(edge.label(), edge.condition(), edge.kind().displayName()),
                edge.kind().displayName(),
                "connector-flow")));
    }

    private void addArchitecture(ArchitectureDiagramDocument document, Builder builder) {
        document.nodes().forEach(node -> builder.node(new SpecializedSvgNode(
                VisualElementLayoutIds.architectureNode(node.id()),
                node.displayName(),
                node.kind().displayName(),
                details(node.technology(), node.environment(), node.owner()),
                "node-architecture node-architecture-" + node.kind().name().toLowerCase(Locale.ROOT).replace('_', '-'))));
        document.edges().forEach(edge -> builder.connector(new SpecializedSvgConnector(
                VisualElementLayoutIds.architectureEdge(edge.id()),
                VisualElementLayoutIds.architectureNode(edge.sourceNodeId()),
                VisualElementLayoutIds.architectureNode(edge.targetNodeId()),
                firstNonBlank(edge.label(), edge.protocol(), edge.kind().displayName()),
                edge.kind().displayName(),
                "connector-architecture")));
    }

    private void addFreeGraph(FreeGraphDocument document, Builder builder) {
        document.nodes().forEach(node -> builder.node(new SpecializedSvgNode(
                VisualElementLayoutIds.freeGraphNode(node.id()),
                node.title(),
                "Nodo",
                details(node.content()),
                "node-free-graph")));
        document.edges().forEach(edge -> builder.connector(new SpecializedSvgConnector(
                VisualElementLayoutIds.freeGraphEdge(edge.id()),
                VisualElementLayoutIds.freeGraphNode(edge.sourceNodeId()),
                VisualElementLayoutIds.freeGraphNode(edge.targetNodeId()),
                firstNonBlank(edge.label(), edge.direction().displayName()),
                edge.direction().displayName(),
                edge.direction().name().equals("UNDIRECTED")
                        ? "connector-free-graph connector-free-graph-undirected"
                        : "connector-free-graph connector-free-graph-directed")));
    }

    private void addLogicalBusinessGraph(LogicalBusinessGraphDocument document, Builder builder) {
        document.nodes().forEach(node -> builder.node(new SpecializedSvgNode(
                VisualElementLayoutIds.logicalBusinessGraphNode(node.code()),
                node.code() + " — " + node.title(),
                node.kind().displayName(),
                details(node.status().displayName(), node.description()),
                "node-logical-business-graph node-logical-business-graph-" + node.kind().name().toLowerCase(Locale.ROOT).replace('_', '-'))));
        document.edges().forEach(edge -> builder.connector(new SpecializedSvgConnector(
                VisualElementLayoutIds.logicalBusinessGraphEdge(edge.id()),
                VisualElementLayoutIds.logicalBusinessGraphNode(edge.sourceCode()),
                VisualElementLayoutIds.logicalBusinessGraphNode(edge.targetCode()),
                firstNonBlank(edge.description(), edge.relationKind().code()),
                edge.relationKind().code(),
                "connector-logical-business-graph")));
    }

    private void addVisualComments(DiagramProject project, Builder builder) {
        for (VisualComment comment : project.visualComments().comments()) {
            builder.node(new SpecializedSvgNode(
                    VisualElementLayoutIds.visualComment(comment.id()),
                    comment.visibleTitle(),
                    "Comentario",
                    details(comment.visibleDescription()),
                    "node-visual-comment"));
        }
    }

    private static List<String> details(String... values) {
        List<String> result = new ArrayList<>();
        if (values == null) {
            return result;
        }
        for (String value : values) {
            String normalized = normalize(value);
            if (!normalized.isBlank()) {
                result.add(normalized);
            }
        }
        return result;
    }

    private static String memberSummary(int count) {
        return count == 1 ? "1 miembro" : count + " miembros";
    }

    private static String firstNonBlank(String... values) {
        if (values == null) {
            return "";
        }
        for (String value : values) {
            String normalized = normalize(value);
            if (!normalized.isBlank()) {
                return normalized;
            }
        }
        return "";
    }

    private static String readableType(DiagramTypeId typeId) {
        String value = typeId == null ? "diagrama" : typeId.value();
        return switch (value) {
            case "admin-module-map" -> "Mapa de módulos";
            case "screen-flow" -> "Flujo de pantallas";
            case "admin-wireframes" -> "Wireframes administrativos";
            case "uml-class" -> "UML clases";
            case "uml-use-case" -> "UML casos de uso";
            case "uml-activity" -> "UML actividad";
            case "uml-sequence" -> "UML secuencia";
            case "uml-state" -> "UML estados";
            case "bpmn-basic" -> "BPMN básico";
            case "operational-flow" -> "Flujo operativo";
            case "c4-context" -> "C4 contexto";
            case "c4-containers" -> "C4 contenedores";
            case "technical-deployment" -> "Despliegue técnico";
            case "roles-permissions-map" -> "Roles y permisos";
            case "free-graph" -> "Grafo libre";
            case "logical-business-graph" -> "Grafo lógico del negocio";
            default -> value;
        };
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }

    private static final class Builder {
        private final DiagramTypeId typeId;
        private final String title;
        private final String typeLabel;
        private final List<String> viewLabels = new ArrayList<>();
        private final List<SpecializedSvgNode> nodes = new ArrayList<>();
        private final List<SpecializedSvgConnector> connectors = new ArrayList<>();

        private Builder(DiagramTypeId typeId, String title, String typeLabel) {
            this.typeId = typeId;
            this.title = title;
            this.typeLabel = typeLabel;
        }

        private void viewLabel(String viewLabel) {
            String normalized = normalize(viewLabel);
            if (!normalized.isBlank()) {
                viewLabels.add(normalized);
            }
        }
        private void node(SpecializedSvgNode node) { nodes.add(node); }
        private void connector(SpecializedSvgConnector connector) { connectors.add(connector); }
        private boolean empty() { return nodes.isEmpty() && connectors.isEmpty(); }
        private SpecializedSvgModel build() { return new SpecializedSvgModel(typeId, title, typeLabel, viewLabels, nodes, connectors); }
    }
}
