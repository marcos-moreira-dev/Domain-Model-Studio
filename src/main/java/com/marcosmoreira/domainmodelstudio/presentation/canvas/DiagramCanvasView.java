package com.marcosmoreira.domainmodelstudio.presentation.canvas;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.canvas.interaction.DiagramCanvasAreaSelectionController;
import com.marcosmoreira.domainmodelstudio.presentation.canvas.interaction.DiagramCanvasKeyboardShortcutController;
import com.marcosmoreira.domainmodelstudio.presentation.canvas.interaction.DiagramCanvasPanController;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.application.Platform;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;

/**
 * Vista central del área de trabajo.
 *
 * <p>el canvas permite seleccionar figuras, arrastrarlas y
 * sincronizar selección con el árbol lateral. La View captura eventos visuales, pero la
 * actualización real de layout se delega al ViewModel y al caso de uso de application.</p>
 */
public final class DiagramCanvasView {

    private final DiagramCanvasViewModel viewModel;
    private final ConceptualCanvasAdapter conceptualCanvasAdapter;
    private static final double WORKSPACE_WIDTH = 12000.0;
    private static final double WORKSPACE_HEIGHT = 8000.0;
    private static final double CONTENT_ORIGIN_X = 4600.0;
    private static final double CONTENT_ORIGIN_Y = 3000.0;

    private final BorderPane root = new BorderPane();
    private final StackPane workspaceHost = new StackPane();
    private final boolean workspaceHeaderEnabled;
    private VBox workspaceHeader;
    private boolean workspaceHeaderDismissed;
    private final Pane canvas = new Pane();
    private final Group zoomGroup = new Group(canvas);
    private final Pane zoomContainer = new Pane(zoomGroup);
    private final Rectangle workspaceFill = new Rectangle(0, 0, WORKSPACE_WIDTH, WORKSPACE_HEIGHT);
    private final Group workspaceBackground = new Group();
    private final Group contentLayer = new Group();
    private final Group liveConnectorLayer = new Group();
    private ScrollPane scrollPane;
    private Parent welcomeRoot;
    private double zoomFactor = 1.0;
    private long zoomSequence;
    private final ChenDiagramRenderer chenRenderer = new ChenDiagramRenderer();
    private final CrowsFootDiagramRenderer crowsFootRenderer = new CrowsFootDiagramRenderer();

    private double dragStartSceneX;
    private double dragStartSceneY;
    private boolean dragging;
    private boolean pointerInteraction;
    private double bendDragStartSceneX;
    private double bendDragStartSceneY;
    private double labelDragStartSceneX;
    private double labelDragStartSceneY;
    private double endpointDragStartSceneX;
    private double endpointDragStartSceneY;
    private DiagramCanvasPanController panController;
    private DiagramCanvasAreaSelectionController areaSelectionController;
    private final List<Node> draggedNodes = new ArrayList<>();

    public DiagramCanvasView(DiagramCanvasViewModel viewModel) {
        this(viewModel, true);
    }

    public DiagramCanvasView(DiagramCanvasViewModel viewModel, boolean workspaceHeaderEnabled) {
        this.viewModel = viewModel;
        this.workspaceHeaderEnabled = workspaceHeaderEnabled;
        this.conceptualCanvasAdapter = new ConceptualCanvasAdapter(viewModel);
        build();
        CanvasPngExporter pngExporter = new CanvasPngExporter(viewModel, chenRenderer, crowsFootRenderer);
        viewModel.registerPngExportAction(pngExporter::export);
        viewModel.registerCanvasNavigationAction(new DiagramCanvasViewModel.CanvasNavigationAction() {
            @Override
            public void zoomIn() {
                changeZoom(1.15);
            }

            @Override
            public void zoomOut() {
                changeZoom(1.0 / 1.15);
            }

            @Override
            public void resetZoom() {
                setZoomAtViewportCenter(1.0);
            }

            @Override
            public void fitToContent() {
                fitContentToViewport();
            }

            @Override
            public void centerDiagram() {
                requestCenterContent();
            }

            @Override
            public void centerSelection() {
                centerSelectedElement();
            }
        });
        bindProjectChanges();
        bindSelectionChanges();
    }

    public Parent getRoot() {
        return root;
    }

    ConceptualCanvasAdapter conceptualCanvasAdapter() {
        return conceptualCanvasAdapter;
    }

    private void build() {
        root.getStyleClass().add("workspace-panel");
        if (workspaceHeaderEnabled) {
            workspaceHeader = buildHeader();
        }
        root.setCenter(buildWorkspaceHost());
    }

