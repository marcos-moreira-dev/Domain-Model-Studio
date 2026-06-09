package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import org.junit.jupiter.api.Test;

class DiagramInteractionProfileResolverTest {

    @Test
    void graphDiagramUsesEditableGraphProfile() {
        DiagramInteractionProfile profile = DiagramInteractionProfileResolver.resolve(DiagramTypeId.UML_CLASS);

        assertEquals(StandardDiagramInteractionProfile.GRAPH, profile);
        assertTrue(profile.supportsBendPoints());
        assertTrue(profile.supportsConnectorLabels());
        assertFalse(profile.supportsEndpointDragging());
    }

    @Test
    void sequenceDiagramKeepsTemporalInteractionProfileAndAreaSelection() {
        DiagramInteractionProfile profile = DiagramInteractionProfileResolver.resolve(DiagramTypeId.UML_SEQUENCE);

        assertEquals(StandardDiagramInteractionProfile.SEQUENCE, profile);
        assertTrue(profile.supportsTemporalOrdering());
        assertTrue(profile.supportsAreaSelection());
        assertFalse(profile.supportsBendPoints());
        assertFalse(profile.supportsEndpointDragging());
        assertTrue(profile.supportsNodeResize());
    }

    @Test
    void documentAndMatrixTypesAreNotFreeCanvasProfiles() {
        assertEquals(
                StandardDiagramInteractionProfile.DOCUMENT,
                DiagramInteractionProfileResolver.resolve(DiagramTypeId.DATA_DICTIONARY)
        );
        assertEquals(
                StandardDiagramInteractionProfile.MATRIX,
                DiagramInteractionProfileResolver.resolve(DiagramTypeId.ROLES_PERMISSIONS_MAP)
        );
    }

    @Test
    void wireframesUseResizableMaquetteProfile() {
        DiagramInteractionProfile profile = DiagramInteractionProfileResolver.resolve(DiagramTypeId.ADMIN_WIREFRAMES);

        assertEquals(StandardDiagramInteractionProfile.WIREFRAME, profile);
        assertTrue(profile.supportsNodeResize());
        assertFalse(profile.supportsConnectorSelection());
    }
}
