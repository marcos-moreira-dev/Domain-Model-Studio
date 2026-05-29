package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import com.marcosmoreira.domainmodelstudio.domain.architecture.*;
import com.marcosmoreira.domainmodelstudio.domain.behavior.*;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.*;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.*;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.*;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.*;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Lee diagramas especializados restantes desde el bloque model. */
final class DmsProjectAuxiliarySpecializedJsonReader {

    RolesPermissionsDocument readRolesPermissions(Map<String, Object> object) {
        ArrayList<RoleNode> roles = new ArrayList<>();
        for (Object value : array(object, "roles")) roles.add(readRole(asObject(value, "role")));
        ArrayList<PermissionNode> permissions = new ArrayList<>();
        for (Object value : array(object, "permissions")) permissions.add(readPermission(asObject(value, "permission")));
        ArrayList<PermissionAssignment> assignments = new ArrayList<>();
        for (Object value : array(object, "assignments")) assignments.add(readAssignment(asObject(value, "assignment")));
        return new RolesPermissionsDocument(stringOrDefault(object, "projectName", "Roles y permisos"),
                stringOrDefault(object, "version", "0.1.0"), java.time.LocalDate.parse(stringOrDefault(object, "documentDate", java.time.LocalDate.now().toString())),
                roles, permissions, assignments);
    }

    private RoleNode readRole(Map<String, Object> object) {
        return new RoleNode(string(object, "id"), stringOrDefault(object, "displayName", "Rol"),
                enumValue(RoleStatus.class, stringOrDefault(object, "status", "PLANNED"), RoleStatus.PLANNED),
                stringOrDefault(object, "responsibility", ""), stringOrDefault(object, "description", ""), stringOrDefault(object, "notes", ""));
    }

    private PermissionNode readPermission(Map<String, Object> object) {
        return new PermissionNode(string(object, "id"), stringOrDefault(object, "displayName", "Permiso"),
                enumValue(PermissionScope.class, stringOrDefault(object, "scope", "ACTION"), PermissionScope.ACTION),
                stringOrDefault(object, "moduleName", ""), stringOrDefault(object, "actionName", ""),
                stringOrDefault(object, "description", ""), stringOrDefault(object, "notes", ""));
    }

    private PermissionAssignment readAssignment(Map<String, Object> object) {
        return new PermissionAssignment(string(object, "id"), string(object, "roleId"), string(object, "permissionId"),
                boolOrDefault(object, "allowed", true), stringOrDefault(object, "condition", ""), stringOrDefault(object, "notes", ""));
    }

    ScreenFlowDocument readScreenFlow(Map<String, Object> object) {
        ArrayList<ScreenNode> screens = new ArrayList<>();
        for (Object value : array(object, "screens")) screens.add(readScreen(asObject(value, "screen")));
        ArrayList<ScreenTransition> transitions = new ArrayList<>();
        for (Object value : array(object, "transitions")) transitions.add(readScreenTransition(asObject(value, "screenTransition")));
        return new ScreenFlowDocument(stringOrDefault(object, "projectName", "Flujo de pantallas"),
                stringOrDefault(object, "version", "0.1.0"), java.time.LocalDate.parse(stringOrDefault(object, "documentDate", java.time.LocalDate.now().toString())),
                screens, transitions);
    }

    private ScreenNode readScreen(Map<String, Object> object) {
        return new ScreenNode(string(object, "id"), stringOrDefault(object, "displayName", "Pantalla"),
                enumValue(ScreenKind.class, stringOrDefault(object, "kind", "OTHER"), ScreenKind.OTHER),
                stringOrDefault(object, "moduleName", ""), stringOrDefault(object, "route", ""),
                stringOrDefault(object, "purpose", ""), stringOrDefault(object, "notes", ""));
    }

    private ScreenTransition readScreenTransition(Map<String, Object> object) {
        return new ScreenTransition(string(object, "id"), string(object, "sourceScreenId"), string(object, "targetScreenId"),
                enumValue(ScreenTransitionKind.class, stringOrDefault(object, "kind", "NAVIGATES"), ScreenTransitionKind.NAVIGATES),
                stringOrDefault(object, "trigger", ""), stringOrDefault(object, "condition", ""), stringOrDefault(object, "notes", ""));
    }

