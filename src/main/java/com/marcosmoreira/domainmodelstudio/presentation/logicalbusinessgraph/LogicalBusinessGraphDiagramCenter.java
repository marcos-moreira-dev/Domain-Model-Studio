package com.marcosmoreira.domainmodelstudio.presentation.logicalbusinessgraph;

import com.marcosmoreira.domainmodelstudio.application.export.DiagramExportHeaderMetadata;
import com.marcosmoreira.domainmodelstudio.application.export.DiagramExportHeaderPolicy;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNode;
import com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas.CanvasInitialFitScheduler;
import com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas.ViewportFitMode;
import com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas.ZoomableDiagramSurface;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasPngExporter;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasSurfaceView;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import javafx.collections.ListChangeListener;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

/**
 * Centro visual del Grafo lógico del negocio montado sobre la superficie canónica.
 *
 * <p>Compone {@link ZoomableDiagramSurface}, {@link InteractiveCanvasSurfaceView}, adaptador,
 * render kit y exportador PNG. Su responsabilidad es montar y refrescar la vista visual, no validar semántica ni decidir capacidades de producto.</p>
 */
final class LogicalBusinessGraphDiagramCenter {

    private final LogicalBusinessGraphViewModel viewModel;
    private final LogicalBusinessGraphCanvasAdapter canvasAdapter;
    private final LogicalBusinessGraphRenderKit renderKit = new LogicalBusinessGraphRenderKit();
    private final ZoomableDiagramSurface surface = new ZoomableDiagramSurface();
    private final InteractiveCanvasSurfaceView surfaceView;
    private final InteractiveCanvasPngExporter pngExporter;
    private final BorderPane root = new BorderPane();
    private final CanvasInitialFitScheduler initialFitScheduler = new CanvasInitialFitScheduler();

    LogicalBusinessGraphDiagramCenter(LogicalBusinessGraphViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.canvasAdapter = new LogicalBusinessGraphCanvasAdapter(viewModel);
        this.surfaceView = new InteractiveCanvasSurfaceView(canvasAdapter, renderKit, surface);
        this.pngExporter = new InteractiveCanvasPngExporter(canvasAdapter, renderKit, "logical-business-graph-export-surface", this::exportHeaderMetadata);
        build();
        bindViewModel();
        refreshAndFitIfNeeded();
    }

    Parent root() { return root; }

    Parent appearancePanel() {
        return new com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasAppearancePanel(
                () -> canvasAdapter,
                this::refreshCanvasOnly
        ).root();
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
            refreshAndFitIfNeeded();
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
        refreshAndFitIfNeeded();
        pngExporter.export(targetFile);
    }


    private DiagramExportHeaderMetadata exportHeaderMetadata() {
        return DiagramExportHeaderPolicy.forProject(viewModel.currentProject(), "Grafo lógico del negocio");
    }

    private void build() {
        root.getStyleClass().add("logical-business-graph-diagram-center");
        root.setCenter(surfaceView.root());
    }

    private void bindViewModel() {
        viewModel.nodes().addListener((ListChangeListener<LogicalBusinessGraphNode>) change -> refreshCanvasOnly());
        viewModel.edges().addListener((ListChangeListener<LogicalBusinessGraphEdge>) change -> refreshCanvasOnly());
    }

    private void scheduleInitialFitIfNeeded() {
        initialFitScheduler.schedule(
                currentProjectId(),
                !viewModel.nodes().isEmpty(),
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
