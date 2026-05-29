package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.DiagramInteractionProfile;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * Guardarraíl de interacción para no mostrar herramientas incompatibles con el perfil activo.
 *
 * <p>La política no reemplaza el catálogo de capacidades del producto. Solo evita que una
 * familia documental, matricial o temporal reciba herramientas propias de otro modo de trabajo.</p>
 */
public final class DiagramToolbarInteractionPolicy {

    private static final Set<DiagramToolbarActionId> DOCUMENT_ACTIONS = EnumSet.of(
            DiagramToolbarActionId.ADD_DICTIONARY_ENTITY,
            DiagramToolbarActionId.ADD_DICTIONARY_FIELD,
            DiagramToolbarActionId.REMOVE_DICTIONARY_ITEM,
            DiagramToolbarActionId.VALIDATE_DICTIONARY,
            DiagramToolbarActionId.EXPORT_DICTIONARY_PDF
    );

    private static final Set<DiagramToolbarActionId> MATRIX_ACTIONS = EnumSet.of(
            DiagramToolbarActionId.ADD_ROLE,
            DiagramToolbarActionId.ADD_PERMISSION,
            DiagramToolbarActionId.ADD_PERMISSION_ASSIGNMENT,
            DiagramToolbarActionId.REMOVE_ROLES_PERMISSIONS_ITEM,
            DiagramToolbarActionId.VALIDATE_ROLES_PERMISSIONS
    );

    private static final Set<DiagramToolbarActionId> WIREFRAME_ACTIONS = EnumSet.of(
            DiagramToolbarActionId.ADD_WIREFRAME_SCREEN,
            DiagramToolbarActionId.ADD_WIREFRAME_SECTION,
            DiagramToolbarActionId.ADD_WIREFRAME_FORM,
            DiagramToolbarActionId.ADD_WIREFRAME_TABLE,
            DiagramToolbarActionId.ADD_WIREFRAME_FIELD,
            DiagramToolbarActionId.ADD_WIREFRAME_BUTTON,
            DiagramToolbarActionId.APPLY_WIREFRAME_TEMPLATE,
            DiagramToolbarActionId.REMOVE_WIREFRAME_ITEM,
            DiagramToolbarActionId.VALIDATE_WIREFRAME
    );

    private static final Set<DiagramToolbarActionId> CONNECTOR_ACTIONS = EnumSet.of(
            DiagramToolbarActionId.ADD_RELATIONSHIP,
            DiagramToolbarActionId.ADD_MODULE_DEPENDENCY,
            DiagramToolbarActionId.ADD_UML_RELATION,
            DiagramToolbarActionId.ADD_SCREEN_TRANSITION,
            DiagramToolbarActionId.ADD_BEHAVIOR_FLOW,
            DiagramToolbarActionId.ADD_USE_CASE_ASSOCIATION,
            DiagramToolbarActionId.ADD_USE_CASE_INCLUDE,
            DiagramToolbarActionId.ADD_USE_CASE_EXTEND,
            DiagramToolbarActionId.ADD_USE_CASE_GENERALIZATION,
            DiagramToolbarActionId.ADD_SEQUENCE_MESSAGE,
            DiagramToolbarActionId.ADD_SEQUENCE_RETURN_MESSAGE,
            DiagramToolbarActionId.ADD_STATE_TRANSITION,
            DiagramToolbarActionId.ADD_ARCHITECTURE_USES,
            DiagramToolbarActionId.ADD_ARCHITECTURE_DEPENDENCY,
            DiagramToolbarActionId.ADD_ARCHITECTURE_INTEGRATION,
            DiagramToolbarActionId.ADD_ARCHITECTURE_CALL,
            DiagramToolbarActionId.ADD_ARCHITECTURE_READS_WRITES,
            DiagramToolbarActionId.ADD_DEPLOYMENT_CONNECTION,
            DiagramToolbarActionId.ADD_DEPLOYMENT_HOSTING,
            DiagramToolbarActionId.ADD_DEPLOYMENT_TARGET,
            DiagramToolbarActionId.FREE_GRAPH_ADD_EDGE_TOOL,
            DiagramToolbarActionId.ADD_FREE_GRAPH_EDGE
    );

