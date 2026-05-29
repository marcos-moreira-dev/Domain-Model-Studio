package com.marcosmoreira.domainmodelstudio.application.catalog;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapabilitySet;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import org.junit.jupiter.api.Test;

class DefaultDiagramCapabilityCatalogTest {


    @Test
    void logicalBusinessShouldExposeDocumentMvpWithoutFalseExports() {
        DiagramCapabilityCatalog catalog = new DefaultDiagramCapabilityCatalog();

        DiagramCapabilitySet capabilities = catalog.capabilitiesOf(DiagramTypeId.LOGICAL_BUSINESS_INTAKE);

        assertTrue(capabilities.has(DiagramCapability.CREATE_PROJECT));
        assertTrue(capabilities.has(DiagramCapability.SHOW_DOCUMENT_OUTPUT));
        assertTrue(capabilities.has(DiagramCapability.MANUAL_EDITING));
        assertTrue(capabilities.has(DiagramCapability.SAVE_DMS));
        assertTrue(capabilities.has(DiagramCapability.LOAD_DMS));
        assertTrue(capabilities.has(DiagramCapability.AI_RESOURCES));
        assertTrue(capabilities.has(DiagramCapability.THEORY_HELP));
        assertFalse(capabilities.has(DiagramCapability.PLANNING_VIEW));
        assertTrue(capabilities.has(DiagramCapability.IMPORT_MARKDOWN));
        assertFalse(capabilities.has(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertFalse(capabilities.has(DiagramCapability.EXPORT_PNG));
        assertFalse(capabilities.has(DiagramCapability.EXPORT_SVG));
        assertFalse(capabilities.has(DiagramCapability.EXPORT_PDF));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_MARKDOWN));
        assertFalse(capabilities.has(DiagramCapability.BATCH_EXPORT));
    }