    WireframeDocument readWireframe(Map<String, Object> object) {
        ArrayList<WireframeScreen> screens = new ArrayList<>();
        for (Object value : array(object, "screens")) screens.add(readWireframeScreen(asObject(value, "wireframeScreen")));
        ArrayList<WireframeComponent> components = new ArrayList<>();
        for (Object value : array(object, "components")) components.add(readWireframeComponent(asObject(value, "wireframeComponent")));
        return new WireframeDocument(stringOrDefault(object, "projectName", "Wireframes administrativos"),
                stringOrDefault(object, "version", "0.1.0"), java.time.LocalDate.parse(stringOrDefault(object, "documentDate", java.time.LocalDate.now().toString())),
                screens, components);
    }

    private WireframeScreen readWireframeScreen(Map<String, Object> object) {
        return new WireframeScreen(string(object, "id"), stringOrDefault(object, "displayName", "Pantalla"),
                stringOrDefault(object, "moduleName", ""), stringOrDefault(object, "purpose", ""), stringOrDefault(object, "notes", ""));
    }

    private WireframeComponent readWireframeComponent(Map<String, Object> object) {
        return new WireframeComponent(string(object, "id"), string(object, "screenId"),
                enumValue(WireframeComponentKind.class, stringOrDefault(object, "kind", "OTHER"), WireframeComponentKind.OTHER),
                stringOrDefault(object, "displayName", "Componente"), (int) numberOrDefault(object, "orderIndex", 0),
                stringOrDefault(object, "dataBinding", ""), stringOrDefault(object, "behavior", ""), stringOrDefault(object, "notes", ""));
    }


    BehaviorDiagramDocument readBehaviorDiagram(Map<String, Object> object) {
        ArrayList<BehaviorNode> nodes = new ArrayList<>();
        for (Object value : array(object, "nodes")) nodes.add(readBehaviorNode(asObject(value, "behaviorNode")));
        ArrayList<BehaviorEdge> edges = new ArrayList<>();
        for (Object value : array(object, "edges")) edges.add(readBehaviorEdge(asObject(value, "behaviorEdge")));
        return new BehaviorDiagramDocument(
                stringOrDefault(object, "projectName", "Diagrama de comportamiento"),
                stringOrDefault(object, "version", "borrador"),
                java.time.LocalDate.parse(stringOrDefault(object, "documentDate", java.time.LocalDate.now().toString())),
                enumValue(BehaviorDiagramKind.class, stringOrDefault(object, "diagramKind", "BPMN_BASIC"), BehaviorDiagramKind.BPMN_BASIC),
                nodes, edges, stringOrDefault(object, "notes", ""));
    }

    private BehaviorNode readBehaviorNode(Map<String, Object> object) {
        return new BehaviorNode(
                string(object, "id"),
                enumValue(BehaviorNodeKind.class, stringOrDefault(object, "kind", "ACTIVITY"), BehaviorNodeKind.ACTIVITY),
                stringOrDefault(object, "displayName", string(object, "id")),
                stringOrDefault(object, "owner", ""),
                stringOrDefault(object, "description", ""),
                stringOrDefault(object, "notes", ""),
                (int) numberOrDefault(object, "orderIndex", 0));
    }

    private BehaviorEdge readBehaviorEdge(Map<String, Object> object) {
        return new BehaviorEdge(
                string(object, "id"),
                stringOrDefault(object, "sourceNodeId", stringOrDefault(object, "source", "")),
                stringOrDefault(object, "targetNodeId", stringOrDefault(object, "target", "")),
                enumValue(BehaviorEdgeKind.class, stringOrDefault(object, "kind", "FLOW"), BehaviorEdgeKind.FLOW),
                stringOrDefault(object, "label", ""),
                stringOrDefault(object, "condition", ""),
                stringOrDefault(object, "notes", ""));
    }


    ArchitectureDiagramDocument readArchitectureDiagram(Map<String, Object> object) {
        ArrayList<ArchitectureNode> nodes = new ArrayList<>();
        for (Object value : array(object, "nodes")) nodes.add(readArchitectureNode(asObject(value, "architectureNode")));
        ArrayList<ArchitectureEdge> edges = new ArrayList<>();
        for (Object value : array(object, "edges")) edges.add(readArchitectureEdge(asObject(value, "architectureEdge")));
        return new ArchitectureDiagramDocument(
                stringOrDefault(object, "projectName", "Diagrama de arquitectura"),
                stringOrDefault(object, "version", "borrador"),
                java.time.LocalDate.parse(stringOrDefault(object, "documentDate", java.time.LocalDate.now().toString())),
                enumValue(ArchitectureDiagramKind.class, stringOrDefault(object, "diagramKind", "C4_CONTEXT"), ArchitectureDiagramKind.C4_CONTEXT),
                nodes, edges, stringOrDefault(object, "notes", ""));
    }

