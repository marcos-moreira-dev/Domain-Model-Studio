package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Selección normalizada del lienzo: nodos, conectores y un punto intermedio activo.
 */
public final class InteractiveCanvasSelection {

    private final Set<String> selectedNodeIds;
    private final Set<String> selectedConnectorIds;
    private final SelectedBendPoint selectedBendPoint;

    public InteractiveCanvasSelection(
            Set<String> selectedNodeIds,
            Set<String> selectedConnectorIds,
            SelectedBendPoint selectedBendPoint
    ) {
        this.selectedNodeIds = normalizeSet(selectedNodeIds);
        this.selectedConnectorIds = normalizeSet(selectedConnectorIds);
        this.selectedBendPoint = selectedBendPoint;
    }

    public static InteractiveCanvasSelection empty() {
        return new InteractiveCanvasSelection(Set.of(), Set.of(), null);
    }

    public Set<String> selectedNodeIds() {
        return Set.copyOf(selectedNodeIds);
    }

    public Set<String> selectedConnectorIds() {
        return Set.copyOf(selectedConnectorIds);
    }

    public Optional<SelectedBendPoint> selectedBendPoint() {
        return Optional.ofNullable(selectedBendPoint);
    }

    public boolean isNodeSelected(String nodeId) {
        return selectedNodeIds.contains(normalize(nodeId));
    }

    public boolean isConnectorSelected(String connectorId) {
        return selectedConnectorIds.contains(normalize(connectorId));
    }

    public boolean isEmpty() {
        return selectedNodeIds.isEmpty() && selectedConnectorIds.isEmpty() && selectedBendPoint == null;
    }

    public InteractiveCanvasSelection withSingleNode(String nodeId) {
        return new InteractiveCanvasSelection(Set.of(requireText(nodeId)), Set.of(), null);
    }

    public InteractiveCanvasSelection toggledNode(String nodeId) {
        Set<String> updated = new LinkedHashSet<>(selectedNodeIds);
        String normalized = requireText(nodeId);
        if (!updated.add(normalized)) {
            updated.remove(normalized);
        }
        return new InteractiveCanvasSelection(updated, selectedConnectorIds, null);
    }

    public InteractiveCanvasSelection withSingleConnector(String connectorId) {
        return new InteractiveCanvasSelection(Set.of(), Set.of(requireText(connectorId)), null);
    }

    public InteractiveCanvasSelection withBendPoint(String connectorId, int index) {
        return new InteractiveCanvasSelection(Set.of(), Set.of(requireText(connectorId)), new SelectedBendPoint(connectorId, index));
    }

    public InteractiveCanvasSelection withNodes(Set<String> nodeIds) {
        return new InteractiveCanvasSelection(nodeIds, Set.of(), null);
    }

    public InteractiveCanvasSelection withNodesAndConnectors(Set<String> nodeIds, Set<String> connectorIds) {
        return new InteractiveCanvasSelection(nodeIds, connectorIds, null);
    }

    private static Set<String> normalizeSet(Set<String> values) {
        Set<String> normalized = new LinkedHashSet<>();
        for (String value : values == null ? Set.<String>of() : values) {
            normalized.add(requireText(value));
        }
        return Set.copyOf(normalized);
    }

    private static String requireText(String value) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El identificador de selección no puede estar vacío");
        }
        return normalized;
    }

    private static String normalize(String value) {
        return Objects.toString(value, "").strip();
    }
}
