package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdge;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphNode;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionAssignment;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RoleNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenNode;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransition;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponent;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreen;

/** Escribe diagramas especializados restantes dentro del bloque model. */
final class DmsProjectAuxiliarySpecializedJsonWriter {

    void writeAuxiliaryDocuments(DiagramProject project, StringBuilder json, int level) {
        if (project.rolesPermissions().isPresent()) {
            writeRolesPermissions(project.rolesPermissions().get(), json, level, true);
        }
        if (project.screenFlow().isPresent()) {
            writeScreenFlow(project.screenFlow().get(), json, level, true);
        }
        if (project.wireframe().isPresent()) {
            writeWireframe(project.wireframe().get(), json, level, true);
        }
        if (project.behaviorDiagram().isPresent()) {
            writeBehaviorDiagram(project.behaviorDiagram().get(), json, level, true);
        }
        if (project.architectureDiagram().isPresent()) {
            writeArchitectureDiagram(project.architectureDiagram().get(), json, level, true);
        }
        if (project.freeGraph().isPresent()) {
            writeFreeGraph(project.freeGraph().get(), json, level, true);
        }
        if (project.logicalBusinessGraphDocument().isPresent()) {
            writeLogicalBusinessGraph(project.logicalBusinessGraphDocument().get(), json, level, true);
        }
    }

