package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas.ZoomableDiagramSurface;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.DiagramInteractionProfile;
import java.util.Objects;
import java.util.Set;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Instala la interacción de nodos del canvas común.
 *
 * <p>La superficie renderiza; este coordinador decide cómo se comporta un nodo ante
 * clic, arrastre, previsualización viva y commit final del layout.</p>
 */
public final class CanvasNodeInteractionCoordinator {

    private final InteractiveCanvasAdapter adapter;
    private final DiagramInteractionProfile interactionProfile;
    private final ZoomableDiagramSurface surface;
    private final CanvasNodeDragController nodeDragController;
    private final CanvasNodeVisualRegistry visualRegistry;
    private final CanvasConnectorDragPreviewLayer connectorPreviewLayer;
    private final CanvasConnectorVisualRegistry connectorVisualRegistry;
    private final DiagramDrawingFacade drawingFacade;
    private final CanvasPointMapper canvasPointMapper;
    private final Runnable refreshPreservingViewport;

    public CanvasNodeInteractionCoordinator(
            InteractiveCanvasAdapter adapter,
            DiagramInteractionProfile interactionProfile,
            ZoomableDiagramSurface surface,
            CanvasNodeDragController nodeDragController,
            CanvasNodeVisualRegistry visualRegistry,
            CanvasConnectorDragPreviewLayer connectorPreviewLayer,
            CanvasConnectorVisualRegistry connectorVisualRegistry,
            DiagramDrawingFacade drawingFacade,
            CanvasPointMapper canvasPointMapper,
            Runnable refreshPreservingViewport
    ) {
        this.adapter = Objects.requireNonNull(adapter, "El adaptador no puede ser null");
        this.interactionProfile = Objects.requireNonNull(interactionProfile, "El perfil de interacción no puede ser null");
        this.surface = Objects.requireNonNull(surface, "La superficie no puede ser null");
        this.nodeDragController = Objects.requireNonNull(nodeDragController, "El controlador de arrastre no puede ser null");
        this.visualRegistry = Objects.requireNonNull(visualRegistry, "El registro visual no puede ser null");
        this.connectorPreviewLayer = Objects.requireNonNull(connectorPreviewLayer, "La previsualización de conectores no puede ser null");
        this.connectorVisualRegistry = Objects.requireNonNull(connectorVisualRegistry, "El registro visual de conectores no puede ser null");
        this.drawingFacade = Objects.requireNonNull(drawingFacade, "La fachada de dibujo no puede ser null");
        this.canvasPointMapper = Objects.requireNonNull(canvasPointMapper, "El mapeador de coordenadas no puede ser null");
        this.refreshPreservingViewport = Objects.requireNonNull(refreshPreservingViewport, "El refresco no puede ser null");
    }

