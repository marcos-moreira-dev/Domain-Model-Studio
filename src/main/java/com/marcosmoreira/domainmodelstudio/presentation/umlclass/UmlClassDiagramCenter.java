package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.application.export.DiagramExportHeaderMetadata;
import com.marcosmoreira.domainmodelstudio.application.export.DiagramExportHeaderPolicy;

import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas.CanvasInitialFitScheduler;
import com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas.ViewportFitMode;
import com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas.ZoomableDiagramSurface;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasSurfaceView;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasPngExporter;
import javafx.application.Platform;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.collections.ListChangeListener;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

/** Centro visual de UML Clases montado sobre la superficie canónica. */
final class UmlClassDiagramCenter {

    private final UmlClassDiagramViewModel viewModel;
    private final UmlClassCanvasAdapter canvasAdapter;
    private final UmlClassRenderKit renderKit;
    private final ZoomableDiagramSurface surface = new ZoomableDiagramSurface();
    private final InteractiveCanvasSurfaceView surfaceView;
    private final InteractiveCanvasPngExporter pngExporter;
    private final BorderPane root = new BorderPane();
    private final CanvasInitialFitScheduler initialFitScheduler = new CanvasInitialFitScheduler();
    private boolean canvasRefreshQueued;

    UmlClassDiagramCenter(UmlClassDiagramViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.canvasAdapter = new UmlClassCanvasAdapter(viewModel);
        this.renderKit = new UmlClassRenderKit(viewModel);
        this.surfaceView = new InteractiveCanvasSurfaceView(canvasAdapter, renderKit, surface);
        this.pngExporter = new InteractiveCanvasPngExporter(canvasAdapter, renderKit, "uml-class-export-surface", this::exportHeaderMetadata);
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
                this::requestCanvasRefresh
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

    void requestCanvasRefresh() {
        if (canvasRefreshQueued) {
            return;
        }
        canvasRefreshQueued = true;
        if (!Platform.isFxApplicationThread()) {
            runQueuedCanvasRefresh();
            return;
        }
        Platform.runLater(this::runQueuedCanvasRefresh);
    }

    private void runQueuedCanvasRefresh() {
        canvasRefreshQueued = false;
        refreshCanvasOnly();
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

    void centerSelection() {
        if (!viewModel.active()) {
            clearInactiveSurface();
            return;
        }
        refreshCanvasOnly();
        selectionBounds().ifPresentOrElse(surface::centerOn, this::forceCenterContent);
    }

    boolean deleteSelectedBendPoint() {
        return surfaceView.deleteSelectedBendPoint();
    }

    void exportVisualAsPng(Path targetFile) throws IOException {
        refresh();
        pngExporter.export(targetFile);
    }


    private DiagramExportHeaderMetadata exportHeaderMetadata() {
        return DiagramExportHeaderPolicy.forProject(viewModel.currentProject(), "Diagrama UML de clases");
    }

    private void build() {
        root.getStyleClass().add("uml-class-diagram-center");
        root.setCenter(surfaceView.root());
    }

    private void bindViewModel() {
        viewModel.modules().addListener((ListChangeListener<UmlModuleGroup>) change -> requestCanvasRefresh());
        viewModel.classes().addListener((ListChangeListener<UmlClassNode>) change -> requestCanvasRefresh());
        viewModel.relations().addListener((ListChangeListener<UmlClassRelation>) change -> requestCanvasRefresh());
        // La selección de nodos UML se pinta localmente durante el gesto para no reconstruir
        // todo el canvas en medio de un clic/arrastre. Las listas/filtros siguen refrescando
        // el lienzo; la selección desde paneles laterales llama refreshCanvas explícitamente.
    }

    private void scheduleInitialFitIfNeeded() {
        if (!viewModel.active()) {
            clearInactiveSurface();
            return;
        }
        initialFitScheduler.schedule(
                currentProjectId(),
                !(viewModel.modules().isEmpty() && viewModel.classes().isEmpty()),
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

    private Optional<Bounds> selectionBounds() {
        double originX = surface.config().contentOriginX();
        double originY = surface.config().contentOriginY();
        if (viewModel.selectedClassProperty().get() != null) {
            NodeLayout layout = viewModel.layoutForClass(viewModel.selectedClassProperty().get());
            return Optional.of(new BoundingBox(originX + layout.x(), originY + layout.y(), layout.width(), layout.height()));
        }
        if (viewModel.selectedModuleProperty().get() != null) {
            NodeLayout layout = viewModel.layoutForModule(viewModel.selectedModuleProperty().get());
            return Optional.of(new BoundingBox(originX + layout.x(), originY + layout.y(), layout.width(), layout.height()));
        }
        return Optional.empty();
    }

    private void clearInactiveSurface() {
        surfaceView.clear();
        initialFitScheduler.reset();
    }

    private String currentProjectId() {
        return viewModel.currentProject() == null ? "" : viewModel.currentProject().metadata().id();
    }


}
