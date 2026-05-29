package com.marcosmoreira.domainmodelstudio.presentation.behavior;

import com.marcosmoreira.domainmodelstudio.application.visual.SequenceTimelineLayoutPolicy;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.SequenceCombinedFragmentSpec;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramSize;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBounds;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasConnectorLabelPort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasAdapterInteractionState;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasProjectStylePort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasResizePort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasAdapter;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasConnector;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasNode;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasSelection;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasSelectionClipboardPort;
import com.marcosmoreira.domainmodelstudio.presentation.visualtransfer.ProjectVisualSelectionTransferService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Adaptador especializado para UML Secuencia.
 *
 * <p>Reutiliza el canvas común para zoom, paneo, selección y movimiento, pero conserva
 * la semántica temporal propia: participantes arriba, líneas de vida y mensajes ordenados
 * verticalmente. El layout persistente guarda principalmente la posición horizontal de los
 * participantes; las coordenadas temporales se derivan del orden de las relaciones.</p>
 */
public final class SequenceCanvasAdapter implements InteractiveCanvasAdapter, CanvasConnectorLabelPort, CanvasProjectStylePort, CanvasResizePort, CanvasSelectionClipboardPort {

    static final double PARTICIPANT_TOP_Y = SequenceTimelineLayoutPolicy.PARTICIPANT_TOP_Y;
    static final double PARTICIPANT_WIDTH = SequenceTimelineLayoutPolicy.PARTICIPANT_WIDTH;
    static final double PARTICIPANT_HEIGHT = SequenceTimelineLayoutPolicy.PARTICIPANT_HEIGHT;
    static final double PARTICIPANT_GAP = SequenceTimelineLayoutPolicy.PARTICIPANT_GAP;
    static final double MESSAGE_START_Y = SequenceTimelineLayoutPolicy.MESSAGE_START_Y;
    static final double MESSAGE_ROW_GAP = SequenceTimelineLayoutPolicy.MESSAGE_ROW_GAP;
    static final double NOTE_WIDTH = SequenceTimelineLayoutPolicy.NOTE_WIDTH;
    static final double NOTE_HEIGHT = SequenceTimelineLayoutPolicy.NOTE_HEIGHT;
    static final double ACTIVATION_WIDTH = SequenceTimelineLayoutPolicy.ACTIVATION_WIDTH;
    static final double ACTIVATION_HEIGHT = SequenceTimelineLayoutPolicy.ACTIVATION_HEIGHT;
    private static final String NODE_PREFIX = "behavior-node:";
    private static final String EDGE_PREFIX = "behavior-edge:";
    private static final double EXPORT_PADDING = 96.0;

    private final BehaviorDiagramViewModel viewModel;
    private final SequenceTimelineLayoutPolicy timelineLayoutPolicy = new SequenceTimelineLayoutPolicy();
    private final CanvasAdapterInteractionState interactionState = new CanvasAdapterInteractionState();

    public SequenceCanvasAdapter(BehaviorDiagramViewModel viewModel) {
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
        return DiagramTypeId.UML_SEQUENCE;
    }

    @Override
    public List<InteractiveCanvasNode> nodes() {
        List<InteractiveCanvasNode> result = new ArrayList<>();
        for (BehaviorNode node : viewModel.nodes()) {
            result.add(new InteractiveCanvasNode(
                    nodeLayoutId(node.id()),
                    titleFor(node),
                    subtitleFor(node),
                    cssKind(node.kind()),
                    true,
                    false));
        }
        return result;
    }

