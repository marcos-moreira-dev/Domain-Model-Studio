package com.marcosmoreira.domainmodelstudio.presentation.freegraph;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdgeDirection;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphNode;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBackgroundClickPort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBendPointEditingSupport;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBounds;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasConnectorLabelPort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasConnectorSelectionHitPolicy;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasContentBoundsCalculator;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasDirtyState;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasDragPreviewPort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasElementIdCodec;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasLivePreviewPort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasProjectStylePort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasSelectionSupport;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasSelectionClipboardPort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasAdapter;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasConnector;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasNode;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasSelection;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.SelectedBendPoint;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
/**
 * Adaptador del Grafo libre hacia el lienzo interactivo común.
 *
 * <p>Traduce nodos/relaciones semánticas a primitivas visuales y delega la creación
 * por clic al ViewModel de Grafo libre.</p>
 */
public final class FreeGraphCanvasAdapter implements InteractiveCanvasAdapter, CanvasConnectorLabelPort, CanvasProjectStylePort, CanvasBackgroundClickPort, CanvasDragPreviewPort, CanvasLivePreviewPort, CanvasSelectionClipboardPort {
    private static final String NODE_PREFIX = "free-graph-node:";
    private static final String EDGE_PREFIX = "free-graph-edge:";
    private static final double EXPORT_PADDING = 96.0;
    private final FreeGraphViewModel viewModel;
    private final CanvasElementIdCodec idCodec = CanvasElementIdCodec.withPrefixes(NODE_PREFIX, EDGE_PREFIX);
    private final CanvasSelectionSupport selectionSupport = new CanvasSelectionSupport();
    private final CanvasDirtyState dirtyState = new CanvasDirtyState();
    private final CanvasBendPointEditingSupport bendPointSupport = new CanvasBendPointEditingSupport(
            selectionSupport,
            dirtyState);
    private final CanvasContentBoundsCalculator boundsCalculator = new CanvasContentBoundsCalculator(
            EXPORT_PADDING,
            880.0,
            600.0);
    private final FreeGraphLivePreviewController livePreviewController = new FreeGraphLivePreviewController();
    // Guardarraíl fuente post-refactor: livePreviewNodeIds vive en FreeGraphLivePreviewController.

    public FreeGraphCanvasAdapter(FreeGraphViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
    }

    @Override
    public DiagramProject currentStyleProject() {
        return viewModel.currentProject();
    }

    @Override
    public void patchStyleProject(java.util.function.UnaryOperator<DiagramProject> patch, String statusMessage) {
        viewModel.patchCurrentProject(patch, statusMessage);
        markDirty();
    }

    @Override
    public DiagramTypeId diagramTypeId() {
        return DiagramTypeId.FREE_GRAPH;
    }

    @Override
    public List<InteractiveCanvasNode> nodes() {
        List<InteractiveCanvasNode> result = new ArrayList<>();
        for (FreeGraphNode node : viewModel.nodes()) {
            result.add(new InteractiveCanvasNode(
                    nodeLayoutId(node.id()),
                    node.title(),
                    subtitleFor(node),
                    "free-graph-node",
                    true,
                    false));
        }
        return result;
    }

    @Override
    public List<InteractiveCanvasConnector> connectors() {
        List<InteractiveCanvasConnector> result = new ArrayList<>();
        for (FreeGraphEdge edge : viewModel.edges()) {
            result.add(new InteractiveCanvasConnector(
                    edgeLayoutId(edge.id()),
                    nodeLayoutId(edge.sourceNodeId()),
                    nodeLayoutId(edge.targetNodeId()),
                    labelFor(edge),
                    edge.direction() == FreeGraphEdgeDirection.UNDIRECTED ? "free-graph-edge-undirected" : "free-graph-edge-directed",
                    true));
        }
        return result;
    }



    private String labelFor(FreeGraphEdge edge) {
        String explicit = normalize(edge.label());
        if (!explicit.isBlank()) {
            return explicit;
        }
        String source = titleForNode(edge.sourceNodeId());
        String target = titleForNode(edge.targetNodeId());
        String separator = edge.direction() == FreeGraphEdgeDirection.UNDIRECTED ? " — " : " → ";
        return source + separator + target;
    }

