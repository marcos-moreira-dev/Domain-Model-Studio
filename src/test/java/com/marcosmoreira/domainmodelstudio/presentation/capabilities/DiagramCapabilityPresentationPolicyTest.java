package com.marcosmoreira.domainmodelstudio.presentation.capabilities;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportFormat;
import com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionId;
import org.junit.jupiter.api.Test;

/** Verifica que la interfaz derive acciones visibles desde capacidades reales del catálogo. */
class DiagramCapabilityPresentationPolicyTest {

    private final DiagramCapabilityPresentationPolicy policy = new DiagramCapabilityPresentationPolicy();

    @Test
    void exportFormatsShouldFollowOfficialCapabilities() {
        assertTrue(policy.supportsExportFormat(DiagramTypeId.CONCEPTUAL_MODEL, ExportFormat.SVG));
        assertTrue(policy.supportsExportFormat(DiagramTypeId.CONCEPTUAL_MODEL, ExportFormat.PNG));
        assertTrue(policy.supportsExportFormat(DiagramTypeId.CONCEPTUAL_MODEL, ExportFormat.MARKDOWN));

        assertFalse(policy.supportsExportFormat(DiagramTypeId.DATA_DICTIONARY, ExportFormat.PNG));
        assertTrue(policy.supportsExportFormat(DiagramTypeId.DATA_DICTIONARY, ExportFormat.PDF));
        assertTrue(policy.supportsExportFormat(DiagramTypeId.DATA_DICTIONARY, ExportFormat.MARKDOWN));

        assertTrue(policy.supportsExportFormat(DiagramTypeId.UML_USE_CASE, ExportFormat.SVG));
        assertTrue(policy.supportsExportFormat(DiagramTypeId.UML_USE_CASE, ExportFormat.PNG));
        assertTrue(policy.supportsExportFormat(DiagramTypeId.UML_USE_CASE, ExportFormat.MARKDOWN));
    }

    @Test
    void conceptualCanvasActionsMustNotLeakToSpecializedWorkspaces() {
        assertTrue(policy.shouldExposeToolbarAction(DiagramTypeId.CONCEPTUAL_MODEL, DiagramToolbarActionId.SWITCH_TO_CHEN));
        assertTrue(policy.shouldExposeToolbarAction(DiagramTypeId.CONCEPTUAL_MODEL, DiagramToolbarActionId.FIT_TO_CONTENT));

        assertFalse(policy.shouldExposeToolbarAction(DiagramTypeId.UML_USE_CASE, DiagramToolbarActionId.SWITCH_TO_CHEN));
        assertTrue(policy.shouldExposeToolbarAction(DiagramTypeId.ADMIN_WIREFRAMES, DiagramToolbarActionId.FIT_TO_CONTENT));
        assertTrue(policy.shouldExposeToolbarAction(DiagramTypeId.ADMIN_WIREFRAMES, DiagramToolbarActionId.CENTER_DIAGRAM));
    }

    @Test
    void toolbarExportActionsShouldFollowOutputCapabilities() {
        assertFalse(policy.shouldExposeToolbarAction(DiagramTypeId.DATA_DICTIONARY, DiagramToolbarActionId.EXPORT_PNG));
        assertTrue(policy.shouldExposeToolbarAction(DiagramTypeId.DATA_DICTIONARY, DiagramToolbarActionId.EXPORT_DICTIONARY_PDF));

        assertTrue(policy.shouldExposeToolbarAction(DiagramTypeId.BPMN_BASIC, DiagramToolbarActionId.EXPORT_SVG));
        assertTrue(policy.shouldExposeToolbarAction(DiagramTypeId.BPMN_BASIC, DiagramToolbarActionId.EXPORT_PNG));
        assertTrue(policy.shouldExposeToolbarAction(DiagramTypeId.BPMN_BASIC, DiagramToolbarActionId.EXPORT_MARKDOWN));
    }
    @Test
    void sourceCodeToolbarActionsShouldOnlyFollowUmlClassSourceCapabilities() {
        assertTrue(policy.shouldExposeToolbarAction(DiagramTypeId.UML_CLASS, DiagramToolbarActionId.IMPORT_UML_FROM_SOURCE));
        assertTrue(policy.shouldExposeToolbarAction(DiagramTypeId.UML_CLASS, DiagramToolbarActionId.OPEN_UML_SOURCE));

        assertFalse(policy.shouldExposeToolbarAction(DiagramTypeId.ADMIN_WIREFRAMES, DiagramToolbarActionId.IMPORT_UML_FROM_SOURCE));
        assertFalse(policy.shouldExposeToolbarAction(DiagramTypeId.ADMIN_WIREFRAMES, DiagramToolbarActionId.OPEN_UML_SOURCE));
        assertFalse(policy.shouldExposeToolbarAction(DiagramTypeId.DATA_DICTIONARY, DiagramToolbarActionId.OPEN_UML_SOURCE));
    }


    @Test
    void logicalBusinessShouldExposeOnlyMarkdownDocumentExport() {
        assertFalse(policy.supportsExportFormat(DiagramTypeId.LOGICAL_BUSINESS_INTAKE, ExportFormat.SVG));
        assertFalse(policy.supportsExportFormat(DiagramTypeId.LOGICAL_BUSINESS_INTAKE, ExportFormat.PNG));
        assertFalse(policy.supportsExportFormat(DiagramTypeId.LOGICAL_BUSINESS_INTAKE, ExportFormat.PDF));
        assertTrue(policy.supportsExportFormat(DiagramTypeId.LOGICAL_BUSINESS_INTAKE, ExportFormat.MARKDOWN));

        assertFalse(policy.shouldExposeToolbarAction(DiagramTypeId.LOGICAL_BUSINESS_INTAKE, DiagramToolbarActionId.EXPORT_SVG));
        assertFalse(policy.shouldExposeToolbarAction(DiagramTypeId.LOGICAL_BUSINESS_INTAKE, DiagramToolbarActionId.EXPORT_PNG));
        assertTrue(policy.shouldExposeToolbarAction(DiagramTypeId.LOGICAL_BUSINESS_INTAKE, DiagramToolbarActionId.EXPORT_MARKDOWN));
        assertFalse(policy.shouldExposeToolbarAction(DiagramTypeId.LOGICAL_BUSINESS_INTAKE, DiagramToolbarActionId.IMPORT_UML_FROM_SOURCE));
    }

}
