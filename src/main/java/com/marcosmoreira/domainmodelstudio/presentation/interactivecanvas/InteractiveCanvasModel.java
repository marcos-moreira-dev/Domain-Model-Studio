package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Snapshot liviano del contenido que el canvas debe pintar en este momento.
 *
 * <p>El snapshot es independiente de JavaFX. Congela nodos, conectores, layouts,
 * selección y advertencias para que la superficie canónica pueda renderizar sin tocar
 * ViewModels concretos ni familias de diagrama.</p>
 */
public final class InteractiveCanvasModel {

    private final List<InteractiveCanvasNode> nodes;
    private final List<InteractiveCanvasConnector> connectors;
    private final Map<String, NodeLayout> nodeLayouts;
    private final Map<String, ConnectorLayout> connectorLayouts;
    private final InteractiveCanvasSelection selection;
    private final List<CanvasModelWarning> warnings;
    private final CanvasBounds contentBounds;

    public InteractiveCanvasModel(
            List<InteractiveCanvasNode> nodes,
            List<InteractiveCanvasConnector> connectors,
            InteractiveCanvasSelection selection
    ) {
        this(nodes, connectors, Map.of(), Map.of(), selection, List.of());
    }

    public InteractiveCanvasModel(
            List<InteractiveCanvasNode> nodes,
            List<InteractiveCanvasConnector> connectors,
            Map<String, NodeLayout> nodeLayouts,
            Map<String, ConnectorLayout> connectorLayouts,
            InteractiveCanvasSelection selection,
            List<CanvasModelWarning> warnings
    ) {
        this.nodes = List.copyOf(nodes == null ? List.of() : nodes);
        this.connectors = List.copyOf(connectors == null ? List.of() : connectors);
        this.nodeLayouts = Map.copyOf(nodeLayouts == null ? Map.of() : nodeLayouts);
        this.connectorLayouts = Map.copyOf(connectorLayouts == null ? Map.of() : connectorLayouts);
        this.selection = Objects.requireNonNullElseGet(selection, InteractiveCanvasSelection::empty);
        this.warnings = List.copyOf(warnings == null ? List.of() : warnings);
        this.contentBounds = computeContentBounds(this.nodeLayouts);
    }

    public static InteractiveCanvasModel from(InteractiveCanvasAdapter adapter) {
        return from((CanvasReadPort) adapter, adapter.selection());
    }

    public static InteractiveCanvasModel from(CanvasReadPort readPort, InteractiveCanvasSelection selection) {
        Objects.requireNonNull(readPort, "El puerto de lectura del canvas no puede ser null");
        List<InteractiveCanvasNode> nodes = List.copyOf(readPort.nodes());
        List<InteractiveCanvasConnector> connectors = List.copyOf(readPort.connectors());
        Map<String, NodeLayout> nodeLayouts = new LinkedHashMap<>();
        Map<String, ConnectorLayout> connectorLayouts = new LinkedHashMap<>();
        List<CanvasModelWarning> warnings = new java.util.ArrayList<>();

        for (InteractiveCanvasNode node : nodes) {
            if (!node.visible()) {
                continue;
            }
            Optional<NodeLayout> layout = readPort.layoutForNode(node.id());
            if (layout.isPresent()) {
                nodeLayouts.put(node.id(), layout.get());
            } else {
                warnings.add(CanvasModelWarning.missingNodeLayout(node.id()));
            }
        }
        for (InteractiveCanvasConnector connector : connectors) {
            if (!connector.visible()) {
                continue;
            }
            Optional<ConnectorLayout> layout = readPort.layoutForConnector(connector.id());
            if (layout.isPresent()) {
                connectorLayouts.put(connector.id(), layout.get());
            } else {
                warnings.add(CanvasModelWarning.missingConnectorLayout(connector.id()));
            }
        }
        return new InteractiveCanvasModel(nodes, connectors, nodeLayouts, connectorLayouts, selection, warnings);
    }

    public List<InteractiveCanvasNode> nodes() {
        return nodes;
    }

    public List<InteractiveCanvasConnector> connectors() {
        return connectors;
    }

    public List<InteractiveCanvasNode> visibleNodes() {
        return nodes.stream().filter(InteractiveCanvasNode::visible).toList();
    }

    public List<InteractiveCanvasConnector> visibleConnectors() {
        return connectors.stream().filter(InteractiveCanvasConnector::visible).toList();
    }

    public Optional<NodeLayout> layoutForNode(String nodeId) {
        return Optional.ofNullable(nodeLayouts.get(normalize(nodeId)));
    }

    public Optional<ConnectorLayout> layoutForConnector(String connectorId) {
        return Optional.ofNullable(connectorLayouts.get(normalize(connectorId)));
    }

    public Map<String, NodeLayout> nodeLayouts() {
        return Map.copyOf(nodeLayouts);
    }

    public Map<String, ConnectorLayout> connectorLayouts() {
        return Map.copyOf(connectorLayouts);
    }

    public InteractiveCanvasSelection selection() {
        return selection;
    }

    public List<CanvasModelWarning> warnings() {
        return warnings;
    }

    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }

    public Optional<CanvasBounds> contentBounds() {
        return Optional.ofNullable(contentBounds);
    }

    public boolean isEmpty() {
        return nodes.isEmpty() && connectors.isEmpty();
    }

    private static CanvasBounds computeContentBounds(Map<String, NodeLayout> layouts) {
        CanvasBounds bounds = null;
        for (NodeLayout layout : layouts.values()) {
            if (!layout.visible()) {
                continue;
            }
            CanvasBounds current = CanvasBounds.from(layout);
            bounds = bounds == null ? current : CanvasBounds.union(bounds, current);
        }
        return bounds;
    }

    private static String normalize(String value) {
        return Objects.toString(value, "").strip();
    }
}
