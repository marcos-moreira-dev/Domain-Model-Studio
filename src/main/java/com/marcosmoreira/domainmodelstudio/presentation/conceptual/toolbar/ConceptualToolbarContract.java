package com.marcosmoreira.domainmodelstudio.presentation.conceptual.toolbar;

import com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionId;
import java.util.List;
import java.util.Set;

/**
 * Contrato transversal de acciones contextuales del modelo conceptual.
 *
 * <p>El modelo conceptual ya se monta dentro del workspace común y expone sus acciones por la
 * toolbar contextual compartida. Este contrato mantiene visible qué comandos conceptuales forman
 * parte de la superficie común sin mover todavía el render Chen/Crow's Foot ni el canvas legacy.</p>
 */
public final class ConceptualToolbarContract {

    private static final List<DiagramToolbarActionId> ACTIONS = List.of(
            DiagramToolbarActionId.ADD_ENTITY,
            DiagramToolbarActionId.ADD_ATTRIBUTE,
            DiagramToolbarActionId.ADD_RELATIONSHIP,
            DiagramToolbarActionId.DUPLICATE_ELEMENT,
            DiagramToolbarActionId.DELETE_ELEMENT,
            DiagramToolbarActionId.DELETE_SELECTED_BEND_POINT,
            DiagramToolbarActionId.VALIDATE_MODEL,
            DiagramToolbarActionId.REORGANIZE_DIAGRAM,
            DiagramToolbarActionId.SWITCH_TO_CHEN,
            DiagramToolbarActionId.SWITCH_TO_CROWS_FOOT,
            DiagramToolbarActionId.ZOOM_IN,
            DiagramToolbarActionId.ZOOM_OUT,
            DiagramToolbarActionId.RESET_ZOOM,
            DiagramToolbarActionId.FIT_TO_CONTENT,
            DiagramToolbarActionId.CENTER_DIAGRAM,
            DiagramToolbarActionId.CENTER_SELECTION,
            DiagramToolbarActionId.EXPORT_SVG,
            DiagramToolbarActionId.EXPORT_MARKDOWN,
            DiagramToolbarActionId.EXPORT_PNG
    );

    private static final Set<DiagramToolbarActionId> ACTION_SET = Set.copyOf(ACTIONS);

    private ConceptualToolbarContract() {
    }

    public static List<DiagramToolbarActionId> actions() {
        return ACTIONS;
    }

    public static boolean isConceptualToolbarAction(DiagramToolbarActionId actionId) {
        return ACTION_SET.contains(actionId);
    }
}
