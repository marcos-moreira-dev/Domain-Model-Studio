package com.marcosmoreira.domainmodelstudio.domain.style;

import java.util.Objects;

/**
 * Apariencia general del área de trabajo y del fondo exportable del diagrama.
 *
 * <p>Se mantiene en dominio para que el color de trabajo y el color exportado
 * no dependan de CSS ni de JavaFX. La interfaz puede editar estos valores, el
 * guardado .dms puede persistirlos y los exportadores pueden respetarlos.</p>
 */
public record DiagramAppearance(RgbaColor workspaceBackground, RgbaColor diagramBackground) {

    public DiagramAppearance {
        Objects.requireNonNull(workspaceBackground, "El fondo del área de trabajo no puede ser null");
        Objects.requireNonNull(diagramBackground, "El fondo del diagrama no puede ser null");
    }

    public static DiagramAppearance defaults() {
        return new DiagramAppearance(
                RgbaColor.rgb(238, 242, 246),
                RgbaColor.rgb(255, 255, 255)
        );
    }

    public DiagramAppearance withWorkspaceBackground(RgbaColor color) {
        return new DiagramAppearance(color, diagramBackground);
    }

    public DiagramAppearance withDiagramBackground(RgbaColor color) {
        return new DiagramAppearance(workspaceBackground, color);
    }
}