    @Test
    void conceptualModelShouldExposeOnlyRealVisualCapabilities() {
        DiagramCapabilityCatalog catalog = new DefaultDiagramCapabilityCatalog();

        DiagramCapabilitySet capabilities = catalog.capabilitiesOf(DiagramTypeId.CONCEPTUAL_MODEL);

        assertTrue(capabilities.has(DiagramCapability.CREATE_PROJECT));
        assertTrue(capabilities.has(DiagramCapability.IMPORT_MARKDOWN));
        assertFalse(capabilities.has(DiagramCapability.IMPORT_SOURCE_CODE));
        assertFalse(capabilities.has(DiagramCapability.OPEN_SOURCE_CODE));
        assertTrue(capabilities.has(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertTrue(capabilities.has(DiagramCapability.MANUAL_EDITING));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_PNG));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_SVG));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_MARKDOWN));
        assertTrue(capabilities.has(DiagramCapability.SAVE_DMS));
        assertTrue(capabilities.has(DiagramCapability.LOAD_DMS));
        assertTrue(capabilities.has(DiagramCapability.AI_RESOURCES));
        assertTrue(capabilities.has(DiagramCapability.THEORY_HELP));
        assertTrue(capabilities.has(DiagramCapability.BATCH_EXPORT));
        assertFalse(capabilities.has(DiagramCapability.PLANNING_VIEW));
        assertFalse(capabilities.has(DiagramCapability.EXPORT_PDF));
    }

    @Test
    void dataDictionaryShouldExposeDocumentCapabilitiesOnly() {
        DiagramCapabilityCatalog catalog = new DefaultDiagramCapabilityCatalog();

        DiagramCapabilitySet capabilities = catalog.capabilitiesOf(DiagramTypeId.DATA_DICTIONARY);

        assertTrue(capabilities.has(DiagramCapability.CREATE_PROJECT));
        assertTrue(capabilities.has(DiagramCapability.IMPORT_MARKDOWN));
        assertFalse(capabilities.has(DiagramCapability.IMPORT_SOURCE_CODE));
        assertFalse(capabilities.has(DiagramCapability.OPEN_SOURCE_CODE));
        assertTrue(capabilities.has(DiagramCapability.SHOW_DOCUMENT_OUTPUT));
        assertTrue(capabilities.has(DiagramCapability.MANUAL_EDITING));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_PDF));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_MARKDOWN));
        assertTrue(capabilities.has(DiagramCapability.SAVE_DMS));
        assertTrue(capabilities.has(DiagramCapability.LOAD_DMS));
        assertTrue(capabilities.has(DiagramCapability.AI_RESOURCES));
        assertTrue(capabilities.has(DiagramCapability.THEORY_HELP));
        assertFalse(capabilities.has(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertFalse(capabilities.has(DiagramCapability.BATCH_EXPORT));
        assertFalse(capabilities.has(DiagramCapability.PLANNING_VIEW));
    }

    @Test
    void moduleMapShouldExposeVisualCapabilities() {
        DiagramCapabilityCatalog catalog = new DefaultDiagramCapabilityCatalog();

        DiagramCapabilitySet capabilities = catalog.capabilitiesOf(DiagramTypeId.ADMIN_MODULE_MAP);

        assertTrue(capabilities.has(DiagramCapability.CREATE_PROJECT));
        assertTrue(capabilities.has(DiagramCapability.IMPORT_MARKDOWN));
        assertTrue(capabilities.has(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertTrue(capabilities.has(DiagramCapability.MANUAL_EDITING));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_PNG));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_MARKDOWN));
        assertTrue(capabilities.has(DiagramCapability.SAVE_DMS));
        assertTrue(capabilities.has(DiagramCapability.LOAD_DMS));
        assertTrue(capabilities.has(DiagramCapability.AI_RESOURCES));
        assertTrue(capabilities.has(DiagramCapability.THEORY_HELP));
        assertFalse(capabilities.has(DiagramCapability.PLANNING_VIEW));
        assertFalse(capabilities.has(DiagramCapability.EXPORT_PDF));
        assertFalse(capabilities.has(DiagramCapability.BATCH_EXPORT));
    }

    @Test
    void rolesPermissionsShouldExposeMatrixDocumentCapabilities() {
        DiagramCapabilityCatalog catalog = new DefaultDiagramCapabilityCatalog();

        DiagramCapabilitySet capabilities = catalog.capabilitiesOf(DiagramTypeId.ROLES_PERMISSIONS_MAP);

        assertTrue(capabilities.has(DiagramCapability.CREATE_PROJECT));
        assertTrue(capabilities.has(DiagramCapability.IMPORT_MARKDOWN));
        assertFalse(capabilities.has(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertTrue(capabilities.has(DiagramCapability.SHOW_DOCUMENT_OUTPUT));
        assertTrue(capabilities.has(DiagramCapability.MANUAL_EDITING));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_PNG));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_MARKDOWN));
        assertTrue(capabilities.has(DiagramCapability.SAVE_DMS));
        assertTrue(capabilities.has(DiagramCapability.LOAD_DMS));
        assertTrue(capabilities.has(DiagramCapability.AI_RESOURCES));
        assertTrue(capabilities.has(DiagramCapability.THEORY_HELP));
        assertFalse(capabilities.has(DiagramCapability.PLANNING_VIEW));
        assertFalse(capabilities.has(DiagramCapability.EXPORT_PDF));
        assertFalse(capabilities.has(DiagramCapability.BATCH_EXPORT));
    }

    @Test
    void screenFlowShouldExposeVisualCapabilities() {
        DiagramCapabilityCatalog catalog = new DefaultDiagramCapabilityCatalog();

        DiagramCapabilitySet capabilities = catalog.capabilitiesOf(DiagramTypeId.SCREEN_FLOW);

        assertTrue(capabilities.has(DiagramCapability.CREATE_PROJECT));
        assertTrue(capabilities.has(DiagramCapability.IMPORT_MARKDOWN));
        assertTrue(capabilities.has(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertTrue(capabilities.has(DiagramCapability.MANUAL_EDITING));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_PNG));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_MARKDOWN));
        assertTrue(capabilities.has(DiagramCapability.SAVE_DMS));
        assertTrue(capabilities.has(DiagramCapability.LOAD_DMS));
        assertTrue(capabilities.has(DiagramCapability.AI_RESOURCES));
        assertTrue(capabilities.has(DiagramCapability.THEORY_HELP));
        assertFalse(capabilities.has(DiagramCapability.PLANNING_VIEW));
        assertFalse(capabilities.has(DiagramCapability.EXPORT_PDF));
        assertFalse(capabilities.has(DiagramCapability.BATCH_EXPORT));
    }


    @Test
    void wireframeShouldExposeVisualCapabilities() {
        DiagramCapabilityCatalog catalog = new DefaultDiagramCapabilityCatalog();

        DiagramCapabilitySet capabilities = catalog.capabilitiesOf(DiagramTypeId.ADMIN_WIREFRAMES);

        assertTrue(capabilities.has(DiagramCapability.CREATE_PROJECT));
        assertTrue(capabilities.has(DiagramCapability.IMPORT_MARKDOWN));
        assertTrue(capabilities.has(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertTrue(capabilities.has(DiagramCapability.MANUAL_EDITING));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_PNG));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_MARKDOWN));
        assertTrue(capabilities.has(DiagramCapability.SAVE_DMS));
        assertTrue(capabilities.has(DiagramCapability.LOAD_DMS));
        assertTrue(capabilities.has(DiagramCapability.AI_RESOURCES));
        assertTrue(capabilities.has(DiagramCapability.THEORY_HELP));
        assertFalse(capabilities.has(DiagramCapability.PLANNING_VIEW));
        assertFalse(capabilities.has(DiagramCapability.EXPORT_PDF));
        assertFalse(capabilities.has(DiagramCapability.BATCH_EXPORT));
    }

    @Test
    void umlClassShouldExposeVisualAndSourceCodeCapabilities() {
        DiagramCapabilityCatalog catalog = new DefaultDiagramCapabilityCatalog();

        DiagramCapabilitySet capabilities = catalog.capabilitiesOf(DiagramTypeId.UML_CLASS);

        assertTrue(capabilities.has(DiagramCapability.CREATE_PROJECT));
        assertTrue(capabilities.has(DiagramCapability.IMPORT_MARKDOWN));
        assertTrue(capabilities.has(DiagramCapability.IMPORT_SOURCE_CODE));
        assertTrue(capabilities.has(DiagramCapability.OPEN_SOURCE_CODE));
        assertTrue(capabilities.has(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertTrue(capabilities.has(DiagramCapability.MANUAL_EDITING));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_PNG));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_MARKDOWN));
        assertTrue(capabilities.has(DiagramCapability.SAVE_DMS));
        assertTrue(capabilities.has(DiagramCapability.LOAD_DMS));
        assertTrue(capabilities.has(DiagramCapability.AI_RESOURCES));
        assertTrue(capabilities.has(DiagramCapability.THEORY_HELP));
        assertFalse(capabilities.has(DiagramCapability.PLANNING_VIEW));
        assertFalse(capabilities.has(DiagramCapability.EXPORT_PDF));
        assertFalse(capabilities.has(DiagramCapability.BATCH_EXPORT));
    }


    @Test
    void architectureDiagramsShouldExposeVisualCapabilities() {
        DiagramCapabilityCatalog catalog = new DefaultDiagramCapabilityCatalog();

        for (DiagramTypeId diagramTypeId : new DiagramTypeId[]{
                DiagramTypeId.C4_CONTEXT,
                DiagramTypeId.C4_CONTAINERS,
                DiagramTypeId.TECHNICAL_DEPLOYMENT}) {
            DiagramCapabilitySet capabilities = catalog.capabilitiesOf(diagramTypeId);

            assertTrue(capabilities.has(DiagramCapability.CREATE_PROJECT));
            assertTrue(capabilities.has(DiagramCapability.IMPORT_MARKDOWN));
            assertTrue(capabilities.has(DiagramCapability.SHOW_VISUAL_OUTPUT));
            assertTrue(capabilities.has(DiagramCapability.MANUAL_EDITING));
            assertTrue(capabilities.has(DiagramCapability.EXPORT_PNG));
            assertTrue(capabilities.has(DiagramCapability.EXPORT_MARKDOWN));
            assertTrue(capabilities.has(DiagramCapability.SAVE_DMS));
            assertTrue(capabilities.has(DiagramCapability.LOAD_DMS));
            assertTrue(capabilities.has(DiagramCapability.AI_RESOURCES));
            assertTrue(capabilities.has(DiagramCapability.THEORY_HELP));
            assertFalse(capabilities.has(DiagramCapability.PLANNING_VIEW));
            assertFalse(capabilities.has(DiagramCapability.EXPORT_PDF));
            assertFalse(capabilities.has(DiagramCapability.BATCH_EXPORT));
        }
    }

    @Test
    void freeGraphShouldExposeVisualExportAndManualEditingCapabilities() {
        DiagramCapabilityCatalog catalog = new DefaultDiagramCapabilityCatalog();

        DiagramCapabilitySet capabilities = catalog.capabilitiesOf(DiagramTypeId.FREE_GRAPH);

        assertTrue(capabilities.has(DiagramCapability.CREATE_PROJECT));
        assertTrue(capabilities.has(DiagramCapability.IMPORT_MARKDOWN));
        assertTrue(capabilities.has(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertTrue(capabilities.has(DiagramCapability.MANUAL_EDITING));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_PNG));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_SVG));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_MARKDOWN));
        assertTrue(capabilities.has(DiagramCapability.SAVE_DMS));
        assertTrue(capabilities.has(DiagramCapability.LOAD_DMS));
        assertTrue(capabilities.has(DiagramCapability.AI_RESOURCES));
        assertTrue(capabilities.has(DiagramCapability.THEORY_HELP));
        assertFalse(capabilities.has(DiagramCapability.PLANNING_VIEW));
        assertFalse(capabilities.has(DiagramCapability.EXPORT_PDF));
        assertFalse(capabilities.has(DiagramCapability.BATCH_EXPORT));
    }

    @Test
    void logicalBusinessGraphShouldExposeMarkdownContractAndInitialVisualCanvas() {
        DiagramCapabilityCatalog catalog = new DefaultDiagramCapabilityCatalog();

        DiagramCapabilitySet capabilities = catalog.capabilitiesOf(DiagramTypeId.LOGICAL_BUSINESS_GRAPH);

        assertFalse(capabilities.has(DiagramCapability.PLANNING_VIEW));
        assertTrue(capabilities.has(DiagramCapability.CREATE_PROJECT));
        assertTrue(capabilities.has(DiagramCapability.IMPORT_MARKDOWN));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_MARKDOWN));
        assertTrue(capabilities.has(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertTrue(capabilities.has(DiagramCapability.MANUAL_EDITING));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_PNG));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_SVG));
        assertTrue(capabilities.has(DiagramCapability.SAVE_DMS));
        assertTrue(capabilities.has(DiagramCapability.LOAD_DMS));
        assertTrue(capabilities.has(DiagramCapability.AI_RESOURCES));
        assertTrue(capabilities.has(DiagramCapability.THEORY_HELP));
    }

    @Test
    void unknownTypeShouldExposeEmptyCapabilities() {
        DiagramCapabilityCatalog catalog = new DefaultDiagramCapabilityCatalog();

        DiagramCapabilitySet capabilities = catalog.capabilitiesOf(DiagramTypeId.of("unknown-type"));
        DiagramCapabilitySet removedCapabilities = catalog.capabilitiesOf(DiagramTypeId.of("logical-relational-model"));

        assertTrue(capabilities.asSet().isEmpty());
        assertTrue(removedCapabilities.asSet().isEmpty());
    }
}
