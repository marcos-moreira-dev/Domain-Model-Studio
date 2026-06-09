package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayerOrderCommand;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualNodeSizeCommand;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportPngAction;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Centraliza acciones transversales de vista para lienzos visuales especializados.
 *
 * <p>Varios ViewModels visuales exponen los mismos comandos públicos para registrar
 * exportación PNG, ajustar la vista y centrar el lienzo. Esta clase mantiene esos
 * comandos en un solo lugar sin acoplarse al tipo concreto de diagrama.</p>
 */
public final class VisualDiagramViewActions {

    private final BooleanSupplier activeSupplier;
    private final Consumer<String> statusConsumer;
    private final String noFitMessage;
    private final String fitStatusMessage;
    private final String noCenterMessage;
    private final String centerStatusMessage;
    private ExportPngAction pngExportAction;
    private Runnable fitDiagramAction = () -> { };
    private Runnable centerDiagramAction = () -> { };
    private Runnable refreshDiagramAction = () -> { };
    private BooleanSupplier deleteSelectedBendPointAction = () -> false;
    private Runnable visualCommentToolAction = () -> { };
    private Function<VisualLayerOrderCommand, Boolean> visualCommentLayerOrderAction = command -> false;
    private Function<VisualNodeSizeCommand, Boolean> visualCommentSizeAction = command -> false;

    public static VisualDiagramViewActions forGenericDiagram(
            BooleanSupplier activeSupplier,
            Consumer<String> statusConsumer,
            String pngUnavailableMessage
    ) {
        return new VisualDiagramViewActions(
                activeSupplier,
                statusConsumer,
                pngUnavailableMessage,
                "No hay diagrama activo para ajustar la vista.",
                "Vista ajustada al contenido del diagrama activo.",
                "No hay diagrama activo para centrar la vista.",
                "Vista centrada en el diagrama activo."
        );
    }

    public static VisualDiagramViewActions forFreeGraph(
            BooleanSupplier activeSupplier,
            Consumer<String> statusConsumer,
            String pngUnavailableMessage
    ) {
        return new VisualDiagramViewActions(
                activeSupplier,
                statusConsumer,
                pngUnavailableMessage,
                "No hay grafo activo para ajustar la vista.",
                "Vista ajustada al contenido del grafo.",
                "No hay grafo activo para centrar la vista.",
                "Vista centrada en el grafo."
        );
    }

    private VisualDiagramViewActions(
            BooleanSupplier activeSupplier,
            Consumer<String> statusConsumer,
            String pngUnavailableMessage,
            String noFitMessage,
            String fitStatusMessage,
            String noCenterMessage,
            String centerStatusMessage
    ) {
        this.activeSupplier = Objects.requireNonNull(activeSupplier, "activeSupplier");
        this.statusConsumer = Objects.requireNonNull(statusConsumer, "statusConsumer");
        this.noFitMessage = Objects.requireNonNull(noFitMessage, "noFitMessage");
        this.fitStatusMessage = Objects.requireNonNull(fitStatusMessage, "fitStatusMessage");
        this.noCenterMessage = Objects.requireNonNull(noCenterMessage, "noCenterMessage");
        this.centerStatusMessage = Objects.requireNonNull(centerStatusMessage, "centerStatusMessage");
        this.pngExportAction = targetFile -> {
            throw new IllegalStateException(Objects.requireNonNull(pngUnavailableMessage, "pngUnavailableMessage"));
        };
    }

    public void registerPngExportAction(ExportPngAction action) {
        if (action != null) {
            this.pngExportAction = action;
        }
    }

    public void registerDiagramFitAction(Runnable action) {
        if (action != null) {
            this.fitDiagramAction = action;
        }
    }

    public void registerDiagramCenterAction(Runnable action) {
        if (action != null) {
            this.centerDiagramAction = action;
        }
    }

    public void registerDiagramRefreshAction(Runnable action) {
        if (action != null) {
            this.refreshDiagramAction = action;
        }
    }

    public void registerDeleteSelectedBendPointAction(BooleanSupplier action) {
        if (action != null) {
            this.deleteSelectedBendPointAction = action;
        }
    }

    public void registerVisualCommentToolAction(Runnable action) {
        if (action != null) {
            this.visualCommentToolAction = action;
        }
    }

    public void registerVisualCommentLayerOrderAction(Function<VisualLayerOrderCommand, Boolean> action) {
        if (action != null) {
            this.visualCommentLayerOrderAction = action;
        }
    }

    public void registerVisualCommentSizeAction(Function<VisualNodeSizeCommand, Boolean> action) {
        if (action != null) {
            this.visualCommentSizeAction = action;
        }
    }

    public void fitDiagramView() {
        runWhenActive(fitDiagramAction, noFitMessage, fitStatusMessage);
    }

    public void centerDiagramView() {
        runWhenActive(centerDiagramAction, noCenterMessage, centerStatusMessage);
    }

    /**
     * Refresca el canvas activo sin ajustar viewport.
     *
     * <p>Se usa para comandos transversales como orden visual/capas: el modelo y el
     * layout ya cambiaron, pero las listas semánticas no necesariamente cambian, por
     * lo que no hay un {@code ListChangeListener} que fuerce redibujado.</p>
     */
    public void refreshDiagramView() {
        if (activeSupplier.getAsBoolean()) {
            refreshDiagramAction.run();
        }
    }

    public boolean deleteSelectedBendPoint() {
        if (!activeSupplier.getAsBoolean()) {
            statusConsumer.accept("No hay diagrama activo para quitar puntos intermedios.");
            return false;
        }
        boolean removed = deleteSelectedBendPointAction.getAsBoolean();
        statusConsumer.accept(removed
                ? "Punto intermedio eliminado."
                : "Selecciona un punto intermedio de una relación para quitarlo.");
        return removed;
    }

    public void activateVisualCommentTool() {
        if (!activeSupplier.getAsBoolean()) {
            statusConsumer.accept("No hay diagrama activo para colocar comentarios visuales.");
            return;
        }
        visualCommentToolAction.run();
        statusConsumer.accept("Comentario: haz clic en el lienzo para colocarlo.");
    }

    public boolean reorderSelectedVisualComment(VisualLayerOrderCommand command) {
        if (!activeSupplier.getAsBoolean()) {
            return false;
        }
        return Boolean.TRUE.equals(visualCommentLayerOrderAction.apply(command));
    }

    public boolean resizeSelectedVisualComment(VisualNodeSizeCommand command) {
        if (!activeSupplier.getAsBoolean()) {
            return false;
        }
        return Boolean.TRUE.equals(visualCommentSizeAction.apply(command));
    }

    public void exportVisualAsPng(Path targetFile) throws IOException {
        pngExportAction.export(targetFile);
    }

    public ExportPngAction pngExportAction() {
        return pngExportAction;
    }

    private void runWhenActive(Runnable action, String inactiveMessage, String successMessage) {
        if (!activeSupplier.getAsBoolean()) {
            statusConsumer.accept(inactiveMessage);
            return;
        }
        action.run();
        statusConsumer.accept(successMessage);
    }
}
