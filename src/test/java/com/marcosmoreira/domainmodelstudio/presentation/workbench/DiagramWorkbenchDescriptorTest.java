package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceKind;
import org.junit.jupiter.api.Test;

class DiagramWorkbenchDescriptorTest {

    @Test
    void visualDiagramFactoryKeepsHeaderDismissibleAndLegacyPanelPolicy() {
        DiagramWorkbenchDescriptor descriptor = DiagramWorkbenchDescriptor.visualDiagram(
                WorkspaceKind.MODULE_MAP_DIAGRAM,
                "Mapa de módulos",
                "Diagrama visual de módulos y dependencias.",
                "Estado listo."
        );

        assertEquals(WorkspaceKind.MODULE_MAP_DIAGRAM, descriptor.workspaceKind());
        assertTrue(descriptor.headerDismissible());
        assertEquals(WorkbenchPanelPolicy.SELF_CONTAINED_LEGACY, descriptor.panelPolicy());
        assertEquals("Estructura", descriptor.structurePanelTitle());
        assertEquals("Propiedades", descriptor.propertiesPanelTitle());
    }

    @Test
    void migratedVisualDiagramFactoryUsesWorkbenchSlots() {
        DiagramWorkbenchDescriptor descriptor = DiagramWorkbenchDescriptor.migratedVisualDiagram(
                WorkspaceKind.MODULE_MAP_DIAGRAM,
                "Mapa de módulos",
                "Diagrama visual de módulos y dependencias.",
                "Migrado."
        );

        assertEquals(WorkbenchPanelPolicy.WORKBENCH_SLOTS, descriptor.panelPolicy());
        assertTrue(descriptor.headerDismissible());
    }

    @Test
    void headerStateUsesDescriptorTexts() {
        DiagramWorkbenchDescriptor descriptor = DiagramWorkbenchDescriptor.visualDiagram(
                WorkspaceKind.SCREEN_FLOW_DIAGRAM,
                "Flujo de pantallas",
                "Navegación principal del sistema.",
                "Vista preparada."
        );

        WorkspaceHeaderState state = descriptor.headerState();

        assertEquals("Flujo de pantallas", state.title());
        assertEquals("Navegación principal del sistema.", state.subtitle());
        assertEquals("Vista preparada.", state.statusText());
        assertTrue(state.visible());
    }
}
