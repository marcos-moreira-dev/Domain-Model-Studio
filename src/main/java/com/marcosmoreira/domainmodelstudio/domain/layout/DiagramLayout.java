package com.marcosmoreira.domainmodelstudio.domain.layout;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Layout de una notación específica.
 *
 * <p>Chen y Crow's Foot pueden compartir modelo semántico, pero no deben estar obligados
 * a compartir posiciones ni rutas de líneas. Esta clase representa el layout de una sola
 * notación.</p>
 */
public final class DiagramLayout {

    private final NotationType notation;
    private final Map<DiagramElementId, NodeLayout> nodesByElementId;
    private final Map<DiagramElementId, ConnectorLayout> connectorsById;

    /**
     * Crea el layout de una notación concreta e indexa nodos y conectores.
     *
     * @param notation notación visual asociada al layout
     * @param nodes posiciones, tamaños y orden visual de nodos
     * @param connectors rutas y puntos intermedios de conectores
     */
    public DiagramLayout(
            NotationType notation,
            List<NodeLayout> nodes,
            List<ConnectorLayout> connectors
    ) {
        this.notation = notation == null ? NotationType.CHEN : notation;
        this.nodesByElementId = indexNodes(normalizeNodeOrder(nodes == null ? List.of() : nodes));
        this.connectorsById = indexConnectors(connectors == null ? List.of() : connectors);
    }

    /**
     * Crea un layout vacío para una notación.
     */
    public static DiagramLayout empty(NotationType notation) {
        return new DiagramLayout(notation, List.of(), List.of());
    }

    public NotationType notation() {
        return notation;
    }

    public List<NodeLayout> nodes() {
        return List.copyOf(nodesByElementId.values());
    }

    public List<ConnectorLayout> connectors() {
        return List.copyOf(connectorsById.values());
    }

    public Optional<NodeLayout> nodeFor(DiagramElementId elementId) {
        return Optional.ofNullable(nodesByElementId.get(elementId));
    }

    public Optional<ConnectorLayout> connectorById(DiagramElementId connectorId) {
        return Optional.ofNullable(connectorsById.get(connectorId));
    }

    /**
     * Devuelve un nuevo layout insertando o reemplazando el layout de un nodo.
     */
    public DiagramLayout withNode(NodeLayout nodeLayout) {
        Objects.requireNonNull(nodeLayout, "El layout del nodo no puede ser null");
        Map<DiagramElementId, NodeLayout> updatedNodes = new LinkedHashMap<>(nodesByElementId);
        updatedNodes.put(nodeLayout.elementId(), nodeLayout);
        return new DiagramLayout(notation, List.copyOf(updatedNodes.values()), connectors());
    }

    /**
     * Devuelve un nuevo layout insertando o reemplazando la ruta de un conector.
     */
    public DiagramLayout withConnector(ConnectorLayout connectorLayout) {
        Objects.requireNonNull(connectorLayout, "El layout del conector no puede ser null");
        Map<DiagramElementId, ConnectorLayout> updatedConnectors = new LinkedHashMap<>(connectorsById);
        updatedConnectors.put(connectorLayout.connectorId(), connectorLayout);
        return new DiagramLayout(notation, nodes(), List.copyOf(updatedConnectors.values()));
    }


    /**
     * Elimina un nodo y descarta conectores que lo referencian.
     */
    public DiagramLayout withoutNode(DiagramElementId elementId) {
        Objects.requireNonNull(elementId, "El ID del nodo no puede ser null");
        Map<DiagramElementId, NodeLayout> updatedNodes = new LinkedHashMap<>(nodesByElementId);
        updatedNodes.remove(elementId);
        List<ConnectorLayout> updatedConnectors = connectorsById.values().stream()
                .filter(connector -> !connector.sourceElementId().equals(elementId))
                .filter(connector -> !connector.targetElementId().equals(elementId))
                .toList();
        return new DiagramLayout(notation, List.copyOf(updatedNodes.values()), updatedConnectors);
    }

    public DiagramLayout withoutConnector(DiagramElementId connectorId) {
        Objects.requireNonNull(connectorId, "El ID del conector no puede ser null");
        Map<DiagramElementId, ConnectorLayout> updatedConnectors = new LinkedHashMap<>(connectorsById);
        updatedConnectors.remove(connectorId);
        return new DiagramLayout(notation, nodes(), List.copyOf(updatedConnectors.values()));
    }

    public DiagramLayout withoutConnectorsReferencing(DiagramElementId elementId) {
        Objects.requireNonNull(elementId, "El ID del elemento no puede ser null");
        List<ConnectorLayout> updatedConnectors = connectorsById.values().stream()
                .filter(connector -> !connector.sourceElementId().equals(elementId))
                .filter(connector -> !connector.targetElementId().equals(elementId))
                .filter(connector -> !connector.connectorId().value().contains(elementId.value()))
                .toList();
        return new DiagramLayout(notation, nodes(), updatedConnectors);
    }

