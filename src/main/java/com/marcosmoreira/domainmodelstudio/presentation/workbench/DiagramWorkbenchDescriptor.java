package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceKind;
import java.util.Objects;

/**
 * Descriptor estable de un workbench visual.
 *
 * <p>No contiene nodos JavaFX ni ViewModels. Solo describe identidad, textos visibles
 * y política de paneles para que la infraestructura común no dependa del dominio del
 * diagrama.</p>
 */
public record DiagramWorkbenchDescriptor(
        WorkspaceKind workspaceKind,
        String title,
        String subtitle,
        String statusText,
        boolean headerDismissible,
        WorkbenchPanelPolicy panelPolicy,
        String structurePanelTitle,
        String propertiesPanelTitle,
        String panelUnavailableMessage
) {

    public DiagramWorkbenchDescriptor {
        Objects.requireNonNull(workspaceKind, "workspaceKind");
        title = requireText(title, "title");
        subtitle = cleanOptional(subtitle);
        statusText = cleanOptional(statusText);
        Objects.requireNonNull(panelPolicy, "panelPolicy");
        structurePanelTitle = defaultText(structurePanelTitle, WorkbenchPanelSlot.STRUCTURE.displayName());
        propertiesPanelTitle = defaultText(propertiesPanelTitle, WorkbenchPanelSlot.PROPERTIES.displayName());
        panelUnavailableMessage = cleanOptional(panelUnavailableMessage);
    }

    public static DiagramWorkbenchDescriptor visualDiagram(
            WorkspaceKind workspaceKind,
            String title,
            String subtitle,
            String statusText
    ) {
        return new DiagramWorkbenchDescriptor(
                workspaceKind,
                title,
                subtitle,
                statusText,
                true,
                WorkbenchPanelPolicy.SELF_CONTAINED_LEGACY,
                WorkbenchPanelSlot.STRUCTURE.displayName(),
                WorkbenchPanelSlot.PROPERTIES.displayName(),
                "Este diagrama todavía conserva paneles internos durante la migración visual."
        );
    }

    public static DiagramWorkbenchDescriptor migratedVisualDiagram(
            WorkspaceKind workspaceKind,
            String title,
            String subtitle,
            String statusText
    ) {
        return new DiagramWorkbenchDescriptor(
                workspaceKind,
                title,
                subtitle,
                statusText,
                true,
                WorkbenchPanelPolicy.WORKBENCH_SLOTS,
                WorkbenchPanelSlot.STRUCTURE.displayName(),
                WorkbenchPanelSlot.PROPERTIES.displayName(),
                "Este diagrama no expone paneles laterales para la selección actual."
        );
    }

    public WorkspaceHeaderState headerState() {
        return WorkspaceHeaderState.visible(title, subtitle, statusText, headerDismissible);
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return value.strip();
    }

    private static String defaultText(String value, String fallback) {
        String clean = cleanOptional(value);
        return clean.isBlank() ? fallback : clean;
    }

    private static String cleanOptional(String value) {
        return value == null ? "" : value.strip();
    }
}
