package com.marcosmoreira.domainmodelstudio.presentation.modulemap;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBounds;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBendPointEditingSupport;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasContentBoundsCalculator;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasConnectorLabelPort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasConnectorSelectionHitPolicy;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasProjectStylePort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasDirtyState;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasElementIdCodec;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasAdapter;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasConnector;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasNode;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasSelection;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasSelectionSupport;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.SelectedBendPoint;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasSelectionClipboardPort;
import com.marcosmoreira.domainmodelstudio.presentation.visualtransfer.ProjectVisualSelectionTransferService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Adaptador del mapa de módulos hacia el lienzo común.
 *
 * <p>El editor conserva su modelo semántico en {@link ModuleMapViewModel}; este adaptador
 * solo traduce módulos/dependencias a nodos/conectores visuales reutilizables.</p>
 */
public final class ModuleMapCanvasAdapter implements InteractiveCanvasAdapter, CanvasSelectionClipboardPort, CanvasConnectorLabelPort, CanvasProjectStylePort {

    private static final String MODULE_PREFIX = "module:";
    private static final String DEPENDENCY_PREFIX = "dependency:";
    private static final String CONTAINMENT_PREFIX = "module-containment:";
    private static final double EXPORT_PADDING = 96.0;

    private final ModuleMapViewModel viewModel;
    private final CanvasElementIdCodec idCodec = CanvasElementIdCodec.withPrefixes(
            MODULE_PREFIX,
            DEPENDENCY_PREFIX,
            CONTAINMENT_PREFIX);
    private final CanvasSelectionSupport selectionSupport = new CanvasSelectionSupport();
    private final CanvasDirtyState dirtyState = new CanvasDirtyState();
    private final CanvasBendPointEditingSupport bendPointSupport = new CanvasBendPointEditingSupport(
            selectionSupport,
            dirtyState);
    private final CanvasContentBoundsCalculator boundsCalculator = new CanvasContentBoundsCalculator(
            EXPORT_PADDING,
            880.0,
            600.0);

    public ModuleMapCanvasAdapter(ModuleMapViewModel viewModel) {
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
        return DiagramTypeId.ADMIN_MODULE_MAP;
    }

    @Override
    public List<InteractiveCanvasNode> nodes() {
        List<InteractiveCanvasNode> result = new ArrayList<>();
        for (ModuleNode module : viewModel.modules()) {
            result.add(new InteractiveCanvasNode(
                    moduleLayoutId(module.id()),
                    module.displayName(),
                    subtitleFor(module),
                    module.rootModule() ? "module-root" : "module-child",
                    true,
                    false));
        }
        return result;
    }

    @Override
    public List<InteractiveCanvasConnector> connectors() {
        List<InteractiveCanvasConnector> result = new ArrayList<>();
        for (ModuleNode module : viewModel.modules()) {
            if (!module.parentId().isBlank()) {
                result.add(new InteractiveCanvasConnector(
                        containmentLayoutId(module.parentId(), module.id()),
                        moduleLayoutId(module.parentId()),
                        moduleLayoutId(module.id()),
                        "Contiene",
                        "module-containment",
                        true));
            }
        }
        for (ModuleDependency dependency : viewModel.dependencies()) {
            result.add(new InteractiveCanvasConnector(
                    dependencyLayoutId(dependency.id()),
                    moduleLayoutId(dependency.sourceModuleId()),
                    moduleLayoutId(dependency.targetModuleId()),
                    dependency.kind().displayName(),
                    "module-dependency",
                    true));
        }
        return result;
    }

    @Override
    public Optional<NodeLayout> layoutForNode(String elementId) {
        String rawModuleId = rawIdAfterPrefix(elementId, MODULE_PREFIX);
        if (rawModuleId.isBlank()) {
            return Optional.empty();
        }
        return viewModel.modules().stream()
                .filter(module -> module.id().equals(rawModuleId))
                .findFirst()
                .map(viewModel::layoutForModule);
    }

    @Override
    public Optional<ConnectorLayout> layoutForConnector(String connectorId) {
        String normalized = normalize(connectorId);
        if (normalized.startsWith(DEPENDENCY_PREFIX) || normalized.startsWith(CONTAINMENT_PREFIX)) {
            return viewModel.layoutForConnector(DiagramElementId.of(normalized));
        }
        return Optional.empty();
    }

    @Override
    public InteractiveCanvasSelection selection() {
        syncSelectionFromViewModel();
        return selectionSupport.current();
    }

    @Override
    public void selectNode(String elementId, boolean additive) {
        String moduleId = rawIdAfterPrefix(elementId, MODULE_PREFIX);
        if (moduleId.isBlank()) {
            return;
        }
        selectionSupport.selectNode(moduleLayoutId(moduleId), additive);
        viewModel.selectModuleById(moduleId);
    }

    @Override
    public void selectConnector(String connectorId, boolean additive) {
        String normalized = normalize(connectorId);
        if (normalized.startsWith(DEPENDENCY_PREFIX)) {
            selectionSupport.selectConnector(normalized);
            viewModel.selectDependencyById(rawIdAfterPrefix(normalized, DEPENDENCY_PREFIX));
            return;
        }
        selectionSupport.selectConnector(normalized);
        viewModel.clearPropertySelection();
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
        for (ModuleNode module : viewModel.modules()) {
            NodeLayout layout = viewModel.layoutForModule(module);
            if (CanvasBounds.from(layout).intersects(selectionBounds)) {
                selectedNodes.add(moduleLayoutId(module.id()));
            }
        }
        for (ModuleDependency dependency : viewModel.dependencies()) {
            String source = moduleLayoutId(dependency.sourceModuleId());
            String target = moduleLayoutId(dependency.targetModuleId());
            String connectorId = dependencyLayoutId(dependency.id());
            if ((selectedNodes.contains(source) && selectedNodes.contains(target))
                    || connectorHasBendPointInside(connectorId, selectionBounds)
                    || connectorRouteTouches(connectorId, source, target, selectionBounds)) {
                selectedConnectors.add(connectorId);
            }
        }
        selectionSupport.selectNodesAndConnectors(selectedNodes, selectedConnectors);
        selectedNodes.stream().findFirst().map(id -> rawIdAfterPrefix(id, MODULE_PREFIX)).ifPresent(viewModel::selectModuleById);
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
        selectionSupport.clear();
        viewModel.clearPropertySelection();
    }

