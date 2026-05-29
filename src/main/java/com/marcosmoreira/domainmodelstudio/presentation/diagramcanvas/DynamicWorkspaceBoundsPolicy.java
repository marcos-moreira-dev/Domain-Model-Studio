package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import javafx.geometry.Bounds;

/**
 * Calcula cuándo una superficie debe crecer para contener diagramas grandes.
 *
 * <p>La política no intenta crear un infinito literal: mantiene un tamaño base y lo
 * expande según los bounds reales del contenido más un margen navegable. Así una
 * importación masiva, por ejemplo un UML de cientos o miles de clases, no queda
 * cortada por el tamaño inicial del canvas.</p>
 */
public final class DynamicWorkspaceBoundsPolicy {

    private static final double DEFAULT_MARGIN = 900.0;
    private static final double DEFAULT_STEP = 1000.0;

    private final double margin;
    private final double step;

    public DynamicWorkspaceBoundsPolicy(double margin, double step) {
        if (!Double.isFinite(margin) || margin < 0.0) {
            throw new IllegalArgumentException("El margen dinámico no puede ser negativo.");
        }
        if (!Double.isFinite(step) || step <= 0.0) {
            throw new IllegalArgumentException("El paso de expansión debe ser positivo.");
        }
        this.margin = margin;
        this.step = step;
    }

    public static DynamicWorkspaceBoundsPolicy defaults() {
        return new DynamicWorkspaceBoundsPolicy(DEFAULT_MARGIN, DEFAULT_STEP);
    }

    public DiagramSurfaceWorkspaceSize expandToFit(
            DiagramSurfaceWorkspaceSize current,
            Bounds contentBounds,
            DiagramSurfaceConfig config
    ) {
        DiagramSurfaceWorkspaceSize safeCurrent = current == null
                ? DiagramSurfaceWorkspaceSize.fromConfig(config)
                : current;
        if (!usable(contentBounds)) {
            return safeCurrent;
        }
        double requiredWidth = Math.max(config.workspaceWidth(), contentBounds.getMaxX() + margin);
        double requiredHeight = Math.max(config.workspaceHeight(), contentBounds.getMaxY() + margin);
        return new DiagramSurfaceWorkspaceSize(
                Math.max(safeCurrent.width(), roundUp(requiredWidth)),
                Math.max(safeCurrent.height(), roundUp(requiredHeight))
        );
    }

    private double roundUp(double value) {
        return Math.ceil(value / step) * step;
    }

    private static boolean usable(Bounds bounds) {
        return bounds != null
                && Double.isFinite(bounds.getMinX())
                && Double.isFinite(bounds.getMinY())
                && Double.isFinite(bounds.getMaxX())
                && Double.isFinite(bounds.getMaxY())
                && bounds.getWidth() > 0.0
                && bounds.getHeight() > 0.0;
    }
}
