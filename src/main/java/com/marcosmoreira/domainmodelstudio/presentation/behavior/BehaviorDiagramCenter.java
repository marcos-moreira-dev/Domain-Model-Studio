package com.marcosmoreira.domainmodelstudio.presentation.behavior;

import com.marcosmoreira.domainmodelstudio.application.export.DiagramExportHeaderMetadata;
import com.marcosmoreira.domainmodelstudio.application.export.DiagramExportHeaderPolicy;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas.CanvasInitialFitScheduler;
import com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas.ViewportFitMode;
import com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas.ZoomableDiagramSurface;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasAdapter;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasRenderKit;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasSurfaceView;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasPngExporter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import javafx.collections.ListChangeListener;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/** Centro visual de comportamiento/secuencia montado sobre la superficie canónica. */
final class BehaviorDiagramCenter {

    private final BehaviorDiagramViewModel viewModel;
    private final BehaviorCanvasAdapter behaviorAdapter;
    private final SequenceCanvasAdapter sequenceAdapter;
    private final BehaviorRenderKit behaviorRenderKit = new BehaviorRenderKit();
    private final SequenceRenderKit sequenceRenderKit = new SequenceRenderKit();
    private final ZoomableDiagramSurface behaviorSurface = new ZoomableDiagramSurface();
    private final ZoomableDiagramSurface sequenceSurface = new ZoomableDiagramSurface();
    private final InteractiveCanvasSurfaceView behaviorSurfaceView;
    private final InteractiveCanvasSurfaceView sequenceSurfaceView;
    private final BorderPane root = new BorderPane();
    private final StackPane surfaceHost = new StackPane();
    private final CanvasInitialFitScheduler initialFitScheduler = new CanvasInitialFitScheduler();

    BehaviorDiagramCenter(BehaviorDiagramViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.behaviorAdapter = new BehaviorCanvasAdapter(viewModel);
        this.sequenceAdapter = new SequenceCanvasAdapter(viewModel);
        this.behaviorSurfaceView = new InteractiveCanvasSurfaceView(behaviorAdapter, behaviorRenderKit, behaviorSurface);
        this.sequenceSurfaceView = new InteractiveCanvasSurfaceView(sequenceAdapter, sequenceRenderKit, sequenceSurface);
        build();
        bindViewModel();
        refreshAndFitIfNeeded();
    }

    Parent root() {
        return root;
    }


    Parent appearancePanel() {
        return new com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasAppearancePanel(
                this::activeAdapter,
                this::refreshCanvasOnly
        ).root();
    }

    void refresh() {
        if (!viewModel.active()) {
            clearInactiveSurfaces();
            return;
        }
        refreshCanvasOnly();
        scheduleInitialFitIfNeeded();
    }

    void refreshCanvasOnly() {
        if (!viewModel.active()) {
            clearInactiveSurfaces();
            return;
        }
        boolean sequence = isSequenceDiagram();
        behaviorSurfaceView.root().setVisible(!sequence);
        behaviorSurfaceView.root().setManaged(!sequence);
        sequenceSurfaceView.root().setVisible(sequence);
        sequenceSurfaceView.root().setManaged(sequence);
        activeSurface().preservingViewport(activeSurfaceView()::refresh);
    }

    void refreshAndFitIfNeeded() {
        if (!viewModel.active()) {
            clearInactiveSurfaces();
            return;
        }
        refreshCanvasOnly();
        scheduleInitialFitIfNeeded();
    }

    void refitOnActivation() {
        if (activeSurface().viewportAdjustedByUser()) {
            refresh();
            return;
        }
        forceFitToContent();
    }

    void fitDiagram() {
        if (!viewModel.active()) {
            clearInactiveSurfaces();
            return;
        }
        forceFitToContent();
    }

    void centerDiagram() {
        if (!viewModel.active()) {
            clearInactiveSurfaces();
            return;
        }
        forceCenterContent();
    }

