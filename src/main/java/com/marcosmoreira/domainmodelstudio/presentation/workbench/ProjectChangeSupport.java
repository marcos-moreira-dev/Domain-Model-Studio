package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Controla la notificación de cambios de proyecto desde ViewModels de workspace.
 *
 * <p>Los editores visuales y documentales comparten el mismo patrón: durante la
 * carga o limpieza del proyecto no deben publicar cambios hacia el shell, pero
 * cualquier edición posterior sí debe marcar el proyecto como actualizado. Esta
 * clase centraliza ese contrato para evitar banderas {@code loading} y listeners
 * duplicados en cada ViewModel.</p>
 *
 * <p>No decide dirty state, selección, layout ni comandos de edición. Es una pieza
 * transversal mínima para el patrón de carga/notificación extraído en la Tanda 33.</p>
 */

public final class ProjectChangeSupport {

    private Consumer<DiagramProject> projectChangeListener = project -> { };
    private boolean loading;

    public void registerProjectChangeListener(Consumer<DiagramProject> listener) {
        this.projectChangeListener = listener == null ? project -> { } : listener;
    }

    public void runLoading(Runnable action) {
        Objects.requireNonNull(action, "action");
        loading = true;
        try {
            action.run();
        } finally {
            loading = false;
        }
    }

    public boolean loading() {
        return loading;
    }

    public void notifyChanged(DiagramProject project) {
        if (!loading && project != null) {
            projectChangeListener.accept(project);
        }
    }
}