    private ArchitectureNode readArchitectureNode(Map<String, Object> object) {
        return new ArchitectureNode(
                string(object, "id"),
                enumValue(ArchitectureNodeKind.class, stringOrDefault(object, "kind", "SOFTWARE_SYSTEM"), ArchitectureNodeKind.SOFTWARE_SYSTEM),
                stringOrDefault(object, "displayName", string(object, "id")),
                stringOrDefault(object, "technology", ""),
                stringOrDefault(object, "owner", ""),
                stringOrDefault(object, "environment", ""),
                stringOrDefault(object, "description", ""),
                stringOrDefault(object, "notes", ""),
                (int) numberOrDefault(object, "orderIndex", 0));
    }

    private ArchitectureEdge readArchitectureEdge(Map<String, Object> object) {
        return new ArchitectureEdge(
                string(object, "id"),
                stringOrDefault(object, "sourceNodeId", stringOrDefault(object, "source", "")),
                stringOrDefault(object, "targetNodeId", stringOrDefault(object, "target", "")),
                enumValue(ArchitectureEdgeKind.class, stringOrDefault(object, "kind", "DEPENDS_ON"), ArchitectureEdgeKind.DEPENDS_ON),
                stringOrDefault(object, "label", ""),
                stringOrDefault(object, "protocol", ""),
                stringOrDefault(object, "notes", ""));
    }

    FreeGraphDocument readFreeGraph(Map<String, Object> object) {
        ArrayList<FreeGraphNode> nodes = new ArrayList<>();
        for (Object value : array(object, "nodes")) {
            nodes.add(readFreeGraphNode(asObject(value, "freeGraphNode")));
        }
        ArrayList<FreeGraphEdge> edges = new ArrayList<>();
        for (Object value : array(object, "edges")) {
            edges.add(readFreeGraphEdge(asObject(value, "freeGraphEdge")));
        }
        return new FreeGraphDocument(
                stringOrDefault(object, "projectName", "Grafo libre"),
                stringOrDefault(object, "version", "borrador"),
                java.time.LocalDate.parse(stringOrDefault(object, "documentDate", java.time.LocalDate.now().toString())),
                enumValue(FreeGraphKind.class, stringOrDefault(object, "graphKind", "MIXED"), FreeGraphKind.MIXED),
                nodes,
                edges,
                stringOrDefault(object, "notes", ""));
    }

    private FreeGraphNode readFreeGraphNode(Map<String, Object> object) {
        return new FreeGraphNode(
                string(object, "id"),
                stringOrDefault(object, "title", string(object, "id")),
                stringOrDefault(object, "content", ""),
                (int) numberOrDefault(object, "orderIndex", 0));
    }

    private FreeGraphEdge readFreeGraphEdge(Map<String, Object> object) {
        return new FreeGraphEdge(
                string(object, "id"),
                stringOrDefault(object, "sourceNodeId", stringOrDefault(object, "source", "")),
                stringOrDefault(object, "targetNodeId", stringOrDefault(object, "target", "")),
                enumValue(FreeGraphEdgeDirection.class,
                        stringOrDefault(object, "direction", "DIRECTED"),
                        FreeGraphEdgeDirection.DIRECTED),
                stringOrDefault(object, "label", ""),
                stringOrDefault(object, "notes", ""));
    }

    LogicalBusinessGraphDocument readLogicalBusinessGraph(Map<String, Object> object) {
        ArrayList<LogicalBusinessGraphNode> nodes = new ArrayList<>();
        for (Object value : array(object, "nodes")) {
            nodes.add(readLogicalBusinessGraphNode(asObject(value, "logicalBusinessGraphNode")));
        }
        ArrayList<LogicalBusinessGraphEdge> edges = new ArrayList<>();
        for (Object value : array(object, "edges")) {
            edges.add(readLogicalBusinessGraphEdge(asObject(value, "logicalBusinessGraphEdge")));
        }
        return new LogicalBusinessGraphDocument(
                stringOrDefault(object, "projectName", "Grafo lógico del negocio"),
                stringOrDefault(object, "version", "borrador"),
                java.time.LocalDate.parse(stringOrDefault(object, "documentDate", java.time.LocalDate.now().toString())),
                nodes,
                edges,
                stringOrDefault(object, "notes", ""));
    }