    private static final Set<DiagramToolbarActionId> NODE_ACTIONS = EnumSet.of(
            DiagramToolbarActionId.ADD_ENTITY,
            DiagramToolbarActionId.ADD_ATTRIBUTE,
            DiagramToolbarActionId.DUPLICATE_ELEMENT,
            DiagramToolbarActionId.DELETE_ELEMENT,
            DiagramToolbarActionId.ADD_MODULE,
            DiagramToolbarActionId.ADD_SUBMODULE,
            DiagramToolbarActionId.ADD_UML_MODULE,
            DiagramToolbarActionId.ADD_UML_CLASS,
            DiagramToolbarActionId.ADD_UML_INTERFACE,
            DiagramToolbarActionId.ADD_UML_ENUM,
            DiagramToolbarActionId.ADD_UML_ATTRIBUTE,
            DiagramToolbarActionId.ADD_UML_METHOD,
            DiagramToolbarActionId.REMOVE_UML_ITEM,
            DiagramToolbarActionId.ADD_SCREEN,
            DiagramToolbarActionId.REMOVE_SCREEN_FLOW_ITEM,
            DiagramToolbarActionId.ADD_BPMN_START,
            DiagramToolbarActionId.ADD_BPMN_ACTIVITY,
            DiagramToolbarActionId.ADD_BPMN_DECISION,
            DiagramToolbarActionId.ADD_BPMN_END,
            DiagramToolbarActionId.ADD_BPMN_LANE,
            DiagramToolbarActionId.ADD_BEHAVIOR_NOTE,
            DiagramToolbarActionId.ADD_USE_CASE_ACTOR,
            DiagramToolbarActionId.ADD_USE_CASE,
            DiagramToolbarActionId.ADD_USE_CASE_SYSTEM,
            DiagramToolbarActionId.ADD_UML_ACTION,
            DiagramToolbarActionId.ADD_UML_DECISION,
            DiagramToolbarActionId.ADD_UML_INITIAL_STATE,
            DiagramToolbarActionId.ADD_UML_FINAL_STATE,
            DiagramToolbarActionId.ADD_SEQUENCE_PARTICIPANT,
            DiagramToolbarActionId.ADD_SEQUENCE_ACTIVATION,
            DiagramToolbarActionId.ADD_SEQUENCE_FRAGMENT,
            DiagramToolbarActionId.ADD_STATE,
            DiagramToolbarActionId.REMOVE_BEHAVIOR_ITEM,
            DiagramToolbarActionId.ADD_C4_PERSON,
            DiagramToolbarActionId.ADD_C4_SYSTEM,
            DiagramToolbarActionId.ADD_C4_EXTERNAL_SYSTEM,
            DiagramToolbarActionId.ADD_C4_BOUNDARY,
            DiagramToolbarActionId.ADD_C4_CONTAINER,
            DiagramToolbarActionId.ADD_C4_APPLICATION,
            DiagramToolbarActionId.ADD_C4_API,
            DiagramToolbarActionId.ADD_ARCHITECTURE_DATABASE,
            DiagramToolbarActionId.ADD_ARCHITECTURE_EXTERNAL_SERVICE,
            DiagramToolbarActionId.ADD_DEPLOYMENT_ENVIRONMENT,
            DiagramToolbarActionId.ADD_DEPLOYMENT_SERVER,
            DiagramToolbarActionId.ADD_DEPLOYMENT_CLIENT,
            DiagramToolbarActionId.ADD_DEPLOYMENT_SERVICE,
            DiagramToolbarActionId.ADD_DEPLOYMENT_NETWORK,
            DiagramToolbarActionId.ADD_DEPLOYMENT_ARTIFACT,
            DiagramToolbarActionId.REMOVE_ARCHITECTURE_ITEM,
            DiagramToolbarActionId.FREE_GRAPH_SELECT_TOOL,
            DiagramToolbarActionId.FREE_GRAPH_ADD_NODE_TOOL,
            DiagramToolbarActionId.ADD_FREE_GRAPH_NODE,
            DiagramToolbarActionId.REMOVE_FREE_GRAPH_ITEM
    );


    private static final Set<DiagramToolbarActionId> LAYER_ORDER_ACTIONS = EnumSet.of(
            DiagramToolbarActionId.BRING_SELECTION_TO_FRONT,
            DiagramToolbarActionId.SEND_SELECTION_TO_BACK,
            DiagramToolbarActionId.RAISE_SELECTION_LAYER,
            DiagramToolbarActionId.LOWER_SELECTION_LAYER
    );

    private static final Set<DiagramToolbarActionId> SIZE_ADJUSTMENT_ACTIONS = EnumSet.of(
            DiagramToolbarActionId.GROW_SELECTED_FIGURE,
            DiagramToolbarActionId.SHRINK_SELECTED_FIGURE
    );

    public boolean shouldExpose(DiagramInteractionProfile profile, DiagramToolbarActionId actionId) {
        Objects.requireNonNull(profile, "El perfil de interacción no puede ser null");
        Objects.requireNonNull(actionId, "La acción no puede ser null");
        if (DOCUMENT_ACTIONS.contains(actionId)) {
            return profile.supportsDocumentEditing() && !profile.supportsMatrixEditing();
        }
        if (MATRIX_ACTIONS.contains(actionId)) {
            return profile.supportsMatrixEditing();
        }
        if (WIREFRAME_ACTIONS.contains(actionId)) {
            return profile.supportsNodeResize();
        }
        if (actionId == DiagramToolbarActionId.DELETE_SELECTED_BEND_POINT) {
            return profile.supportsBendPoints();
        }
        if (LAYER_ORDER_ACTIONS.contains(actionId)) {
            return profile.supportsNodeDragging();
        }
        if (SIZE_ADJUSTMENT_ACTIONS.contains(actionId)) {
            return profile.supportsNodeDragging();
        }
        if (CONNECTOR_ACTIONS.contains(actionId)) {
            return profile.supportsConnectorSelection();
        }
        if (NODE_ACTIONS.contains(actionId)) {
            return profile.supportsNodeDragging() || profile.supportsTemporalOrdering();
        }
        return true;
    }
}
