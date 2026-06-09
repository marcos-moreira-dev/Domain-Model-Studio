package com.marcosmoreira.domainmodelstudio.presentation.workspace;

import java.util.Objects;

/**
 * Describe una familia de workspace visible sin depender de JavaFX.
 *
 * <p>El descriptor concentra lenguaje de producto y política de paneles para que
 * {@code MainShellView} no repita condicionales por cada editor especializado.</p>
 */
public record WorkspaceDescriptor(
        WorkspaceKind workspaceKind,
        String displayName,
        boolean usesGenericConceptualSidePanels,
        String panelUnavailableMessage
) {

    public WorkspaceDescriptor {
        Objects.requireNonNull(workspaceKind, "workspaceKind");
        displayName = requireText(displayName, "displayName");
        panelUnavailableMessage = requireText(panelUnavailableMessage, "panelUnavailableMessage");
    }

    public boolean panelsAvailableWhenProjectIsOpen(boolean projectOpen) {
        return projectOpen && usesGenericConceptualSidePanels;
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return value;
    }
}
