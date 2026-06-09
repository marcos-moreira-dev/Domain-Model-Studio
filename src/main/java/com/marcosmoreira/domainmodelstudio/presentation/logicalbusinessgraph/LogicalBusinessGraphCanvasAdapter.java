package com.marcosmoreira.domainmodelstudio.presentation.logicalbusinessgraph;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNode;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBendPointEditingSupport;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBounds;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasConnectorSelectionHitPolicy;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasContentBoundsCalculator;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasDirtyState;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasElementIdCodec;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasProjectStylePort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasSelectionClipboardPort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasSelectionSupport;
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
 * Adaptador del Grafo lógico del negocio hacia el lienzo interactivo común.
 *
 * <p>Traduce nodos {@code MF/FL/CU/ACC/...} y relaciones semánticas a tarjetas y conectores
 * interactivos. También transforma selección, movimiento y estilo visual en comandos que el
 * ViewModel puede persistir dentro del {@code DiagramProject}.</p>
 *
 * <p>El adaptador no convierte el Grafo lógico en grafo libre: reutiliza infraestructura de canvas. El documento semántico sigue siendo {@code LogicalBusinessGraphDocument}.</p>
 */
public final class LogicalBusinessGraphCanvasAdapter implements InteractiveCanvasAdapter, CanvasProjectStylePort, CanvasSelectionClipboardPort {

    private static final String NODE_PREFIX = "logical-business-graph-node:";
    private static final String EDGE_PREFIX = "logical-business-graph-edge:";
    private static final double EXPORT_PADDING = 96.0;

    private final LogicalBusinessGraphViewModel viewModel;
    private final CanvasElementIdCodec idCodec = CanvasElementIdCodec.withPrefixes(NODE_PREFIX, EDGE_PREFIX);
    private final CanvasSelectionSupport selectionSupport = new CanvasSelectionSupport();
    private final CanvasDirtyState dirtyState = new CanvasDirtyState();
    private final CanvasBendPointEditingSupport bendPointSupport = new CanvasBendPointEditingSupport(selectionSupport, dirtyState);
    private final CanvasContentBoundsCalculator boundsCalculator = new CanvasContentBoundsCalculator(EXPORT_PADDING, 980.0, 680.0);

    public LogicalBusinessGraphCanvasAdapter(LogicalBusinessGraphViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
    }

