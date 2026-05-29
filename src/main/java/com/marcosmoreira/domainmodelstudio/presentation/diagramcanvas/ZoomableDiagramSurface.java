package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import java.util.Collection;
import javafx.application.Platform;
import java.util.List;
import java.util.Objects;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * Superficie canónica para diagramas visuales con workspace grande, capas, grilla, zoom anclado
 * y paneo por scrollbars.
 *
 * <p>Esta clase no conoce módulos, UML, BPMN, C4 ni modelo conceptual. Su responsabilidad es
 * ofrecer una base visual coherente para que cada familia monte sus nodos mediante adaptadores
 * y render kits propios.</p>
 *
 * <p>Al estudiar esta clase, separar tres conceptos: el workspace físico que puede crecer,
 * el viewport visible que se ajusta con zoom/paneo y las capas donde se ubican fondo,
 * conectores, nodos y overlays. Esa separación permite compartir comportamiento visual sin
 * copiar el canvas conceptual.</p>
 */
public final class ZoomableDiagramSurface implements DiagramSurfaceCommandPort {

    private static final int FIT_READY_MAX_ATTEMPTS = 18;
    private static final double MIN_READY_VIEWPORT_WIDTH = 120.0;
    private static final double MIN_READY_VIEWPORT_HEIGHT = 120.0;

    private final DiagramSurfaceConfig config;
    private final DynamicWorkspaceBoundsPolicy workspaceBoundsPolicy;
    private final ScrollPane scrollPane;
    private final Pane workspaceRoot;
    private final Group zoomGroup;
    private final Pane zoomContainer;
    private final Rectangle workspaceFill;
    private final DiagramSurfaceLayers layers;
    private final DiagramSurfaceViewportController viewportController;
    private final DiagramSurfaceZoomController zoomController;
    private final DiagramSurfacePanController panController;
    private final DiagramSurfaceCoordinateMapper coordinateMapper;
    private double workspaceWidth;
    private double workspaceHeight;
    private long fitSequence;
    private boolean viewportAdjustedByUser;

    public ZoomableDiagramSurface() {
        this(DiagramSurfaceConfig.defaults());
    }

    public ZoomableDiagramSurface(DiagramSurfaceConfig config) {
        this.config = Objects.requireNonNull(config, "config");
        this.workspaceBoundsPolicy = DynamicWorkspaceBoundsPolicy.defaults();
        this.workspaceWidth = config.workspaceWidth();
        this.workspaceHeight = config.workspaceHeight();
        this.workspaceRoot = new Pane();
        this.zoomGroup = new Group(workspaceRoot);
        this.zoomContainer = new Pane(zoomGroup);
        this.scrollPane = new ScrollPane(zoomContainer);
        this.workspaceFill = new Rectangle(0, 0, workspaceWidth, workspaceHeight);
        this.layers = createLayers();
        this.viewportController = new DiagramSurfaceViewportController(
                scrollPane,
                zoomContainer,
                zoomGroup,
                config,
                this::workspaceWidth,
                this::workspaceHeight
        );
        this.zoomController = new DiagramSurfaceZoomController(scrollPane, viewportController, config, this::markViewportAdjustedByUser);
        this.panController = new DiagramSurfacePanController(scrollPane, workspaceRoot, this::markViewportAdjustedByUser);
        this.coordinateMapper = new DiagramSurfaceCoordinateMapper(scrollPane, workspaceRoot);
        configureStructure();
        installGestures();
        refreshBackground();
    }

    public Parent root() {
        return scrollPane;
    }

    /**
     * Raíz interna del workspace donde viven fondo, capas y gestos de lienzo.
     *
     * <p>Los gestos de selección por área y clic en fondo deben instalarse aquí,
     * no en el ScrollPane completo, para comportarse igual que el canvas conceptual:
     * solo el área real del diagrama participa en selección y deselección.</p>
     */
    public Parent workspaceRoot() {
        return workspaceRoot;
    }

    public DiagramSurfaceConfig config() {
        return config;
    }

    public double workspaceWidth() {
        return workspaceWidth;
    }

