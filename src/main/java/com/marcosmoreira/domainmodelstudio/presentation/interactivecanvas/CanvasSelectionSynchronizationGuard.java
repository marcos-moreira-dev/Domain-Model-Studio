package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import java.util.Objects;

/**
 * Guardia pequeña para evitar bucles panel ↔ canvas durante sincronización de selección.
 */
public final class CanvasSelectionSynchronizationGuard {

    private boolean synchronizing;

    public boolean active() {
        return synchronizing;
    }

    public void runGuarded(Runnable operation) {
        Objects.requireNonNull(operation, "operation");
        if (synchronizing) {
            return;
        }
        synchronizing = true;
        try {
            operation.run();
        } finally {
            synchronizing = false;
        }
    }
}
