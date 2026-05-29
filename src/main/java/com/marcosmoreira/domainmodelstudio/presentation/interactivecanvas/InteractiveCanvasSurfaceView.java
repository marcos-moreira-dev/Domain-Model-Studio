package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.style.ElementStyle;
import com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas.ZoomableDiagramSurface;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.DiagramInteractionProfile;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.DiagramInteractionProfileResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Rectangle;
/**
 * Vista JavaFX transversal que renderiza un modelo interactivo sobre una superficie zoomable.
 *
 * <p>Coordina capas, selección, arrastre, bendpoints y refresco mediante adaptadores, sin conocer
 * la semántica concreta de UML, C4, BPMN, grafo libre o Grafo lógico. Evita que el canvas escriba directamente sobre el dominio y delega fallos hacia {@link CanvasRenderFailurePort}.</p>
 */
public final class InteractiveCanvasSurfaceView {
    private static final InteractiveCanvasRenderBatchPolicy BATCH_POLICY = InteractiveCanvasRenderBatchPolicy.defaults();
    private final InteractiveCanvasAdapter adapter;
    private final InteractiveCanvasRenderKit renderKit;
    private final DiagramDrawingFacade drawingFacade;
    private final DiagramInteractionProfile interactionProfile;
    private final boolean endpointDraggingEnabled;
    private final ZoomableDiagramSurface surface;
    private final CanvasLayeringPolicy layeringPolicy = CanvasLayeringPolicy.standard();
    private final CanvasAreaSelectionController areaSelectionController;
    private final CanvasBendPointController bendPointController;
    private final CanvasResizeHandleLayer resizeHandleLayer;
    private final CanvasNodeVisualRegistry nodeVisualRegistry;
    private final CanvasConnectorVisualRegistry connectorVisualRegistry;
    private final CanvasNodeInteractionCoordinator nodeInteractionCoordinator;
    private final CanvasConnectorLabelOverlayRenderer connectorLabelOverlayRenderer;
    private final CanvasBendPointHandleRenderer bendPointHandleRenderer;
    private final CanvasRenderFailureHandler renderFailureHandler;
    private final Rectangle selectionRectangle = new Rectangle();
    private InteractiveCanvasModel currentModel = new InteractiveCanvasModel(List.of(), List.of(), InteractiveCanvasSelection.empty());
    private long renderSequence;
    public InteractiveCanvasSurfaceView(InteractiveCanvasAdapter adapter) {
        this(adapter, new DefaultInteractiveCanvasRenderKit(), new ZoomableDiagramSurface());
    }
    public InteractiveCanvasSurfaceView(
            InteractiveCanvasAdapter adapter,
            InteractiveCanvasRenderKit renderKit,
            ZoomableDiagramSurface surface
    ) {
        this(
                adapter,
                renderKit,
                surface,
                DiagramInteractionProfileResolver.resolve(Objects.requireNonNull(adapter, "El adaptador no puede ser null").diagramTypeId())
        );
    }
    public InteractiveCanvasSurfaceView(
            InteractiveCanvasAdapter adapter,
            InteractiveCanvasRenderKit renderKit,
            ZoomableDiagramSurface surface,
            DiagramInteractionProfile interactionProfile
    ) {
        this.adapter = Objects.requireNonNull(adapter, "El adaptador no puede ser null");
        this.renderKit = Objects.requireNonNull(renderKit, "El render kit no puede ser null");
        this.surface = Objects.requireNonNull(surface, "La superficie no puede ser null");
        this.interactionProfile = Objects.requireNonNull(interactionProfile, "El perfil de interacción no puede ser null");
        this.endpointDraggingEnabled = CanvasEndpointInteractionPolicy.isEndpointDraggingEnabled(this.interactionProfile, this.adapter);
        this.drawingFacade = DiagramDrawingFacade.defaults();
        this.renderFailureHandler = new CanvasRenderFailureHandler(adapter, surface, () -> renderSequence++);
        CanvasNodeDragController nodeDragController = new CanvasNodeDragController(adapter);
        this.areaSelectionController = new CanvasAreaSelectionController(adapter);
        this.bendPointController = new CanvasBendPointController(adapter);
        CanvasConnectorLabelController connectorLabelController = adapter instanceof CanvasConnectorLabelPort labelPort
                ? new CanvasConnectorLabelController(labelPort, adapter)
                : null;
        CanvasResizePort resizePort = adapter instanceof CanvasResizePort port ? port : null;
        this.resizeHandleLayer = new CanvasResizeHandleLayer(
                resizePort,
                interactionProfile,
                this::canvasPoint,
                this::refreshPreservingViewport,
                surface.layers().livePreviewLayer()
        );
        this.nodeVisualRegistry = new CanvasNodeVisualRegistry(adapter);
        this.connectorVisualRegistry = new CanvasConnectorVisualRegistry();
        CanvasConnectorDragPreviewLayer connectorPreviewLayer = new CanvasConnectorDragPreviewLayer(
                adapter,
                surface.layers().livePreviewLayer(),
                drawingFacade,
                connectorVisualRegistry
        );
        this.nodeInteractionCoordinator = new CanvasNodeInteractionCoordinator(
                adapter,
                interactionProfile,
                surface,
                nodeDragController,
                nodeVisualRegistry,
                connectorPreviewLayer,
                connectorVisualRegistry,
                drawingFacade,
                this::canvasPoint,
                this::refreshPreservingViewport
        );
        this.connectorLabelOverlayRenderer = new CanvasConnectorLabelOverlayRenderer(
                adapter,
                interactionProfile,
                drawingFacade,
                connectorLabelController,
                this::canvasPoint,
                this::refreshPreservingViewport,
                surface.root()::requestFocus
        );
        this.bendPointHandleRenderer = new CanvasBendPointHandleRenderer(
                adapter,
                interactionProfile,
                surface,
                bendPointController,
                this::canvasPoint,
                this::refreshPreservingViewport,
                connectorVisualRegistry,
                drawingFacade
        );
        configureOverlay();
        installKeyboardShortcuts();
        installConnectorEditingGestures();
        installBackgroundSelection();
        refresh();
    }
    public Parent root() { return surface.root(); }
    public ZoomableDiagramSurface surface() { return surface; }
    public DiagramInteractionProfile interactionProfile() { return interactionProfile; }
    public boolean endpointDraggingEnabled() { return endpointDraggingEnabled; }
    public Bounds semanticContentBounds() {
        CanvasBounds bounds = adapter.contentBounds();
        return new BoundingBox(
                surface.config().contentOriginX() + bounds.x(),
                surface.config().contentOriginY() + bounds.y(),
                Math.max(1.0, bounds.width()),
                Math.max(1.0, bounds.height())
        );
    }
    public void clear() {
        renderSequence++;
        selectionRectangle.setVisible(false);
        surface.clearContent();
        surface.resetView();
    }
    public void refresh() { refreshInternal(false); }
    public void refreshPreservingViewport() { refreshInternal(true); }
    public boolean deleteSelectedBendPoint() {
        if (!interactionProfile.supportsBendPoints()
                || !adapter.supportsBendPoints()
                || adapter.selection().selectedBendPoint().isEmpty()) {
            return false;
        }
        bendPointController.removeSelected();
        refreshPreservingViewport();
        surface.root().requestFocus();
        return true;
    }
    private void refreshInternal(boolean preserveViewport) {
        long sequence = ++renderSequence;
        InteractiveCanvasModel model;
        List<InteractiveCanvasConnector> connectors = List.of();
        List<InteractiveCanvasNode> nodes = List.of();
        try {
            model = InteractiveCanvasModel.from(adapter);
            currentModel = model;
            connectors = model.visibleConnectors();
            nodes = layeringPolicy.orderNodes(model.visibleNodes(), model);
            surface.ensureWorkspaceContains(semanticContentBounds());
            if (BATCH_POLICY.requiresBatchedRender(connectors.size(), nodes.size())) {
                renderInBatches(sequence, model, connectors, nodes);
                return;
            }
            if (preserveViewport) {
                final InteractiveCanvasModel finalModel = model;
                final List<InteractiveCanvasConnector> finalConnectors = connectors;
                final List<InteractiveCanvasNode> finalNodes = nodes;
                surface.preservingViewport(() -> renderSynchronously(finalModel, finalConnectors, finalNodes));
                return;
            }
            renderSynchronously(model, connectors, nodes);
        } catch (OutOfMemoryError error) {
            handleRenderFailure("preparar/renderizar", connectors.size(), nodes.size(), error);
        } catch (RuntimeException exception) {
            handleRenderFailure("preparar/renderizar", connectors.size(), nodes.size(), exception);
        }
    }
    private void renderSynchronously(
            InteractiveCanvasModel model,
            List<InteractiveCanvasConnector> connectors,
            List<InteractiveCanvasNode> nodes
    ) {
        nodeVisualRegistry.clear();
        connectorVisualRegistry.clear();
        configureConnectorLayerHitTesting();
        List<Node> connectorNodes = new ArrayList<>();
        List<Node> overlays = new ArrayList<>();
        overlays.add(selectionRectangle);
        for (InteractiveCanvasConnector connector : connectors) {
            addRenderedConnector(connector, model, connectorNodes, overlays);
        }
        CanvasRenderedNodeLayers renderedNodes = CanvasRenderedNodeLayers.from(
                nodes, model, layeringPolicy, (node, layout) -> renderNode(node, layout, model));
        overlays.addAll(renderResizeHandles(model));
        surface.setLayeredContent(renderedNodes.backgroundNodes(), connectorNodes, renderedNodes.foregroundNodes(), overlays);
    }
    private void renderInBatches(
            long sequence,
            InteractiveCanvasModel model,
            List<InteractiveCanvasConnector> connectors,
            List<InteractiveCanvasNode> nodes
    ) {
        try {
            nodeVisualRegistry.clear();
            connectorVisualRegistry.clear();
            configureConnectorLayerHitTesting();
            surface.layers().backgroundNodeLayer().getChildren().clear();
            surface.layers().connectorLayer().getChildren().clear();
            surface.layers().nodeLayer().getChildren().clear();
            surface.layers().overlayLayer().getChildren().setAll(selectionRectangle);
            surface.layers().livePreviewLayer().getChildren().clear();
            Platform.runLater(() -> safeRenderConnectorBatch(sequence, model, connectors, nodes, 0));
        } catch (OutOfMemoryError error) {
            handleRenderFailure("iniciar render por lotes", connectors.size(), nodes.size(), error);
        } catch (RuntimeException exception) {
            handleRenderFailure("iniciar render por lotes", connectors.size(), nodes.size(), exception);
        }
    }
    private void safeRenderConnectorBatch(
            long sequence,
            InteractiveCanvasModel model,
            List<InteractiveCanvasConnector> connectors,
            List<InteractiveCanvasNode> nodes,
            int start
    ) {
        try {
            renderConnectorBatch(sequence, model, connectors, nodes, start);
        } catch (OutOfMemoryError error) {
            handleRenderFailure("renderizar conectores", connectors.size(), nodes.size(), error);
        } catch (RuntimeException exception) {
            handleRenderFailure("renderizar conectores", connectors.size(), nodes.size(), exception);
        }
    }
    private void safeRenderNodeBatch(
            long sequence,
            InteractiveCanvasModel model,
            List<InteractiveCanvasNode> nodes,
            int start
    ) {
        try {
            renderNodeBatch(sequence, model, nodes, start);
        } catch (OutOfMemoryError error) {
            handleRenderFailure("renderizar nodos", 0, nodes.size(), error);
        } catch (RuntimeException exception) {
            handleRenderFailure("renderizar nodos", 0, nodes.size(), exception);
        }
    }
    private void renderConnectorBatch(
            long sequence,
            InteractiveCanvasModel model,
            List<InteractiveCanvasConnector> connectors,
            List<InteractiveCanvasNode> nodes,
            int start
    ) {
        if (sequence != renderSequence) {
            return;
        }
        int end = Math.min(connectors.size(), start + BATCH_POLICY.connectorBatchSize());
        List<Node> connectorNodes = new ArrayList<>();
        List<Node> overlays = new ArrayList<>();
        for (int index = start; index < end; index++) {
            addRenderedConnector(connectors.get(index), model, connectorNodes, overlays);
        }
        surface.layers().connectorLayer().getChildren().addAll(connectorNodes);
        surface.layers().overlayLayer().getChildren().addAll(overlays);
        if (end < connectors.size()) {
            Platform.runLater(() -> safeRenderConnectorBatch(sequence, model, connectors, nodes, end));
            return;
        }
        Platform.runLater(() -> safeRenderNodeBatch(sequence, model, nodes, 0));
    }
    private void renderNodeBatch(
            long sequence,
            InteractiveCanvasModel model,
            List<InteractiveCanvasNode> nodes,
            int start
    ) {
        if (sequence != renderSequence) {
            return;
        }
        int end = Math.min(nodes.size(), start + BATCH_POLICY.nodeBatchSize());
        CanvasRenderedNodeLayers renderedNodes = CanvasRenderedNodeLayers.from(
                nodes.subList(start, end), model, layeringPolicy, (node, layout) -> renderNode(node, layout, model));
        surface.layers().backgroundNodeLayer().getChildren().addAll(renderedNodes.backgroundNodes());
        surface.layers().nodeLayer().getChildren().addAll(renderedNodes.foregroundNodes());
        if (end < nodes.size()) {
            Platform.runLater(() -> safeRenderNodeBatch(sequence, model, nodes, end));
            return;
        }
        finishBatchedRender(sequence, model);
    }
    private void finishBatchedRender(long sequence, InteractiveCanvasModel model) {
        if (sequence != renderSequence) {
            return;
        }
        surface.layers().overlayLayer().getChildren().addAll(renderResizeHandles(model));
        surface.ensureWorkspaceContains(semanticContentBounds());
    }
    private void handleRenderFailure(String phase, int connectorCount, int nodeCount, Throwable throwable) {
        renderFailureHandler.handle(phase, connectorCount, nodeCount, throwable);
    }
    private void addRenderedConnector(
            InteractiveCanvasConnector connector,
            InteractiveCanvasModel model,
            List<Node> connectorNodes,
            List<Node> overlays
    ) {
        connectorNodes.add(renderConnector(connector, model));
        Node label = renderConnectorLabel(connector, model);
        if (label != null) {
            overlays.add(label);
        }
        overlays.addAll(renderBendPointHandles(connector, model));
    }
    private Node renderConnector(InteractiveCanvasConnector connector, InteractiveCanvasModel model) {
        Node rendered = renderKit.renderConnector(
                connector,
                adapter,
                model.selection().isConnectorSelected(connector.id()),
                drawingFacade
        );
        explicitStyleFor(connector.id()).ifPresent(style -> CanvasStyleApplier.applyConnectorStyle(rendered, style));
        if (model.selection().isConnectorSelected(connector.id())) {
            CanvasConnectorSelectionVisualSupport.apply(rendered);
        }
        connectorVisualRegistry.register(connector, rendered);
        if (!connectorLineHitTestingEnabled()) {
            rendered.setMouseTransparent(true);
            return rendered;
        }
        rendered.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY
                    && event.getClickCount() >= 2
                    && interactionProfile.supportsBendPoints()
                    && adapter.supportsBendPoints()) {
                Point2D canvasPoint = canvasPoint(event.getSceneX(), event.getSceneY());
                bendPointController.add(connector.id(), canvasPoint.getX(), canvasPoint.getY());
                refreshPreservingViewport();
                event.consume();
                return;
            }
            if (event.getButton() == MouseButton.PRIMARY && interactionProfile.supportsConnectorSelection()) {
                adapter.selectConnector(connector.id(), event.isShiftDown());
                refreshPreservingViewport();
                event.consume();
            }
        });
        return rendered;
    }
    private boolean connectorLineHitTestingEnabled() {
        return adapter instanceof CanvasConnectorHitTestPort hitTestPort
                && hitTestPort.connectorLineHitTestingEnabled();
    }
    private void configureConnectorLayerHitTesting() { surface.layers().connectorLayer().setMouseTransparent(!connectorLineHitTestingEnabled()); }
    private Node renderConnectorLabel(InteractiveCanvasConnector connector, InteractiveCanvasModel model) { return connectorLabelOverlayRenderer.renderLabel(connector, model); }
    private List<Node> renderBendPointHandles(InteractiveCanvasConnector connector, InteractiveCanvasModel model) { return bendPointHandleRenderer.renderBendPointHandles(connector, model); }
    private List<Node> renderResizeHandles(InteractiveCanvasModel model) { return resizeHandleLayer.renderHandles(model); }
    private Node renderNode(InteractiveCanvasNode node, NodeLayout layout, InteractiveCanvasModel model) {
        Node rendered = renderKit.renderNode(
                node,
                CanvasBounds.from(layout),
                CanvasSelectionProjection.isNodeSelectedOrEndpointOfSelectedConnector(model, node.id()),
                drawingFacade
        );
        nodeVisualRegistry.register(node.id(), rendered);
        explicitStyleFor(node.id()).ifPresent(style -> CanvasStyleApplier.applyNodeStyle(rendered, style));
        nodeInteractionCoordinator.install(node, rendered);
        return rendered;
    }

    private java.util.Optional<ElementStyle> explicitStyleFor(String elementId) {
        return adapter instanceof CanvasStylePort stylePort
                ? stylePort.explicitStyleForElement(elementId)
                : java.util.Optional.empty();
    }
    private void installKeyboardShortcuts() {
        Parent root = surface.root();
        root.setFocusTraversable(true);
        CanvasClipboardKeyboardInstaller.install(root, adapter, this::refreshPreservingViewport);
        root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if ((event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE)
                    && interactionProfile.supportsBendPoints()
                    && adapter.supportsBendPoints()
                    && adapter.selection().selectedBendPoint().isPresent()) {
                bendPointController.removeSelected();
                refreshPreservingViewport();
                event.consume();
            }
        });
    }
    private void installConnectorEditingGestures() {
        CanvasConnectorGestureInstaller.install(
                surface,
                adapter,
                interactionProfile,
                this::canvasPoint,
                () -> currentModel,
                drawingFacade,
                this::refreshPreservingViewport
        );
    }
    private void installBackgroundSelection() {
        CanvasBackgroundGestureInstaller.install(
                surface,
                adapter,
                interactionProfile,
                areaSelectionController,
                selectionRectangle,
                this::canvasPoint,
                this::refreshPreservingViewport
        );
    }
    private Point2D canvasPoint(double sceneX, double sceneY) {
        Point2D workspacePoint = surface.coordinates().sceneToWorkspace(sceneX, sceneY);
        return new Point2D(
                workspacePoint.getX() - surface.config().contentOriginX(),
                workspacePoint.getY() - surface.config().contentOriginY()
        );
    }
    private void configureOverlay() {
        selectionRectangle.getStyleClass().addAll(
                "interactive-canvas-selection-rectangle",
                "diagram-surface-selection-rectangle"
        );
        selectionRectangle.setManaged(false);
        selectionRectangle.setVisible(false);
        selectionRectangle.setMouseTransparent(true);
    }
}