    boolean deleteSelectedBendPoint() {
        return activeSurfaceView().deleteSelectedBendPoint();
    }

    void exportDiagramAsPng(Path targetFile) throws IOException {
        refresh();
        new InteractiveCanvasPngExporter(
                activeAdapter(),
                activeRenderKit(),
                "behavior-export-surface",
                this::exportHeaderMetadata
        ).export(targetFile);
    }


    private DiagramExportHeaderMetadata exportHeaderMetadata() {
        return DiagramExportHeaderPolicy.forProject(viewModel.currentProject(), viewModel.title());
    }

    private void build() {
        root.getStyleClass().add("behavior-diagram-center");
        surfaceHost.getStyleClass().add("behavior-diagram-surface-host");
        surfaceHost.getChildren().addAll(behaviorSurfaceView.root(), sequenceSurfaceView.root());
        root.setCenter(surfaceHost);
    }

    private void bindViewModel() {
        viewModel.nodes().addListener((ListChangeListener<BehaviorNode>) change -> refreshCanvasOnly());
        viewModel.edges().addListener((ListChangeListener<BehaviorEdge>) change -> refreshCanvasOnly());
    }

    private void scheduleInitialFitIfNeeded() {
        if (!viewModel.active()) {
            clearInactiveSurfaces();
            return;
        }
        initialFitScheduler.schedule(
                currentFitKey(),
                !viewModel.nodes().isEmpty(),
                activeSurface(),
                isSequenceDiagram() ? ViewportFitMode.FIT_WIDTH : ViewportFitMode.FIT_TO_CONTENT,
                activeSurfaceView().semanticContentBounds()
        );
    }

    private void forceFitToContent() {
        InteractiveCanvasSurfaceView currentSurfaceView = activeSurfaceView();
        ZoomableDiagramSurface currentSurface = activeSurface();
        currentSurfaceView.refresh();
        initialFitScheduler.reset();
        currentSurface.clearViewportAdjustedByUser();
        currentSurface.fitToContentWhenReady(
                isSequenceDiagram() ? ViewportFitMode.FIT_WIDTH : ViewportFitMode.FIT_TO_CONTENT,
                currentSurfaceView.semanticContentBounds());
    }

    private void forceCenterContent() {
        InteractiveCanvasSurfaceView currentSurfaceView = activeSurfaceView();
        ZoomableDiagramSurface currentSurface = activeSurface();
        currentSurfaceView.refresh();
        initialFitScheduler.reset();
        currentSurface.fitToContentWhenReady(ViewportFitMode.CENTER_CONTENT, currentSurfaceView.semanticContentBounds());
    }

    private void clearInactiveSurfaces() {
        behaviorSurfaceView.clear();
        sequenceSurfaceView.clear();
        behaviorSurfaceView.root().setVisible(true);
        behaviorSurfaceView.root().setManaged(true);
        sequenceSurfaceView.root().setVisible(false);
        sequenceSurfaceView.root().setManaged(false);
        initialFitScheduler.reset();
    }

    private String currentFitKey() {
        String projectId = viewModel.currentProject() == null ? "" : viewModel.currentProject().metadata().id();
        return projectId + "::" + viewModel.currentKind().name();
    }


    private boolean isSequenceDiagram() {
        return viewModel.currentKind() == BehaviorDiagramKind.UML_SEQUENCE;
    }

    private InteractiveCanvasSurfaceView activeSurfaceView() {
        return isSequenceDiagram() ? sequenceSurfaceView : behaviorSurfaceView;
    }

    private ZoomableDiagramSurface activeSurface() {
        return isSequenceDiagram() ? sequenceSurface : behaviorSurface;
    }

    private InteractiveCanvasAdapter activeAdapter() {
        return isSequenceDiagram() ? sequenceAdapter : behaviorAdapter;
    }


    private InteractiveCanvasRenderKit activeRenderKit() {
        return isSequenceDiagram() ? sequenceRenderKit : behaviorRenderKit;
    }

}
