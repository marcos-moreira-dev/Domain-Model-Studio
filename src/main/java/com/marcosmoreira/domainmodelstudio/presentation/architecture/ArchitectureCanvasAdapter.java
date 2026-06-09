package com.marcosmoreira.domainmodelstudio.presentation.architecture;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdge;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNode;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasAdapterInteractionState;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasConnectorLabelPort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasConnectorSelectionHitPolicy;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasProjectStylePort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBounds;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasAdapter;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasConnector;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasNode;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasSelection;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.SelectedBendPoint;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasSelectionClipboardPort;
import com.marcosmoreira.domainmodelstudio.presentation.visualtransfer.ProjectVisualSelectionTransferService;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Adaptador de C4 Contexto, C4 Contenedores y Despliegue técnico hacia el canvas común.
 *
 * <p>El documento de arquitectura conserva su semántica; las coordenadas, selección,
 * drag y puntos intermedios viven en {@code DiagramLayouts} mediante el ViewModel.</p>
 */
public final class ArchitectureCanvasAdapter implements InteractiveCanvasAdapter, CanvasSelectionClipboardPort, CanvasConnectorLabelPort, CanvasProjectStylePort {

    private static final String NODE_PREFIX = "architecture-node:";
    private static final String EDGE_PREFIX = "architecture-edge:";
    private static final double EXPORT_PADDING = 96.0;

    private final ArchitectureDiagramViewModel viewModel;
    private final CanvasAdapterInteractionState interactionState = new CanvasAdapterInteractionState();

    public ArchitectureCanvasAdapter(ArchitectureDiagramViewModel viewModel) {
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
        return viewModel.currentKind().diagramTypeId();
    }

    @Override
    public List<InteractiveCanvasNode> nodes() {
        List<InteractiveCanvasNode> result = new ArrayList<>();
        for (ArchitectureNode node : viewModel.nodes()) {
            result.add(new InteractiveCanvasNode(
                    nodeLayoutId(node.id()),
                    node.displayName(),
                    subtitleFor(node),
                    cssKind(node.kind().name()),
                    true,
                    false));
        }
        return result;
    }

    @Override
    public List<InteractiveCanvasConnector> connectors() {
        List<InteractiveCanvasConnector> result = new ArrayList<>();
        for (ArchitectureEdge edge : viewModel.edges()) {
            result.add(new InteractiveCanvasConnector(
                    edgeLayoutId(edge.id()),
                    nodeLayoutId(edge.sourceNodeId()),
                    nodeLayoutId(edge.targetNodeId()),
                    labelFor(edge),
                    cssKind(edge.kind().name()),
                    true));
        }
        return result;
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
        return interactionState.selection();
    }

    @Override
    public void selectNode(String elementId, boolean additive) {
        String nodeId = rawIdAfterPrefix(elementId, NODE_PREFIX);
        if (nodeId.isBlank()) {
            return;
        }
        if (additive) {
            interactionState.selectNode(nodeLayoutId(nodeId), true);
        } else {
            interactionState.selectNode(nodeLayoutId(nodeId), false);
        }
        viewModel.selectNodeById(nodeId);
    }

    @Override
    public void selectConnector(String connectorId, boolean additive) {
        String normalized = normalize(connectorId);
        if (!normalized.startsWith(EDGE_PREFIX)) {
            return;
        }
        interactionState.selectConnector(normalized);
        viewModel.selectEdgeById(rawIdAfterPrefix(normalized, EDGE_PREFIX));
    }

    @Override
    public void selectNodesInside(CanvasBounds selectionBounds, boolean additive) {
        selectElementsInside(selectionBounds, additive);
    }

