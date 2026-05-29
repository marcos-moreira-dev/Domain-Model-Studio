package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import java.util.function.DoubleSupplier;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;

/**
 * Controla zoom, tamaño escalado y posición de scroll de una superficie.
 *
 * <p>El paneo y el zoom no usan translate permanente sobre el contenido. El movimiento
 * del viewport queda en los scrollbars, como en el lienzo maduro del modelo conceptual.</p>
 */
public final class DiagramSurfaceViewportController {

    private final ScrollPane scrollPane;
    private final Pane zoomContainer;
    private final Group zoomGroup;
    private final DiagramSurfaceConfig config;
    private final DoubleSupplier workspaceWidthSupplier;
    private final DoubleSupplier workspaceHeightSupplier;
    private double zoomFactor = 1.0;
    private long zoomSequence;

    DiagramSurfaceViewportController(
            ScrollPane scrollPane,
            Pane zoomContainer,
            Group zoomGroup,
            DiagramSurfaceConfig config
    ) {
        this(scrollPane, zoomContainer, zoomGroup, config, config::workspaceWidth, config::workspaceHeight);
    }

    DiagramSurfaceViewportController(
            ScrollPane scrollPane,
            Pane zoomContainer,
            Group zoomGroup,
            DiagramSurfaceConfig config,
            DoubleSupplier workspaceWidthSupplier,
            DoubleSupplier workspaceHeightSupplier
    ) {
        this.scrollPane = scrollPane;
        this.zoomContainer = zoomContainer;
        this.zoomGroup = zoomGroup;
        this.config = config;
        this.workspaceWidthSupplier = workspaceWidthSupplier;
        this.workspaceHeightSupplier = workspaceHeightSupplier;
        applyZoom();
    }

    public double zoomFactor() {
        return zoomFactor;
    }

    public DiagramSurfaceViewport capture() {
        return new DiagramSurfaceViewport(scrollPane.getHvalue(), scrollPane.getVvalue(), zoomFactor);
    }

    public ViewportAnchor captureAnchor() {
        return new ViewportAnchor(
                scrollPane.getHvalue() * horizontalScrollRange(viewportWidth()),
                scrollPane.getVvalue() * verticalScrollRange(viewportHeight()),
                zoomFactor);
    }

    public void restoreAnchor(ViewportAnchor anchor) {
        if (anchor == null) {
            return;
        }
        setZoomFactor(anchor.zoomFactor());
        scrollPane.setHvalue(clamp01(anchor.offsetX() / horizontalScrollRange(viewportWidth())));
        scrollPane.setVvalue(clamp01(anchor.offsetY() / verticalScrollRange(viewportHeight())));
    }

    public void restore(DiagramSurfaceViewport viewport) {
        if (viewport == null) {
            return;
        }
        setZoomFactor(viewport.zoomFactor());
        scrollPane.setHvalue(viewport.hvalue());
        scrollPane.setVvalue(viewport.vvalue());
    }

    public void resetView() {
        setZoomFactor(1.0);
        centerWorkspace();
    }

    public void workspaceSizeChanged() {
        applyZoom();
    }

    public boolean hasUsableViewport(double minimumWidth, double minimumHeight) {
        return viewportWidth() >= minimumWidth && viewportHeight() >= minimumHeight;
    }

    public void setZoomAtViewportCenter(double requestedZoom) {
        double viewportWidth = viewportWidth();
        double viewportHeight = viewportHeight();
        applyAnchoredZoom(requestedZoom, viewportWidth / 2.0, viewportHeight / 2.0);
    }

    public void setZoomAtViewportPoint(double requestedZoom, double viewportX, double viewportY) {
        applyAnchoredZoom(requestedZoom, clamp(viewportX, 0.0, viewportWidth()), clamp(viewportY, 0.0, viewportHeight()));
    }

    public void fitToBounds(Bounds contentBounds) {
        fitToBounds(contentBounds, ViewportFitMode.FIT_TO_CONTENT);
    }

    public void fitToBounds(Bounds contentBounds, ViewportFitMode mode) {
        if (contentBounds == null || contentBounds.getWidth() <= 0.0 || contentBounds.getHeight() <= 0.0) {
            setZoomAtViewportCenter(1.0);
            return;
        }
        ViewportFitMode effectiveMode = mode == null ? ViewportFitMode.FIT_TO_CONTENT : mode;
        double fitZoom = DiagramSurfaceFitController.computeZoom(
                contentBounds.getWidth(),
                contentBounds.getHeight(),
                viewportWidth(),
                viewportHeight(),
                config,
                effectiveMode,
                zoomFactor
        );
        setZoomFactor(fitZoom);
        centerOn(contentBounds);
    }