    private String titleForNode(String nodeId) {
        return viewModel.nodes().stream()
                .filter(node -> node.id().equals(nodeId))
                .map(FreeGraphNode::title)
                .map(this::normalize)
                .filter(title -> !title.isBlank())
                .findFirst()
                .orElse(nodeId == null ? "" : nodeId);
    }

    @Override
    public Optional<NodeLayout> layoutForNode(String elementId) {
        String rawNodeId = rawIdAfterPrefix(elementId, NODE_PREFIX);
        if (rawNodeId.isBlank()) {
            return Optional.empty();
        }
        return viewModel.nodes().stream()
                .filter(node -> node.id().equals(rawNodeId))
                .findFirst()
                .map(viewModel::layoutForNode);
    }

    @Override
    public Optional<ConnectorLayout> layoutForConnector(String connectorId) {
        String normalized = normalize(connectorId);
        if (!normalized.startsWith(EDGE_PREFIX)) {
            return Optional.empty();
        }
        return viewModel.layoutForConnector(DiagramElementId.of(normalized));
    }

    @Override
    public InteractiveCanvasSelection selection() {
        syncSelectionFromViewModel();
        return selectionSupport.current();
    }

    @Override
    public void selectNode(String elementId, boolean additive) {
        String nodeId = rawIdAfterPrefix(elementId, NODE_PREFIX);
        if (nodeId.isBlank()) {
            return;
        }
        selectionSupport.selectNode(nodeLayoutId(nodeId), additive);
        rememberSelection();
        viewModel.selectNodeById(nodeId);
        if (viewModel.handleNodeCanvasClick(nodeId)) {
            markDirty();
        }
    }

    @Override
    public void selectConnector(String connectorId, boolean additive) {
        String normalized = normalize(connectorId);
        if (!normalized.startsWith(EDGE_PREFIX)) {
            return;
        }
        selectionSupport.selectConnector(normalized);
        rememberSelection();
        viewModel.selectEdgeById(rawIdAfterPrefix(normalized, EDGE_PREFIX));
    }

    @Override
    public void selectNodesInside(CanvasBounds selectionBounds, boolean additive) {
        selectElementsInside(selectionBounds, additive);
    }

    @Override
    public void selectElementsInside(CanvasBounds selectionBounds, boolean additive) {
        Objects.requireNonNull(selectionBounds, "selectionBounds");
        Set<String> selectedNodes = new LinkedHashSet<>(
                additive ? selectionSupport.current().selectedNodeIds() : Set.of());
        Set<String> selectedConnectors = new LinkedHashSet<>(
                additive ? selectionSupport.current().selectedConnectorIds() : Set.of());
        for (FreeGraphNode node : viewModel.nodes()) {
            NodeLayout layout = viewModel.layoutForNode(node);
            if (CanvasBounds.from(layout).intersects(selectionBounds)) {
                selectedNodes.add(nodeLayoutId(node.id()));
            }
        }
        for (FreeGraphEdge edge : viewModel.edges()) {
            String source = nodeLayoutId(edge.sourceNodeId());
            String target = nodeLayoutId(edge.targetNodeId());
            String connectorId = edgeLayoutId(edge.id());
            if ((selectedNodes.contains(source) && selectedNodes.contains(target))
                    || connectorHasBendPointInside(connectorId, selectionBounds)
                    || connectorRouteTouches(connectorId, source, target, selectionBounds)) {
                selectedConnectors.add(connectorId);
            }
        }
        selectionSupport.selectNodesAndConnectors(selectedNodes, selectedConnectors);
        rememberSelection();
        selectedNodes.stream().findFirst().map(id -> rawIdAfterPrefix(id, NODE_PREFIX)).ifPresent(viewModel::selectNodeById);
    }

    private boolean connectorHasBendPointInside(String connectorId, CanvasBounds bounds) {
        return layoutForConnector(connectorId)
                .map(layout -> layout.bendPoints().stream().anyMatch(point -> contains(bounds, point.x(), point.y())))
                .orElse(false);
    }

