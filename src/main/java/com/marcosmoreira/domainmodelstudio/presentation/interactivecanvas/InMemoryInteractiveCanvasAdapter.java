package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.layout.BendPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Adaptador mutable pequeño para pruebas, ejemplos internos y migraciones incrementales.
 */
public final class InMemoryInteractiveCanvasAdapter implements InteractiveCanvasAdapter {

    private final DiagramTypeId diagramTypeId;
    private final Map<String, InteractiveCanvasNode> nodes = new LinkedHashMap<>();
    private final Map<String, InteractiveCanvasConnector> connectors = new LinkedHashMap<>();
    private final Map<String, NodeLayout> nodeLayouts = new LinkedHashMap<>();
    private final Map<String, ConnectorLayout> connectorLayouts = new LinkedHashMap<>();
    private InteractiveCanvasSelection selection = InteractiveCanvasSelection.empty();
    private boolean dirty;

    public InMemoryInteractiveCanvasAdapter(DiagramTypeId diagramTypeId) {
        this.diagramTypeId = diagramTypeId == null ? DiagramTypeId.CONCEPTUAL_MODEL : diagramTypeId;
    }

    public InMemoryInteractiveCanvasAdapter addNode(InteractiveCanvasNode node, NodeLayout layout) {
        nodes.put(node.id(), node);
        nodeLayouts.put(node.id(), layout);
        return this;
    }

    public InMemoryInteractiveCanvasAdapter addConnector(
            InteractiveCanvasConnector connector,
            ConnectorLayout layout
    ) {
        connectors.put(connector.id(), connector);
        connectorLayouts.put(connector.id(), layout);
        return this;
    }

    @Override
    public DiagramTypeId diagramTypeId() {
        return diagramTypeId;
    }

    @Override
    public List<InteractiveCanvasNode> nodes() {
        return List.copyOf(nodes.values());
    }

    @Override
    public List<InteractiveCanvasConnector> connectors() {
        return List.copyOf(connectors.values());
    }

    @Override
    public Optional<NodeLayout> layoutForNode(String elementId) {
        return Optional.ofNullable(nodeLayouts.get(normalize(elementId)));
    }

    @Override
    public Optional<ConnectorLayout> layoutForConnector(String connectorId) {
        return Optional.ofNullable(connectorLayouts.get(normalize(connectorId)));
    }

    @Override
    public InteractiveCanvasSelection selection() {
        return selection;
    }

    @Override
    public void selectNode(String elementId, boolean additive) {
        String normalized = normalize(elementId);
        selection = additive ? selection.toggledNode(normalized) : selection.withSingleNode(normalized);
    }

    @Override
    public void selectConnector(String connectorId, boolean additive) {
        String normalized = normalize(connectorId);
        selection = additive && selection.isConnectorSelected(normalized)
                ? InteractiveCanvasSelection.empty()
                : selection.withSingleConnector(normalized);
    }

    @Override
    public void selectNodesInside(CanvasBounds selectionBounds, boolean additive) {
        Set<String> selected = new LinkedHashSet<>(additive ? selection.selectedNodeIds() : Set.of());
        for (Map.Entry<String, NodeLayout> entry : nodeLayouts.entrySet()) {
            if (CanvasBounds.from(entry.getValue()).intersects(selectionBounds)) {
                selected.add(entry.getKey());
            }
        }
        selection = new InteractiveCanvasSelection(selected, Set.of(), null);
    }

    @Override
    public void clearSelection() {
        selection = InteractiveCanvasSelection.empty();
    }

    @Override
    public void moveNode(String elementId, double x, double y) {
        String normalized = normalize(elementId);
        NodeLayout layout = nodeLayouts.get(normalized);
        if (layout != null) {
            nodeLayouts.put(normalized, layout.movedTo(x, y));
            markDirty();
        }
    }

    @Override
    public void moveSelectedNodesBy(double deltaX, double deltaY) {
        for (String nodeId : selection.selectedNodeIds()) {
            NodeLayout layout = nodeLayouts.get(nodeId);
            if (layout != null && !layout.locked()) {
                nodeLayouts.put(nodeId, layout.translatedBy(deltaX, deltaY));
            }
        }
        markDirty();
    }

    @Override
    public void addBendPoint(String connectorId, double x, double y) {
        String normalized = normalize(connectorId);
        ConnectorLayout layout = connectorLayouts.get(normalized);
        if (layout != null) {
            BendPoint point = BendPoint.of(x, y);
            ConnectorLayout updated = layout.withBendPoints(bendPointsWithInsertedPoint(layout, point));
            connectorLayouts.put(normalized, updated);
            selection = selection.withBendPoint(normalized, bendPointIndexAt(updated, point));
            markDirty();
        }
    }

