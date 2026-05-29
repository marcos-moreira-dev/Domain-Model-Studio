package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.presentation.toolbar.DefaultDiagramToolbarActionProvider;
import com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarAction;
import com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionId;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/** Guardarraíl anti-fachada: exportaciones prometidas por catálogo deben estar reflejadas en toolbar. */
class Tanda32ToolbarCapabilityAntiFacadeTest {

    private final DefaultDiagramTypeRegistry registry = new DefaultDiagramTypeRegistry();
    private final DefaultDiagramToolbarActionProvider toolbar = new DefaultDiagramToolbarActionProvider();

    @Test
    void exportCapabilitiesShouldMatchContextualToolbarActions() {
        for (DiagramTypeDescriptor type : registry.findAll()) {
            Set<DiagramToolbarActionId> actionIds = toolbar.actionsFor(type.id()).stream()
                    .map(DiagramToolbarAction::id)
                    .collect(Collectors.toUnmodifiableSet());

            assertExportAction(type, DiagramCapability.EXPORT_SVG, DiagramToolbarActionId.EXPORT_SVG, actionIds);
            assertExportAction(type, DiagramCapability.EXPORT_PNG, DiagramToolbarActionId.EXPORT_PNG, actionIds);
            assertExportAction(type, DiagramCapability.EXPORT_MARKDOWN, DiagramToolbarActionId.EXPORT_MARKDOWN, actionIds);
            assertExportAction(type, DiagramCapability.EXPORT_PDF, DiagramToolbarActionId.EXPORT_DICTIONARY_PDF, actionIds);
        }
    }

    @Test
    void logicalBusinessShouldRemainMarkdownOnlyInToolbar() {
        DiagramTypeDescriptor type = registry.findById(com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId.LOGICAL_BUSINESS_INTAKE)
                .orElseThrow();
        Set<DiagramToolbarActionId> actionIds = toolbar.actionsFor(type.id()).stream()
                .map(DiagramToolbarAction::id)
                .collect(Collectors.toUnmodifiableSet());

        assertTrue(actionIds.contains(DiagramToolbarActionId.EXPORT_MARKDOWN));
        assertFalse(actionIds.contains(DiagramToolbarActionId.EXPORT_SVG));
        assertFalse(actionIds.contains(DiagramToolbarActionId.EXPORT_PNG));
        assertFalse(actionIds.contains(DiagramToolbarActionId.EXPORT_DICTIONARY_PDF));
    }

    private static void assertExportAction(
            DiagramTypeDescriptor type,
            DiagramCapability capability,
            DiagramToolbarActionId actionId,
            Set<DiagramToolbarActionId> actionIds
    ) {
        if (type.supports(capability)) {
            assertTrue(
                    actionIds.contains(actionId),
                    () -> type.id().value() + " promete " + capability + " pero no expone " + actionId + " en toolbar");
        } else {
            assertFalse(
                    actionIds.contains(actionId),
                    () -> type.id().value() + " expone " + actionId + " sin prometer " + capability + " en catálogo");
        }
    }
}
