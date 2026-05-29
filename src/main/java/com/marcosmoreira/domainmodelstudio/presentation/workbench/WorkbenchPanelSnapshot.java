package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import java.util.Objects;

/** Estado liviano de un panel lateral del workbench. */
public record WorkbenchPanelSnapshot(
        WorkbenchPanelSlot slot,
        String title,
        boolean available,
        boolean visible,
        String unavailableMessage
) {

    public WorkbenchPanelSnapshot {
        Objects.requireNonNull(slot, "slot");
        title = requireText(title, "title");
        unavailableMessage = unavailableMessage == null ? "" : unavailableMessage.strip();
    }

    public boolean collapsed() {
        return available && !visible;
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return value;
    }
}
