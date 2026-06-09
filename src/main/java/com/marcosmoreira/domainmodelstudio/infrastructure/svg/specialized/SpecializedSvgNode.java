package com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import java.util.List;
import java.util.Objects;

/** Nodo semántico normalizado para exportación SVG vectorial de diagramas especializados. */
public record SpecializedSvgNode(
        DiagramElementId layoutId,
        String title,
        String kindLabel,
        List<String> details,
        String cssClass
) {
    public SpecializedSvgNode {
        layoutId = Objects.requireNonNull(layoutId, "layoutId");
        title = normalize(title, layoutId.value());
        kindLabel = normalize(kindLabel, "Elemento");
        details = List.copyOf(details == null ? List.of() : details.stream()
                .map(value -> normalize(value, ""))
                .filter(value -> !value.isBlank())
                .limit(4)
                .toList());
        cssClass = normalize(cssClass, "node-default");
    }

    private static String normalize(String value, String fallback) {
        String normalized = value == null ? "" : value.strip();
        return normalized.isBlank() ? fallback : normalized;
    }
}
