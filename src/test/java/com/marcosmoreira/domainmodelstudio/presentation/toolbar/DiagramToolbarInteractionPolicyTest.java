package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.StandardDiagramInteractionProfile;
import org.junit.jupiter.api.Test;

class DiagramToolbarInteractionPolicyTest {

    private final DiagramToolbarInteractionPolicy policy = new DiagramToolbarInteractionPolicy();

    @Test
    void sequenceAllowsTemporalMessageActionsButRejectsFreeBendPointDeletion() {
        assertTrue(policy.shouldExpose(StandardDiagramInteractionProfile.SEQUENCE, DiagramToolbarActionId.ADD_SEQUENCE_MESSAGE));
        assertTrue(policy.shouldExpose(StandardDiagramInteractionProfile.SEQUENCE, DiagramToolbarActionId.ADD_SEQUENCE_PARTICIPANT));
        assertFalse(policy.shouldExpose(StandardDiagramInteractionProfile.SEQUENCE, DiagramToolbarActionId.DELETE_SELECTED_BEND_POINT));
    }

    @Test
    void documentProfileAllowsDictionaryActionsButRejectsGraphActions() {
        assertTrue(policy.shouldExpose(StandardDiagramInteractionProfile.DOCUMENT, DiagramToolbarActionId.ADD_DICTIONARY_ENTITY));
        assertFalse(policy.shouldExpose(StandardDiagramInteractionProfile.DOCUMENT, DiagramToolbarActionId.ADD_RELATIONSHIP));
        assertFalse(policy.shouldExpose(StandardDiagramInteractionProfile.DOCUMENT, DiagramToolbarActionId.ADD_ROLE));
    }

    @Test
    void matrixProfileAllowsRolesActionsButRejectsCanvasActions() {
        assertTrue(policy.shouldExpose(StandardDiagramInteractionProfile.MATRIX, DiagramToolbarActionId.ADD_ROLE));
        assertFalse(policy.shouldExpose(StandardDiagramInteractionProfile.MATRIX, DiagramToolbarActionId.ADD_UML_CLASS));
        assertFalse(policy.shouldExpose(StandardDiagramInteractionProfile.MATRIX, DiagramToolbarActionId.DELETE_SELECTED_BEND_POINT));
    }

    @Test
    void wireframeProfileAllowsMaquetteActionsButRejectsConnectorActions() {
        assertTrue(policy.shouldExpose(StandardDiagramInteractionProfile.WIREFRAME, DiagramToolbarActionId.ADD_WIREFRAME_SCREEN));
        assertFalse(policy.shouldExpose(StandardDiagramInteractionProfile.WIREFRAME, DiagramToolbarActionId.ADD_RELATIONSHIP));
    }
}
