package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import java.util.Objects;

/** Nodo semántico que debe tener una caja visual persistente en el layout. */
public record VisualNodeReference(
        DiagramElementId layoutId,
        double preferredWidth,
        double preferredHeight,
        int orderIndex
) {
    public VisualNodeReference {
        layoutId = Objects.requireNonNull(layoutId, "layoutId");
        if (!Double.isFinite(preferredWidth) || preferredWidth <= 0.0) {
            throw new IllegalArgumentException("El ancho preferido debe ser positivo.");
        }
        if (!Double.isFinite(preferredHeight) || preferredHeight <= 0.0) {
            throw new IllegalArgumentException("El alto preferido debe ser positivo.");
        }
        orderIndex = Math.max(0, orderIndex);
    }
}