    public void centerContent(Bounds contentBounds) {
        centerOn(contentBounds);
    }

    public void centerOn(Bounds bounds) {
        if (bounds == null) {
            return;
        }
        double targetCenterX = (bounds.getMinX() + bounds.getWidth() / 2.0) * zoomFactor;
        double targetCenterY = (bounds.getMinY() + bounds.getHeight() / 2.0) * zoomFactor;
        double hRange = horizontalScrollRange(viewportWidth());
        double vRange = verticalScrollRange(viewportHeight());
        scrollPane.setHvalue(clamp01((targetCenterX - viewportWidth() / 2.0) / hRange));
        scrollPane.setVvalue(clamp01((targetCenterY - viewportHeight() / 2.0) / vRange));
    }

    public void centerWorkspace() {
        centerOn(new javafx.geometry.BoundingBox(0, 0, workspaceWidth(), workspaceHeight()));
    }

    private void applyAnchoredZoom(double requestedZoom, double viewportX, double viewportY) {
        long sequence = ++zoomSequence;
        double previousZoom = zoomFactor;
        double oldOffsetX = scrollPane.getHvalue() * horizontalScrollRange(viewportWidth());
        double oldOffsetY = scrollPane.getVvalue() * verticalScrollRange(viewportHeight());
        double logicalX = (oldOffsetX + viewportX) / previousZoom;
        double logicalY = (oldOffsetY + viewportY) / previousZoom;

        setZoomFactor(requestedZoom);
        scrollPane.applyCss();
        scrollPane.layout();
        applyAnchoredViewport(logicalX, logicalY, viewportX, viewportY);
        Platform.runLater(() -> {
            if (sequence == zoomSequence) {
                applyAnchoredViewport(logicalX, logicalY, viewportX, viewportY);
            }
        });
    }

    private void applyAnchoredViewport(double logicalX, double logicalY, double viewportX, double viewportY) {
        double newOffsetX = logicalX * zoomFactor - viewportX;
        double newOffsetY = logicalY * zoomFactor - viewportY;
        scrollPane.setHvalue(clamp01(newOffsetX / horizontalScrollRange(viewportWidth())));
        scrollPane.setVvalue(clamp01(newOffsetY / verticalScrollRange(viewportHeight())));
    }

    private void setZoomFactor(double requestedZoom) {
        zoomFactor = config.clampZoom(requestedZoom);
        applyZoom();
    }

    private void applyZoom() {
        zoomGroup.getTransforms().setAll(new Scale(zoomFactor, zoomFactor, 0, 0));
        double scaledWidth = workspaceWidth() * zoomFactor;
        double scaledHeight = workspaceHeight() * zoomFactor;
        zoomContainer.setMinSize(scaledWidth, scaledHeight);
        zoomContainer.setPrefSize(scaledWidth, scaledHeight);
        zoomContainer.setMaxSize(scaledWidth, scaledHeight);
    }

    private double viewportWidth() {
        return Math.max(1.0, scrollPane.getViewportBounds().getWidth());
    }

    private double viewportHeight() {
        return Math.max(1.0, scrollPane.getViewportBounds().getHeight());
    }

    private double horizontalScrollRange(double viewportWidth) {
        return Math.max(1.0, workspaceWidth() * zoomFactor - viewportWidth);
    }

    private double verticalScrollRange(double viewportHeight) {
        return Math.max(1.0, workspaceHeight() * zoomFactor - viewportHeight);
    }

    private double workspaceWidth() {
        double value = workspaceWidthSupplier.getAsDouble();
        return Double.isFinite(value) && value > 0.0 ? value : config.workspaceWidth();
    }

    private double workspaceHeight() {
        double value = workspaceHeightSupplier.getAsDouble();
        return Double.isFinite(value) && value > 0.0 ? value : config.workspaceHeight();
    }

    private static double clamp01(double value) {
        return clamp(value, 0.0, 1.0);
    }

    private static double clamp(double value, double min, double max) {
        if (!Double.isFinite(value)) {
            return min;
        }
        return Math.max(min, Math.min(max, value));
    }

    public record ViewportAnchor(double offsetX, double offsetY, double zoomFactor) {
        public ViewportAnchor {
            offsetX = Double.isFinite(offsetX) ? Math.max(0.0, offsetX) : 0.0;
            offsetY = Double.isFinite(offsetY) ? Math.max(0.0, offsetY) : 0.0;
            zoomFactor = Double.isFinite(zoomFactor) && zoomFactor > 0.0 ? zoomFactor : 1.0;
        }
    }
}
