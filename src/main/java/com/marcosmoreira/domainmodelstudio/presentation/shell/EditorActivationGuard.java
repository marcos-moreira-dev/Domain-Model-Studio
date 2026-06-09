package com.marcosmoreira.domainmodelstudio.presentation.shell;

import java.util.Objects;

/**
 * Verifica que el editor contextual esté activo antes de ejecutar comandos de toolbar.
 *
 * <p>El guard queda separado del manejador global para que los comandos especializados no
 * repitan mensajes de disponibilidad ni acumulen detalles de presentación en cada método.</p>
 */
final class EditorActivationGuard {

    private final MainShellState shellState;

    EditorActivationGuard(MainShellState shellState) {
        this.shellState = Objects.requireNonNull(shellState, "shellState");
    }

    boolean ensureActive(boolean active, String editorName, String action) {
        if (active) {
            return true;
        }
        shellState.updateStatus("Abre " + editorName + " para " + action + ".");
        return false;
    }
}
