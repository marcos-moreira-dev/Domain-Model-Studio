package com.marcosmoreira.domainmodelstudio.presentation.wireframe;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponent;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreen;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasAdapterInteractionState;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBounds;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasAdapter;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasProjectStylePort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasResizePort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasConnector;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasNode;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasSelection;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasSelectionClipboardPort;
import com.marcosmoreira.domainmodelstudio.presentation.visualtransfer.ProjectVisualSelectionTransferService;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Adaptador de wireframes administrativos hacia el lienzo común reutilizable. */
public final class WireframeCanvasAdapter implements InteractiveCanvasAdapter, CanvasSelectionClipboardPort, CanvasProjectStylePort, CanvasResizePort {

    private static final String SCREEN_PREFIX = "wireframe-screen:";
    private static final String COMPONENT_PREFIX = "wireframe-component:";
    private static final double EXPORT_PADDING = 96.0;

    private final WireframeViewModel viewModel;
    private final CanvasAdapterInteractionState interactionState = new CanvasAdapterInteractionState();

    public WireframeCanvasAdapter(WireframeViewModel viewModel) {
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
        return DiagramTypeId.ADMIN_WIREFRAMES;
    }

    @Override
    public List<InteractiveCanvasNode> nodes() {
        List<InteractiveCanvasNode> result = new ArrayList<>();
        for (WireframeScreen screen : viewModel.screens()) {
            result.add(new InteractiveCanvasNode(
                    screenLayoutId(screen.id()),
                    screen.displayName(),
                    subtitleFor(screen),
                    "wireframe-screen",
                    true,
                    false));
        }
        for (WireframeComponent component : viewModel.components()) {
            result.add(new InteractiveCanvasNode(
                    componentLayoutId(component.id()),
                    component.displayName(),
                    subtitleFor(component),
                    "wireframe-component-" + component.kind().name().toLowerCase(java.util.Locale.ROOT).replace('_', '-'),
                    true,
                    false));
        }
        return result;
    }

    @Override
    public List<InteractiveCanvasConnector> connectors() {
        return List.of();
    }

    @Override
    public Optional<NodeLayout> layoutForNode(String elementId) {
        String screenId = rawIdAfterPrefix(elementId, SCREEN_PREFIX);
        if (!screenId.isBlank()) {
            return viewModel.screens().stream()
                    .filter(screen -> screen.id().equals(screenId))
                    .findFirst()
                    .map(viewModel::layoutForScreen);
        }
        String componentId = rawIdAfterPrefix(elementId, COMPONENT_PREFIX);
        if (!componentId.isBlank()) {
            return viewModel.components().stream()
                    .filter(component -> component.id().equals(componentId))
                    .findFirst()
                    .map(viewModel::layoutForComponent);
        }
        return Optional.empty();
    }

    @Override
    public Optional<ConnectorLayout> layoutForConnector(String connectorId) {
        return Optional.empty();
    }

    @Override
    public InteractiveCanvasSelection selection() {
        syncSelectionFromViewModel();
        return interactionState.selection();
    }

    @Override
    public void selectNode(String elementId, boolean additive) {
        String normalized = normalize(elementId);
        if (normalized.startsWith(SCREEN_PREFIX)) {
            selectScreenNode(rawIdAfterPrefix(normalized, SCREEN_PREFIX), additive);
            return;
        }
        if (normalized.startsWith(COMPONENT_PREFIX)) {
            selectComponentNode(rawIdAfterPrefix(normalized, COMPONENT_PREFIX), additive);
        }
    }

    @Override
    public void selectConnector(String connectorId, boolean additive) {
        // Wireframes no usan conectores semánticos en esta versión.
    }

    @Override
    public void selectNodesInside(CanvasBounds selectionBounds, boolean additive) {
        Objects.requireNonNull(selectionBounds, "selectionBounds");
        Set<String> selected = new LinkedHashSet<>(additive ? interactionState.selectedNodeIds() : Set.of());
        Set<String> componentHits = new LinkedHashSet<>();
        for (WireframeComponent component : viewModel.components()) {
            NodeLayout layout = viewModel.layoutForComponent(component);
            if (CanvasBounds.from(layout).intersects(selectionBounds)) {
                componentHits.add(componentLayoutId(component.id()));
            }
        }
        if (componentHits.isEmpty()) {
            for (WireframeScreen screen : viewModel.screens()) {
                NodeLayout layout = viewModel.layoutForScreen(screen);
                if (CanvasBounds.from(layout).intersects(selectionBounds)) {
                    selected.add(screenLayoutId(screen.id()));
                }
            }
        } else {
            selected.addAll(componentHits);
        }
        interactionState.selectNodes(selected);
        selected.stream().filter(id -> id.startsWith(COMPONENT_PREFIX)).findFirst()
                .or(() -> selected.stream().findFirst())
                .ifPresent(this::selectFirstPropertyNode);
    }

    @Override
    public void clearSelection() {
        interactionState.clearSelection();
        viewModel.clearPropertySelection();
    }

