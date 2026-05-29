package com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import java.util.Objects;

/** Conector semántico normalizado para exportación SVG vectorial de diagramas especializados. */
public record SpecializedSvgConnector(
        DiagramElementId layoutId,
        DiagramElementId sourceLayoutId,
        DiagramElementId targetLayoutId,
        String label,
        String kindLabel,
        String cssClass
) {
    public SpecializedSvgConnector {
        layoutId = Objects.requireNonNull(layoutId, "layoutId");
        sourceLayoutId = Objects.requireNonNull(sourceLayoutId, "sourceLayoutId");
        targetLayoutId = Objects.requireNonNull(targetLayoutId, "targetLayoutId");
        label = normalize(label, "");
        kindLabel = normalize(kindLabel, "Relación");
        cssClass = normalize(cssClass, "connector-default");
    }

    public String visibleLabel() {
        return label.isBlank() ? kindLabel : label;
    }

    private static String normalize(String value, String fallback) {
        String normalized = value == null ? "" : value.strip();
        return normalized.isBlank() ? fallback : normalized;
    }
}