    private boolean connectorRouteTouches(String connectorId, String sourceNodeId, String targetNodeId, CanvasBounds bounds) {
        return CanvasConnectorSelectionHitPolicy.routeTouches(
                bounds,
                layoutForNode(sourceNodeId),
                layoutForNode(targetNodeId),
                layoutForConnector(connectorId),
                com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade.defaults());
    }

    private static boolean contains(CanvasBounds bounds, double x, double y) {
        return bounds != null && bounds.contains(x, y);
    }

    @Override
    public void clearSelection() {
        selectionSupport.clear();
        rememberSelection();
        viewModel.clearPropertySelection();
    }

    @Override
    public boolean copySelectionToClipboard() {
        syncSelectionFromViewModel();
        return viewModel.copySelectionToClipboard(selectionSupport.current());
    }

    @Override
    public boolean pasteSelectionFromClipboard() {
        return viewModel.pasteSelectionFromClipboard();
    }

    @Override
    public void moveNode(String elementId, double x, double y) {
        String nodeId = rawIdAfterPrefix(elementId, NODE_PREFIX);
        if (!nodeId.isBlank()) {
            viewModel.moveNodeTo(nodeId, x, y);
            markDirty();
        }
    }

    @Override
    public void moveSelectedNodesBy(double deltaX, double deltaY) {
        moveSelectedNodesBy(deltaX, deltaY, true);
    }

    private void moveSelectedNodesBy(double deltaX, double deltaY, boolean announce) {
        List<NodeMoveTarget> targets = new ArrayList<>();
        for (String visualNodeId : movableNodeIdsForCurrentGesture()) {
            String rawNodeId = rawIdAfterPrefix(visualNodeId, NODE_PREFIX);
            layoutForNode(visualNodeId).ifPresent(layout -> targets.add(new NodeMoveTarget(
                    rawNodeId,
                    layout.x() + deltaX,
                    layout.y() + deltaY)));
        }
        for (NodeMoveTarget target : targets) {
            viewModel.moveNodeTo(target.nodeId(), target.x(), target.y(), announce);
        }
        markDirty();
    }

    private Set<String> movableNodeIdsForCurrentGesture() {
        return livePreviewController.movableNodeIds(
                selectionSupport.current().selectedNodeIds(),
                this::previewNodeIdsForDraggedNode);
    }

    @Override
    public Set<String> previewNodeIdsForDraggedNode(String draggedNodeId, Set<String> selectedNodeIds) {
        Set<String> selected = selectedNodeIds == null ? Set.of() : selectedNodeIds;
        String normalizedDraggedNodeId = normalize(draggedNodeId);
        if (selected.isEmpty()) {
            return normalizedDraggedNodeId.isBlank() ? Set.of() : Set.of(normalizedDraggedNodeId);
        }
        if (normalizedDraggedNodeId.isBlank() || selected.contains(normalizedDraggedNodeId)) {
            return Set.copyOf(selected);
        }
        return Set.of(normalizedDraggedNodeId);
    }

    @Override
    public boolean supportsLivePreview() {
        // El Grafo libre usa ahora previsualización visual diferida del canvas común.
        // Evita reconstruir el lienzo durante cada pixel de arrastre y corrige el caso
        // donde un nodo ya seleccionado dejaba de moverse al iniciar el drag.
        return false;
    }

    @Override
    public void beginPreview(String elementId, double canvasX, double canvasY) {
        livePreviewController.begin(
                elementId, canvasX, canvasY,
                selectionSupport.current().selectedNodeIds(),
                this::previewNodeIdsForDraggedNode);
    }

    @Override
    public void updatePreview(String elementId, double canvasX, double canvasY) {
        livePreviewController.update(
                elementId, canvasX, canvasY,
                selectionSupport.current().selectedNodeIds(),
                this::previewNodeIdsForDraggedNode,
                this::moveSelectedNodesBy);
    }

    @Override
    public void commitPreview() {
        livePreviewController.clear();
    }

    @Override
    public void cancelPreview() {
        livePreviewController.clear();
    }

    @Override
    public void addBendPoint(String connectorId, double x, double y) {
        String normalized = normalize(connectorId);
        if (!normalized.startsWith(EDGE_PREFIX)) {
            return;
        }
        viewModel.clearPropertySelection();
        viewModel.addConnectorBendPoint(DiagramElementId.of(normalized), x, y)
                .ifPresent(index -> bendPointSupport.markEditedBendPoint(normalized, index));
        markDirty();
    }

