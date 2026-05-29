package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Aplica parches visuales transversales sobre el proyecto activo.
 *
 * <p>Los adapters de apariencia de varios diagramas necesitan actualizar el
 * {@link DiagramProject} sin conocer el tipo concreto de diagrama. Esta ayuda
 * centraliza la validación, el mensaje de error común y la notificación de
 * estado para que cada ViewModel no duplique la misma secuencia.</p>
 */
public final class VisualProjectPatchSupport {

    private static final String NO_ACTIVE_PROJECT_MESSAGE = "No hay proyecto activo para editar apariencia.";
    private static final String DEFAULT_UPDATED_MESSAGE = "Proyecto actualizado.";

    private VisualProjectPatchSupport() {
    }

    public static void apply(
            DiagramProject currentProject,
            UnaryOperator<DiagramProject> patch,
            Consumer<String> statusConsumer,
            String statusMessage,
            Consumer<DiagramProject> projectUpdater,
            Runnable changeNotifier
    ) {
        Objects.requireNonNull(statusConsumer, "statusConsumer");
        Objects.requireNonNull(projectUpdater, "projectUpdater");
        Objects.requireNonNull(changeNotifier, "changeNotifier");

        if (currentProject == null) {
            statusConsumer.accept(NO_ACTIVE_PROJECT_MESSAGE);
            return;
        }

        DiagramProject updatedProject = Objects.requireNonNull(patch, "patch").apply(currentProject);
        projectUpdater.accept(updatedProject);
        changeNotifier.run();
        statusConsumer.accept(normalizedStatusMessage(statusMessage));
    }

    static String normalizedStatusMessage(String statusMessage) {
        return statusMessage == null || statusMessage.isBlank() ? DEFAULT_UPDATED_MESSAGE : statusMessage;
    }
}
