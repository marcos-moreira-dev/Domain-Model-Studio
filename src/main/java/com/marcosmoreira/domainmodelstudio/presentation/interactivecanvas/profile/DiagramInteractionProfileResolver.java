package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramWorkspaceKind;
import java.util.Objects;

/**
 * Resuelve el perfil de interacción apropiado para el tipo o workspace activo.
 *
 * <p>El tipo de diagrama tiene prioridad porque distingue casos especiales dentro de
 * una misma familia de workspace; por ejemplo, UML Secuencia vive en la familia de
 * comportamiento, pero no debe comportarse como grafo libre normal.</p>
 */
public final class DiagramInteractionProfileResolver {

    private DiagramInteractionProfileResolver() {
        // Utilidad estática.
    }

    public static DiagramInteractionProfile resolve(DiagramTypeId diagramTypeId) {
        Objects.requireNonNull(diagramTypeId, "El tipo de diagrama no puede ser null");
        if (DiagramTypeId.UML_SEQUENCE.equals(diagramTypeId)) {
            return StandardDiagramInteractionProfile.SEQUENCE;
        }
        if (DiagramTypeId.ADMIN_WIREFRAMES.equals(diagramTypeId)) {
            return StandardDiagramInteractionProfile.WIREFRAME;
        }
        if (DiagramTypeId.ROLES_PERMISSIONS_MAP.equals(diagramTypeId)) {
            return StandardDiagramInteractionProfile.MATRIX;
        }
        if (DiagramTypeId.DATA_DICTIONARY.equals(diagramTypeId)
                || DiagramTypeId.LOGICAL_BUSINESS_INTAKE.equals(diagramTypeId)) {
            return StandardDiagramInteractionProfile.DOCUMENT;
        }
        return StandardDiagramInteractionProfile.GRAPH;
    }

    public static DiagramInteractionProfile resolve(DiagramWorkspaceKind workspaceKind) {
        Objects.requireNonNull(workspaceKind, "La familia de workspace no puede ser null");
        return switch (workspaceKind) {
            case DATA_DICTIONARY_DOCUMENT, LOGICAL_BUSINESS_DOCUMENT -> StandardDiagramInteractionProfile.DOCUMENT;
            case ROLES_PERMISSIONS_MATRIX -> StandardDiagramInteractionProfile.MATRIX;
            case WIREFRAME_DIAGRAM -> StandardDiagramInteractionProfile.WIREFRAME;
            case PLACEHOLDER_GUIDE -> StandardDiagramInteractionProfile.READ_ONLY_REFERENCE;
            case CONCEPTUAL_CANVAS, MODULE_MAP_DIAGRAM, UML_CLASS_DIAGRAM, SCREEN_FLOW_DIAGRAM,
                    ARCHITECTURE_DIAGRAM, BEHAVIOR_DIAGRAM, FREE_GRAPH_DIAGRAM,
                    LOGICAL_BUSINESS_GRAPH_DIAGRAM -> StandardDiagramInteractionProfile.GRAPH;
        };
    }

    public static DiagramInteractionProfile resolve(DiagramTypeId diagramTypeId, DiagramWorkspaceKind workspaceKind) {
        if (diagramTypeId != null) {
            return resolve(diagramTypeId);
        }
        return resolve(workspaceKind);
    }
}