    @Override
    public void selectElementsInside(CanvasBounds selectionBounds, boolean additive) {
        Objects.requireNonNull(selectionBounds, "selectionBounds");
        Set<String> selectedNodes = new LinkedHashSet<>(additive ? interactionState.selectedNodeIds() : Set.of());
        Set<String> selectedConnectors = new LinkedHashSet<>(additive ? interactionState.selectedConnectorIds() : Set.of());
        for (ArchitectureNode node : viewModel.nodes()) {
            NodeLayout layout = viewModel.layoutForNode(node);
            if (CanvasBounds.from(layout).intersects(selectionBounds)) {
                selectedNodes.add(nodeLayoutId(node.id()));
            }
        }
        for (ArchitectureEdge edge : viewModel.edges()) {
            String source = nodeLayoutId(edge.sourceNodeId());
            String target = nodeLayoutId(edge.targetNodeId());
            String connectorId = edgeLayoutId(edge.id());
            if ((selectedNodes.contains(source) && selectedNodes.contains(target))
                    || connectorHasBendPointInside(connectorId, selectionBounds)
                    || connectorRouteTouches(connectorId, source, target, selectionBounds)) {
                selectedConnectors.add(connectorId);
            }
        }
        interactionState.selectNodesAndConnectors(selectedNodes, selectedConnectors);
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

    @Override
    public void clearSelection() {
        interactionState.clearSelection();
        viewModel.clearPropertySelection();
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
        for (String nodeId : interactionState.selectedNodeIds()) {
            layoutForNode(nodeId).ifPresent(layout -> moveNode(nodeId, layout.x() + deltaX, layout.y() + deltaY));
        }
        markDirty();
    }

    @Override
    public void moveSelectedConnectorBendPointsBy(double deltaX, double deltaY) {
        for (String connectorId : interactionState.selectedConnectorIds()) {
            ConnectorLayout layout = layoutForConnector(connectorId).orElse(null);
            if (layout == null || layout.bendPoints().isEmpty()) {
                continue;
            }
            for (int index = 0; index < layout.bendPoints().size(); index++) {
                var point = layout.bendPoints().get(index);
                viewModel.moveConnectorBendPointTo(DiagramElementId.of(connectorId), index, point.x() + deltaX, point.y() + deltaY);
            }
        }
        markDirty();
    }

    @Override
    public void addBendPoint(String connectorId, double x, double y) {
        String normalized = normalize(connectorId);
        viewModel.clearPropertySelection();
        viewModel.addConnectorBendPoint(DiagramElementId.of(normalized), x, y)
                .ifPresent(index -> interactionState.selectBendPoint(normalized, index));
        markDirty();
    }

    @Override
    public void moveBendPoint(String connectorId, int index, double x, double y) {
        String normalized = normalize(connectorId);
        viewModel.clearPropertySelection();
        viewModel.moveConnectorBendPointTo(DiagramElementId.of(normalized), index, x, y);
        interactionState.markEditedBendPoint(normalized, index);
    }

    @Override
    public void selectBendPoint(String connectorId, int index) {
        viewModel.clearPropertySelection();
        interactionState.selectBendPoint(normalize(connectorId), index);
    }


    @Override
    public void moveConnectorLabelBy(String connectorId, double deltaX, double deltaY) {
        String normalized = normalize(connectorId);
        if (!normalized.startsWith(EDGE_PREFIX)) {
            return;
        }
        viewModel.clearPropertySelection();
        viewModel.moveConnectorLabelBy(DiagramElementId.of(normalized), deltaX, deltaY);
        interactionState.selectConnector(normalized);
        markDirty();
    }

    @Override
    public void removeSelectedBendPoint() {
        Optional<SelectedBendPoint> selectedBendPoint = interactionState.selectedBendPoint();
        if (selectedBendPoint.isEmpty()) {
            return;
        }
        SelectedBendPoint point = selectedBendPoint.get();
        viewModel.removeConnectorBendPoint(DiagramElementId.of(point.connectorId()), point.index());
        interactionState.clearSelection();
        markDirty();
    }

    @Override
    public void markDirty() {
        interactionState.markDirty();
    }

    public boolean dirty() {
        return interactionState.dirty();
    }

    @Override
    public CanvasBounds contentBounds() {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = 0.0;
        double maxY = 0.0;
        for (ArchitectureNode node : viewModel.nodes()) {
            NodeLayout layout = viewModel.layoutForNode(node);
            minX = Math.min(minX, layout.x());
            minY = Math.min(minY, layout.y());
            maxX = Math.max(maxX, layout.x() + layout.width());
            maxY = Math.max(maxY, layout.y() + layout.height());
        }
        if (minX == Double.MAX_VALUE) {
            return CanvasBounds.of(0, 0, 900, 620);
        }
        return CanvasBounds.of(
                Math.max(0.0, minX - EXPORT_PADDING),
                Math.max(0.0, minY - EXPORT_PADDING),
                Math.max(900.0, maxX - minX + EXPORT_PADDING * 2.0),
                Math.max(620.0, maxY - minY + EXPORT_PADDING * 2.0));
    }

    private void syncSelectionFromViewModel() {
        if (interactionState.shouldPreserveManualBendPointSelection()) {
            return;
        }
        if (viewModel.selectedNodeProperty().get() != null) {
            String selectedId = nodeLayoutId(viewModel.selectedNodeProperty().get().id());
            interactionState.syncSingleNode(selectedId);
            return;
        }
        if (viewModel.selectedEdgeProperty().get() != null) {
            String selectedId = edgeLayoutId(viewModel.selectedEdgeProperty().get().id());
            interactionState.syncSingleConnector(selectedId);
            return;
        }
        interactionState.clearIfNoBendPoint();
    }

    private static String subtitleFor(ArchitectureNode node) {
        String base = node.kind().displayName();
        String detail = firstNonBlank(node.technology(), node.environment(), node.owner(), node.description(), node.notes());
        return detail.isBlank() ? base : base + "\n" + detail;
    }

    private static String labelFor(ArchitectureEdge edge) {
        return firstNonBlank(edge.label(), edge.protocol(), edge.kind().displayName());
    }

    static String nodeLayoutId(String nodeId) {
        return VisualElementLayoutIds.architectureNode(nodeId).value();
    }

    static String edgeLayoutId(String edgeId) {
        return VisualElementLayoutIds.architectureEdge(edgeId).value();
    }

    private static String rawIdAfterPrefix(String value, String prefix) {
        String normalized = normalize(value);
        return normalized.startsWith(prefix) ? normalized.substring(prefix.length()) : "";
    }

    private static String cssKind(String value) {
        return normalize(value).toLowerCase(java.util.Locale.ROOT).replace('_', '-');
    }

    private static String firstNonBlank(String... values) {
        if (values == null) {
            return "";
        }
        for (String value : values) {
            String normalized = normalize(value);
            if (!normalized.isBlank()) {
                return normalized;
            }
        }
        return "";
    }

    private static boolean contains(CanvasBounds bounds, double x, double y) {
        return x >= bounds.x() && x <= bounds.x() + bounds.width()
                && y >= bounds.y() && y <= bounds.y() + bounds.height();
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }

    @Override
    public boolean copySelectionToClipboard() {
        return ProjectVisualSelectionTransferService.copySelectionToClipboard(
                viewModel.currentProject(),
                diagramTypeId(),
                selection(),
                connectors(),
                this::layoutForNode,
                this::layoutForConnector);
    }

    @Override
    public boolean pasteSelectionFromClipboard() {
        return ProjectVisualSelectionTransferService.pasteSelectionFromClipboard(viewModel.currentProject())
                .map(result -> {
                    viewModel.patchCurrentProject(ignored -> result.project(), result.message());
                    markDirty();
                    return true;
                })
                .orElse(false);
    }

}