    @Override
    public void moveBendPoint(String connectorId, int index, double x, double y) {
        String normalized = normalize(connectorId);
        ConnectorLayout layout = connectorLayouts.get(normalized);
        if (layout == null || index < 0 || index >= layout.bendPoints().size()) {
            return;
        }
        BendPoint current = layout.bendPoints().get(index);
        ConnectorLayout updated = layout.withMovedBendPoint(index, x - current.x(), y - current.y());
        connectorLayouts.put(normalized, updated);
        selection = selection.withBendPoint(normalized, index);
        markDirty();
    }

    @Override
    public void selectBendPoint(String connectorId, int index) {
        selection = selection.withBendPoint(connectorId, index);
    }

    @Override
    public void removeSelectedBendPoint() {
        selection.selectedBendPoint().ifPresent(point -> {
            ConnectorLayout layout = connectorLayouts.get(point.connectorId());
            if (layout != null && point.index() < layout.bendPoints().size()) {
                connectorLayouts.put(point.connectorId(), layout.withoutBendPoint(point.index()));
                selection = InteractiveCanvasSelection.empty();
                markDirty();
            }
        });
    }

    private List<BendPoint> bendPointsWithInsertedPoint(ConnectorLayout layout, BendPoint point) {
        List<BendPoint> currentPoints = layout.bendPoints();
        if (currentPoints.isEmpty()) {
            return List.of(point);
        }
        NodeLayout source = nodeLayouts.get(layout.sourceElementId().value());
        NodeLayout target = nodeLayouts.get(layout.targetElementId().value());
        if (source == null || target == null) {
            List<BendPoint> appended = new ArrayList<>(currentPoints);
            appended.add(point);
            return appended;
        }
        List<DiagramPoint> route = new ArrayList<>();
        route.add(centerOf(source));
        currentPoints.stream().map(BendPoint::asPoint).forEach(route::add);
        route.add(centerOf(target));
        int segmentIndex = nearestSegmentIndex(point.asPoint(), route);
        int insertIndex = Math.max(0, Math.min(segmentIndex, currentPoints.size()));
        List<BendPoint> updated = new ArrayList<>(currentPoints);
        updated.add(insertIndex, point);
        return updated;
    }

    private static int bendPointIndexAt(ConnectorLayout layout, BendPoint point) {
        List<BendPoint> bendPoints = layout.bendPoints();
        for (int index = 0; index < bendPoints.size(); index++) {
            BendPoint candidate = bendPoints.get(index);
            if (Double.compare(candidate.x(), point.x()) == 0 && Double.compare(candidate.y(), point.y()) == 0) {
                return index;
            }
        }
        return Math.max(0, bendPoints.size() - 1);
    }

    private static DiagramPoint centerOf(NodeLayout layout) {
        return DiagramPoint.of(layout.x() + layout.width() / 2.0, layout.y() + layout.height() / 2.0);
    }

    private static int nearestSegmentIndex(DiagramPoint point, List<DiagramPoint> route) {
        int nearestIndex = 0;
        double nearestDistance = Double.MAX_VALUE;
        for (int index = 0; index < route.size() - 1; index++) {
            double distance = distanceToSegment(point, route.get(index), route.get(index + 1));
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestIndex = index;
            }
        }
        return nearestIndex;
    }

    private static double distanceToSegment(DiagramPoint point, DiagramPoint a, DiagramPoint b) {
        double dx = b.x() - a.x();
        double dy = b.y() - a.y();
        double lengthSquared = dx * dx + dy * dy;
        if (lengthSquared <= 0.0001) {
            return Math.hypot(point.x() - a.x(), point.y() - a.y());
        }
        double t = ((point.x() - a.x()) * dx + (point.y() - a.y()) * dy) / lengthSquared;
        double clamped = Math.max(0.0, Math.min(1.0, t));
        double projectedX = a.x() + clamped * dx;
        double projectedY = a.y() + clamped * dy;
        return Math.hypot(point.x() - projectedX, point.y() - projectedY);
    }

    @Override
    public void markDirty() {
        dirty = true;
    }

    public boolean dirty() {
        return dirty;
    }

    private static String normalize(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El identificador no puede estar vacío");
        }
        return value.strip();
    }
}
