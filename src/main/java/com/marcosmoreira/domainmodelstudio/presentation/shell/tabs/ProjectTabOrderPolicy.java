package com.marcosmoreira.domainmodelstudio.presentation.shell.tabs;

import com.marcosmoreira.domainmodelstudio.presentation.shell.EditorTabViewState;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Política pura para reordenar pestañas de proyectos abiertos.
 *
 * <p>El reordenamiento es solamente visual/de sesión: no modifica proyectos,
 * rutas {@code .dms}, estado dirty ni payloads. La pestaña de inicio permanece
 * fija para evitar que el área de trabajo pierda su ancla de navegación.</p>
 */
public final class ProjectTabOrderPolicy {

    private ProjectTabOrderPolicy() {
    }

    public static List<EditorTabViewState> moveAfter(
            List<EditorTabViewState> tabs,
            String movedTabId,
            String targetTabId
    ) {
        Objects.requireNonNull(tabs, "tabs");
        if (movedTabId == null || movedTabId.isBlank()
                || targetTabId == null || targetTabId.isBlank()
                || movedTabId.equals(targetTabId)) {
            return List.copyOf(tabs);
        }

        int movedIndex = indexOf(tabs, movedTabId);
        int targetIndex = indexOf(tabs, targetTabId);
        if (movedIndex < 0 || targetIndex < 0) {
            return List.copyOf(tabs);
        }

        EditorTabViewState moved = tabs.get(movedIndex);
        EditorTabViewState target = tabs.get(targetIndex);
        if (!moved.closeable() || target.home()) {
            return List.copyOf(tabs);
        }

        List<EditorTabViewState> reordered = new ArrayList<>(tabs);
        reordered.remove(movedIndex);
        int insertionIndex = indexOf(reordered, targetTabId) + 1;
        reordered.add(insertionIndex, moved);
        return List.copyOf(reordered);
    }

    private static int indexOf(List<EditorTabViewState> tabs, String tabId) {
        for (int index = 0; index < tabs.size(); index++) {
            if (tabs.get(index).id().equals(tabId)) {
                return index;
            }
        }
        return -1;
    }
}