    /**
     * Mueve un nodo existente preservando el resto del layout.
     */
    public DiagramLayout moveNode(DiagramElementId elementId, double x, double y) {
        NodeLayout current = nodeFor(elementId)
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para el elemento: " + elementId));
        return withNode(current.movedTo(x, y));
    }

    public DiagramLayout bringNodesToFront(Set<DiagramElementId> elementIds) {
        Set<DiagramElementId> selected = normalizeIds(elementIds);
        if (selected.isEmpty()) {
            return this;
        }
        List<NodeLayout> base = nodes();
        List<NodeLayout> unselected = base.stream().filter(node -> !selected.contains(node.elementId())).toList();
        List<NodeLayout> selectedNodes = base.stream().filter(node -> selected.contains(node.elementId())).toList();
        if (selectedNodes.isEmpty()) {
            return this;
        }
        List<NodeLayout> reordered = new ArrayList<>(unselected);
        reordered.addAll(selectedNodes);
        return withNodeOrder(reordered);
    }

    public DiagramLayout sendNodesToBack(Set<DiagramElementId> elementIds) {
        Set<DiagramElementId> selected = normalizeIds(elementIds);
        if (selected.isEmpty()) {
            return this;
        }
        List<NodeLayout> base = nodes();
        List<NodeLayout> selectedNodes = base.stream().filter(node -> selected.contains(node.elementId())).toList();
        if (selectedNodes.isEmpty()) {
            return this;
        }
        List<NodeLayout> reordered = new ArrayList<>(selectedNodes);
        base.stream().filter(node -> !selected.contains(node.elementId())).forEach(reordered::add);
        return withNodeOrder(reordered);
    }

    public DiagramLayout raiseNodes(Set<DiagramElementId> elementIds) {
        Set<DiagramElementId> selected = normalizeIds(elementIds);
        if (selected.isEmpty()) {
            return this;
        }
        List<NodeLayout> reordered = new ArrayList<>(nodes());
        for (int index = reordered.size() - 2; index >= 0; index--) {
            boolean currentSelected = selected.contains(reordered.get(index).elementId());
            boolean nextSelected = selected.contains(reordered.get(index + 1).elementId());
            if (currentSelected && !nextSelected) {
                Collections.swap(reordered, index, index + 1);
            }
        }
        return withNodeOrder(reordered);
    }

    public DiagramLayout lowerNodes(Set<DiagramElementId> elementIds) {
        Set<DiagramElementId> selected = normalizeIds(elementIds);
        if (selected.isEmpty()) {
            return this;
        }
        List<NodeLayout> reordered = new ArrayList<>(nodes());
        for (int index = 1; index < reordered.size(); index++) {
            boolean currentSelected = selected.contains(reordered.get(index).elementId());
            boolean previousSelected = selected.contains(reordered.get(index - 1).elementId());
            if (currentSelected && !previousSelected) {
                Collections.swap(reordered, index, index - 1);
            }
        }
        return withNodeOrder(reordered);
    }

    public int nodeCount() {
        return nodesByElementId.size();
    }

    public int connectorCount() {
        return connectorsById.size();
    }

    private DiagramLayout withNodeOrder(List<NodeLayout> orderedNodes) {
        return new DiagramLayout(notation, orderedNodes, connectors());
    }

    private static List<NodeLayout> normalizeNodeOrder(List<NodeLayout> nodes) {
        List<NodeLayout> ordered = new ArrayList<>();
        for (int index = 0; index < nodes.size(); index++) {
            NodeLayout node = Objects.requireNonNull(nodes.get(index), "El layout del nodo no puede ser null");
            ordered.add(node.withZOrder(index));
        }
        return ordered;
    }

    private static Set<DiagramElementId> normalizeIds(Set<DiagramElementId> elementIds) {
        Set<DiagramElementId> normalized = new LinkedHashSet<>();
        if (elementIds != null) {
            for (DiagramElementId id : elementIds) {
                if (id != null) {
                    normalized.add(id);
                }
            }
        }
        return normalized;
    }

    private static Map<DiagramElementId, NodeLayout> indexNodes(List<NodeLayout> nodes) {
        Map<DiagramElementId, NodeLayout> index = new LinkedHashMap<>();
        for (NodeLayout node : nodes) {
            NodeLayout previous = index.putIfAbsent(node.elementId(), node);
            if (previous != null) {
                throw new IllegalArgumentException("Layout de nodo duplicado: " + node.elementId());
            }
        }
        return Collections.unmodifiableMap(index);
    }

    private static Map<DiagramElementId, ConnectorLayout> indexConnectors(List<ConnectorLayout> connectors) {
        Map<DiagramElementId, ConnectorLayout> index = new LinkedHashMap<>();
        for (ConnectorLayout connector : connectors) {
            ConnectorLayout previous = index.putIfAbsent(connector.connectorId(), connector);
            if (previous != null) {
                throw new IllegalArgumentException("Layout de conector duplicado: " + connector.connectorId());
            }
        }
        return Collections.unmodifiableMap(index);
    }
}
