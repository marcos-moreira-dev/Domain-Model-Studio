package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceKind;
import org.junit.jupiter.api.Test;

class StructuredWorkbenchDescriptorTest {

    @Test
    void documentDescriptorKeepsDismissibleHeaderAndPanelTitles() {
        StructuredWorkbenchDescriptor descriptor = StructuredWorkbenchDescriptor.document(
                WorkspaceKind.DATA_DICTIONARY_DOCUMENT,
                "data-dictionary-root",
                "Diccionario de datos",
                "Documento estructurado.",
                "Editable."
        );

        assertEquals(WorkspaceKind.DATA_DICTIONARY_DOCUMENT, descriptor.workspaceKind());
        assertEquals("data-dictionary-root", descriptor.rootStyleClass());
        assertEquals("Estructura", descriptor.structurePanelTitle());
        assertEquals("Propiedades", descriptor.propertiesPanelTitle());
        assertEquals("Diccionario de datos", descriptor.headerState().title());
        assertTrue(descriptor.headerState().dismissible());
    }

    @Test
    void matrixDescriptorDoesNotUseCanvasVocabulary() {
        StructuredWorkbenchDescriptor descriptor = StructuredWorkbenchDescriptor.matrix(
                WorkspaceKind.ROLES_PERMISSIONS_MATRIX,
                "roles-permissions-root",
                "Roles y permisos",
                "Matriz estructurada.",
                "Editable."
        );

        assertEquals(WorkspaceKind.ROLES_PERMISSIONS_MATRIX, descriptor.workspaceKind());
        assertEquals("Roles y permisos", descriptor.headerState().title());
        assertEquals("Matriz estructurada.", descriptor.headerState().subtitle());
        assertEquals("Editable.", descriptor.headerState().statusText());
    }
}
