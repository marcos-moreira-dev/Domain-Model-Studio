package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/** Cobertura funcional de la tanda 13 para promesas visibles de toolbar por tipo activo. */
class Tanda13ToolbarProviderCoverageTest {

    private final DefaultDiagramToolbarActionProvider provider = new DefaultDiagramToolbarActionProvider();

    @Test
    void everyVisualDiagramWorkspaceShouldExposeNavigationAndRealVisualExports() {
        for (DiagramTypeId typeId : Set.of(
                DiagramTypeId.ADMIN_MODULE_MAP,
                DiagramTypeId.UML_CLASS,
                DiagramTypeId.SCREEN_FLOW,
                DiagramTypeId.ADMIN_WIREFRAMES,
                DiagramTypeId.BPMN_BASIC,
                DiagramTypeId.OPERATIONAL_FLOW,
                DiagramTypeId.UML_USE_CASE,
                DiagramTypeId.UML_ACTIVITY,
                DiagramTypeId.UML_SEQUENCE,
                DiagramTypeId.UML_STATE,
                DiagramTypeId.C4_CONTEXT,
                DiagramTypeId.C4_CONTAINERS,
                DiagramTypeId.TECHNICAL_DEPLOYMENT,
                DiagramTypeId.FREE_GRAPH)) {
            Set<DiagramToolbarActionId> actions = actionIds(typeId);

            assertTrue(actions.contains(DiagramToolbarActionId.FIT_TO_CONTENT), typeId + " debe poder ajustar vista.");
            assertTrue(actions.contains(DiagramToolbarActionId.CENTER_DIAGRAM), typeId + " debe poder centrar vista.");
            assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_SVG), typeId + " debe prometer SVG real.");
            assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_MARKDOWN), typeId + " debe prometer Markdown real.");
            assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_PNG), typeId + " debe prometer PNG real.");
        }
    }

    @Test
    void structuredDocumentsAndMatricesMustNotExposeCanvasNavigationPromises() {
        for (DiagramTypeId typeId : Set.of(
                DiagramTypeId.DATA_DICTIONARY,
                DiagramTypeId.ROLES_PERMISSIONS_MAP,
                DiagramTypeId.LOGICAL_BUSINESS_INTAKE)) {
            Set<DiagramToolbarActionId> actions = actionIds(typeId);

            assertFalse(actions.contains(DiagramToolbarActionId.FIT_TO_CONTENT), typeId + " no debe fingir navegación de canvas.");
            assertFalse(actions.contains(DiagramToolbarActionId.CENTER_DIAGRAM), typeId + " no debe fingir navegación de canvas.");
        }
    }

    @Test
    void technicalDeploymentIsExplicitlyNavigationAndExportOnlyForThisPhase() {
        Set<DiagramToolbarActionId> actions = actionIds(DiagramTypeId.TECHNICAL_DEPLOYMENT);

        assertTrue(actions.contains(DiagramToolbarActionId.FIT_TO_CONTENT));
        assertTrue(actions.contains(DiagramToolbarActionId.CENTER_DIAGRAM));
        assertTrue(actions.contains(DiagramToolbarActionId.VALIDATE_ARCHITECTURE_DIAGRAM));
        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_SVG));
        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_MARKDOWN));
        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_PNG));
        assertFalse(actions.contains(DiagramToolbarActionId.REORGANIZE_DIAGRAM));
    }

    @Test
    void logicalBusinessMustExposeOnlyItsSideDockActionsAndMarkdownExport() {
        Set<DiagramToolbarActionId> actions = actionIds(DiagramTypeId.LOGICAL_BUSINESS_INTAKE);

        assertTrue(actions.contains(DiagramToolbarActionId.LOGICAL_BUSINESS_SHOW_STRUCTURE));
        assertTrue(actions.contains(DiagramToolbarActionId.LOGICAL_BUSINESS_SHOW_PROPERTIES));
        assertTrue(actions.contains(DiagramToolbarActionId.LOGICAL_BUSINESS_SHOW_VALIDATION));
        assertTrue(actions.contains(DiagramToolbarActionId.LOGICAL_BUSINESS_SHOW_TRACEABILITY));
        assertFalse(actions.contains(DiagramToolbarActionId.LOGICAL_BUSINESS_SHOW_DERIVATIONS));
        assertTrue(actions.contains(DiagramToolbarActionId.LOGICAL_BUSINESS_SHOW_HELP));
        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_MARKDOWN));
        assertFalse(actions.contains(DiagramToolbarActionId.EXPORT_SVG));
        assertFalse(actions.contains(DiagramToolbarActionId.EXPORT_PNG));
    }

    private Set<DiagramToolbarActionId> actionIds(DiagramTypeId diagramTypeId) {
        return provider.actionsFor(diagramTypeId).stream()
                .map(DiagramToolbarAction::id)
                .collect(Collectors.toSet());
    }
}
