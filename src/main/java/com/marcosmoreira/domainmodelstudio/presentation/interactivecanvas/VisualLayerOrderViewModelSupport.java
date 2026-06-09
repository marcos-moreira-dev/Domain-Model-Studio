package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayerOrderCommand;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutService;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Apoyo común para aplicar acciones de orden visual desde ViewModels especializados.
 *
 * <p>El canvas no conoce los documentos concretos; cada ViewModel traduce la selección semántica
 * activa al ID de layout persistible y este helper aplica la operación sobre {@code DiagramLayouts}.</p>
 */
public final class VisualLayerOrderViewModelSupport {

    private VisualLayerOrderViewModelSupport() {
    }

    public static boolean reorderSelectedNode(
            VisualLayoutService visualLayoutService,
            DiagramProject currentProject,
            DiagramElementId selectedLayoutId,
            VisualLayerOrderCommand command,
            Consumer<DiagramProject> projectUpdater,
            Runnable changeNotifier,
            Consumer<String> statusConsumer
    ) {
        Objects.requireNonNull(visualLayoutService, "visualLayoutService");
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(projectUpdater, "projectUpdater");
        Objects.requireNonNull(changeNotifier, "changeNotifier");
        Objects.requireNonNull(statusConsumer, "statusConsumer");
        if (currentProject == null) {
            statusConsumer.accept("No hay diagrama visual activo para reordenar capas.");
            return false;
        }
        if (selectedLayoutId == null) {
            statusConsumer.accept("Selecciona un nodo, tarjeta o rectángulo antes de cambiar su capa.");
            return false;
        }
        try {
            DiagramProject updated = visualLayoutService.reorderNodes(currentProject, Set.of(selectedLayoutId), command);
            projectUpdater.accept(updated);
            changeNotifier.run();
            statusConsumer.accept(statusFor(command));
            return true;
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo cambiar el orden visual: " + exception.getMessage());
            return false;
        }
    }

    private static String statusFor(VisualLayerOrderCommand command) {
        return switch (command) {
            case BRING_TO_FRONT -> "Elemento seleccionado traído al frente.";
            case SEND_TO_BACK -> "Elemento seleccionado enviado al fondo.";
            case RAISE -> "Elemento seleccionado subido una capa.";
            case LOWER -> "Elemento seleccionado bajado una capa.";
        };
    }
}
