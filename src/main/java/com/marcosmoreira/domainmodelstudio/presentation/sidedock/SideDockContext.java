package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.DiagramInteractionProfile;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.DiagramInteractionProfileResolver;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.StandardDiagramInteractionProfile;
import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceKind;
import java.util.Objects;
import java.util.Optional;

/**
 * Contexto activo que decide qué módulos laterales son válidos.
 *
 * <p>Se calcula desde la pestaña o área de trabajo activa. No representa un estado global
 * fijo del shell; cada cambio de tab debe producir o refrescar un contexto equivalente.</p>
 */
public record SideDockContext(
        WorkspaceKind workspaceKind,
        DiagramTypeId diagramTypeId,
        DiagramInteractionProfile interactionProfile,
        String title
) {

    public SideDockContext {
        Objects.requireNonNull(workspaceKind, "workspaceKind");
        interactionProfile = Objects.requireNonNullElseGet(
                interactionProfile,
                () -> resolveWorkspaceProfile(workspaceKind)
        );
        title = title == null ? "" : title.strip();
    }

    public static SideDockContext forWorkspace(WorkspaceKind workspaceKind, String title) {
        return new SideDockContext(
                workspaceKind,
                null,
                resolveWorkspaceProfile(workspaceKind),
                title
        );
    }

    public static SideDockContext forDiagram(DiagramTypeId diagramTypeId, WorkspaceKind workspaceKind, String title) {
        return new SideDockContext(
                workspaceKind,
                diagramTypeId,
                diagramTypeId == null ? resolveWorkspaceProfile(workspaceKind) : DiagramInteractionProfileResolver.resolve(diagramTypeId),
                title
        );
    }

    public Optional<DiagramTypeId> diagramType() {
        return Optional.ofNullable(diagramTypeId);
    }

    public boolean graphLike() {
        return interactionProfile.supportsConnectorSelection() || interactionProfile.supportsNodeDragging();
    }

    public boolean documentLike() {
        return interactionProfile.supportsDocumentEditing();
    }

    public boolean matrixLike() {
        return interactionProfile.supportsMatrixEditing();
    }

    public boolean referenceLike() {
        return StandardDiagramInteractionProfile.READ_ONLY_REFERENCE.id().equals(interactionProfile.id());
    }

    private static DiagramInteractionProfile resolveWorkspaceProfile(WorkspaceKind workspaceKind) {
        return switch (workspaceKind) {
            case DATA_DICTIONARY_DOCUMENT, LOGICAL_BUSINESS_DOCUMENT -> StandardDiagramInteractionProfile.DOCUMENT;
            case ROLES_PERMISSIONS_MATRIX -> StandardDiagramInteractionProfile.MATRIX;
            case WIREFRAME_DIAGRAM -> StandardDiagramInteractionProfile.WIREFRAME;
            case PLACEHOLDER_GUIDE, WELCOME_HOME -> StandardDiagramInteractionProfile.READ_ONLY_REFERENCE;
            case CONCEPTUAL_CANVAS, MODULE_MAP_DIAGRAM, UML_CLASS_DIAGRAM, SCREEN_FLOW_DIAGRAM,
                    ARCHITECTURE_DIAGRAM, BEHAVIOR_DIAGRAM, FREE_GRAPH_DIAGRAM,
                    LOGICAL_BUSINESS_GRAPH_DIAGRAM -> StandardDiagramInteractionProfile.GRAPH;
        };
    }
}