    private VBox buildHeader() {
        Label title = new Label();
        title.textProperty().bind(viewModel.titleProperty());
        title.getStyleClass().add("workspace-title");

        Label subtitle = new Label();
        subtitle.textProperty().bind(viewModel.subtitleProperty());
        subtitle.getStyleClass().add("workspace-subtitle");

        Label summary = new Label();
        summary.textProperty().bind(viewModel.importedSummaryProperty());
        summary.getStyleClass().add("workspace-subtitle");

        VBox textBox = new VBox(2, title, subtitle, summary);
        HBox headerRow = new HBox(8);
        headerRow.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        Button close = new Button("×");
        close.getStyleClass().addAll("workspace-header-close", "panel-header-icon-button");
        close.setTooltip(new Tooltip("Ocultar encabezado del área de trabajo"));
        close.setMnemonicParsing(false);
        close.setOnAction(event -> {
            workspaceHeaderDismissed = true;
            updateHeaderVisibility(false);
        });

        headerRow.getChildren().addAll(textBox, close);

        VBox header = new VBox(headerRow);
        header.getStyleClass().add("workspace-header");
        header.setPadding(new Insets(8, 10, 8, 10));
        return header;
    }

    private StackPane buildWorkspaceHost() {
        workspaceHost.getStyleClass().add("workspace-host");
        ScrollPane diagramScroll = buildScrollCanvas();
        welcomeRoot = new WelcomeWorkspaceView(viewModel).build();
        workspaceHost.getChildren().setAll(diagramScroll, welcomeRoot);
        showWelcomeWorkspace();
        return workspaceHost;
    }

