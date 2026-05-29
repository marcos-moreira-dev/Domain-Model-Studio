package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Polyline;

/**
 * Dibuja conectores temporales durante el arrastre diferido de nodos.
 *
 * <p>El canvas común conserva el commit diferido para no reconstruir todo el diagrama
 * por cada pixel. Esta capa mantiene, al mismo tiempo, la invariante visual del
 * canvas conceptual: mientras una pieza se desplaza, sus relaciones siguen siendo
 * comprensibles y no parecen romperse hasta soltar el mouse.</p>
 */
public final class CanvasConnectorDragPreviewLayer {

    private final InteractiveCanvasAdapter adapter;
    private final Group livePreviewLayer;
    private final DiagramDrawingFacade drawingFacade;
    private final CanvasConnectorVisualRegistry connectorVisualRegistry;

    public CanvasConnectorDragPreviewLayer(
            InteractiveCanvasAdapter adapter,
            Group livePreviewLayer,
            DiagramDrawingFacade drawingFacade,
            CanvasConnectorVisualRegistry connectorVisualRegistry
    ) {
        this.adapter = Objects.requireNonNull(adapter, "El adaptador no puede ser null");
        this.livePreviewLayer = Objects.requireNonNull(livePreviewLayer, "La capa de preview no puede ser null");
        this.drawingFacade = drawingFacade == null ? DiagramDrawingFacade.defaults() : drawingFacade;
        this.connectorVisualRegistry = Objects.requireNonNull(connectorVisualRegistry, "El registro visual de conectores no puede ser null");
    }

    public void show(String draggedNodeId, double deltaX, double deltaY) {
        Set<String> movedNodeIds = previewNodeIds(draggedNodeId);
        if (movedNodeIds.isEmpty()) {
            clear();
            return;
        }
        connectorVisualRegistry.showDragPreviewFor(movedNodeIds);
        List<Polyline> previewConnectors = new ArrayList<>();
        for (InteractiveCanvasConnector connector : adapter.connectors()) {
            if (!isAffected(connector, movedNodeIds)) {
                continue;
            }
            previewConnector(connector, movedNodeIds, deltaX, deltaY).ifPresent(previewConnectors::add);
        }
        livePreviewLayer.getChildren().setAll(previewConnectors);
    }

    public void clear() {
        connectorVisualRegistry.clearDragPreview();
        livePreviewLayer.getChildren().clear();
    }

    private java.util.Optional<Polyline> previewConnector(
            InteractiveCanvasConnector connector,
            Set<String> movedNodeIds,
            double deltaX,
            double deltaY
    ) {
        var sourceLayout = adapter.layoutForNode(connector.sourceNodeId());
        var targetLayout = adapter.layoutForNode(connector.targetNodeId());
        if (sourceLayout.isEmpty() || targetLayout.isEmpty()) {
            return java.util.Optional.empty();
        }
        NodeLayout source = shiftedIfMoved(sourceLayout.get(), connector.sourceNodeId(), movedNodeIds, deltaX, deltaY);
        NodeLayout target = shiftedIfMoved(targetLayout.get(), connector.targetNodeId(), movedNodeIds, deltaX, deltaY);
        List<Point2D> points = CanvasConnectorGeometry.edgeToEdgePoints(
                source,
                target,
                adapter.layoutForConnector(connector.id()),
                drawingFacade);
        Polyline line = polylineFrom(points);
        line.getStyleClass().add("interactive-canvas-connector");
        line.getStyleClass().add("interactive-canvas-connector-live-preview");
        line.getStyleClass().add("interactive-canvas-connector-live-preview-" + safeKind(connector.kind()));
        line.setMouseTransparent(true);
        line.setManaged(false);
        return java.util.Optional.of(line);
    }

    private Set<String> previewNodeIds(String draggedNodeId) {
        Set<String> selected = adapter.selection().selectedNodeIds();
        if (adapter instanceof CanvasDragPreviewPort dragPreviewPort) {
            return dragPreviewPort.previewNodeIdsForDraggedNode(draggedNodeId, selected);
        }
        if (selected == null || selected.isEmpty()) {
            return draggedNodeId == null || draggedNodeId.isBlank() ? Set.of() : Set.of(draggedNodeId);
        }
        return new LinkedHashSet<>(selected);
    }

    private static boolean isAffected(InteractiveCanvasConnector connector, Set<String> movedNodeIds) {
        return movedNodeIds.contains(connector.sourceNodeId()) || movedNodeIds.contains(connector.targetNodeId());
    }

    private static NodeLayout shiftedIfMoved(
            NodeLayout layout,
            String nodeId,
            Set<String> movedNodeIds,
            double deltaX,
            double deltaY
    ) {
        return movedNodeIds.contains(nodeId) ? layout.translatedBy(deltaX, deltaY) : layout;
    }

    private static Polyline polylineFrom(List<Point2D> points) {
        Polyline line = new Polyline();
        for (Point2D point : points) {
            line.getPoints().addAll(point.getX(), point.getY());
        }
        return line;
    }

    private static String safeKind(String kind) {
        if (kind == null || kind.isBlank()) {
            return "generic";
        }
        return kind.strip().toLowerCase().replaceAll("[^a-z0-9_-]", "-");
    }
}
