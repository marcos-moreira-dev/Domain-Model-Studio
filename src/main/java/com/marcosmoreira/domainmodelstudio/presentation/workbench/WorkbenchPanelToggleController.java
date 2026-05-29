package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Controlador pequeño de visibilidad para los slots laterales del workbench.
 *
 * <p>No conoce JavaFX. Por eso puede reutilizarse desde tests, menús o futuras pestañas
 * laterales sin acoplarse al renderizado.</p>
 */
public final class WorkbenchPanelToggleController {

    private final Map<WorkbenchPanelSlot, Boolean> visibility = new EnumMap<>(WorkbenchPanelSlot.class);

    public WorkbenchPanelToggleController() {
        for (WorkbenchPanelSlot slot : WorkbenchPanelSlot.values()) {
            visibility.put(slot, Boolean.TRUE);
        }
    }

    public boolean isVisible(WorkbenchPanelSlot slot) {
        Objects.requireNonNull(slot, "slot");
        return Boolean.TRUE.equals(visibility.get(slot));
    }

    public void setVisible(WorkbenchPanelSlot slot, boolean visible) {
        Objects.requireNonNull(slot, "slot");
        visibility.put(slot, visible);
    }

    public void hide(WorkbenchPanelSlot slot) {
        setVisible(slot, false);
    }

    public void show(WorkbenchPanelSlot slot) {
        setVisible(slot, true);
    }

    public WorkbenchPanelSnapshot snapshot(
            WorkbenchPanelSlot slot,
            String title,
            boolean available,
            String unavailableMessage
    ) {
        return new WorkbenchPanelSnapshot(slot, title, available, available && isVisible(slot), unavailableMessage);
    }
}