    public double workspaceHeight() {
        return workspaceHeight;
    }

    public DiagramSurfaceWorkspaceSize workspaceSize() {
        return new DiagramSurfaceWorkspaceSize(workspaceWidth, workspaceHeight);
    }

    public DiagramSurfaceLayers layers() {
        return layers;
    }

    public DiagramSurfaceCoordinateMapper coordinates() {
        return coordinateMapper;
    }

    public DiagramSurfaceViewport viewport() {
        return viewportController.capture();
    }

    public double zoomFactor() {
        return viewportController.zoomFactor();
    }

    public void restoreViewport(DiagramSurfaceViewport viewport) {
        viewportController.restore(viewport);
    }

    public boolean panning() {
        return panController.active();
    }


    /**
     * Ejecuta un refresco visual preservando paneo y zoom actuales.
     *
     * <p>Actualizar nodos del lienzo no debe comportarse como una orden de
     * navegación. Esto mantiene Ctrl+Z/Ctrl+Y, selección y cambios de estilo
     * enfocados en la geometría del diagrama, sin recentrar ni despannear la
     * vista.</p>
     */
    public void preservingViewport(Runnable refreshAction) {
        if (refreshAction == null) {
            return;
        }
        DiagramSurfaceViewportController.ViewportAnchor anchor = viewportController.captureAnchor();
        fitSequence++;
        refreshAction.run();
        restoreViewportAfterLayout(anchor, 0);
    }

    private void restoreViewportAfterLayout(DiagramSurfaceViewportController.ViewportAnchor anchor, int attempt) {
        applyLayoutPass();
        viewportController.restoreAnchor(anchor);
        if (attempt >= 6) {
            return;
        }
        Platform.runLater(() -> restoreViewportAfterLayout(anchor, attempt + 1));
    }

    public boolean viewportAdjustedByUser() {
        return viewportAdjustedByUser;
    }

    public void clearViewportAdjustedByUser() {
        viewportAdjustedByUser = false;
    }

    public void setContent(
            Collection<? extends Node> connectors,
            Collection<? extends Node> nodes,
            Collection<? extends Node> overlays
    ) {
        setLayeredContent(List.of(), connectors, nodes, overlays);
    }

    public void setLayeredContent(
            Collection<? extends Node> backgroundNodes,
            Collection<? extends Node> connectors,
            Collection<? extends Node> nodes,
            Collection<? extends Node> overlays
    ) {
        layers.setDiagramContent(backgroundNodes, connectors, nodes, overlays);
        ensureWorkspaceContains(coordinateMapper.contentBoundsOf(List.of(
                layers.backgroundNodeLayer(),
                layers.connectorLayer(),
                layers.nodeLayer(),
                layers.overlayLayer()
        )));
    }

    public void setContent(Collection<? extends Node> nodes) {
        setContent(List.of(), nodes, List.of());
    }

    public void clearContent() {
        layers.clearDiagramContent();
        applyWorkspaceSize(DiagramSurfaceWorkspaceSize.fromConfig(config));
    }

    public void refreshBackground() {
        DiagramSurfaceBackground.rebuild(layers.backgroundLayer(), workspaceFill, config, workspaceWidth, workspaceHeight);
    }

    public void ensureWorkspaceContains(Bounds contentBounds) {
        applyWorkspaceSize(workspaceBoundsPolicy.expandToFit(workspaceSize(), contentBounds, config));
    }

    @Override
    public void zoomIn() {
        zoomController.zoomIn();
    }

    @Override
    public void zoomOut() {
        zoomController.zoomOut();
    }

    @Override
    public void resetZoom() {
        zoomController.resetZoom();
    }

    @Override
    public void fitToContent() {
        Bounds bounds = coordinateMapper.contentBoundsOf(List.of(
                layers.backgroundNodeLayer(),
                layers.connectorLayer(),
                layers.nodeLayer(),
                layers.overlayLayer()
        ));
        fitToContent(bounds);
    }

    @Override
    public void fitToContent(Bounds bounds) {
        fitToContent(bounds, ViewportFitMode.FIT_TO_CONTENT);
    }

