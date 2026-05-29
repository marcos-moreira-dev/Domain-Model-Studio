package com.marcosmoreira.domainmodelstudio.presentation.workspace;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramWorkspaceKind;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Política central de ruteo entre tipo de diagrama y familia de workspace.
 *
 * <p>La tabla de ruteo se deriva del catálogo oficial de tipos. Esta clase solo
 * traduce el contrato de producto a la familia visual de presentación.</p>
 */
public final class WorkspaceTypeRoutingPolicy {

    private final Map<DiagramTypeId, WorkspaceKind> workspaceByType;

    public WorkspaceTypeRoutingPolicy() {
        this(new DefaultDiagramTypeRegistry());
    }

    public WorkspaceTypeRoutingPolicy(DiagramTypeRegistry diagramTypeRegistry) {
        Objects.requireNonNull(diagramTypeRegistry, "diagramTypeRegistry");
        this.workspaceByType = diagramTypeRegistry.findAll().stream()
                .collect(Collectors.toMap(
                        DiagramTypeDescriptor::id,
                        descriptor -> toPresentationWorkspace(descriptor.workspaceKind()),
                        (left, right) -> left,
                        LinkedHashMap::new));
    }

    public WorkspaceKind kindOf(DiagramTypeId diagramTypeId) {
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        return workspaceByType.getOrDefault(diagramTypeId, WorkspaceKind.PLACEHOLDER_GUIDE);
    }

    public boolean usesGenericConceptualSidePanels(DiagramTypeId diagramTypeId) {
        return false;
    }

    public boolean usesSpecializedWorkspace(DiagramTypeId diagramTypeId) {
        WorkspaceKind kind = kindOf(diagramTypeId);
        return kind != WorkspaceKind.WELCOME_HOME && kind != WorkspaceKind.PLACEHOLDER_GUIDE;
    }

    public boolean isBehaviorDiagram(DiagramTypeId diagramTypeId) {
        return kindOf(diagramTypeId) == WorkspaceKind.BEHAVIOR_DIAGRAM;
    }

    public Map<DiagramTypeId, WorkspaceKind> workspaceKindsByType() {
        return Map.copyOf(workspaceByType);
    }

    private static WorkspaceKind toPresentationWorkspace(DiagramWorkspaceKind workspaceKind) {
        return switch (workspaceKind) {
            case CONCEPTUAL_CANVAS -> WorkspaceKind.CONCEPTUAL_CANVAS;
            case DATA_DICTIONARY_DOCUMENT -> WorkspaceKind.DATA_DICTIONARY_DOCUMENT;
            case LOGICAL_BUSINESS_DOCUMENT -> WorkspaceKind.LOGICAL_BUSINESS_DOCUMENT;
            case MODULE_MAP_DIAGRAM -> WorkspaceKind.MODULE_MAP_DIAGRAM;
            case UML_CLASS_DIAGRAM -> WorkspaceKind.UML_CLASS_DIAGRAM;
            case ROLES_PERMISSIONS_MATRIX -> WorkspaceKind.ROLES_PERMISSIONS_MATRIX;
            case SCREEN_FLOW_DIAGRAM -> WorkspaceKind.SCREEN_FLOW_DIAGRAM;
            case WIREFRAME_DIAGRAM -> WorkspaceKind.WIREFRAME_DIAGRAM;
            case ARCHITECTURE_DIAGRAM -> WorkspaceKind.ARCHITECTURE_DIAGRAM;
            case BEHAVIOR_DIAGRAM -> WorkspaceKind.BEHAVIOR_DIAGRAM;
            case FREE_GRAPH_DIAGRAM -> WorkspaceKind.FREE_GRAPH_DIAGRAM;
            case LOGICAL_BUSINESS_GRAPH_DIAGRAM -> WorkspaceKind.LOGICAL_BUSINESS_GRAPH_DIAGRAM;
            case PLACEHOLDER_GUIDE -> WorkspaceKind.PLACEHOLDER_GUIDE;
        };
    }
}
