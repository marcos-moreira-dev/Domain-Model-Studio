package com.marcosmoreira.domainmodelstudio.presentation.workbench;

/**
 * Política de montaje para los paneles laterales de un workspace.
 *
 * <p>La política evita nombres como "panel conceptual" en componentes que deben ser
 * transversales. También permite migrar gradualmente: algunos editores seguirán siendo
 * autocontenidos hasta que sus paneles puedan salir a slots comunes.</p>
 */
public enum WorkbenchPanelPolicy {
    NONE(false, false),
    SHELL_SLOTS(true, false),
    WORKBENCH_SLOTS(true, true),
    SELF_CONTAINED_LEGACY(true, true),
    DOCUMENT_VIEW(false, false);

    private final boolean panelSlotsAvailable;
    private final boolean renderPanelsInsideWorkbench;

    WorkbenchPanelPolicy(boolean panelSlotsAvailable, boolean renderPanelsInsideWorkbench) {
        this.panelSlotsAvailable = panelSlotsAvailable;
        this.renderPanelsInsideWorkbench = renderPanelsInsideWorkbench;
    }

    public boolean panelSlotsAvailable() {
        return panelSlotsAvailable;
    }

    public boolean renderPanelsInsideWorkbench() {
        return renderPanelsInsideWorkbench;
    }
}