    private void writeRolesPermissions(RolesPermissionsDocument document, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("\"rolesPermissions\": {\n");
        field(json, level + 1, "projectName", document.projectName(), true);
        field(json, level + 1, "version", document.version(), true);
        field(json, level + 1, "documentDate", document.documentDate().toString(), true);
        indent(json, level + 1).append("\"roles\": [\n");
        for (int i = 0; i < document.roles().size(); i++) writeRole(document.roles().get(i), json, level + 2, i + 1 < document.roles().size());
        indent(json, level + 1).append("],\n");
        indent(json, level + 1).append("\"permissions\": [\n");
        for (int i = 0; i < document.permissions().size(); i++) writePermission(document.permissions().get(i), json, level + 2, i + 1 < document.permissions().size());
        indent(json, level + 1).append("],\n");
        indent(json, level + 1).append("\"assignments\": [\n");
        for (int i = 0; i < document.assignments().size(); i++) writeAssignment(document.assignments().get(i), json, level + 2, i + 1 < document.assignments().size());
        indent(json, level + 1).append("]\n");
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeRole(RoleNode role, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", role.id(), true);
        field(json, level + 1, "displayName", role.displayName(), true);
        field(json, level + 1, "status", role.status().name(), true);
        field(json, level + 1, "responsibility", role.responsibility(), true);
        field(json, level + 1, "description", role.description(), true);
        field(json, level + 1, "notes", role.notes(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writePermission(PermissionNode permission, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", permission.id(), true);
        field(json, level + 1, "displayName", permission.displayName(), true);
        field(json, level + 1, "scope", permission.scope().name(), true);
        field(json, level + 1, "moduleName", permission.moduleName(), true);
        field(json, level + 1, "actionName", permission.actionName(), true);
        field(json, level + 1, "description", permission.description(), true);
        field(json, level + 1, "notes", permission.notes(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeAssignment(PermissionAssignment assignment, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", assignment.id(), true);
        field(json, level + 1, "roleId", assignment.roleId(), true);
        field(json, level + 1, "permissionId", assignment.permissionId(), true);
        booleanField(json, level + 1, "allowed", assignment.allowed(), true);
        field(json, level + 1, "condition", assignment.condition(), true);
        field(json, level + 1, "notes", assignment.notes(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeScreenFlow(ScreenFlowDocument document, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("\"screenFlow\": {\n");
        field(json, level + 1, "projectName", document.projectName(), true);
        field(json, level + 1, "version", document.version(), true);
        field(json, level + 1, "documentDate", document.documentDate().toString(), true);
        indent(json, level + 1).append("\"screens\": [\n");
        for (int i = 0; i < document.screens().size(); i++) writeScreen(document.screens().get(i), json, level + 2, i + 1 < document.screens().size());
        indent(json, level + 1).append("],\n");
        indent(json, level + 1).append("\"transitions\": [\n");
        for (int i = 0; i < document.transitions().size(); i++) writeScreenTransition(document.transitions().get(i), json, level + 2, i + 1 < document.transitions().size());
        indent(json, level + 1).append("]\n");
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeScreen(ScreenNode screen, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", screen.id(), true);
        field(json, level + 1, "displayName", screen.displayName(), true);
        field(json, level + 1, "kind", screen.kind().name(), true);
        field(json, level + 1, "moduleName", screen.moduleName(), true);
        field(json, level + 1, "route", screen.route(), true);
        field(json, level + 1, "purpose", screen.purpose(), true);
        field(json, level + 1, "notes", screen.notes(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeScreenTransition(ScreenTransition transition, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", transition.id(), true);
        field(json, level + 1, "sourceScreenId", transition.sourceScreenId(), true);
        field(json, level + 1, "targetScreenId", transition.targetScreenId(), true);
        field(json, level + 1, "kind", transition.kind().name(), true);
        field(json, level + 1, "trigger", transition.trigger(), true);
        field(json, level + 1, "condition", transition.condition(), true);
        field(json, level + 1, "notes", transition.notes(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeWireframe(WireframeDocument document, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("\"wireframe\": {\n");
        field(json, level + 1, "projectName", document.projectName(), true);
        field(json, level + 1, "version", document.version(), true);
        field(json, level + 1, "documentDate", document.documentDate().toString(), true);
        indent(json, level + 1).append("\"screens\": [\n");
        for (int i = 0; i < document.screens().size(); i++) writeWireframeScreen(document.screens().get(i), json, level + 2, i + 1 < document.screens().size());
        indent(json, level + 1).append("],\n");
        indent(json, level + 1).append("\"components\": [\n");
        for (int i = 0; i < document.components().size(); i++) writeWireframeComponent(document.components().get(i), json, level + 2, i + 1 < document.components().size());
        indent(json, level + 1).append("]\n");
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeWireframeScreen(WireframeScreen screen, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", screen.id(), true);
        field(json, level + 1, "displayName", screen.displayName(), true);
        field(json, level + 1, "moduleName", screen.moduleName(), true);
        field(json, level + 1, "purpose", screen.purpose(), true);
        field(json, level + 1, "notes", screen.notes(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeWireframeComponent(WireframeComponent component, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", component.id(), true);
        field(json, level + 1, "screenId", component.screenId(), true);
        field(json, level + 1, "kind", component.kind().name(), true);
        field(json, level + 1, "displayName", component.displayName(), true);
        numberField(json, level + 1, "orderIndex", component.orderIndex(), true);
        field(json, level + 1, "dataBinding", component.dataBinding(), true);
        field(json, level + 1, "behavior", component.behavior(), true);
        field(json, level + 1, "notes", component.notes(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }


    private void writeBehaviorDiagram(BehaviorDiagramDocument document, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("\"behaviorDiagram\": {\n");
        field(json, level + 1, "projectName", document.projectName(), true);
        field(json, level + 1, "version", document.version(), true);
        field(json, level + 1, "documentDate", document.documentDate().toString(), true);
        field(json, level + 1, "diagramKind", document.diagramKind().name(), true);
        field(json, level + 1, "notes", document.notes(), true);
        indent(json, level + 1).append("\"nodes\": [\n");
        for (int i = 0; i < document.nodes().size(); i++) {
            writeBehaviorNode(document.nodes().get(i), json, level + 2, i + 1 < document.nodes().size());
        }
        indent(json, level + 1).append("],\n");
        indent(json, level + 1).append("\"edges\": [\n");
        for (int i = 0; i < document.edges().size(); i++) {
            writeBehaviorEdge(document.edges().get(i), json, level + 2, i + 1 < document.edges().size());
        }
        indent(json, level + 1).append("]\n");
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeBehaviorNode(BehaviorNode node, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", node.id(), true);
        field(json, level + 1, "kind", node.kind().name(), true);
        field(json, level + 1, "displayName", node.displayName(), true);
        field(json, level + 1, "owner", node.owner(), true);
        field(json, level + 1, "description", node.description(), true);
        field(json, level + 1, "notes", node.notes(), true);
        numberField(json, level + 1, "orderIndex", node.orderIndex(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeBehaviorEdge(BehaviorEdge edge, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", edge.id(), true);
        field(json, level + 1, "sourceNodeId", edge.sourceNodeId(), true);
        field(json, level + 1, "targetNodeId", edge.targetNodeId(), true);
        field(json, level + 1, "kind", edge.kind().name(), true);
        field(json, level + 1, "label", edge.label(), true);
        field(json, level + 1, "condition", edge.condition(), true);
        field(json, level + 1, "notes", edge.notes(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }


    private void writeArchitectureDiagram(ArchitectureDiagramDocument document, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("\"architectureDiagram\": {\n");
        field(json, level + 1, "projectName", document.projectName(), true);
        field(json, level + 1, "version", document.version(), true);
        field(json, level + 1, "documentDate", document.documentDate().toString(), true);
        field(json, level + 1, "diagramKind", document.diagramKind().name(), true);
        field(json, level + 1, "notes", document.notes(), true);
        indent(json, level + 1).append("\"nodes\": [\n");
        for (int i = 0; i < document.nodes().size(); i++) {
            writeArchitectureNode(document.nodes().get(i), json, level + 2, i + 1 < document.nodes().size());
        }
        indent(json, level + 1).append("],\n");
        indent(json, level + 1).append("\"edges\": [\n");
        for (int i = 0; i < document.edges().size(); i++) {
            writeArchitectureEdge(document.edges().get(i), json, level + 2, i + 1 < document.edges().size());
        }
        indent(json, level + 1).append("]\n");
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeArchitectureNode(ArchitectureNode node, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", node.id(), true);
        field(json, level + 1, "kind", node.kind().name(), true);
        field(json, level + 1, "displayName", node.displayName(), true);
        field(json, level + 1, "technology", node.technology(), true);
        field(json, level + 1, "owner", node.owner(), true);
        field(json, level + 1, "environment", node.environment(), true);
        field(json, level + 1, "description", node.description(), true);
        field(json, level + 1, "notes", node.notes(), true);
        numberField(json, level + 1, "orderIndex", node.orderIndex(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeArchitectureEdge(ArchitectureEdge edge, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", edge.id(), true);
        field(json, level + 1, "sourceNodeId", edge.sourceNodeId(), true);
        field(json, level + 1, "targetNodeId", edge.targetNodeId(), true);
        field(json, level + 1, "kind", edge.kind().name(), true);
        field(json, level + 1, "label", edge.label(), true);
        field(json, level + 1, "protocol", edge.protocol(), true);
        field(json, level + 1, "notes", edge.notes(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeFreeGraph(FreeGraphDocument document, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("\"freeGraph\": {\n");
        field(json, level + 1, "projectName", document.projectName(), true);
        field(json, level + 1, "version", document.version(), true);
        field(json, level + 1, "documentDate", document.documentDate().toString(), true);
        field(json, level + 1, "graphKind", document.graphKind().name(), true);
        field(json, level + 1, "notes", document.notes(), true);
        indent(json, level + 1).append("\"nodes\": [\n");
        for (int i = 0; i < document.nodes().size(); i++) {
            writeFreeGraphNode(document.nodes().get(i), json, level + 2, i + 1 < document.nodes().size());
        }
        indent(json, level + 1).append("],\n");
        indent(json, level + 1).append("\"edges\": [\n");
        for (int i = 0; i < document.edges().size(); i++) {
            writeFreeGraphEdge(document.edges().get(i), json, level + 2, i + 1 < document.edges().size());
        }
        indent(json, level + 1).append("]\n");
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeFreeGraphNode(FreeGraphNode node, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", node.id(), true);
        field(json, level + 1, "title", node.title(), true);
        field(json, level + 1, "content", node.content(), true);
        numberField(json, level + 1, "orderIndex", node.orderIndex(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeFreeGraphEdge(FreeGraphEdge edge, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", edge.id(), true);
        field(json, level + 1, "sourceNodeId", edge.sourceNodeId(), true);
        field(json, level + 1, "targetNodeId", edge.targetNodeId(), true);
        field(json, level + 1, "direction", edge.direction().name(), true);
        field(json, level + 1, "label", edge.label(), true);
        field(json, level + 1, "notes", edge.notes(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeLogicalBusinessGraph(LogicalBusinessGraphDocument document, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("\"logicalBusinessGraphDocument\": {\n");
        field(json, level + 1, "projectName", document.projectName(), true);
        field(json, level + 1, "version", document.version(), true);
        field(json, level + 1, "documentDate", document.documentDate().toString(), true);
        field(json, level + 1, "notes", document.notes(), true);
        indent(json, level + 1).append("\"nodes\": [\n");
        for (int i = 0; i < document.nodes().size(); i++) {
            writeLogicalBusinessGraphNode(document.nodes().get(i), json, level + 2, i + 1 < document.nodes().size());
        }
        indent(json, level + 1).append("],\n");
        indent(json, level + 1).append("\"edges\": [\n");
        for (int i = 0; i < document.edges().size(); i++) {
            writeLogicalBusinessGraphEdge(document.edges().get(i), json, level + 2, i + 1 < document.edges().size());
        }
        indent(json, level + 1).append("]\n");
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeLogicalBusinessGraphNode(LogicalBusinessGraphNode node, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "code", node.code(), true);
        field(json, level + 1, "kind", node.kind().name(), true);
        field(json, level + 1, "title", node.title(), true);
        field(json, level + 1, "description", node.description(), true);
        field(json, level + 1, "status", node.status().name(), true);
        writeStringArray("sourceReferenceIds", node.sourceReferenceIds(), json, level + 1, false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeLogicalBusinessGraphEdge(LogicalBusinessGraphEdge edge, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", edge.id(), true);
        field(json, level + 1, "sourceCode", edge.sourceCode(), true);
        field(json, level + 1, "relationKind", edge.relationKind().name(), true);
        field(json, level + 1, "targetCode", edge.targetCode(), true);
        field(json, level + 1, "description", edge.description(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeStringArray(String name, java.util.List<String> values, StringBuilder json, int level, boolean comma) {
        indent(json, level).append(quote(name)).append(": [");
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) json.append(", ");
            json.append(quote(values.get(i)));
        }
        json.append("]").append(comma ? "," : "").append("\n");
    }

    private void field(StringBuilder json, int level, String name, String value, boolean comma) {
        indent(json, level).append(quote(name)).append(": ").append(quote(value)).append(comma ? "," : "").append("\n");
    }

    private void numberField(StringBuilder json, int level, String name, double value, boolean comma) {
        indent(json, level).append(quote(name)).append(": ").append(formatNumber(value)).append(comma ? "," : "").append("\n");
    }

    private String formatNumber(double value) {
        if (Math.rint(value) == value) {
            return Long.toString((long) value);
        }
        return Double.toString(value);
    }

    private void booleanField(StringBuilder json, int level, String name, boolean value, boolean comma) {
        indent(json, level).append(quote(name)).append(": ").append(value).append(comma ? "," : "").append("\n");
    }

    private StringBuilder indent(StringBuilder json, int level) {
        return json.append("  ".repeat(Math.max(0, level)));
    }

    private String quote(String value) {
        return JsonStringEscaper.quote(value);
    }
}