    @Override
    public List<InteractiveCanvasConnector> connectors() {
        List<InteractiveCanvasConnector> result = new ArrayList<>();
        for (BehaviorEdge edge : viewModel.edges()) {
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
                .map(this::sequenceLayoutForNode);
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
        String layoutId = nodeLayoutId(nodeId);
        interactionState.selectNode(layoutId, additive);
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
        Objects.requireNonNull(selectionBounds, "selectionBounds");
        Set<String> selected = new LinkedHashSet<>(additive ? interactionState.selectedNodeIds() : Set.of());
        for (BehaviorNode node : viewModel.nodes()) {
            NodeLayout layout = sequenceLayoutForNode(node);
            if (CanvasBounds.from(layout).intersects(selectionBounds)) {
                selected.add(nodeLayoutId(node.id()));
            }
        }
        interactionState.selectNodes(selected);
        selected.stream().findFirst().map(id -> rawIdAfterPrefix(id, NODE_PREFIX)).ifPresent(viewModel::selectNodeById);
    }

    @Override
    public void clearSelection() {
        interactionState.clearSelection();
        viewModel.clearPropertySelection();
    }

    @Override
    public void moveNode(String elementId, double x, double y) {
        String nodeId = rawIdAfterPrefix(elementId, NODE_PREFIX);
        if (nodeId.isBlank()) {
            return;
        }
        BehaviorNode node = viewModel.nodes().stream()
                .filter(candidate -> candidate.id().equals(nodeId))
                .findFirst()
                .orElse(null);
        if (node == null) {
            return;
        }
        double lockedY = node.kind() == BehaviorNodeKind.PARTICIPANT ? PARTICIPANT_TOP_Y : Math.max(PARTICIPANT_TOP_Y, y);
        viewModel.moveNodeTo(nodeId, Math.max(32.0, x), lockedY);
        markDirty();
    }

    @Override
    public void moveSelectedNodesBy(double deltaX, double deltaY) {
        for (String selectedNodeId : interactionState.selectedNodeIds()) {
            layoutForNode(selectedNodeId).ifPresent(layout -> moveNode(selectedNodeId, layout.x() + deltaX, layout.y() + deltaY));
        }
        markDirty();
    }

    @Override
    public void resizeNode(String elementId, double width, double height) {
        String nodeId = rawIdAfterPrefix(elementId, NODE_PREFIX);
        if (nodeId.isBlank()) {
            return;
        }
        BehaviorNode node = viewModel.nodes().stream()
                .filter(candidate -> candidate.id().equals(nodeId))
                .findFirst()
                .orElse(null);
        if (node == null || node.kind() != BehaviorNodeKind.FRAGMENT) {
            return;
        }
        viewModel.resizeSequenceFragmentTo(nodeId, width, height);
        interactionState.selectNode(nodeLayoutId(nodeId), false);
        markDirty();
    }

    @Override
    public boolean supportsNodeResize(String elementId) {
        String nodeId = rawIdAfterPrefix(elementId, NODE_PREFIX);
        if (nodeId.isBlank()) {
            return false;
        }
        return viewModel.nodes().stream()
                .anyMatch(node -> node.id().equals(nodeId) && node.kind() == BehaviorNodeKind.FRAGMENT);
    }

    @Override
    public void addBendPoint(String connectorId, double x, double y) {
        // UML Secuencia usa mensajes horizontales derivados del orden temporal; no se agregan vértices manuales.
    }

    @Override
    public void moveBendPoint(String connectorId, int index, double x, double y) {
        // Sin puntos intermedios manuales en UML Secuencia.
    }

    @Override
    public void selectBendPoint(String connectorId, int index) {
        // Sin puntos intermedios manuales en UML Secuencia.
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
        // Sin puntos intermedios manuales en UML Secuencia.
    }

    @Override
    public void markDirty() {
        interactionState.markDirty();
    }

    @Override
    public boolean supportsBendPoints() {
        return false;
    }

    public boolean dirty() {
        return interactionState.dirty();
    }

    @Override
    public CanvasBounds contentBounds() {
        if (viewModel.nodes().isEmpty()) {
            return CanvasBounds.of(0, 0, 900, 620);
        }
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = 0.0;
        double maxY = Math.max(timelineLayoutPolicy.sequenceBottomY(viewModel.currentDocument()), PARTICIPANT_TOP_Y + PARTICIPANT_HEIGHT);
        for (BehaviorNode node : viewModel.nodes()) {
            NodeLayout layout = sequenceLayoutForNode(node);
            minX = Math.min(minX, layout.x());
            minY = Math.min(minY, layout.y());
            maxX = Math.max(maxX, layout.x() + layout.width());
            maxY = Math.max(maxY, layout.y() + layout.height());
        }
        return CanvasBounds.of(
                Math.max(0.0, minX - EXPORT_PADDING),
                Math.max(0.0, minY - EXPORT_PADDING),
                Math.max(900.0, maxX - minX + EXPORT_PADDING * 2.0),
                Math.max(620.0, maxY - minY + EXPORT_PADDING * 2.0));
    }

    public double messageY(String connectorId) {
        return timelineLayoutPolicy.messageY(viewModel.currentDocument(), connectorId);
    }

    public String messageLabel(String connectorId, String rawLabel) {
        return timelineLayoutPolicy.temporalLabel(viewModel.currentDocument(), connectorId, rawLabel);
    }

    public List<BehaviorEdge> orderedMessages() {
        return new ArrayList<>(timelineLayoutPolicy.orderedMessages(viewModel.currentDocument()));
    }

    public List<BehaviorNode> participants() {
        return timelineLayoutPolicy.participants(viewModel.currentDocument());
    }

    private NodeLayout sequenceLayoutForNode(BehaviorNode node) {
        return timelineLayoutPolicy.layoutForNode(viewModel.currentDocument(), node, viewModel.layoutForNode(node));
    }

    private void syncSelectionFromViewModel() {
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
        interactionState.clearSelection();
    }

    private static String titleFor(BehaviorNode node) {
        if (node.kind() == BehaviorNodeKind.FRAGMENT) {
            SequenceCombinedFragmentSpec spec = SequenceCombinedFragmentSpec.fromNode(node);
            return spec.kind().keyword() + " " + spec.displayTitle();
        }
        return node.displayName();
    }

    private static String subtitleFor(BehaviorNode node) {
        if (node.kind() == BehaviorNodeKind.PARTICIPANT) {
            return firstNonBlank(node.owner(), node.description(), "Participante");
        }
        if (node.kind() == BehaviorNodeKind.ACTIVATION) {
            return firstNonBlank(node.owner(), node.description(), "Activación");
        }
        if (node.kind() == BehaviorNodeKind.FRAGMENT) {
            SequenceCombinedFragmentSpec spec = SequenceCombinedFragmentSpec.fromNode(node);
            return firstNonBlank(spec.detailText(), node.description(), node.notes(), node.kind().displayName());
        }
        return firstNonBlank(node.kind().displayName(), node.description(), node.notes());
    }

    private static String labelFor(BehaviorEdge edge) {
        return firstNonBlank(edge.label(), edge.condition(), edge.kind().displayName());
    }

    static String nodeLayoutId(String nodeId) {
        return VisualElementLayoutIds.behaviorNode(nodeId).value();
    }

    static String edgeLayoutId(String edgeId) {
        return VisualElementLayoutIds.behaviorEdge(edgeId).value();
    }

    private static String rawIdAfterPrefix(String value, String prefix) {
        String normalized = normalize(value);
        return normalized.startsWith(prefix) ? normalized.substring(prefix.length()) : "";
    }

    private static String cssKind(BehaviorNodeKind kind) {
        if (kind == BehaviorNodeKind.PARTICIPANT) return "sequence-participant";
        if (kind == BehaviorNodeKind.ACTIVATION) return "sequence-activation";
        if (kind == BehaviorNodeKind.FRAGMENT) return "sequence-fragment";
        if (kind == BehaviorNodeKind.NOTE) return "sequence-note";
        return cssKind(kind == null ? "" : kind.name());
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

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }

    public boolean activeForCurrentDocument() {
        return viewModel.currentKind() == BehaviorDiagramKind.UML_SEQUENCE;
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
