package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramWorkspaceKind;
import org.junit.jupiter.api.Test;

class InteractionProfileContextTest {

    @Test
    void workspaceKindResolvesStructuredProfilesWhenTypeIsMissing() {
        assertEquals(
                StandardDiagramInteractionProfile.DOCUMENT,
                DiagramInteractionProfileResolver.resolve(DiagramWorkspaceKind.DATA_DICTIONARY_DOCUMENT)
        );
        assertEquals(
                StandardDiagramInteractionProfile.DOCUMENT,
                DiagramInteractionProfileResolver.resolve(DiagramWorkspaceKind.LOGICAL_BUSINESS_DOCUMENT)
        );
        assertEquals(
                StandardDiagramInteractionProfile.MATRIX,
                DiagramInteractionProfileResolver.resolve(DiagramWorkspaceKind.ROLES_PERMISSIONS_MATRIX)
        );
        assertEquals(
                StandardDiagramInteractionProfile.READ_ONLY_REFERENCE,
                DiagramInteractionProfileResolver.resolve(DiagramWorkspaceKind.PLACEHOLDER_GUIDE)
        );
    }

    @Test
    void diagramTypeWinsOverGenericWorkspaceFamily() {
        assertEquals(
                StandardDiagramInteractionProfile.SEQUENCE,
                DiagramInteractionProfileResolver.resolve(DiagramTypeId.UML_SEQUENCE, DiagramWorkspaceKind.BEHAVIOR_DIAGRAM)
        );
    }

    @Test
    void contextClassifiesFreeCanvasAndStructuredWorkspaces() {
        InteractionProfileContext graph = InteractionProfileContext.from(DiagramTypeId.UML_CLASS, DiagramWorkspaceKind.UML_CLASS_DIAGRAM);
        InteractionProfileContext matrix = InteractionProfileContext.from(DiagramTypeId.ROLES_PERMISSIONS_MAP, DiagramWorkspaceKind.ROLES_PERMISSIONS_MATRIX);

        assertTrue(graph.isFreeCanvas());
        assertFalse(graph.isStructuredWorkspace());
        assertFalse(matrix.isFreeCanvas());
        assertTrue(matrix.isStructuredWorkspace());
    }
}
