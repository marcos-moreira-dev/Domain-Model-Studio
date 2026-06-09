package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/** Guardarraíl para que la toolbar no prometa exportaciones ni acciones fuera de capacidad. */
class DefaultDiagramToolbarActionProviderCapabilityTest {

    private final DefaultDiagramToolbarActionProvider provider = new DefaultDiagramToolbarActionProvider();

    @Test
    void dataDictionaryToolbarShouldExposePdfButNotPng() {
        Set<DiagramToolbarActionId> actions = actionIds(DiagramTypeId.DATA_DICTIONARY);

        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_PDF));
        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_MARKDOWN));
        assertFalse(actions.contains(DiagramToolbarActionId.EXPORT_PNG));
    }

    @Test
    void behaviorToolbarShouldExposeSvgPngAndMarkdownButNotNotation() {
        Set<DiagramToolbarActionId> actions = actionIds(DiagramTypeId.UML_USE_CASE);

        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_PNG));
        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_MARKDOWN));
        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_SVG));
        assertFalse(actions.contains(DiagramToolbarActionId.SWITCH_TO_CHEN));
    }

    @Test
    void conceptualToolbarKeepsConceptualOnlyActions() {
        Set<DiagramToolbarActionId> actions = actionIds(DiagramTypeId.CONCEPTUAL_MODEL);

        assertTrue(actions.contains(DiagramToolbarActionId.SWITCH_TO_CHEN));
        assertTrue(actions.contains(DiagramToolbarActionId.REORGANIZE_DIAGRAM));
        assertTrue(actions.contains(DiagramToolbarActionId.DELETE_SELECTED_BEND_POINT));
        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_SVG));
        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_PNG));
    }


    @Test
    void wireframeToolbarMovesTemplateInsertionToToolbar() {
        Set<DiagramToolbarActionId> actions = actionIds(DiagramTypeId.ADMIN_WIREFRAMES);

        assertTrue(actions.contains(DiagramToolbarActionId.APPLY_WIREFRAME_TEMPLATE));
        assertTrue(actions.contains(DiagramToolbarActionId.ADD_WIREFRAME_SCREEN));
        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_PNG));
        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_SVG));
    }




    @Test
    void logicalBusinessToolbarShouldExposeSideDockActionsAndDocumentExports() {
        Set<DiagramToolbarActionId> actions = actionIds(DiagramTypeId.LOGICAL_BUSINESS_INTAKE);

        assertTrue(actions.contains(DiagramToolbarActionId.LOGICAL_BUSINESS_SHOW_STRUCTURE));
        assertTrue(actions.contains(DiagramToolbarActionId.LOGICAL_BUSINESS_SHOW_PROPERTIES));
        assertTrue(actions.contains(DiagramToolbarActionId.LOGICAL_BUSINESS_SHOW_VALIDATION));
        assertTrue(actions.contains(DiagramToolbarActionId.LOGICAL_BUSINESS_SHOW_TRACEABILITY));
        assertFalse(actions.contains(DiagramToolbarActionId.LOGICAL_BUSINESS_SHOW_DERIVATIONS));
        assertTrue(actions.contains(DiagramToolbarActionId.LOGICAL_BUSINESS_SHOW_HELP));
        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_PDF));
        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_MARKDOWN));
        assertFalse(actions.contains(DiagramToolbarActionId.EXPORT_PNG));
        assertFalse(actions.contains(DiagramToolbarActionId.EXPORT_SVG));
    }

    @Test
    void umlClassToolbarShouldExposeSourceCodeImportAction() {
        Set<DiagramToolbarActionId> actions = actionIds(DiagramTypeId.UML_CLASS);

        assertTrue(actions.contains(DiagramToolbarActionId.IMPORT_UML_FROM_SOURCE));
        assertTrue(actions.contains(DiagramToolbarActionId.OPEN_UML_SOURCE));
        assertFalse(actionIds(DiagramTypeId.ADMIN_WIREFRAMES).contains(DiagramToolbarActionId.IMPORT_UML_FROM_SOURCE));
        assertFalse(actionIds(DiagramTypeId.ADMIN_WIREFRAMES).contains(DiagramToolbarActionId.OPEN_UML_SOURCE));
    }

    @Test
    void visualGraphsWithAutoLayoutExposeAutoOrganizeAction() {
        assertTrue(actionIds(DiagramTypeId.ADMIN_MODULE_MAP).contains(DiagramToolbarActionId.REORGANIZE_DIAGRAM));
        assertTrue(actionIds(DiagramTypeId.UML_CLASS).contains(DiagramToolbarActionId.REORGANIZE_DIAGRAM));
        assertTrue(actionIds(DiagramTypeId.UML_USE_CASE).contains(DiagramToolbarActionId.REORGANIZE_DIAGRAM));
        assertTrue(actionIds(DiagramTypeId.BPMN_BASIC).contains(DiagramToolbarActionId.REORGANIZE_DIAGRAM));
        assertTrue(actionIds(DiagramTypeId.OPERATIONAL_FLOW).contains(DiagramToolbarActionId.REORGANIZE_DIAGRAM));
        assertTrue(actionIds(DiagramTypeId.UML_ACTIVITY).contains(DiagramToolbarActionId.REORGANIZE_DIAGRAM));
        assertTrue(actionIds(DiagramTypeId.UML_STATE).contains(DiagramToolbarActionId.REORGANIZE_DIAGRAM));
        assertTrue(actionIds(DiagramTypeId.UML_SEQUENCE).contains(DiagramToolbarActionId.REORGANIZE_DIAGRAM));
        assertTrue(actionIds(DiagramTypeId.C4_CONTEXT).contains(DiagramToolbarActionId.REORGANIZE_DIAGRAM));
        assertTrue(actionIds(DiagramTypeId.C4_CONTAINERS).contains(DiagramToolbarActionId.REORGANIZE_DIAGRAM));
    }



    @Test
    void freeGraphToolbarShouldExposeVisualCommonActionsAndExports() {
        Set<DiagramToolbarActionId> actions = actionIds(DiagramTypeId.FREE_GRAPH);

        assertTrue(actions.contains(DiagramToolbarActionId.FREE_GRAPH_ADD_NODE_TOOL));
        assertTrue(actions.contains(DiagramToolbarActionId.FREE_GRAPH_ADD_EDGE_TOOL));
        assertTrue(actions.contains(DiagramToolbarActionId.REORGANIZE_DIAGRAM));
        assertTrue(actions.contains(DiagramToolbarActionId.FIT_TO_CONTENT));
        assertTrue(actions.contains(DiagramToolbarActionId.CENTER_DIAGRAM));
        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_SVG));
        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_MARKDOWN));
        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_PNG));
    }

    @Test
    void technicalDeploymentToolbarShouldExposeNavigationAndExportsButNotAutoOrganize() {
        Set<DiagramToolbarActionId> actions = actionIds(DiagramTypeId.TECHNICAL_DEPLOYMENT);

        assertTrue(actions.contains(DiagramToolbarActionId.FIT_TO_CONTENT));
        assertTrue(actions.contains(DiagramToolbarActionId.CENTER_DIAGRAM));
        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_SVG));
        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_MARKDOWN));
        assertTrue(actions.contains(DiagramToolbarActionId.EXPORT_PNG));
        assertFalse(actions.contains(DiagramToolbarActionId.REORGANIZE_DIAGRAM));
    }

    private Set<DiagramToolbarActionId> actionIds(DiagramTypeId diagramTypeId) {
        return provider.actionsFor(diagramTypeId).stream()
                .map(DiagramToolbarAction::id)
                .collect(Collectors.toSet());
    }
}
