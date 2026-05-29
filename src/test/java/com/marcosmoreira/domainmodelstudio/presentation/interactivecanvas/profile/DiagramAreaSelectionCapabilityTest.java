package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;
import org.junit.jupiter.api.Test;

class DiagramAreaSelectionCapabilityTest {

    @Test
    void everyVisualDiagramProfileSupportsRectangleSelection() {
        List<DiagramTypeId> visualDiagramTypes = List.of(
                DiagramTypeId.CONCEPTUAL_MODEL,
                DiagramTypeId.BPMN_BASIC,
                DiagramTypeId.OPERATIONAL_FLOW,
                DiagramTypeId.C4_CONTEXT,
                DiagramTypeId.C4_CONTAINERS,
                DiagramTypeId.TECHNICAL_DEPLOYMENT,
                DiagramTypeId.UML_USE_CASE,
                DiagramTypeId.UML_CLASS,
                DiagramTypeId.UML_ACTIVITY,
                DiagramTypeId.UML_SEQUENCE,
                DiagramTypeId.UML_STATE,
                DiagramTypeId.ADMIN_MODULE_MAP,
                DiagramTypeId.SCREEN_FLOW,
                DiagramTypeId.ADMIN_WIREFRAMES
        );

        for (DiagramTypeId diagramType : visualDiagramTypes) {
            assertTrue(
                    DiagramInteractionProfileResolver.resolve(diagramType).supportsAreaSelection(),
                    () -> "El tipo visual debe soportar selección rectangular: " + diagramType.value()
            );
        }
    }

    @Test
    void structuredNonDiagramProfilesDoNotExposeCanvasAreaSelection() {
        assertFalse(DiagramInteractionProfileResolver.resolve(DiagramTypeId.DATA_DICTIONARY).supportsAreaSelection());
        assertFalse(DiagramInteractionProfileResolver.resolve(DiagramTypeId.ROLES_PERMISSIONS_MAP).supportsAreaSelection());
    }
}
