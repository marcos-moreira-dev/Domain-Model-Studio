package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import java.util.Objects;

/** Datos visibles del encabezado superior de un workspace de diagrama. */
public record WorkspaceHeaderState(
        String title,
        String subtitle,
        String statusText,
        boolean dismissible,
        boolean visible
) {

    public WorkspaceHeaderState {
        title = requireText(title, "title");
        subtitle = cleanOptional(subtitle);
        statusText = cleanOptional(statusText);
    }

    public static WorkspaceHeaderState visible(
            String title,
            String subtitle,
            String statusText,
            boolean dismissible
    ) {
        return new WorkspaceHeaderState(title, subtitle, statusText, dismissible, true);
    }

    public WorkspaceHeaderState hidden() {
        return new WorkspaceHeaderState(title, subtitle, statusText, dismissible, false);
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return value.strip();
    }

    private static String cleanOptional(String value) {
        return value == null ? "" : value.strip();
    }
}
