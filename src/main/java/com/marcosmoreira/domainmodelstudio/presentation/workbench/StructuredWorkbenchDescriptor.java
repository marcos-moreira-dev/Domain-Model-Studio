package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceKind;
import java.util.Objects;

/** Descriptor de workspaces no libres: documentos, matrices y vistas administrativas estructuradas. */
public record StructuredWorkbenchDescriptor(
        WorkspaceKind workspaceKind,
        String rootStyleClass,
        WorkspaceHeaderState headerState,
        String structurePanelTitle,
        String propertiesPanelTitle
) {

    public StructuredWorkbenchDescriptor {
        Objects.requireNonNull(workspaceKind, "workspaceKind");
        rootStyleClass = cleanOptional(rootStyleClass);
        headerState = Objects.requireNonNull(headerState, "headerState");
        structurePanelTitle = defaultText(structurePanelTitle, WorkbenchPanelSlot.STRUCTURE.displayName());
        propertiesPanelTitle = defaultText(propertiesPanelTitle, WorkbenchPanelSlot.PROPERTIES.displayName());
    }

    public static StructuredWorkbenchDescriptor document(
            WorkspaceKind workspaceKind,
            String rootStyleClass,
            String title,
            String subtitle,
            String statusText
    ) {
        return new StructuredWorkbenchDescriptor(
                workspaceKind,
                rootStyleClass,
                WorkspaceHeaderState.visible(title, subtitle, statusText, true),
                "Estructura",
                "Propiedades"
        );
    }

    public static StructuredWorkbenchDescriptor matrix(
            WorkspaceKind workspaceKind,
            String rootStyleClass,
            String title,
            String subtitle,
            String statusText
    ) {
        return new StructuredWorkbenchDescriptor(
                workspaceKind,
                rootStyleClass,
                WorkspaceHeaderState.visible(title, subtitle, statusText, true),
                "Estructura",
                "Propiedades"
        );
    }

    private static String defaultText(String value, String fallback) {
        String clean = cleanOptional(value);
        return clean.isBlank() ? fallback : clean;
    }

    private static String cleanOptional(String value) {
        return value == null ? "" : value.strip();
    }
}