    @Override
    public void moveBendPoint(String connectorId, int index, double x, double y) {
        String normalized = normalize(connectorId);
        if (!normalized.startsWith(EDGE_PREFIX)) {
            return;
        }
        viewModel.clearPropertySelection();
        viewModel.moveConnectorBendPointTo(DiagramElementId.of(normalized), index, x, y);
        bendPointSupport.markEditedBendPoint(normalized, index);
        markDirty();
    }

    @Override
    public void selectBendPoint(String connectorId, int index) {
        viewModel.clearPropertySelection();
        bendPointSupport.selectBendPoint(normalize(connectorId), index);
    }

    @Override
    public void removeSelectedBendPoint() {
        Optional<SelectedBendPoint> selectedBendPoint = selectionSupport.current().selectedBendPoint();
        if (selectedBendPoint.isEmpty()) {
            return;
        }
        SelectedBendPoint point = selectedBendPoint.get();
        viewModel.removeConnectorBendPoint(DiagramElementId.of(point.connectorId()), point.index());
        bendPointSupport.clearAfterRemoval();
        markDirty();
    }

    @Override
    public void moveConnectorLabelBy(String connectorId, double deltaX, double deltaY) {
        String normalized = normalize(connectorId);
        if (!normalized.startsWith(EDGE_PREFIX)) {
            return;
        }
        viewModel.clearPropertySelection();
        viewModel.moveConnectorLabelBy(DiagramElementId.of(normalized), deltaX, deltaY);
        selectionSupport.selectConnector(normalized);
        rememberSelection();
        markDirty();
    }

    @Override
    public void markDirty() {
        dirtyState.markDirty();
    }

    public boolean dirty() {
        return dirtyState.dirty();
    }

    @Override
    public CanvasBounds contentBounds() {
        List<NodeLayout> layouts = new ArrayList<>();
        for (FreeGraphNode node : viewModel.nodes()) {
            layouts.add(viewModel.layoutForNode(node));
        }
        return boundsCalculator.fromNodeLayouts(layouts);
    }

    @Override
    public boolean handleBackgroundClick(double x, double y, boolean additive, int clickCount) {
        boolean handled = viewModel.handleBackgroundCanvasClick(x, y, additive, clickCount);
        if (handled) {
            markDirty();
        }
        return handled;
    }

    private void syncSelectionFromViewModel() {
        if (selectionSupport.shouldPreserveManualBendPointSelection()) {
            return;
        }
        if (viewModel.selectedNodeProperty().get() != null) {
            String selectedId = nodeLayoutId(viewModel.selectedNodeProperty().get().id());
            if (!selectionSupport.current().isNodeSelected(selectedId)) {
                selectionSupport.selectNode(selectedId, false);
                rememberSelection();
            }
            return;
        }
        if (viewModel.selectedEdgeProperty().get() != null) {
            String selectedId = edgeLayoutId(viewModel.selectedEdgeProperty().get().id());
            if (!selectionSupport.current().isConnectorSelected(selectedId)) {
                selectionSupport.selectConnector(selectedId);
                rememberSelection();
            }
        }
    }

    private void rememberSelection() {
        viewModel.rememberCanvasSelection(selectionSupport.current());
    }

    private record NodeMoveTarget(String nodeId, double x, double y) {
    }

    private static String subtitleFor(FreeGraphNode node) {
        String content = node.content() == null ? "" : node.content().strip();
        if (content.isBlank()) {
            return "Nodo";
        }
        String oneLine = content.replace('\n', ' ').strip();
        return oneLine.length() <= 96 ? oneLine : oneLine.substring(0, 95) + "…";
    }

    static String nodeLayoutId(String nodeId) {
        return VisualElementLayoutIds.freeGraphNode(nodeId).value();
    }

    static String edgeLayoutId(String edgeId) {
        return VisualElementLayoutIds.freeGraphEdge(edgeId).value();
    }

    private String rawIdAfterPrefix(String value, String prefix) {
        return idCodec.rawIdAfterPrefix(value, prefix);
    }

    private String normalize(String value) {
        return idCodec.normalize(value);
    }
}