    public void fitToContent(Bounds bounds, ViewportFitMode mode) {
        ensureWorkspaceContains(bounds);
        viewportAdjustedByUser = false;
        viewportController.fitToBounds(bounds, mode);
    }

    @Override
    public void centerContent() {
        Bounds bounds = coordinateMapper.contentBoundsOf(List.of(
                layers.backgroundNodeLayer(),
                layers.connectorLayer(),
                layers.nodeLayer(),
                layers.overlayLayer()
        ));
        ensureWorkspaceContains(bounds);
        centerOn(bounds);
    }

    public void fitWidthToContent() {
        fitToContent(renderedContentBounds(), ViewportFitMode.FIT_WIDTH);
    }

    /**
     * Ajusta contenido cuando el viewport y los nodos renderizados ya tienen medidas reales.
     *
     * <p>La superficie puede montarse antes de que el contenedor central tenga tamaño útil.
     * Por eso el ajuste no se ejecuta una sola vez: reintenta durante algunos pulsos de
     * JavaFX y prefiere los bounds reales renderizados. Si aún no existen, usa los bounds
     * semánticos recibidos como respaldo.</p>
     */
    public void fitToContentWhenReady(Bounds fallbackBounds) {
        fitToContentWhenReady(ViewportFitMode.FIT_TO_CONTENT, fallbackBounds);
    }

    public void fitToContentWhenReady() {
        fitToContentWhenReady(ViewportFitMode.FIT_TO_CONTENT, null);
    }

    public void fitToContentWhenReady(ViewportFitMode mode, Bounds fallbackBounds) {
        long sequence = ++fitSequence;
        viewportAdjustedByUser = false;
        scheduleFitToContentWhenReady(mode == null ? ViewportFitMode.FIT_TO_CONTENT : mode, fallbackBounds, sequence, 0);
    }

    public void fitToContentWhenReadyIfAutomatic(Bounds fallbackBounds) {
        if (!viewportAdjustedByUser) {
            fitToContentWhenReady(fallbackBounds);
        }
    }

    public void resetView() {
        fitSequence++;
        viewportAdjustedByUser = false;
        viewportController.resetView();
    }

    public Bounds renderedContentBounds() {
        applyLayoutPass();
        Bounds bounds = coordinateMapper.contentBoundsOf(List.of(
                layers.backgroundNodeLayer(),
                layers.connectorLayer(),
                layers.nodeLayer(),
                layers.overlayLayer()
        ));
        ensureWorkspaceContains(bounds);
        return bounds;
    }

    private void scheduleFitToContentWhenReady(ViewportFitMode mode, Bounds fallbackBounds, long sequence, int attempt) {
        Platform.runLater(() -> {
            if (sequence != fitSequence) {
                return;
            }
            applyLayoutPass();
            Bounds renderedBounds = renderedContentBounds();
            Bounds targetBounds = usableBounds(renderedBounds) ? renderedBounds : fallbackBounds;
            boolean ready = viewportController.hasUsableViewport(MIN_READY_VIEWPORT_WIDTH, MIN_READY_VIEWPORT_HEIGHT)
                    && usableBounds(targetBounds);

            if (!ready && attempt < FIT_READY_MAX_ATTEMPTS) {
                scheduleFitToContentWhenReady(mode, fallbackBounds, sequence, attempt + 1);
                return;
            }
            if (sequence != fitSequence) {
                return;
            }
            if (usableBounds(targetBounds)) {
                fitToContent(targetBounds, mode);
                return;
            }
            viewportController.centerWorkspace();
        });
    }


    private void applyWorkspaceSize(DiagramSurfaceWorkspaceSize nextSize) {
        if (nextSize == null || workspaceSize().sameAs(nextSize)) {
            return;
        }
        workspaceWidth = nextSize.width();
        workspaceHeight = nextSize.height();
        workspaceRoot.setMinSize(workspaceWidth, workspaceHeight);
        workspaceRoot.setPrefSize(workspaceWidth, workspaceHeight);
        workspaceRoot.setMaxSize(workspaceWidth, workspaceHeight);
        viewportController.workspaceSizeChanged();
        refreshBackground();
    }

