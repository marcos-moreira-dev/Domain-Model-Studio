package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import java.util.Objects;

/** Conector semántico que debe tener ruta visual persistente en el layout. */
public record VisualConnectorReference(
        DiagramElementId layoutId,
        DiagramElementId sourceLayoutId,
        DiagramElementId targetLayoutId
) {
    public VisualConnectorReference {
        layoutId = Objects.requireNonNull(layoutId, "layoutId");
        sourceLayoutId = Objects.requireNonNull(sourceLayoutId, "sourceLayoutId");
        targetLayoutId = Objects.requireNonNull(targetLayoutId, "targetLayoutId");
    }
}
