package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

/**
 * Tamaño navegable real de una superficie visual.
 *
 * <p>El tamaño inicial puede venir de {@link DiagramSurfaceConfig}, pero los diagramas
 * grandes pueden requerir un workspace mayor para que el usuario pueda panear y
 * hacer zoom sobre todo el contenido renderizado.</p>
 */
public record DiagramSurfaceWorkspaceSize(double width, double height) {

    public DiagramSurfaceWorkspaceSize {
        requirePositive(width, "width");
        requirePositive(height, "height");
    }

    public static DiagramSurfaceWorkspaceSize fromConfig(DiagramSurfaceConfig config) {
        return new DiagramSurfaceWorkspaceSize(config.workspaceWidth(), config.workspaceHeight());
    }

    public boolean sameAs(DiagramSurfaceWorkspaceSize other) {
        return other != null
                && Math.abs(width - other.width) < 0.0001
                && Math.abs(height - other.height) < 0.0001;
    }

    private static void requirePositive(double value, String name) {
        if (!Double.isFinite(value) || value <= 0.0) {
            throw new IllegalArgumentException("La dimensión " + name + " del workspace debe ser positiva y finita.");
        }
    }
}