    private LogicalBusinessGraphNode readLogicalBusinessGraphNode(Map<String, Object> object) {
        String code = string(object, "code");
        return new LogicalBusinessGraphNode(
                code,
                enumValue(LogicalBusinessGraphNodeKind.class,
                        stringOrDefault(object, "kind", LogicalBusinessGraphNodeKind.fromCode(code)
                                .orElse(LogicalBusinessGraphNodeKind.USE_CASE).name()),
                        LogicalBusinessGraphNodeKind.USE_CASE),
                stringOrDefault(object, "title", code),
                stringOrDefault(object, "description", ""),
                enumValue(LogicalBusinessGraphNodeStatus.class,
                        stringOrDefault(object, "status", "DRAFT"),
                        LogicalBusinessGraphNodeStatus.DRAFT),
                stringList(array(object, "sourceReferenceIds")));
    }

    private LogicalBusinessGraphEdge readLogicalBusinessGraphEdge(Map<String, Object> object) {
        return new LogicalBusinessGraphEdge(
                string(object, "id"),
                stringOrDefault(object, "sourceCode", stringOrDefault(object, "source", "")),
                enumValue(LogicalBusinessGraphRelationKind.class,
                        stringOrDefault(object, "relationKind", "DEPENDS_ON"),
                        LogicalBusinessGraphRelationKind.DEPENDS_ON),
                stringOrDefault(object, "targetCode", stringOrDefault(object, "target", "")),
                stringOrDefault(object, "description", ""));
    }


    private List<String> stringList(List<Object> values) {
        List<String> result = new ArrayList<>();
        for (Object value : values) {
            String normalized = String.valueOf(value).strip();
            if (!normalized.isBlank()) {
                result.add(normalized);
            }
        }
        return result;
    }

    private <E extends Enum<E>> java.util.Set<E> enumSet(Class<E> enumType, List<Object> values) {
        java.util.LinkedHashSet<E> result = new java.util.LinkedHashSet<>();
        for (Object value : values) {
            E enumValue = enumValue(enumType, String.valueOf(value), null);
            if (enumValue != null) {
                result.add(enumValue);
            }
        }
        return result;
    }

    private List<Object> array(Map<String, Object> parent, String key) {
        Object value = parent.get(key);
        if (value == null) {
            return List.of();
        }
        if (value instanceof List<?> list) {
            return new ArrayList<>(list);
        }
        throw new IllegalArgumentException("Se esperaba arreglo en " + key);
    }

    private Map<String, Object> asObject(Object value, String context) {
        if (value instanceof Map<?, ?> map) {
            LinkedHashMap<String, Object> result = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                result.put(String.valueOf(entry.getKey()), entry.getValue());
            }
            return result;
        }
        throw new IllegalArgumentException("Se esperaba objeto JSON en " + context);
    }

    private String string(Map<String, Object> object, String key) {
        Object value = object.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Campo obligatorio ausente: " + key);
        }
        return String.valueOf(value);
    }

    private String stringOrDefault(Map<String, Object> object, String key, String defaultValue) {
        Object value = object.get(key);
        return value == null ? defaultValue : String.valueOf(value);
    }

    private double numberOrDefault(Map<String, Object> object, String key, double defaultValue) {
        Object value = object.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        try {
            return Double.parseDouble(String.valueOf(value));
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }

    private boolean boolOrDefault(Map<String, Object> object, String key, boolean defaultValue) {
        Object value = object.get(key);
        return value == null ? defaultValue : booleanValue(value);
    }

    private boolean booleanValue(Object value) {
        if (value instanceof Boolean bool) {
            return bool;
        }
        return Boolean.parseBoolean(String.valueOf(value));
    }

    private <E extends Enum<E>> E enumValue(Class<E> enumType, String rawValue, E fallback) {
        if (rawValue == null || rawValue.isBlank()) {
            return fallback;
        }
        try {
            return Enum.valueOf(enumType, rawValue.trim().toUpperCase(java.util.Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            return fallback;
        }
    }
}