    private void markViewportAdjustedByUser() {
        fitSequence++;
        viewportAdjustedByUser = true;
    }

    private void applyLayoutPass() {
        scrollPane.applyCss();
        scrollPane.layout();
        zoomContainer.applyCss();
        zoomContainer.layout();
        workspaceRoot.applyCss();
        workspaceRoot.layout();
    }

    private static boolean usableBounds(Bounds bounds) {
        return bounds != null
                && Double.isFinite(bounds.getMinX())
                && Double.isFinite(bounds.getMinY())
                && Double.isFinite(bounds.getWidth())
                && Double.isFinite(bounds.getHeight())
                && bounds.getWidth() > 0.0
                && bounds.getHeight() > 0.0;
    }

    @Override
    public void centerOn(Bounds bounds) {
        viewportController.centerOn(bounds);
    }

    public DiagramSurfaceExportNode exportNode() {
        return new DiagramSurfaceExportNode(workspaceRoot, workspaceWidth, workspaceHeight);
    }

    private DiagramSurfaceLayers createLayers() {
        Group backgroundLayer = new Group();
        Group backgroundNodeLayer = new Group();
        Group connectorLayer = new Group();
        Group nodeLayer = new Group();
        Group overlayLayer = new Group();
        Group livePreviewLayer = new Group();
        return new DiagramSurfaceLayers(backgroundLayer, backgroundNodeLayer, connectorLayer, nodeLayer, overlayLayer, livePreviewLayer);
    }

    private void configureStructure() {
        scrollPane.getStyleClass().addAll("diagram-surface-scroll-pane", "canvas-scroll-pane");
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        scrollPane.setPannable(false);

        zoomContainer.getStyleClass().add("diagram-surface-zoom-container");
        zoomGroup.getStyleClass().add("diagram-surface-zoom-group");
        workspaceRoot.getStyleClass().addAll("diagram-surface-workspace", "diagram-infinite-workspace");
        workspaceRoot.setMinSize(workspaceWidth, workspaceHeight);
        workspaceRoot.setPrefSize(workspaceWidth, workspaceHeight);
        workspaceRoot.setMaxSize(workspaceWidth, workspaceHeight);
        workspaceRoot.setFocusTraversable(true);
        workspaceRoot.setPickOnBounds(true);

        configureLayer(layers.backgroundLayer(), "diagram-surface-background-layer", true, 0, 0);
        configureLayer(layers.backgroundNodeLayer(), "diagram-surface-background-node-layer", false, config.contentOriginX(), config.contentOriginY());
        configureLayer(layers.connectorLayer(), "diagram-surface-connector-layer", false, config.contentOriginX(), config.contentOriginY());
        configureLayer(layers.nodeLayer(), "diagram-surface-node-layer", false, config.contentOriginX(), config.contentOriginY());
        configureLayer(layers.overlayLayer(), "diagram-surface-overlay-layer", false, config.contentOriginX(), config.contentOriginY());
        configureLayer(layers.livePreviewLayer(), "diagram-surface-live-preview-layer", true, config.contentOriginX(), config.contentOriginY());
        workspaceRoot.getChildren().setAll(
                layers.backgroundLayer(),
                layers.backgroundNodeLayer(),
                // Tanda 60: los conectores quedan entre zonas/fondos y nodos operables;
                // labels, bendpoints, handles y selección siguen en overlay.
                layers.connectorLayer(),
                layers.nodeLayer(),
                layers.overlayLayer(),
                layers.livePreviewLayer()
        );
    }

    private void configureLayer(Group layer, String styleClass, boolean mouseTransparent, double layoutX, double layoutY) {
        layer.getStyleClass().add(styleClass);
        layer.setMouseTransparent(mouseTransparent);
        layer.setLayoutX(layoutX);
        layer.setLayoutY(layoutY);
    }

    private void installGestures() {
        zoomController.install();
        panController.install();
    }
}
