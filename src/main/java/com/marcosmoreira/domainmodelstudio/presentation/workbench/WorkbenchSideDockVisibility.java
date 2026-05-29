package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModuleId;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.WorkbenchSideDock;

/** Coordina la visibilidad contextual de módulos laterales ya registrados. */
final class WorkbenchSideDockVisibility {

    private WorkbenchSideDockVisibility() {
    }

    static void setVisible(WorkbenchSideDock sideDock, SideDockModuleId moduleId, boolean visible) {
        if (visible) {
            sideDock.activate(moduleId);
            return;
        }
        sideDock.activeModuleId()
                .filter(active -> active == moduleId)
                .ifPresent(sideDock::activate);
    }
}
