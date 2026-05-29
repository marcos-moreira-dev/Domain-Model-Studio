package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class WorkbenchPanelToggleControllerTest {

    @Test
    void panelsStartVisibleAndCanBeCollapsedIndependently() {
        WorkbenchPanelToggleController controller = new WorkbenchPanelToggleController();

        assertTrue(controller.isVisible(WorkbenchPanelSlot.STRUCTURE));
        assertTrue(controller.isVisible(WorkbenchPanelSlot.PROPERTIES));

        controller.hide(WorkbenchPanelSlot.STRUCTURE);

        assertFalse(controller.isVisible(WorkbenchPanelSlot.STRUCTURE));
        assertTrue(controller.isVisible(WorkbenchPanelSlot.PROPERTIES));
    }

    @Test
    void snapshotCombinesAvailabilityAndVisibility() {
        WorkbenchPanelToggleController controller = new WorkbenchPanelToggleController();
        controller.hide(WorkbenchPanelSlot.PROPERTIES);

        WorkbenchPanelSnapshot snapshot = controller.snapshot(
                WorkbenchPanelSlot.PROPERTIES,
                "Propiedades",
                true,
                "Panel no disponible"
        );

        assertTrue(snapshot.available());
        assertFalse(snapshot.visible());
        assertTrue(snapshot.collapsed());
    }
}