    @Override
    public DiagramTypeId diagramTypeId() {
        return DiagramTypeId.LOGICAL_BUSINESS_GRAPH;
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
    public List<InteractiveCanvasNode> nodes() {
        List<InteractiveCanvasNode> result = new ArrayList<>();
        for (LogicalBusinessGraphNode node : viewModel.nodes()) {
            result.add(new InteractiveCanvasNode(
                    nodeLayoutId(node.code()),
                    node.code() + " — " + node.title(),
                    subtitleFor(node),
                    "logical-business-graph-node-" + node.kind().prefix().toLowerCase(),
                    true,
                    false));
        }
        return result;
    }

    @Override
    public List<InteractiveCanvasConnector> connectors() {
        List<InteractiveCanvasConnector> result = new ArrayList<>();
        for (LogicalBusinessGraphEdge edge : viewModel.edges()) {
            result.add(new InteractiveCanvasConnector(
                    edgeLayoutId(edge.id()),
                    nodeLayoutId(edge.sourceCode()),
                    nodeLayoutId(edge.targetCode()),
                    edge.label(),
                    "logical-business-graph-edge-" + edge.relationKind().code().replace('_', '-'),
                    true));
        }
        return result;
    }

    @Override
    public Optional<NodeLayout> layoutForNode(String elementId) {
        String code = rawIdAfterPrefix(elementId, NODE_PREFIX).toUpperCase();
        if (code.isBlank()) {
            return Optional.empty();
        }
        return viewModel.nodes().stream()
                .filter(node -> node.code().equals(code))
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
        String code = rawIdAfterPrefix(elementId, NODE_PREFIX).toUpperCase();
        if (code.isBlank()) {
            return;
        }
        selectionSupport.selectNode(nodeLayoutId(code), additive);
        rememberSelection();
        viewModel.selectNodeByCode(code);
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
        Set<String> selectedNodes = new LinkedHashSet<>(additive ? selectionSupport.current().selectedNodeIds() : Set.of());
        Set<String> selectedConnectors = new LinkedHashSet<>(additive ? selectionSupport.current().selectedConnectorIds() : Set.of());
        for (LogicalBusinessGraphNode node : viewModel.nodes()) {
            NodeLayout layout = viewModel.layoutForNode(node);
            if (CanvasBounds.from(layout).intersects(selectionBounds)) {
                selectedNodes.add(nodeLayoutId(node.code()));
            }
        }
        for (LogicalBusinessGraphEdge edge : viewModel.edges()) {
            String source = nodeLayoutId(edge.sourceCode());
            String target = nodeLayoutId(edge.targetCode());
            String connectorId = edgeLayoutId(edge.id());
            if ((selectedNodes.contains(source) && selectedNodes.contains(target))
                    || connectorRouteTouches(connectorId, source, target, selectionBounds)) {
                selectedConnectors.add(connectorId);
            }
        }
        selectionSupport.selectNodesAndConnectors(selectedNodes, selectedConnectors);
        rememberSelection();
        selectedNodes.stream().findFirst().map(id -> rawIdAfterPrefix(id, NODE_PREFIX)).ifPresent(viewModel::selectNodeByCode);
    }

    private boolean connectorRouteTouches(String connectorId, String sourceNodeId, String targetNodeId, CanvasBounds bounds) {
        return CanvasConnectorSelectionHitPolicy.routeTouches(
                bounds,
                layoutForNode(sourceNodeId),
                layoutForNode(targetNodeId),
                layoutForConnector(connectorId),
                com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade.defaults());
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
        String code = rawIdAfterPrefix(elementId, NODE_PREFIX).toUpperCase();
        if (!code.isBlank()) {
            viewModel.moveNodeTo(code, x, y);
            markDirty();
        }
    }

    @Override
    public void moveSelectedNodesBy(double deltaX, double deltaY) {
        for (String visualNodeId : selectionSupport.current().selectedNodeIds()) {
            String code = rawIdAfterPrefix(visualNodeId, NODE_PREFIX).toUpperCase();
            layoutForNode(visualNodeId).ifPresent(layout -> viewModel.moveNodeTo(code, layout.x() + deltaX, layout.y() + deltaY));
        }
        markDirty();
    }

    @Override
    public void addBendPoint(String connectorId, double x, double y) {
        // Los puntos intermedios editables quedan para la tanda de propiedades/exportación avanzada.
    }

    @Override
    public void moveBendPoint(String connectorId, int index, double x, double y) {
        // No-op intencional.
    }

    @Override
    public void selectBendPoint(String connectorId, int index) {
        bendPointSupport.selectBendPoint(normalize(connectorId), index);
    }

    @Override
    public void removeSelectedBendPoint() {
        bendPointSupport.clearAfterRemoval();
    }

    @Override
    public boolean supportsBendPoints() {
        return false;
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
        for (LogicalBusinessGraphNode node : viewModel.nodes()) {
            layouts.add(viewModel.layoutForNode(node));
        }
        return boundsCalculator.fromNodeLayouts(layouts);
    }

    private void syncSelectionFromViewModel() {
        Optional<SelectedBendPoint> ignored = selectionSupport.current().selectedBendPoint();
        if (ignored.isPresent()) {
            return;
        }
        if (viewModel.selectedNodeProperty().get() != null) {
            String selectedId = nodeLayoutId(viewModel.selectedNodeProperty().get().code());
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

    static String nodeLayoutId(String code) {
        return VisualElementLayoutIds.logicalBusinessGraphNode(code).value();
    }

    static String edgeLayoutId(String edgeId) {
        return VisualElementLayoutIds.logicalBusinessGraphEdge(edgeId).value();
    }

    private String rawIdAfterPrefix(String value, String prefix) {
        return idCodec.rawIdAfterPrefix(value, prefix);
    }

    private String normalize(String value) {
        return idCodec.normalize(value);
    }

    private static String subtitleFor(LogicalBusinessGraphNode node) {
        String description = node.description() == null ? "" : node.description().replace('\n', ' ').strip();
        String legend = node.kind().prefix() + " = " + node.kind().displayName() + " · " + node.status().displayName();
        if (description.isBlank()) {
            return legend;
        }
        String text = legend + " · " + description;
        return text.length() <= 130 ? text : text.substring(0, 129) + "…";
    }
}
