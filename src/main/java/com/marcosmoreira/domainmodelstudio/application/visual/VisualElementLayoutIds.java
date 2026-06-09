package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import java.util.Objects;

/**
 * Convención única de IDs de layout para documentos visuales especializados.
 *
 * <p>El layout usa prefijos estables para no mezclar nombres visibles con identidad
 * técnica. El texto de una caja puede cambiar; estos IDs deben sobrevivir a renombres,
 * guardados {@code .dms} y exportaciones.</p>
 */
public final class VisualElementLayoutIds {

    private VisualElementLayoutIds() {
    }

    public static DiagramElementId module(String moduleId) {
        return prefixed("module", moduleId);
    }

    public static DiagramElementId dependency(String dependencyId) {
        return prefixed("dependency", dependencyId);
    }

    public static DiagramElementId moduleContainment(String parentModuleId, String childModuleId) {
        String parent = Objects.requireNonNull(parentModuleId, "parentModuleId").strip();
        String child = Objects.requireNonNull(childModuleId, "childModuleId").strip();
        if (parent.isBlank() || child.isBlank()) {
            throw new IllegalArgumentException("Los IDs padre/hijo para contención de módulos no pueden estar vacíos");
        }
        return prefixed("module-containment", parent + "->" + child);
    }

    public static DiagramElementId screen(String screenId) {
        return prefixed("screen", screenId);
    }

    public static DiagramElementId transition(String transitionId) {
        return prefixed("transition", transitionId);
    }

    public static DiagramElementId wireframeScreen(String screenId) {
        return prefixed("wireframe-screen", screenId);
    }

    public static DiagramElementId wireframeComponent(String componentId) {
        return prefixed("wireframe-component", componentId);
    }

    public static DiagramElementId umlModule(String moduleId) {
        return prefixed("uml-module", moduleId);
    }

    public static DiagramElementId umlClass(String classId) {
        return prefixed("uml-class", classId);
    }

    public static DiagramElementId umlRelation(String relationId) {
        return prefixed("uml-relation", relationId);
    }

    public static DiagramElementId behaviorNode(String nodeId) {
        return prefixed("behavior-node", nodeId);
    }

    public static DiagramElementId behaviorEdge(String edgeId) {
        return prefixed("behavior-edge", edgeId);
    }

    public static DiagramElementId architectureNode(String nodeId) {
        return prefixed("architecture-node", nodeId);
    }

    public static DiagramElementId architectureEdge(String edgeId) {
        return prefixed("architecture-edge", edgeId);
    }

    public static DiagramElementId freeGraphNode(String nodeId) {
        return prefixed("free-graph-node", nodeId);
    }

    public static DiagramElementId freeGraphEdge(String edgeId) {
        return prefixed("free-graph-edge", edgeId);
    }

    public static DiagramElementId logicalBusinessGraphNode(String nodeCode) {
        return prefixed("logical-business-graph-node", nodeCode);
    }

    public static DiagramElementId logicalBusinessGraphEdge(String edgeId) {
        return prefixed("logical-business-graph-edge", edgeId);
    }

    public static DiagramElementId visualComment(String commentId) {
        return prefixed("visual-comment", commentId);
    }

    public static boolean isVisualComment(DiagramElementId elementId) {
        return elementId != null && isVisualComment(elementId.value());
    }

    public static boolean isVisualComment(String elementId) {
        return elementId != null && elementId.strip().startsWith("visual-comment:");
    }

    public static String rawVisualCommentId(String elementId) {
        String normalized = elementId == null ? "" : elementId.strip();
        return normalized.startsWith("visual-comment:") ? normalized.substring("visual-comment:".length()).strip() : "";
    }

    public static DiagramElementId role(String roleId) {
        return prefixed("role", roleId);
    }

    public static DiagramElementId permission(String permissionId) {
        return prefixed("permission", permissionId);
    }

    public static DiagramElementId assignment(String assignmentId) {
        return prefixed("assignment", assignmentId);
    }

    private static DiagramElementId prefixed(String prefix, String rawId) {
        String normalized = Objects.requireNonNull(rawId, "rawId").strip();
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El ID visual no puede estar vacío para prefijo: " + prefix);
        }
        return DiagramElementId.of(prefix + ":" + normalized);
    }
}
