package com.marcosmoreira.domainmodelstudio.presentation.wireframe;

import com.marcosmoreira.domainmodelstudio.application.export.DiagramExportHeaderMetadata;
import com.marcosmoreira.domainmodelstudio.application.export.DiagramExportHeaderPolicy;

import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponent;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreen;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

/** Centro visual de wireframes montado sobre la superficie canónica. */
final class WireframeDiagramCenter {

    private final WireframeViewModel viewModel;
    private final WireframeCanvasAdapter canvasAdapter;
    private final WireframeRenderKit renderKit = new WireframeRenderKit();
    private final ZoomableDiagramSurface surface = new ZoomableDiagramSurface();
    private final InteractiveCanvasSurfaceView surfaceView;
    private final InteractiveCanvasPngExporter pngExporter;
    private final BorderPane root = new BorderPane();
    private final CanvasInitialFitScheduler initialFitScheduler = new CanvasInitialFitScheduler();

    WireframeDiagramCenter(WireframeViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.canvasAdapter = new WireframeCanvasAdapter(viewModel);
        this.surfaceView = new InteractiveCanvasSurfaceView(canvasAdapter, renderKit, surface);
        this.pngExporter = new InteractiveCanvasPngExporter(canvasAdapter, renderKit, "wireframe-export-surface", this::exportHeaderMetadata);
        build();
        bindViewModel();
        installWireframeShortcuts();
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

    void exportWireframesAsPng(Path targetFile) throws IOException {
        refresh();
        pngExporter.export(targetFile);
    }


    private DiagramExportHeaderMetadata exportHeaderMetadata() {
        return DiagramExportHeaderPolicy.forProject(viewModel.currentProject(), "Wireframes administrativos");
    }

    private void build() {
        root.getStyleClass().add("wireframe-diagram-center");
        root.setCenter(surfaceView.root());
    }



    private void installWireframeShortcuts() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
                canvasAdapter.deleteSelectedItems();
                refreshCanvasOnly();
                event.consume();
                return;
            }
            if (event.isShortcutDown() && event.getCode() == KeyCode.D) {
                viewModel.duplicateSelected();
                refreshCanvasOnly();
                event.consume();
            }
        });
    }

    private void bindViewModel() {
        viewModel.screens().addListener((ListChangeListener<WireframeScreen>) change -> refreshCanvasOnly());
        viewModel.components().addListener((ListChangeListener<WireframeComponent>) change -> refreshCanvasOnly());
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
                ViewportFitMode.FIT_TO_CONTENT,
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