    @Override
    public void moveNode(String elementId, double x, double y) {
        String normalized = normalize(elementId);
        if (normalized.startsWith(SCREEN_PREFIX)) {
            viewModel.moveScreenTo(rawIdAfterPrefix(normalized, SCREEN_PREFIX), x, y);
            markDirty();
            return;
        }
        if (normalized.startsWith(COMPONENT_PREFIX)) {
            viewModel.moveComponentTo(rawIdAfterPrefix(normalized, COMPONENT_PREFIX), x, y);
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
    public void resizeNode(String elementId, double width, double height) {
        String normalized = normalize(elementId);
        if (normalized.startsWith(SCREEN_PREFIX)) {
            viewModel.resizeScreenTo(rawIdAfterPrefix(normalized, SCREEN_PREFIX), width, height);
            markDirty();
            return;
        }
        if (normalized.startsWith(COMPONENT_PREFIX)) {
            viewModel.resizeComponentTo(rawIdAfterPrefix(normalized, COMPONENT_PREFIX), width, height);
            markDirty();
        }
    }


    void deleteSelectedItems() {
        Set<String> screensToDelete = new LinkedHashSet<>();
        Set<String> componentsToDelete = new LinkedHashSet<>();
        for (String nodeId : interactionState.selectedNodeIds()) {
            String normalized = normalize(nodeId);
            if (normalized.startsWith(SCREEN_PREFIX)) {
                screensToDelete.add(rawIdAfterPrefix(normalized, SCREEN_PREFIX));
            } else if (normalized.startsWith(COMPONENT_PREFIX)) {
                componentsToDelete.add(rawIdAfterPrefix(normalized, COMPONENT_PREFIX));
            }
        }
        if (screensToDelete.isEmpty() && componentsToDelete.isEmpty()) {
            viewModel.removeSelected();
            return;
        }
        viewModel.removeItemsById(screensToDelete, componentsToDelete);
        interactionState.clearSelection();
    }

    @Override
    public void addBendPoint(String connectorId, double x, double y) {
        // Wireframes no tienen conectores semánticos en esta versión.
    }

    @Override
    public void moveBendPoint(String connectorId, int index, double x, double y) {
        // Wireframes no tienen conectores semánticos en esta versión.
    }

    @Override
    public void selectBendPoint(String connectorId, int index) {
        // Wireframes no tienen conectores semánticos en esta versión.
    }

    @Override
    public void removeSelectedBendPoint() {
        // Wireframes no tienen conectores semánticos en esta versión.
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
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = 0.0;
        double maxY = 0.0;
        for (WireframeScreen screen : viewModel.screens()) {
            NodeLayout layout = viewModel.layoutForScreen(screen);
            minX = Math.min(minX, layout.x());
            minY = Math.min(minY, layout.y());
            maxX = Math.max(maxX, layout.x() + layout.width());
            maxY = Math.max(maxY, layout.y() + layout.height());
        }
        for (WireframeComponent component : viewModel.components()) {
            NodeLayout layout = viewModel.layoutForComponent(component);
            minX = Math.min(minX, layout.x());
            minY = Math.min(minY, layout.y());
            maxX = Math.max(maxX, layout.x() + layout.width());
            maxY = Math.max(maxY, layout.y() + layout.height());
        }
        if (minX == Double.MAX_VALUE) {
            return CanvasBounds.of(0, 0, 960, 620);
        }
        return CanvasBounds.of(
                Math.max(0.0, minX - EXPORT_PADDING),
                Math.max(0.0, minY - EXPORT_PADDING),
                Math.max(960.0, maxX - minX + EXPORT_PADDING * 2.0),
                Math.max(620.0, maxY - minY + EXPORT_PADDING * 2.0));
    }

    private void selectScreenNode(String screenId, boolean additive) {
        if (screenId.isBlank()) {
            return;
        }
        String nodeId = screenLayoutId(screenId);
        interactionState.selectNode(nodeId, additive);
        viewModel.selectScreenById(screenId);
    }

    private void selectComponentNode(String componentId, boolean additive) {
        if (componentId.isBlank()) {
            return;
        }
        String nodeId = componentLayoutId(componentId);
        interactionState.selectNode(nodeId, additive);
        viewModel.selectComponentById(componentId);
    }

    private void selectFirstPropertyNode(String nodeId) {
        String normalized = normalize(nodeId);
        if (normalized.startsWith(COMPONENT_PREFIX)) {
            viewModel.selectComponentById(rawIdAfterPrefix(normalized, COMPONENT_PREFIX));
        } else if (normalized.startsWith(SCREEN_PREFIX)) {
            viewModel.selectScreenById(rawIdAfterPrefix(normalized, SCREEN_PREFIX));
        }
    }

    private void syncSelectionFromViewModel() {
        if (viewModel.selectedComponentProperty().get() != null) {
            String selectedId = componentLayoutId(viewModel.selectedComponentProperty().get().id());
            interactionState.syncSingleNode(selectedId);
            return;
        }
        if (viewModel.selectedScreenProperty().get() != null) {
            String selectedId = screenLayoutId(viewModel.selectedScreenProperty().get().id());
            interactionState.syncSingleNode(selectedId);
        }
    }

    private static String subtitleFor(WireframeScreen screen) {
        return firstNonBlank(screen.moduleName(), "Pantalla administrativa") + "\n" + firstNonBlank(screen.purpose(), "Maqueta estructural");
    }

    private static String subtitleFor(WireframeComponent component) {
        return component.kind().displayName() + "\n" + firstNonBlank(component.dataBinding(), component.behavior(), "Componente visual");
    }

    private static String screenLayoutId(String screenId) {
        return VisualElementLayoutIds.wireframeScreen(screenId).value();
    }

    private static String componentLayoutId(String componentId) {
        return VisualElementLayoutIds.wireframeComponent(componentId).value();
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
