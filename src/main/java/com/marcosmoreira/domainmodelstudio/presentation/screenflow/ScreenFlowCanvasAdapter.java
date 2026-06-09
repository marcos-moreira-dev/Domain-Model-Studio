package com.marcosmoreira.domainmodelstudio.presentation.screenflow;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenNode;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransition;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasAdapterInteractionState;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBounds;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasConnectorLabelPort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasConnectorSelectionHitPolicy;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasProjectStylePort;
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

/** Adaptador del flujo de pantallas hacia el lienzo común reutilizable. */
public final class ScreenFlowCanvasAdapter implements InteractiveCanvasAdapter, CanvasSelectionClipboardPort, CanvasConnectorLabelPort, CanvasProjectStylePort {

    private static final String SCREEN_PREFIX = "screen:";
    private static final String TRANSITION_PREFIX = "transition:";
    private static final double EXPORT_PADDING = 96.0;

    private final ScreenFlowViewModel viewModel;
    private final CanvasAdapterInteractionState interactionState = new CanvasAdapterInteractionState();

    public ScreenFlowCanvasAdapter(ScreenFlowViewModel viewModel) {
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
        return DiagramTypeId.SCREEN_FLOW;
    }

    @Override
    public List<InteractiveCanvasNode> nodes() {
        List<InteractiveCanvasNode> result = new ArrayList<>();
        for (ScreenNode screen : viewModel.screens()) {
            result.add(new InteractiveCanvasNode(
                    screenLayoutId(screen.id()),
                    screen.displayName(),
                    subtitleFor(screen),
                    "screen-" + screen.kind().name().toLowerCase(java.util.Locale.ROOT).replace('_', '-'),
                    true,
                    false));
        }
        return result;
    }

    @Override
    public List<InteractiveCanvasConnector> connectors() {
        List<InteractiveCanvasConnector> result = new ArrayList<>();
        for (ScreenTransition transition : viewModel.transitions()) {
            result.add(new InteractiveCanvasConnector(
                    transitionLayoutId(transition.id()),
                    screenLayoutId(transition.sourceScreenId()),
                    screenLayoutId(transition.targetScreenId()),
                    firstNonBlank(transition.trigger(), transition.kind().displayName()),
                    "screen-transition",
                    true));
        }
        return result;
    }

    @Override
    public Optional<NodeLayout> layoutForNode(String elementId) {
        String rawScreenId = rawIdAfterPrefix(elementId, SCREEN_PREFIX);
        if (rawScreenId.isBlank()) {
            return Optional.empty();
        }
        return viewModel.screens().stream()
                .filter(screen -> screen.id().equals(rawScreenId))
                .findFirst()
                .map(viewModel::layoutForScreen);
    }

    @Override
    public Optional<ConnectorLayout> layoutForConnector(String connectorId) {
        String normalized = normalize(connectorId);
        if (normalized.startsWith(TRANSITION_PREFIX)) {
            return viewModel.layoutForConnector(DiagramElementId.of(normalized));
        }
        return Optional.empty();
    }

    @Override
    public InteractiveCanvasSelection selection() {
        syncSelectionFromViewModel();
        return interactionState.selection();
    }

    @Override
    public void selectNode(String elementId, boolean additive) {
        String screenId = rawIdAfterPrefix(elementId, SCREEN_PREFIX);
        if (screenId.isBlank()) {
            return;
        }
        if (additive) {
            interactionState.selectNode(screenLayoutId(screenId), true);
        } else {
            interactionState.selectNode(screenLayoutId(screenId), false);
        }
        viewModel.selectScreenById(screenId);
    }

    @Override
    public void selectConnector(String connectorId, boolean additive) {
        String normalized = normalize(connectorId);
        if (!normalized.startsWith(TRANSITION_PREFIX)) {
            return;
        }
        interactionState.selectConnector(normalized);
        viewModel.selectTransitionById(rawIdAfterPrefix(normalized, TRANSITION_PREFIX));
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
        for (ScreenNode screen : viewModel.screens()) {
            NodeLayout layout = viewModel.layoutForScreen(screen);
            if (CanvasBounds.from(layout).intersects(selectionBounds)) {
                selectedNodes.add(screenLayoutId(screen.id()));
            }
        }
        for (ScreenTransition transition : viewModel.transitions()) {
            String source = screenLayoutId(transition.sourceScreenId());
            String target = screenLayoutId(transition.targetScreenId());
            String connectorId = transitionLayoutId(transition.id());
            if ((selectedNodes.contains(source) && selectedNodes.contains(target))
                    || connectorHasBendPointInside(connectorId, selectionBounds)
                    || connectorRouteTouches(connectorId, source, target, selectionBounds)) {
                selectedConnectors.add(connectorId);
            }
        }
        interactionState.selectNodesAndConnectors(selectedNodes, selectedConnectors);
        selectedNodes.stream().findFirst().map(id -> rawIdAfterPrefix(id, SCREEN_PREFIX)).ifPresent(viewModel::selectScreenById);
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
        interactionState.clearSelection();
        viewModel.clearPropertySelection();
    }

    @Override
    public void moveNode(String elementId, double x, double y) {
        String screenId = rawIdAfterPrefix(elementId, SCREEN_PREFIX);
        if (!screenId.isBlank()) {
            viewModel.moveScreenTo(screenId, x, y);
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
        if (!normalized.startsWith(TRANSITION_PREFIX)) {
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
        interactionState.clearAfterRemoval();
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
        for (ScreenNode screen : viewModel.screens()) {
            NodeLayout layout = viewModel.layoutForScreen(screen);
            minX = Math.min(minX, layout.x());
            minY = Math.min(minY, layout.y());
            maxX = Math.max(maxX, layout.x() + layout.width());
            maxY = Math.max(maxY, layout.y() + layout.height());
        }
        if (minX == Double.MAX_VALUE) {
            return CanvasBounds.of(0, 0, 880, 600);
        }
        return CanvasBounds.of(
                Math.max(0.0, minX - EXPORT_PADDING),
                Math.max(0.0, minY - EXPORT_PADDING),
                Math.max(880.0, maxX - minX + EXPORT_PADDING * 2.0),
                Math.max(600.0, maxY - minY + EXPORT_PADDING * 2.0));
    }

    private void syncSelectionFromViewModel() {
        if (interactionState.shouldPreserveManualBendPointSelection()) {
            return;
        }
        if (viewModel.selectedScreenProperty().get() != null) {
            String selectedId = screenLayoutId(viewModel.selectedScreenProperty().get().id());
            interactionState.syncSingleNode(selectedId);
            return;
        }
        if (viewModel.selectedTransitionProperty().get() != null) {
            String selectedId = transitionLayoutId(viewModel.selectedTransitionProperty().get().id());
            interactionState.syncSingleConnector(selectedId);
        }
    }

    private static String subtitleFor(ScreenNode screen) {
        String routeOrModule = firstNonBlank(screen.route(), screen.moduleName(), "Sin ruta definida");
        return screen.kind().displayName() + "\n" + routeOrModule;
    }

    private static String screenLayoutId(String screenId) {
        return VisualElementLayoutIds.screen(screenId).value();
    }

    private static String transitionLayoutId(String transitionId) {
        return VisualElementLayoutIds.transition(transitionId).value();
    }

    private static String rawIdAfterPrefix(String value, String prefix) {
        String normalized = normalize(value);
        return normalized.startsWith(prefix) ? normalized.substring(prefix.length()) : "";
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            String normalized = normalize(value);
            if (!normalized.isBlank()) {
                return normalized;
            }
        }
        return "";
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
