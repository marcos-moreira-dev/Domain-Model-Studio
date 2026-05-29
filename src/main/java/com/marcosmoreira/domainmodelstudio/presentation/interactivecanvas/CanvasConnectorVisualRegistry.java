package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramArrowFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;

/**
 * Registro visual de conectores renderizados.
 *
 * <p>Permite que la previsualización transversal de arrastre de nodos atenúe las
 * relaciones originales afectadas mientras dibuja la ruta temporal encima. Así el
 * usuario ve que la línea acompaña al nodo durante el gesto, sin mutar el layout
 * por cada pixel.</p>
 */
public final class CanvasConnectorVisualRegistry {

    private final Map<String, ConnectorVisual> visuals = new HashMap<>();
    private final DiagramArrowFactory arrowFactory = new DiagramArrowFactory();

    public void clear() {
        visuals.clear();
    }

    public void register(InteractiveCanvasConnector connector, Node rendered) {
        if (connector == null || rendered == null) {
            return;
        }
        visuals.put(connector.id(), new ConnectorVisual(connector, rendered));
    }

    /**
     * Actualiza en sitio la ruta de un conector durante el arrastre de un bendpoint.
     *
     * <p>Esta operación evita reconstruir todo el canvas en cada pixel del gesto. El
     * adaptador ya actualizó el layout; aquí solo se sincronizan las {@link Polyline}
     * y la punta vectorial del conector renderizado. El refresh completo queda para
     * {@code MOUSE_RELEASED}, cuando el gesto ya terminó y no se destruye el handle
     * que el usuario está arrastrando.</p>
     */
    public void updateLiveRoute(String connectorId, InteractiveCanvasAdapter adapter, DiagramDrawingFacade drawingFacade) {
        ConnectorVisual visual = visuals.get(normalize(connectorId));
        if (visual == null) {
            return;
        }
        updateLiveRoute(visual, adapter, drawingFacade);
    }

    /**
     * Actualiza en sitio las relaciones conectadas a nodos que se están moviendo.
     *
     * <p>El drag de nodos actualiza el {@code NodeLayout} real durante el gesto,
     * pero no debe reconstruir todo el canvas por cada pixel porque eso corta el
     * {@code MOUSE_DRAGGED}. Esta operación reutiliza los conectores ya
     * renderizados y recalcula únicamente sus rutas vivas, igual que el flujo de
     * bendpoints.</p>
     */
    public void updateLiveRoutesForMovedNodes(
            Set<String> movedNodeIds,
            InteractiveCanvasAdapter adapter,
            DiagramDrawingFacade drawingFacade
    ) {
        if (movedNodeIds == null || movedNodeIds.isEmpty()) {
            return;
        }
        for (ConnectorVisual visual : visuals.values()) {
            if (visual.touchesAny(movedNodeIds)) {
                updateLiveRoute(visual, adapter, drawingFacade);
            }
        }
    }

    private void updateLiveRoute(ConnectorVisual visual, InteractiveCanvasAdapter adapter, DiagramDrawingFacade drawingFacade) {
        if (visual == null || adapter == null) {
            return;
        }
        var sourceLayout = adapter.layoutForNode(visual.connector().sourceNodeId());
        var targetLayout = adapter.layoutForNode(visual.connector().targetNodeId());
        if (sourceLayout.isEmpty() || targetLayout.isEmpty()) {
            return;
        }
        var points = CanvasConnectorGeometry.edgeToEdgePoints(
                sourceLayout.get(),
                targetLayout.get(),
                adapter.layoutForConnector(visual.connector().id()),
                drawingFacade);
        if (points.size() < 2) {
            return;
        }
        updatePolylines(visual.node(), points);
        updateArrowPolygons(visual.node(), points);
    }

    public void showDragPreviewFor(Set<String> movedNodeIds) {
        clearDragPreview();
        if (movedNodeIds == null || movedNodeIds.isEmpty()) {
            return;
        }
        for (ConnectorVisual visual : visuals.values()) {
            if (!visual.touchesAny(movedNodeIds)) {
                continue;
            }
            visual.node().setOpacity(0.22);
            addStyleClassIfMissing(visual.node(), "interactive-canvas-connector-drag-original");
        }
    }

    public void clearDragPreview() {
        for (ConnectorVisual visual : visuals.values()) {
            visual.node().setOpacity(1.0);
            visual.node().getStyleClass().remove("interactive-canvas-connector-drag-original");
        }
    }

    private void updatePolylines(Node node, java.util.List<Point2D> points) {
        if (node instanceof Polyline line && !line.getStyleClass().stream().anyMatch(style -> style.contains("preview"))) {
            line.getPoints().setAll(flatten(points));
        }
        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                updatePolylines(child, points);
            }
        }
    }

    private void updateArrowPolygons(Node node, java.util.List<Point2D> points) {
        if (node instanceof Polygon polygon && !polygon.getStyleClass().stream().anyMatch(style -> style.contains("handle"))) {
            Point2D previous = points.get(points.size() - 2);
            Point2D end = points.get(points.size() - 1);
            int pointCount = polygon.getPoints().size() / 2;
            if (pointCount == 3) {
                polygon.getPoints().setAll(arrowFactory.triangle(previous, end, DiagramArrowFactory.DEFAULT_LENGTH, DiagramArrowFactory.DEFAULT_WIDTH).getPoints());
            } else if (pointCount == 4) {
                polygon.getPoints().setAll(arrowFactory.diamond(previous, end, DiagramArrowFactory.DEFAULT_LENGTH + 5.0, DiagramArrowFactory.DEFAULT_WIDTH + 2.0).getPoints());
            }
        }
        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                updateArrowPolygons(child, points);
            }
        }
    }

    private static java.util.List<Double> flatten(java.util.List<Point2D> points) {
        java.util.List<Double> flattened = new java.util.ArrayList<>(points.size() * 2);
        for (Point2D point : points) {
            flattened.add(point.getX());
            flattened.add(point.getY());
        }
        return flattened;
    }

    private static void addStyleClassIfMissing(Node node, String styleClass) {
        if (!node.getStyleClass().contains(styleClass)) {
            node.getStyleClass().add(styleClass);
        }
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }

    private record ConnectorVisual(InteractiveCanvasConnector connector, Node node) {
        private ConnectorVisual {
            Objects.requireNonNull(connector, "connector");
            Objects.requireNonNull(node, "node");
        }

        private boolean touchesAny(Set<String> nodeIds) {
            return nodeIds.contains(connector.sourceNodeId()) || nodeIds.contains(connector.targetNodeId());
        }
    }
}
