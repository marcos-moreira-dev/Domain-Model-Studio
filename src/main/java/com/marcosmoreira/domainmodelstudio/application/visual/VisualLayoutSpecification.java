package com.marcosmoreira.domainmodelstudio.application.visual;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Lista normalizada de elementos visuales que un documento especializado promete mostrar.
 */
public record VisualLayoutSpecification(
        List<VisualNodeReference> nodes,
        List<VisualConnectorReference> connectors
) {
    public VisualLayoutSpecification {
        nodes = List.copyOf(nodes == null ? List.of() : nodes);
        connectors = List.copyOf(connectors == null ? List.of() : connectors);
        ensureUniqueNodes(nodes);
        ensureUniqueConnectors(connectors);
    }

    public static VisualLayoutSpecification empty() {
        return new VisualLayoutSpecification(List.of(), List.of());
    }

    public boolean emptySpecification() {
        return nodes.isEmpty() && connectors.isEmpty();
    }

    private static void ensureUniqueNodes(List<VisualNodeReference> nodes) {
        Set<String> ids = new LinkedHashSet<>();
        for (VisualNodeReference node : nodes) {
            Objects.requireNonNull(node, "node");
            if (!ids.add(node.layoutId().value())) {
                throw new IllegalArgumentException("Nodo visual duplicado: " + node.layoutId());
            }
        }
    }

    private static void ensureUniqueConnectors(List<VisualConnectorReference> connectors) {
        Set<String> ids = new LinkedHashSet<>();
        for (VisualConnectorReference connector : connectors) {
            Objects.requireNonNull(connector, "connector");
            if (!ids.add(connector.layoutId().value())) {
                throw new IllegalArgumentException("Conector visual duplicado: " + connector.layoutId());
            }
        }
    }
}