    public void install(InteractiveCanvasNode node, Node rendered) {
        // Nodos normales: equivalente a setPickOnBounds(true).
        // Contenedores transparentes handle-only: solo la banda de título captura drag.
        rendered.setPickOnBounds(!handleOnlyDrag(rendered));
        // Contrato de gesto: if (moved) se previsualiza en JavaFX y se confirma
        // una sola vez al soltar; si no hubo movimiento, la selección local queda estable.
        NodeDragGestureState state = new NodeDragGestureState();
        rendered.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> handleNodePressed(node, rendered, state, event));
        rendered.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> handleNodeDragged(node, rendered, state, event));
        rendered.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> handleNodeReleased(node, rendered, state, event));
        rendered.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                event.consume();
            }
        });
    }

    private void handleNodePressed(InteractiveCanvasNode node, Node rendered, NodeDragGestureState state, MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY) {
            return;
        }
        surface.root().requestFocus();
        Point2D canvasPoint = canvasPointMapper.toCanvasPoint(event.getSceneX(), event.getSceneY());
        // Si el nodo ya pertenece a una selección múltiple, no la destruimos al
        // iniciar el drag: el gesto debe arrastrar todo el grupo seleccionado.
        boolean preserveSelectionGroup = shouldPreserveSelectionGroup(node.id(), event.isShiftDown());
        if (!preserveSelectionGroup) {
            adapter.selectNode(node.id(), event.isShiftDown());
        }
        boolean additiveOrGroup = event.isShiftDown() || preserveSelectionGroup;
        visualRegistry.markSelectedNodeLocally(node.id(), event.isShiftDown() || preserveSelectionGroup);
        visualRegistry.markActiveNodeLocally(node.id(), additiveOrGroup);
        if (!interactionProfile.supportsNodeDragging()) {
            refreshPreservingViewport.run();
            event.consume();
            return;
        }
        beginNodeDrag(node, rendered, state, canvasPoint, event.isShiftDown());
        event.consume();
    }


    private boolean shouldPreserveSelectionGroup(String nodeId, boolean additiveGesture) {
        if (additiveGesture) {
            return false;
        }
        Set<String> selectedNodeIds = adapter.selection().selectedNodeIds();
        return selectedNodeIds.size() > 1 && selectedNodeIds.contains(nodeId);
    }

    private void beginNodeDrag(
            InteractiveCanvasNode node,
            Node rendered,
            NodeDragGestureState state,
            Point2D canvasPoint,
            boolean additive
    ) {
        nodeDragController.begin(node.id(), canvasPoint.getX(), canvasPoint.getY(), additive);
        state.dragActive = true;
        state.dragStart = canvasPoint;
        state.dragPreviewAnchor[0] = canvasPoint;
        state.dragLayoutAtStart = adapter.layoutForNode(node.id()).orElse(null);
        state.dragTotal[0] = 0.0;
        state.dragTotal[1] = 0.0;
        // Tanda 45: el arrastre debe actualizar el layout real durante el gesto.
        // Esto mantiene nodos, flechas, labels y selección múltiple sincronizados
        // en tiempo real; la previsualización por translate queda solo como legado.
        state.livePreviewActive = false;
        visualRegistry.clearDragPreviewTranslations(node.id(), rendered);
    }

    private void handleNodeDragged(InteractiveCanvasNode node, Node rendered, NodeDragGestureState state, MouseEvent event) {
        if (!event.isPrimaryButtonDown() || !interactionProfile.supportsNodeDragging() || !state.dragActive) {
            return;
        }
        Point2D canvasPoint = canvasPointMapper.toCanvasPoint(event.getSceneX(), event.getSceneY());
        state.dragTotal[0] = canvasPoint.getX() - state.dragStart.getX();
        state.dragTotal[1] = canvasPoint.getY() - state.dragStart.getY();
        nodeDragController.dragTo(canvasPoint.getX(), canvasPoint.getY());
        // Mantener el nodo JavaFX vivo durante el gesto: reconstruir el canvas en
        // cada pixel puede cortar el drag tras el primer movimiento. El layout ya
        // se actualiza en tiempo real; aquí solo desplazamos la vista actual y el
        // refresh completo queda para MOUSE_RELEASED.
        visualRegistry.previewNodeDrag(node.id(), rendered, state.dragPreviewAnchor, canvasPoint);
        updateLiveConnectorRoutesDuringNodeDrag();
        connectorPreviewLayer.clear();
        event.consume();
    }

    private void updateLiveConnectorRoutesDuringNodeDrag() {
        connectorVisualRegistry.updateLiveRoutesForMovedNodes(
                adapter.selection().selectedNodeIds(),
                adapter,
                drawingFacade
        );
    }

    private void handleNodeReleased(InteractiveCanvasNode node, Node rendered, NodeDragGestureState state, MouseEvent event) {
        if (!state.dragActive) {
            return;
        }
        state.dragActive = false;
        nodeDragController.end();
        if (state.livePreviewActive && adapter instanceof CanvasLivePreviewPort livePreviewPort) {
            finishLivePreview(state, livePreviewPort);
        } else {
            finishStandardDrag(node, rendered, state);
        }
        event.consume();
    }

    private void finishLivePreview(NodeDragGestureState state, CanvasLivePreviewPort livePreviewPort) {
        if (state.moved()) {
            livePreviewPort.commitPreview();
            adapter.markDirty();
        } else {
            livePreviewPort.cancelPreview();
        }
        state.livePreviewActive = false;
        connectorPreviewLayer.clear();
    }

    private void finishStandardDrag(InteractiveCanvasNode node, Node rendered, NodeDragGestureState state) {
        if (state.moved()) {
            // El layout ya fue actualizado incrementalmente durante MOUSE_DRAGGED.
            // En release solo limpiamos cualquier residuo visual y dejamos estable
            // la selección/movimiento de grupo sin aplicar el delta por segunda vez.
            visualRegistry.resetDragPreviewTranslations(node.id(), rendered);
            connectorPreviewLayer.clear();
            refreshPreservingViewport.run();
            return;
        }
        visualRegistry.resetDragPreviewTranslations(node.id(), rendered);
        connectorPreviewLayer.clear();
        // Clic sin movimiento: no reconstruimos el canvas; la selección semántica ya
        // quedó actualizada y la marca local permanece estable sin parpadeo.
    }

    private void commitNodeMove(String draggedNodeId, NodeLayout layoutAtStart, double[] dragTotal) {
        Set<String> selectedNodeIds = adapter.selection().selectedNodeIds();
        if (selectedNodeIds.contains(draggedNodeId)) {
            adapter.moveSelectedNodesBy(dragTotal[0], dragTotal[1]);
            return;
        }
        moveDraggedNodeOrSelection(draggedNodeId, layoutAtStart, dragTotal[0], dragTotal[1]);
    }

    private void moveDraggedNodeOrSelection(String draggedNodeId, NodeLayout layoutAtStart, double deltaX, double deltaY) {
        Set<String> selected = adapter.selection().selectedNodeIds();
        if (selected.contains(draggedNodeId)) {
            adapter.moveSelectedNodesBy(deltaX, deltaY);
            return;
        }
        if (layoutAtStart != null) {
            adapter.moveNode(draggedNodeId, layoutAtStart.x() + deltaX, layoutAtStart.y() + deltaY);
            return;
        }
        adapter.moveSelectedNodesBy(deltaX, deltaY);
    }

    private boolean beginLivePreviewIfSupported(String nodeId, Point2D canvasPoint) {
        if (!(adapter instanceof CanvasLivePreviewPort livePreviewPort) || !livePreviewPort.supportsLivePreview()) {
            return false;
        }
        livePreviewPort.beginPreview(nodeId, canvasPoint.getX(), canvasPoint.getY());
        return true;
    }

    private static boolean handleOnlyDrag(Node rendered) {
        return rendered != null && rendered.getStyleClass().contains("interactive-canvas-handle-only-node");
    }

    private static final class NodeDragGestureState {
        private Point2D dragStart;
        private final Point2D[] dragPreviewAnchor = new Point2D[1];
        private NodeLayout dragLayoutAtStart;
        private final double[] dragTotal = new double[2];
        private boolean dragActive;
        private boolean livePreviewActive;

        private boolean moved() {
            return Math.abs(dragTotal[0]) >= 0.5 || Math.abs(dragTotal[1]) >= 0.5;
        }

    }
}