    @Override
    public void moveNode(String elementId, double x, double y) {
        String moduleId = rawIdAfterPrefix(elementId, MODULE_PREFIX);
        if (!moduleId.isBlank()) {
            viewModel.moveModuleTo(moduleId, x, y);
            markDirty();
        }
    }

    @Override
    public void moveSelectedNodesBy(double deltaX, double deltaY) {
        Set<String> selectedModuleIds = selectionSupport.current().selectedNodeIds().stream()
                .map(id -> rawIdAfterPrefix(id, MODULE_PREFIX))
                .filter(id -> !id.isBlank())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        for (String moduleId : selectedModuleIds) {
            if (hasSelectedAncestor(moduleId, selectedModuleIds)) {
                continue;
            }
            String nodeId = moduleLayoutId(moduleId);
            layoutForNode(nodeId).ifPresent(layout -> moveNode(nodeId, layout.x() + deltaX, layout.y() + deltaY));
        }
        markDirty();
    }

    @Override
    public void moveSelectedConnectorBendPointsBy(double deltaX, double deltaY) {
        for (String connectorId : selectionSupport.current().selectedConnectorIds()) {
            if (!connectorId.startsWith(DEPENDENCY_PREFIX)) {
                continue;
            }
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

    private boolean hasSelectedAncestor(String moduleId, Set<String> selectedModuleIds) {
        String parentId = parentIdOf(moduleId);
        while (!parentId.isBlank()) {
            if (selectedModuleIds.contains(parentId)) {
                return true;
            }
            parentId = parentIdOf(parentId);
        }
        return false;
    }

    private String parentIdOf(String moduleId) {
        return viewModel.modules().stream()
                .filter(module -> module.id().equals(moduleId))
                .map(ModuleNode::parentId)
                .findFirst()
                .orElse("");
    }

    @Override
    public void addBendPoint(String connectorId, double x, double y) {
        String normalized = normalize(connectorId);
        viewModel.clearPropertySelection();
        viewModel.addConnectorBendPoint(DiagramElementId.of(normalized), x, y)
                .ifPresent(index -> bendPointSupport.markEditedBendPoint(normalized, index));
        markDirty();
    }

    @Override
    public void moveBendPoint(String connectorId, int index, double x, double y) {
        String normalized = normalize(connectorId);
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
    public void moveConnectorLabelBy(String connectorId, double deltaX, double deltaY) {
        String normalized = normalize(connectorId);
        if (normalized.isBlank()) {
            return;
        }
        viewModel.clearPropertySelection();
        viewModel.moveConnectorLabelBy(DiagramElementId.of(normalized), deltaX, deltaY);
        selectionSupport.selectConnector(normalized);
        markDirty();
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
    public void markDirty() {
        dirtyState.markDirty();
    }

    public boolean dirty() {
        return dirtyState.dirty();
    }

    @Override
    public CanvasBounds contentBounds() {
        java.util.List<NodeLayout> layouts = new java.util.ArrayList<>();
        for (ModuleNode module : viewModel.modules()) {
            layouts.add(viewModel.layoutForModule(module));
        }
        return boundsCalculator.fromNodeLayouts(layouts);
    }

    private void syncSelectionFromViewModel() {
        if (selectionSupport.shouldPreserveManualBendPointSelection()) {
            return;
        }
        if (viewModel.selectedModuleProperty().get() != null) {
            String selectedId = moduleLayoutId(viewModel.selectedModuleProperty().get().id());
            if (!selectionSupport.current().isNodeSelected(selectedId)) {
                selectionSupport.selectNode(selectedId, false);
            }
            return;
        }
        if (viewModel.selectedDependencyProperty().get() != null) {
            String selectedId = dependencyLayoutId(viewModel.selectedDependencyProperty().get().id());
            if (!selectionSupport.current().isConnectorSelected(selectedId)) {
                selectionSupport.selectConnector(selectedId);
            }
        }
    }

    private static String subtitleFor(ModuleNode module) {
        String responsibility = module.responsibility().isBlank() ? module.description() : module.responsibility();
        String base = module.kind().displayName() + " · " + module.status().displayName();
        return responsibility.isBlank() ? base : base + "\n" + responsibility;
    }

    static String moduleLayoutId(String moduleId) {
        return VisualElementLayoutIds.module(moduleId).value();
    }

    static String dependencyLayoutId(String dependencyId) {
        return VisualElementLayoutIds.dependency(dependencyId).value();
    }

    static String containmentLayoutId(String parentModuleId, String childModuleId) {
        return VisualElementLayoutIds.moduleContainment(parentModuleId, childModuleId).value();
    }

    private String rawIdAfterPrefix(String value, String prefix) {
        return idCodec.rawIdAfterPrefix(value, prefix);
    }

    private static boolean contains(CanvasBounds bounds, double x, double y) {
        return x >= bounds.x() && x <= bounds.x() + bounds.width()
                && y >= bounds.y() && y <= bounds.y() + bounds.height();
    }

    private String normalize(String value) {
        return idCodec.normalize(value);
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
