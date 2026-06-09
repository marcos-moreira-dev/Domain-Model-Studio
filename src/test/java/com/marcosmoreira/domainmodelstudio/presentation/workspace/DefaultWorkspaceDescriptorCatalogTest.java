package com.marcosmoreira.domainmodelstudio.presentation.workspace;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Pruebas del catálogo común de metadata para workspaces del shell. */
class DefaultWorkspaceDescriptorCatalogTest {

    private final DefaultWorkspaceDescriptorCatalog catalog = new DefaultWorkspaceDescriptorCatalog();

    @Test
    void everyWorkspaceKindMustHaveDescriptor() {
        for (WorkspaceKind kind : WorkspaceKind.values()) {
            assertDoesNotThrow(() -> catalog.descriptorFor(kind), kind.name());
        }
        assertEquals(WorkspaceKind.values().length, catalog.findAll().size());
    }

    @Test
    void noWorkspaceUsesGenericConceptualSidePanelsAfterConceptualMigration() {
        for (WorkspaceKind kind : WorkspaceKind.values()) {
            assertFalse(catalog.descriptorFor(kind).usesGenericConceptualSidePanels(), kind.name());
        }
    }

    @Test
    void descriptorTextsMustBeUsefulForProductMessages() {
        for (WorkspaceDescriptor descriptor : catalog.findAll()) {
            assertFalse(descriptor.displayName().isBlank(), descriptor.workspaceKind().name());
            assertFalse(descriptor.panelUnavailableMessage().isBlank(), descriptor.workspaceKind().name());
        }
    }

    @Test
    void routePolicyMustRespectPlaceholderBeforeGenericPanels() {
        WorkspaceRoute route = new WorkspaceRoute(null, WorkspaceKind.PLACEHOLDER_GUIDE, true);

        assertFalse(catalog.usesGenericConceptualSidePanels(route));
        assertTrue(catalog.panelUnavailableMessage(route, "fallback").contains("guía"));
    }
}
