package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

/**
 * Configuración estable de una superficie de diagrama.
 *
 * <p>Los valores pertenecen al lienzo transversal, no a un tipo de diagrama
 * concreto. Cualquier familia puede ajustar dimensiones o zoom sin duplicar
 * reglas de viewport.</p>
 */
public record DiagramSurfaceConfig(
        double workspaceWidth,
        double workspaceHeight,
        double contentOriginX,
        double contentOriginY,
        double minZoom,
        double maxZoom,
        double zoomStep,
        double fitPadding,
        boolean showGrid
) {

    public static final double DEFAULT_WORKSPACE_WIDTH = 12000.0;
    public static final double DEFAULT_WORKSPACE_HEIGHT = 8000.0;
    public static final double DEFAULT_CONTENT_ORIGIN_X = 4600.0;
    public static final double DEFAULT_CONTENT_ORIGIN_Y = 3000.0;
    public static final double DEFAULT_MIN_ZOOM = 0.08;
    public static final double DEFAULT_MAX_ZOOM = 3.00;
    public static final double DEFAULT_ZOOM_STEP = 1.08;
    public static final double DEFAULT_FIT_PADDING = 80.0;

    public DiagramSurfaceConfig {
        requirePositive(workspaceWidth, "workspaceWidth");
        requirePositive(workspaceHeight, "workspaceHeight");
        requirePositive(minZoom, "minZoom");
        requirePositive(maxZoom, "maxZoom");
        requirePositive(zoomStep, "zoomStep");
        if (maxZoom <= minZoom) {
            throw new IllegalArgumentException("maxZoom debe ser mayor que minZoom.");
        }
        if (zoomStep <= 1.0) {
            throw new IllegalArgumentException("zoomStep debe ser mayor que 1.0.");
        }
        if (fitPadding < 0.0) {
            throw new IllegalArgumentException("fitPadding no puede ser negativo.");
        }
    }

    public static DiagramSurfaceConfig defaults() {
        return new DiagramSurfaceConfig(
                DEFAULT_WORKSPACE_WIDTH,
                DEFAULT_WORKSPACE_HEIGHT,
                DEFAULT_CONTENT_ORIGIN_X,
                DEFAULT_CONTENT_ORIGIN_Y,
                DEFAULT_MIN_ZOOM,
                DEFAULT_MAX_ZOOM,
                DEFAULT_ZOOM_STEP,
                DEFAULT_FIT_PADDING,
                true
        );
    }

    public DiagramSurfaceConfig withoutGrid() {
        return withGrid(false);
    }

    public DiagramSurfaceConfig withGrid(boolean visible) {
        return new DiagramSurfaceConfig(
                workspaceWidth,
                workspaceHeight,
                contentOriginX,
                contentOriginY,
                minZoom,
                maxZoom,
                zoomStep,
                fitPadding,
                visible
        );
    }

    public double clampZoom(double requestedZoom) {
        return Math.max(minZoom, Math.min(maxZoom, requestedZoom));
    }

    private static void requirePositive(double value, String name) {
        if (!Double.isFinite(value) || value <= 0.0) {
            throw new IllegalArgumentException(name + " debe ser positivo y finito.");
        }
    }
}
