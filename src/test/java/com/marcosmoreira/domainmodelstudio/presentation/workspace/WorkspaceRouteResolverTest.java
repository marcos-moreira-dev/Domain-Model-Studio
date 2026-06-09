package com.marcosmoreira.domainmodelstudio.presentation.workspace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import org.junit.jupiter.api.Test;

/** Verifica que el shell pueda derivar workspace desde la pestaña/proyecto activo. */
class WorkspaceRouteResolverTest {

    private final WorkspaceRouteResolver resolver = new WorkspaceRouteResolver();

    @Test
    void nullTypeWithoutPlaceholderRoutesToWelcomeHome() {
        WorkspaceRoute route = resolver.resolve(null, false);

        assertEquals(WorkspaceKind.WELCOME_HOME, route.workspaceKind());
        assertFalse(route.usesPlaceholderWorkspace());
        assertFalse(route.usesGenericConceptualSidePanels());
        assertFalse(route.usesSpecializedWorkspace());
    }

    @Test
    void homeTabWinsWhenNoPlaceholderIsActive() {
        WorkspaceRoute route = resolver.resolve(DiagramTypeId.CONCEPTUAL_MODEL, false, true);

        assertEquals(WorkspaceKind.WELCOME_HOME, route.workspaceKind());
        assertFalse(route.usesPlaceholderWorkspace());
        assertFalse(route.usesGenericConceptualSidePanels());
    }

    @Test
    void placeholderStateWinsOverDiagramType() {
        WorkspaceRoute route = resolver.resolve(DiagramTypeId.CONCEPTUAL_MODEL, true);

        assertEquals(WorkspaceKind.PLACEHOLDER_GUIDE, route.workspaceKind());
        assertTrue(route.usesPlaceholderWorkspace());
        assertFalse(route.usesGenericConceptualSidePanels());
    }

    @Test
    void conceptualModelRoutesToCommonWorkspaceWithoutGenericLegacyPanels() {
        WorkspaceRoute route = resolver.resolve(DiagramTypeId.CONCEPTUAL_MODEL, false);

        assertEquals(WorkspaceKind.CONCEPTUAL_CANVAS, route.workspaceKind());
        assertFalse(route.usesGenericConceptualSidePanels());
        assertTrue(route.usesSpecializedWorkspace());
    }

    @Test
    void useCaseDiagramRoutesToSpecializedBehaviorWorkspace() {
        WorkspaceRoute route = resolver.resolve(DiagramTypeId.UML_USE_CASE, false);

        assertEquals(WorkspaceKind.BEHAVIOR_DIAGRAM, route.workspaceKind());
        assertFalse(route.usesGenericConceptualSidePanels());
        assertTrue(route.usesSpecializedWorkspace());
    }
}
