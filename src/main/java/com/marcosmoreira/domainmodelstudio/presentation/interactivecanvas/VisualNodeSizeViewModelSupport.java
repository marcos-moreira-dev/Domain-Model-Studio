package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutService;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualNodeSizeCommand;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramSize;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Apoyo común para ampliar o reducir la figura seleccionada sin duplicar reglas por diagrama.
 *
 * <p>La operación actúa sobre el layout visual persistido. Los render kits siguen aplicando sus
 * políticas de ajuste de texto, por lo que agrandar una caja da más aire al contenido sin cambiar
 * la semántica del documento.</p>
 */
public final class VisualNodeSizeViewModelSupport {

    private static final double MIN_WIDTH = 88.0;
    private static final double MIN_HEIGHT = 56.0;
    private static final double MAX_WIDTH = 760.0;
    private static final double MAX_HEIGHT = 540.0;

    private VisualNodeSizeViewModelSupport() {
    }

    public static boolean resizeSelectedNode(
            VisualLayoutService visualLayoutService,
            DiagramProject currentProject,
            DiagramElementId selectedLayoutId,
            VisualNodeSizeCommand command,
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
            statusConsumer.accept("No hay diagrama visual activo para redimensionar figuras.");
            return false;
        }
        if (selectedLayoutId == null) {
            statusConsumer.accept("Selecciona una figura antes de cambiar su tamaño.");
            return false;
        }
        try {
            NodeLayout current = visualLayoutService.nodeLayout(currentProject, selectedLayoutId)
                    .orElseThrow(() -> new IllegalArgumentException("No existe layout para la figura seleccionada."));
            DiagramSize readableMinimum = readableMinimumSize(visualLayoutService, currentProject, selectedLayoutId);
            double targetWidth = bounded(current.width() * command.factor(), readableMinimum.width(), MAX_WIDTH);
            double targetHeight = bounded(current.height() * command.factor(), readableMinimum.height(), MAX_HEIGHT);
            if (unchanged(current, targetWidth, targetHeight)) {
                statusConsumer.accept("La figura seleccionada ya está en el límite de tamaño permitido.");
                return false;
            }
            DiagramProject updated = visualLayoutService.resizeNodeTo(currentProject, selectedLayoutId, targetWidth, targetHeight);
            projectUpdater.accept(updated);
            changeNotifier.run();
            statusConsumer.accept("Figura seleccionada " + command.visibleResult()
                    + "; el texto mantiene el autoajuste visual.");
            return true;
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo cambiar el tamaño visual: " + exception.getMessage());
            return false;
        }
    }


    private static DiagramSize readableMinimumSize(
            VisualLayoutService visualLayoutService,
            DiagramProject currentProject,
            DiagramElementId selectedLayoutId
    ) {
        return visualLayoutService.preferredNodeSize(currentProject, selectedLayoutId)
                .map(size -> DiagramSize.of(
                        Math.max(MIN_WIDTH, size.width() * 0.68),
                        Math.max(MIN_HEIGHT, size.height() * 0.68)))
                .orElseGet(() -> DiagramSize.of(MIN_WIDTH, MIN_HEIGHT));
    }

    private static double bounded(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static boolean unchanged(NodeLayout current, double width, double height) {
        return Math.abs(current.width() - width) < 0.5 && Math.abs(current.height() - height) < 0.5;
    }
}
