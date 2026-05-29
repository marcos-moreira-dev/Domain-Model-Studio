package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ExportableOutputDescriptorTest {

    @Test
    void visualDiagramDescriptorDeclaresSupportedFormatsOnly() {
        ExportableOutputDescriptor descriptor = ExportableOutputDescriptor.visualDiagram(
                DiagramTypeId.CONCEPTUAL_MODEL,
                "Modelo de Colegio",
                "Modelo de Colegio",
                Set.of(ExportFormat.SVG, ExportFormat.PNG, ExportFormat.MARKDOWN));

        assertEquals(ExportableOutputKind.VISUAL_DIAGRAM, descriptor.kind());
        assertTrue(descriptor.supports(ExportFormat.SVG));
        assertTrue(descriptor.supports(ExportFormat.PNG));
        assertTrue(descriptor.supports(ExportFormat.MARKDOWN));
        assertFalse(descriptor.supports(ExportFormat.PDF));
    }

    @Test
    void documentDescriptorKeepsPdfSeparateFromDiagramFormats() {
        ExportableOutputDescriptor descriptor = ExportableOutputDescriptor.document(
                DiagramTypeId.DATA_DICTIONARY,
                "Diccionario de datos",
                "Diccionario de datos",
                Set.of(ExportFormat.PDF, ExportFormat.MARKDOWN));

        assertEquals(ExportableOutputKind.DOCUMENT, descriptor.kind());
        assertTrue(descriptor.supports(ExportFormat.PDF));
        assertTrue(descriptor.supports(ExportFormat.MARKDOWN));
        assertFalse(descriptor.supports(ExportFormat.PNG));
        assertFalse(descriptor.supports(ExportFormat.SVG));
    }


    @Test
    void matrixDescriptorKeepsAdministrativeMatrixSeparateFromCanvasKind() {
        ExportableOutputDescriptor descriptor = ExportableOutputDescriptor.matrix(
                DiagramTypeId.ROLES_PERMISSIONS_MAP,
                "Roles y permisos",
                "Roles y permisos",
                Set.of(ExportFormat.PNG, ExportFormat.MARKDOWN));

        assertEquals(ExportableOutputKind.MATRIX, descriptor.kind());
        assertTrue(descriptor.supports(ExportFormat.PNG));
        assertTrue(descriptor.supports(ExportFormat.MARKDOWN));
        assertFalse(descriptor.supports(ExportFormat.PDF));
    }

    @Test
    void suggestedFileNameIsStableAndSafeEnoughForExports() {
        ExportableOutputDescriptor descriptor = ExportableOutputDescriptor.document(
                DiagramTypeId.DATA_DICTIONARY,
                "Diccionario",
                "Óptica / Clientes y Ventas",
                Set.of(ExportFormat.PDF));

        assertEquals("optica_clientes_y_ventas.pdf", descriptor.suggestedFileName(ExportFormat.PDF));
    }
}
