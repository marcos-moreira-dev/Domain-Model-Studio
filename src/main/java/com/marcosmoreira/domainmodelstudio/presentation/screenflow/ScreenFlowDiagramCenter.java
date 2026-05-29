package com.marcosmoreira.domainmodelstudio.presentation.screenflow;

import com.marcosmoreira.domainmodelstudio.application.export.DiagramExportHeaderMetadata;
import com.marcosmoreira.domainmodelstudio.application.export.DiagramExportHeaderPolicy;

import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenNode;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransition;
import com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas.CanvasInitialFitScheduler;
import com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas.ViewportFitMode;
import com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas.ZoomableDiagramSurface;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasSurfaceView;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasPngExporter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import javafx.collections.ListChangeListener;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

/** Centro visual del flujo de pantallas montado sobre la superficie canónica. */
final class ScreenFlowDiagramCenter {

    private final ScreenFlowViewModel viewModel;
    private final ScreenFlowCanvasAdapter canvasAdapter;
    private final ScreenFlowRenderKit renderKit = new ScreenFlowRenderKit();
    private final ZoomableDiagramSurface surface = new ZoomableDiagramSurface();
    private final InteractiveCanvasSurfaceView surfaceView;
    private final InteractiveCanvasPngExporter pngExporter;
    private final BorderPane root = new BorderPane();
    private final CanvasInitialFitScheduler initialFitScheduler = new CanvasInitialFitScheduler();

    ScreenFlowDiagramCenter(ScreenFlowViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.canvasAdapter = new ScreenFlowCanvasAdapter(viewModel);
        this.surfaceView = new InteractiveCanvasSurfaceView(canvasAdapter, renderKit, surface);
        this.pngExporter = new InteractiveCanvasPngExporter(canvasAdapter, renderKit, "screen-flow-export-surface", this::exportHeaderMetadata);
        build();
        bindViewModel();
        refreshAndFitIfNeeded();
    }

    Parent root() {
        return root;
    }


    Parent appearancePanel() {
        return new com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasAppearancePanel(
                () -> canvasAdapter,
                this::refreshCanvasOnly
        ).root();
    }

    void refresh() {
        if (!viewModel.active()) {
            clearInactiveSurface();
            return;
        }
        refreshCanvasOnly();
        scheduleInitialFitIfNeeded();
    }

    void refreshCanvasOnly() {
        if (!viewModel.active()) {
            clearInactiveSurface();
            return;
        }
        surfaceView.refreshPreservingViewport();
    }

    void refreshAndFitIfNeeded() {
        if (!viewModel.active()) {
            clearInactiveSurface();
            return;
        }
        refreshCanvasOnly();
        scheduleInitialFitIfNeeded();
    }

    void refitOnActivation() {
        if (surface.viewportAdjustedByUser()) {
            refresh();
            return;
        }
        forceFitToContent();
    }

    void fitDiagram() {
        if (!viewModel.active()) {
            clearInactiveSurface();
            return;
        }
        forceFitToContent();
    }

    void centerDiagram() {
        if (!viewModel.active()) {
            clearInactiveSurface();
            return;
        }
        forceCenterContent();
    }

    boolean deleteSelectedBendPoint() {
        return surfaceView.deleteSelectedBendPoint();
    }

    void exportDiagramAsPng(Path targetFile) throws IOException {
        refresh();
        pngExporter.export(targetFile);
    }


    private DiagramExportHeaderMetadata exportHeaderMetadata() {
        return DiagramExportHeaderPolicy.forProject(viewModel.currentProject(), "Flujo de pantallas");
    }

    private void build() {
        root.getStyleClass().add("screen-flow-diagram-center");
        root.setCenter(surfaceView.root());
    }

    private void bindViewModel() {
        viewModel.screens().addListener((ListChangeListener<ScreenNode>) change -> refreshCanvasOnly());
        viewModel.transitions().addListener((ListChangeListener<ScreenTransition>) change -> refreshCanvasOnly());
    }

    private void scheduleInitialFitIfNeeded() {
        if (!viewModel.active()) {
            clearInactiveSurface();
            return;
        }
        initialFitScheduler.schedule(
                currentProjectId(),
                !viewModel.screens().isEmpty(),
                surface,
                ViewportFitMode.FIT_WIDTH,
                surfaceView.semanticContentBounds());
    }

    private void forceFitToContent() {
        surfaceView.refresh();
        initialFitScheduler.reset();
        surface.clearViewportAdjustedByUser();
        surface.fitToContentWhenReady(ViewportFitMode.FIT_TO_CONTENT, surfaceView.semanticContentBounds());
    }

    private void forceCenterContent() {
        surfaceView.refresh();
        initialFitScheduler.reset();
        surface.fitToContentWhenReady(ViewportFitMode.CENTER_CONTENT, surfaceView.semanticContentBounds());
    }

    private void clearInactiveSurface() {
        surfaceView.clear();
        initialFitScheduler.reset();
    }

    private String currentProjectId() {
        return viewModel.currentProject() == null ? "" : viewModel.currentProject().metadata().id();
    }


}