    private ScrollPane buildScrollCanvas() {
        canvas.getStyleClass().addAll("diagram-canvas", "diagram-infinite-workspace");
        canvas.setFocusTraversable(true);
        installCanvasKeyboardShortcuts();
        canvas.setMinSize(WORKSPACE_WIDTH, WORKSPACE_HEIGHT);
        canvas.setPrefSize(WORKSPACE_WIDTH, WORKSPACE_HEIGHT);
        canvas.setMaxSize(WORKSPACE_WIDTH, WORKSPACE_HEIGHT);
        rebuildWorkspaceBackground();
        liveConnectorLayer.setMouseTransparent(true);
        liveConnectorLayer.getStyleClass().add("diagram-live-preview-root");
        canvas.getChildren().setAll(workspaceBackground, contentLayer, liveConnectorLayer);
        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                startPanning(event);
                return;
            }
            if (event.getButton() == MouseButton.PRIMARY && !isInteractiveTarget(event.getTarget())) {
                Point2D contentPoint = contentLayer.sceneToLocal(event.getSceneX(), event.getSceneY());
                if (viewModel.handleCanvasPrimaryClick(contentPoint.getX(), contentPoint.getY())) {
                    event.consume();
                    return;
                }
                startAreaSelection(event);
            }
        });
        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (isPanning()) {
                continuePanning(event);
            } else if (isAreaSelecting()) {
                continueAreaSelection(event);
            }
        });
        canvas.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            if (isPanning() && event.getButton() == MouseButton.SECONDARY) {
                finishPanning(event);
            } else if (isAreaSelecting() && event.getButton() == MouseButton.PRIMARY) {
                finishAreaSelection(event);
            }
        });

        zoomGroup.getStyleClass().add("diagram-zoom-group");
        zoomContainer.getStyleClass().add("diagram-zoom-container");
        zoomContainer.setMinSize(WORKSPACE_WIDTH, WORKSPACE_HEIGHT);
        zoomContainer.setPrefSize(WORKSPACE_WIDTH, WORKSPACE_HEIGHT);
        scrollPane = new ScrollPane(zoomContainer);
        scrollPane.getStyleClass().add("canvas-scroll-pane");
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        scrollPane.setPannable(false);
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (Math.abs(event.getDeltaY()) < 0.01) {
                return;
            }
            double factor = event.getDeltaY() > 0 ? 1.08 : 1.0 / 1.08;
            changeZoom(factor);
            event.consume();
        });
        panController = new DiagramCanvasPanController(
                scrollPane,
                canvas,
                zoomContainer,
                () -> zoomFactor,
                WORKSPACE_WIDTH,
                WORKSPACE_HEIGHT
        );
        areaSelectionController = new DiagramCanvasAreaSelectionController(
                contentLayer,
                () -> viewModel.currentProject() != null,
                viewModel::clearSelection,
                viewModel::selectElements,
                this::selectableNodesInside
        );
        applyZoom();
        requestCenterContent();
        return scrollPane;
    }

    private void installCanvasKeyboardShortcuts() {
        new DiagramCanvasKeyboardShortcutController(canvas, viewModel::removeSelectedBendPoint).install();
    }

    private void rebuildWorkspaceBackground() {
        CanvasWorkspaceBackground.rebuild(workspaceBackground, workspaceFill, WORKSPACE_WIDTH, WORKSPACE_HEIGHT);
    }

    private void showWelcomeWorkspace() {
        contentLayer.getChildren().clear();
        setZoomFactor(1.0);
        updateHeaderVisibility(false);
        if (scrollPane != null) {
            scrollPane.setVisible(false);
            scrollPane.setManaged(false);
        }
        if (welcomeRoot != null) {
            welcomeRoot.setVisible(true);
            welcomeRoot.setManaged(true);
        }
    }

    private void showDiagramWorkspace() {
        updateHeaderVisibility(true);
        if (welcomeRoot != null) {
            welcomeRoot.setVisible(false);
            welcomeRoot.setManaged(false);
        }
        if (scrollPane != null) {
            scrollPane.setManaged(true);
            scrollPane.setVisible(true);
        }
    }

    private void updateHeaderVisibility(boolean visible) {
        if (!workspaceHeaderEnabled || workspaceHeader == null) {
            root.setTop(null);
            return;
        }
        if (visible && !workspaceHeaderDismissed) {
            root.setTop(workspaceHeader);
        } else {
            root.setTop(null);
        }
    }

    private void bindProjectChanges() {
        viewModel.currentProjectProperty().addListener((observable, previousProject, currentProject) -> {
            if (currentProject == null) {
                applyDefaultWorkspaceAppearance();
                showWelcomeWorkspace();
                return;
            }
            ViewportState viewportBefore = captureViewportState();
            boolean openingFromEmptyState = previousProject == null;
            if (openingFromEmptyState) {
                workspaceHeaderDismissed = false;
            }
            applyWorkspaceAppearance(currentProject);
            renderProject(currentProject);
            if (openingFromEmptyState) {
                requestCenterContent();
            } else {
                restoreViewportStateLater(viewportBefore);
            }
        });
    }

    private void bindSelectionChanges() {
        viewModel.selectedElementIdsProperty().addListener((observable, previous, current) -> {
            DiagramProject currentProject = viewModel.currentProject();
            if (currentProject != null && !dragging && !pointerInteraction && !isAreaSelecting()) {
                ViewportState viewportBefore = captureViewportState();
                renderProject(currentProject);
                restoreViewportStateLater(viewportBefore);
            }
        });
    }

    private void renderProject(DiagramProject project) {
        showDiagramWorkspace();
        applyWorkspaceAppearance(project);
        clearLiveConnectorPreview();
        Group rendered = switch (project.metadata().activeNotation()) {
            case CHEN -> chenRenderer.render(project, viewModel.selectedElementIds());
            case CROWS_FOOT -> crowsFootRenderer.render(project, viewModel.selectedElementIds());
        };
        installNodeInteractions(rendered);
        contentLayer.setLayoutX(CONTENT_ORIGIN_X);
        contentLayer.setLayoutY(CONTENT_ORIGIN_Y);
        liveConnectorLayer.setLayoutX(CONTENT_ORIGIN_X);
        liveConnectorLayer.setLayoutY(CONTENT_ORIGIN_Y);
        contentLayer.getChildren().setAll(rendered);
    }

    private void renderProjectPreview(DiagramProject project) {
        if (project == null) {
            return;
        }
        ViewportState viewportBefore = captureViewportState();
        renderProject(project);
        restoreViewportStateImmediately(viewportBefore);
    }

    private void showLiveConnectorPreview(DiagramProject previewProject) {
        if (previewProject == null) {
            return;
        }
        setStaticConnectorVisualsVisible(false);
        Group dynamicConnectors = switch (previewProject.metadata().activeNotation()) {
            case CHEN -> chenRenderer.renderDynamicConnectors(previewProject);
            case CROWS_FOOT -> crowsFootRenderer.renderDynamicConnectors(previewProject);
        };
        dynamicConnectors.setMouseTransparent(true);
        liveConnectorLayer.getChildren().setAll(dynamicConnectors);
    }

    private void clearLiveConnectorPreview() {
        liveConnectorLayer.getChildren().clear();
        setStaticConnectorVisualsVisible(true);
    }

    private void setStaticConnectorVisualsVisible(boolean visible) {
        setStaticConnectorVisualsVisible(contentLayer, visible);
    }

    private void setStaticConnectorVisualsVisible(Node node, boolean visible) {
        if (node == null) {
            return;
        }
        if (isConnectorVisual(node)) {
            node.setVisible(visible);
        }
        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                setStaticConnectorVisualsVisible(child, visible);
            }
        }
    }

    private boolean isConnectorVisual(Node node) {
        return node.getStyleClass().contains("chen-connector-segment")
                || node.getStyleClass().contains("chen-cardinality")
                || node.getStyleClass().contains("crowsfoot-connector")
                || node.getStyleClass().contains("crowsfoot-marker")
                || node.getStyleClass().contains("crowsfoot-marker-circle")
                || node.getStyleClass().contains("crowsfoot-relationship-label");
    }

    private void restoreViewportStateImmediately(ViewportState state) {
        if (state == null || scrollPane == null) {
            return;
        }
        setZoomFactor(state.zoomFactor());
        scrollPane.setHvalue(clamp01(state.hvalue()));
        scrollPane.setVvalue(clamp01(state.vvalue()));
    }

    private ViewportState captureViewportState() {
        if (scrollPane == null) {
            return new ViewportState(0.0, 0.0, zoomFactor);
        }
        return new ViewportState(scrollPane.getHvalue(), scrollPane.getVvalue(), zoomFactor);
    }

    private void restoreViewportStateLater(ViewportState state) {
        if (state == null || scrollPane == null) {
            return;
        }
        Platform.runLater(() -> {
            setZoomFactor(state.zoomFactor());
            scrollPane.setHvalue(clamp01(state.hvalue()));
            scrollPane.setVvalue(clamp01(state.vvalue()));
        });
    }

    private void zoomAt(double sceneX, double sceneY, double multiplier) {
        if (scrollPane == null) {
            changeZoom(multiplier);
            return;
        }
        double previousZoom = zoomFactor;
        Point2D localPoint = scrollPane.sceneToLocal(sceneX, sceneY);
        double viewportWidth = Math.max(1.0, scrollPane.getViewportBounds().getWidth());
        double viewportHeight = Math.max(1.0, scrollPane.getViewportBounds().getHeight());
        double viewportX = Math.max(0.0, Math.min(viewportWidth, localPoint.getX()));
        double viewportY = Math.max(0.0, Math.min(viewportHeight, localPoint.getY()));

        double oldHMax = Math.max(1.0, WORKSPACE_WIDTH * previousZoom - viewportWidth);
        double oldVMax = Math.max(1.0, WORKSPACE_HEIGHT * previousZoom - viewportHeight);
        double oldOffsetX = scrollPane.getHvalue() * oldHMax;
        double oldOffsetY = scrollPane.getVvalue() * oldVMax;

        double logicalX = (oldOffsetX + viewportX) / previousZoom;
        double logicalY = (oldOffsetY + viewportY) / previousZoom;

        setZoomFactor(previousZoom * multiplier);
        zoomContainer.applyCss();
        zoomContainer.layout();
        applyAnchoredViewport(logicalX, logicalY, viewportX, viewportY);
    }

    private void applyAnchoredViewport(double logicalX, double logicalY, double viewportX, double viewportY) {
        if (scrollPane == null) {
            return;
        }
        double viewportWidth = Math.max(1.0, scrollPane.getViewportBounds().getWidth());
        double viewportHeight = Math.max(1.0, scrollPane.getViewportBounds().getHeight());
        double newHMax = horizontalScrollRange(viewportWidth);
        double newVMax = verticalScrollRange(viewportHeight);
        double newOffsetX = logicalX * zoomFactor - viewportX;
        double newOffsetY = logicalY * zoomFactor - viewportY;
        scrollPane.setHvalue(clamp01(newOffsetX / newHMax));
        scrollPane.setVvalue(clamp01(newOffsetY / newVMax));
    }

    private void startPanning(MouseEvent event) {
        if (panController != null) {
            panController.begin(event);
        }
    }

    private void continuePanning(MouseEvent event) {
        if (panController != null) {
            panController.drag(event);
        }
    }

    private void finishPanning(MouseEvent event) {
        if (panController != null) {
            panController.end(event);
        }
    }

    private boolean isPanning() {
        return panController != null && panController.active();
    }

    private double horizontalScrollRange(double viewportWidth) {
        double contentWidth = zoomContainer == null
                ? WORKSPACE_WIDTH * zoomFactor
                : Math.max(WORKSPACE_WIDTH * zoomFactor, zoomContainer.getLayoutBounds().getWidth());
        return Math.max(1.0, contentWidth - viewportWidth);
    }

    private double verticalScrollRange(double viewportHeight) {
        double contentHeight = zoomContainer == null
                ? WORKSPACE_HEIGHT * zoomFactor
                : Math.max(WORKSPACE_HEIGHT * zoomFactor, zoomContainer.getLayoutBounds().getHeight());
        return Math.max(1.0, contentHeight - viewportHeight);
    }
    private void changeZoom(double multiplier) {
        setZoomAtViewportCenter(zoomFactor * multiplier);
    }

    private void zoomAtViewportCenter(double multiplier) {
        setZoomAtViewportCenter(zoomFactor * multiplier);
    }

    private void setZoomAtViewportCenter(double requestedZoom) {
        if (scrollPane == null) {
            setZoomFactor(requestedZoom);
            return;
        }
        long sequence = ++zoomSequence;
        double previousZoom = zoomFactor;
        double viewportWidth = Math.max(1.0, scrollPane.getViewportBounds().getWidth());
        double viewportHeight = Math.max(1.0, scrollPane.getViewportBounds().getHeight());
        double centerX = viewportWidth / 2.0;
        double centerY = viewportHeight / 2.0;
        double oldHMax = Math.max(1.0, WORKSPACE_WIDTH * previousZoom - viewportWidth);
        double oldVMax = Math.max(1.0, WORKSPACE_HEIGHT * previousZoom - viewportHeight);
        double logicalX = (scrollPane.getHvalue() * oldHMax + centerX) / previousZoom;
        double logicalY = (scrollPane.getVvalue() * oldVMax + centerY) / previousZoom;
        setZoomFactor(requestedZoom);
        scrollPane.applyCss();
        scrollPane.layout();
        applyAnchoredViewport(logicalX, logicalY, centerX, centerY);
        Platform.runLater(() -> {
            if (sequence == zoomSequence) {
                applyAnchoredViewport(logicalX, logicalY, centerX, centerY);
            }
        });
    }

    private void setZoomFactor(double newZoomFactor) {
        zoomFactor = Math.max(0.25, Math.min(3.0, newZoomFactor));
        applyZoom();
    }

    private void applyZoom() {
        zoomGroup.getTransforms().setAll(new Scale(zoomFactor, zoomFactor, 0, 0));
        double scaledWidth = WORKSPACE_WIDTH * zoomFactor;
        double scaledHeight = WORKSPACE_HEIGHT * zoomFactor;
        zoomContainer.setMinSize(scaledWidth, scaledHeight);
        zoomContainer.setPrefSize(scaledWidth, scaledHeight);
        zoomContainer.setMaxSize(scaledWidth, scaledHeight);
        viewModel.updateZoomFactor(zoomFactor);
    }

    private void fitContentToViewport() {
        if (scrollPane == null || contentLayer.getChildren().isEmpty()) {
            setZoomFactor(1.0);
            return;
        }
        Bounds contentBounds = contentBounds();
        double viewportWidth = scrollPane.getViewportBounds().getWidth();
        double viewportHeight = scrollPane.getViewportBounds().getHeight();
        if (contentBounds.getWidth() <= 0 || contentBounds.getHeight() <= 0
                || viewportWidth <= 0 || viewportHeight <= 0) {
            setZoomFactor(1.0);
            return;
        }
        double horizontalScale = (viewportWidth - 80) / contentBounds.getWidth();
        double verticalScale = (viewportHeight - 80) / contentBounds.getHeight();
        setZoomFactor(Math.min(horizontalScale, verticalScale));
        centerBounds(contentBounds);
    }

    private Bounds contentBounds() {
        Bounds bounds = contentLayer.getBoundsInParent();
        if (bounds == null || bounds.getWidth() <= 0 || bounds.getHeight() <= 0) {
            return canvas.getBoundsInLocal();
        }
        return bounds;
    }

    private Bounds union(Bounds a, Bounds b) {
        double minX = Math.min(a.getMinX(), b.getMinX());
        double minY = Math.min(a.getMinY(), b.getMinY());
        double maxX = Math.max(a.getMaxX(), b.getMaxX());
        double maxY = Math.max(a.getMaxY(), b.getMaxY());
        return new javafx.geometry.BoundingBox(minX, minY, maxX - minX, maxY - minY);
    }

    private void centerSelectedElement() {
        Set<DiagramElementId> selectedIds = viewModel.selectedElementIds();
        if (selectedIds.isEmpty() || scrollPane == null) {
            return;
        }
        Bounds selectedBounds = null;
        for (DiagramElementId selected : selectedIds) {
            Node selectedNode = findNodeByElementId(contentLayer, selected);
            if (selectedNode == null) {
                continue;
            }
            Bounds sceneBounds = selectedNode.localToScene(selectedNode.getBoundsInLocal());
            Bounds localBounds = canvas.sceneToLocal(sceneBounds);
            selectedBounds = selectedBounds == null ? localBounds : union(selectedBounds, localBounds);
        }
        if (selectedBounds != null) {
            centerBounds(selectedBounds);
        }
    }

    private Node findNodeByElementId(Node node, DiagramElementId elementId) {
        if (elementId.equals(node.getUserData())) {
            return node;
        }
        if (node instanceof Group group) {
            for (Node child : group.getChildren()) {
                Node found = findNodeByElementId(child, elementId);
                if (found != null) {
                    return found;
                }
            }
        }
        if (node instanceof Pane pane) {
            for (Node child : pane.getChildren()) {
                Node found = findNodeByElementId(child, elementId);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private void centerBounds(Bounds bounds) {
        if (scrollPane == null) {
            return;
        }
        double scaledWidth = Math.max(1, canvas.getBoundsInLocal().getWidth() * zoomFactor);
        double scaledHeight = Math.max(1, canvas.getBoundsInLocal().getHeight() * zoomFactor);
        double viewportWidth = Math.max(1, scrollPane.getViewportBounds().getWidth());
        double viewportHeight = Math.max(1, scrollPane.getViewportBounds().getHeight());
        double targetCenterX = (bounds.getMinX() + bounds.getWidth() / 2.0) * zoomFactor;
        double targetCenterY = (bounds.getMinY() + bounds.getHeight() / 2.0) * zoomFactor;
        double hMax = Math.max(1, scaledWidth - viewportWidth);
        double vMax = Math.max(1, scaledHeight - viewportHeight);
        scrollPane.setHvalue(clamp01((targetCenterX - viewportWidth / 2.0) / hMax));
        scrollPane.setVvalue(clamp01((targetCenterY - viewportHeight / 2.0) / vMax));
    }

    private void requestCenterContent() {
        Platform.runLater(() -> {
            if (scrollPane != null && !contentLayer.getChildren().isEmpty()) {
                centerBounds(contentBounds());
            }
        });
    }

    private double clamp01(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }


    private void installNodeInteractions(Group renderedRoot) {
        installNodeInteractionsRecursively(renderedRoot);
    }

    private void installNodeInteractionsRecursively(Node node) {
        if (node.getUserData() instanceof DiagramElementId elementId) {
            if (node.getStyleClass().contains("chen-draggable-node")) {
                installDraggableNode(node, elementId);
            } else if (node.getStyleClass().contains("chen-endpoint-handle")) {
                Object endpoint = node.getProperties().get("connectorEndpoint");
                if (endpoint instanceof String endpointName) {
                    installEndpointHandle(node, elementId, "source".equals(endpointName));
                }
            } else if (node.getStyleClass().contains("chen-bend-point-handle")) {
                Object index = node.getProperties().get("bendPointIndex");
                if (index instanceof Integer bendPointIndex) {
                    installBendPointHandle(node, elementId, bendPointIndex);
                }
            } else if (node.getStyleClass().contains("crowsfoot-relationship-label")) {
                installRelationshipLabelHandle(node, elementId);
            } else if (node.getStyleClass().contains("chen-connector-segment")) {
                installConnectorSegment(node, elementId);
            }
        }
        if (node instanceof Group group) {
            for (Node child : group.getChildren()) {
                installNodeInteractionsRecursively(child);
            }
        }
    }


    private boolean isInteractiveTarget(Object target) {
        if (!(target instanceof Node node)) {
            return false;
        }
        Node current = node;
        while (current != null && current != canvas) {
            if (current.getUserData() instanceof DiagramElementId) {
                return true;
            }
            if (current.getStyleClass().contains("chen-draggable-node")
                    || current.getStyleClass().contains("chen-connector-segment")
                    || current.getStyleClass().contains("crowsfoot-relationship-label")
                    || current.getStyleClass().contains("chen-bend-point-handle")
                    || current.getStyleClass().contains("chen-endpoint-handle")) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    private void startAreaSelection(MouseEvent event) {
        if (areaSelectionController != null) {
            areaSelectionController.begin(event);
        }
    }

    private void continueAreaSelection(MouseEvent event) {
        if (areaSelectionController != null) {
            areaSelectionController.drag(event);
        }
    }

    private void finishAreaSelection(MouseEvent event) {
        if (areaSelectionController != null) {
            areaSelectionController.end(event);
        }
    }

    private boolean isAreaSelecting() {
        return areaSelectionController != null && areaSelectionController.active();
    }

    private Set<DiagramElementId> selectableNodesInside(Rectangle2D selectionBounds) {
        LinkedHashSet<DiagramElementId> selectedIds = new LinkedHashSet<>();
        collectSelectableNodesInside(contentLayer, selectionBounds, selectedIds);
        return selectedIds;
    }

    private void collectSelectableNodesInside(Node node, Rectangle2D selectionBounds, Set<DiagramElementId> selectedIds) {
        if (isDraggableNode(node) && node.getUserData() instanceof DiagramElementId elementId) {
            Bounds sceneBounds = node.localToScene(node.getBoundsInLocal());
            Bounds contentBounds = contentLayer.sceneToLocal(sceneBounds);
            if (selectionBounds.intersects(
                    contentBounds.getMinX(),
                    contentBounds.getMinY(),
                    contentBounds.getWidth(),
                    contentBounds.getHeight())) {
                selectedIds.add(elementId);
            }
            return;
        }
        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                collectSelectableNodesInside(child, selectionBounds, selectedIds);
            }
        }
    }

    private boolean isDraggableNode(Node node) {
        return node != null && node.getStyleClass().contains("chen-draggable-node");
    }

    private void prepareDraggedNodes(Node pressedNode, DiagramElementId elementId) {
        draggedNodes.clear();
        if (viewModel.selectionCount() > 1 && viewModel.isSelected(elementId)) {
            for (DiagramElementId selectedId : viewModel.selectedElementIds()) {
                Node selectedNode = findDraggableNodeByElementId(contentLayer, selectedId);
                if (selectedNode != null) {
                    draggedNodes.add(selectedNode);
                }
            }
        }
        if (draggedNodes.isEmpty() && viewModel.isSelected(elementId)) {
            draggedNodes.add(pressedNode);
        }
    }

    private Node findDraggableNodeByElementId(Node node, DiagramElementId elementId) {
        if (isDraggableNode(node) && elementId.equals(node.getUserData())) {
            return node;
        }
        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                Node found = findDraggableNodeByElementId(child, elementId);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private void translateDraggedNodes(double deltaX, double deltaY) {
        for (Node draggedNode : draggedNodes) {
            draggedNode.setTranslateX(deltaX);
            draggedNode.setTranslateY(deltaY);
        }
    }

    private void resetDraggedNodesTranslation() {
        for (Node draggedNode : draggedNodes) {
            draggedNode.setTranslateX(0);
            draggedNode.setTranslateY(0);
        }
    }


    private void installRelationshipLabelHandle(Node node, DiagramElementId connectorId) {
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            pointerInteraction = true;
            dragging = false;
            labelDragStartSceneX = event.getSceneX();
            labelDragStartSceneY = event.getSceneY();
            if (event.isShiftDown()) {
                viewModel.toggleElementSelection(connectorId);
            } else {
                viewModel.selectElement(connectorId);
            }
            node.setCursor(Cursor.CLOSED_HAND);
            node.toFront();
            event.consume();
        });
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            double deltaX = event.getSceneX() - labelDragStartSceneX;
            double deltaY = event.getSceneY() - labelDragStartSceneY;
            double logicalDeltaX = deltaX / zoomFactor;
            double logicalDeltaY = deltaY / zoomFactor;
            if (Math.abs(deltaX) > 0.5 || Math.abs(deltaY) > 0.5) {
                dragging = true;
            }
            if (dragging) {
                node.setTranslateX(logicalDeltaX);
                node.setTranslateY(logicalDeltaY);
            }
            event.consume();
        });
        node.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            double deltaX = (event.getSceneX() - labelDragStartSceneX) / zoomFactor;
            double deltaY = (event.getSceneY() - labelDragStartSceneY) / zoomFactor;
            node.setTranslateX(0);
            node.setTranslateY(0);
            node.setCursor(Cursor.OPEN_HAND);
            if (dragging) {
                viewModel.moveConnectorLabelBy(connectorId, deltaX, deltaY);
            } else if (viewModel.currentProject() != null) {
                renderProject(viewModel.currentProject());
            }
            dragging = false;
            pointerInteraction = false;
            event.consume();
        });
        node.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> node.setCursor(Cursor.OPEN_HAND));
        node.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            if (!dragging) {
                node.setCursor(Cursor.DEFAULT);
            }
        });
    }

    private void installConnectorSegment(Node node, DiagramElementId connectorId) {
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            pointerInteraction = true;
            if (event.isShiftDown()) {
                viewModel.toggleElementSelection(connectorId);
            } else {
                viewModel.selectElement(connectorId);
            }
            event.consume();
        });
        node.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                Point2D contentPoint = contentLayer.sceneToLocal(event.getSceneX(), event.getSceneY());
                viewModel.addBendPoint(connectorId, contentPoint.getX(), contentPoint.getY());
            } else if (viewModel.currentProject() != null) {
                renderProject(viewModel.currentProject());
            }
            pointerInteraction = false;
            event.consume();
        });
    }

    private void installEndpointHandle(Node node, DiagramElementId connectorId, boolean sourceEndpoint) {
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            pointerInteraction = true;
            dragging = false;
            endpointDragStartSceneX = event.getSceneX();
            endpointDragStartSceneY = event.getSceneY();
            viewModel.selectElement(connectorId);
            node.toFront();
            event.consume();
        });
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            double deltaX = event.getSceneX() - endpointDragStartSceneX;
            double deltaY = event.getSceneY() - endpointDragStartSceneY;
            double logicalDeltaX = deltaX / zoomFactor;
            double logicalDeltaY = deltaY / zoomFactor;
            if (Math.abs(deltaX) > 0.5 || Math.abs(deltaY) > 0.5) {
                dragging = true;
            }
            node.setTranslateX(logicalDeltaX);
            node.setTranslateY(logicalDeltaY);
            if (dragging) {
                Point2D contentPoint = contentLayer.sceneToLocal(event.getSceneX(), event.getSceneY());
                showLiveConnectorPreview(viewModel.previewMoveConnectorEndpointTo(
                        connectorId,
                        sourceEndpoint,
                        contentPoint.getX(),
                        contentPoint.getY()
                ));
            }
            event.consume();
        });
        node.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            node.setTranslateX(0);
            node.setTranslateY(0);
            if (dragging) {
                Point2D contentPoint = contentLayer.sceneToLocal(event.getSceneX(), event.getSceneY());
                viewModel.moveConnectorEndpointTo(
                        connectorId,
                        sourceEndpoint,
                        contentPoint.getX(),
                        contentPoint.getY()
                );
            } else if (viewModel.currentProject() != null) {
                renderProject(viewModel.currentProject());
            }
            clearLiveConnectorPreview();
            dragging = false;
            pointerInteraction = false;
            event.consume();
        });
    }

    private void installBendPointHandle(Node node, DiagramElementId connectorId, int bendPointIndex) {
        markBendPointSelectionState(node, connectorId, bendPointIndex);
        Tooltip.install(node, new Tooltip("Punto intermedio: arrastra para mover. Usa Suprimir o la toolbar para eliminarlo."));
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            pointerInteraction = true;
            dragging = false;
            bendDragStartSceneX = event.getSceneX();
            bendDragStartSceneY = event.getSceneY();
            viewModel.selectBendPoint(connectorId, bendPointIndex);
            node.getStyleClass().add("chen-selected-bend-point-handle");
            canvas.requestFocus();
            node.toFront();
            event.consume();
        });
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            double deltaX = event.getSceneX() - bendDragStartSceneX;
            double deltaY = event.getSceneY() - bendDragStartSceneY;
            double logicalDeltaX = deltaX / zoomFactor;
            double logicalDeltaY = deltaY / zoomFactor;
            if (Math.abs(deltaX) > 0.5 || Math.abs(deltaY) > 0.5) {
                dragging = true;
            }
            if (dragging) {
                node.setTranslateX(logicalDeltaX);
                node.setTranslateY(logicalDeltaY);
                showLiveConnectorPreview(viewModel.previewMoveBendPointBy(connectorId, bendPointIndex, logicalDeltaX, logicalDeltaY));
            }
            event.consume();
        });
        node.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            double deltaX = (event.getSceneX() - bendDragStartSceneX) / zoomFactor;
            double deltaY = (event.getSceneY() - bendDragStartSceneY) / zoomFactor;
            node.setTranslateX(0);
            node.setTranslateY(0);
            if (dragging) {
                viewModel.moveBendPointBy(connectorId, bendPointIndex, deltaX, deltaY);
            } else if (viewModel.currentProject() != null) {
                renderProject(viewModel.currentProject());
            }
            clearLiveConnectorPreview();
            dragging = false;
            pointerInteraction = false;
            event.consume();
        });
    }

    private void markBendPointSelectionState(Node node, DiagramElementId connectorId, int bendPointIndex) {
        if (viewModel.isSelectedBendPoint(connectorId, bendPointIndex)
                && !node.getStyleClass().contains("chen-selected-bend-point-handle")) {
            node.getStyleClass().add("chen-selected-bend-point-handle");
        }
    }


    private void installDraggableNode(Node node, DiagramElementId elementId) {
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getButton() == MouseButton.PRIMARY && viewModel.handleElementPrimaryPressForActiveTool(elementId)) {
                event.consume();
                return;
            }
            pointerInteraction = true;
            dragging = false;
            dragStartSceneX = event.getSceneX();
            dragStartSceneY = event.getSceneY();
            if (event.isShiftDown()) {
                viewModel.toggleElementSelection(elementId);
            } else if (!viewModel.isSelected(elementId)) {
                viewModel.selectElement(elementId);
            }
            prepareDraggedNodes(node, elementId);
            for (Node draggedNode : draggedNodes) {
                draggedNode.toFront();
            }
            event.consume();
        });

        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            double deltaX = event.getSceneX() - dragStartSceneX;
            double deltaY = event.getSceneY() - dragStartSceneY;
            double logicalDeltaX = deltaX / zoomFactor;
            double logicalDeltaY = deltaY / zoomFactor;
            if (Math.abs(deltaX) > 0.5 || Math.abs(deltaY) > 0.5) {
                dragging = true;
            }
            if (dragging) {
                translateDraggedNodes(logicalDeltaX, logicalDeltaY);
                showLiveConnectorPreview(viewModel.previewMoveSelectedElementBy(logicalDeltaX, logicalDeltaY));
            }
            event.consume();
        });

        node.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            double deltaX = (event.getSceneX() - dragStartSceneX) / zoomFactor;
            double deltaY = (event.getSceneY() - dragStartSceneY) / zoomFactor;
            if (dragging && !draggedNodes.isEmpty()) {
                resetDraggedNodesTranslation();
                viewModel.moveSelectedElementBy(deltaX, deltaY);
            } else if (viewModel.currentProject() != null) {
                renderProject(viewModel.currentProject());
            }
            clearLiveConnectorPreview();
            draggedNodes.clear();
            dragging = false;
            pointerInteraction = false;
            event.consume();
        });
    }


    private void applyWorkspaceAppearance(DiagramProject project) {
        String color = project.styleSheet().appearance().workspaceBackground().toHex();
        workspaceFill.setStyle("-fx-fill: " + color + ";");
    }

    private void applyDefaultWorkspaceAppearance() {
        workspaceFill.setStyle("-fx-fill: #EEF2F6;");
    }


    private record ViewportState(double hvalue, double vvalue, double zoomFactor) { }


}
