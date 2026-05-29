package com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;
import java.util.Objects;

/** Modelo plano y trazable para escribir SVG sin depender de controles JavaFX. */
public record SpecializedSvgModel(
        DiagramTypeId diagramTypeId,
        String title,
        String typeLabel,
        List<String> viewLabels,
        List<SpecializedSvgNode> nodes,
        List<SpecializedSvgConnector> connectors
) {
    public SpecializedSvgModel(
            DiagramTypeId diagramTypeId,
            String title,
            String typeLabel,
            List<SpecializedSvgNode> nodes,
            List<SpecializedSvgConnector> connectors
    ) {
        this(diagramTypeId, title, typeLabel, List.of(), nodes, connectors);
    }

    public SpecializedSvgModel {
        diagramTypeId = Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        title = normalize(title, "Diagrama especializado");
        typeLabel = normalize(typeLabel, diagramTypeId.value());
        viewLabels = List.copyOf(viewLabels == null ? List.of() : viewLabels.stream()
                .map(value -> normalize(value, ""))
                .filter(value -> !value.isBlank())
                .distinct()
                .toList());
        nodes = List.copyOf(nodes == null ? List.of() : nodes);
        connectors = List.copyOf(connectors == null ? List.of() : connectors);
    }

    public boolean empty() {
        return nodes.isEmpty() && connectors.isEmpty();
    }

    public String viewSummary() {
        if (viewLabels.isEmpty()) {
            return "";
        }
        return "Vistas: " + String.join(" · ", viewLabels);
    }

    private static String normalize(String value, String fallback) {
        String normalized = value == null ? "" : value.strip();
        return normalized.isBlank() ? fallback : normalized;
    }
}
