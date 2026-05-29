package com.marcosmoreira.domainmodelstudio.presentation.umlclass;
import static com.marcosmoreira.domainmodelstudio.presentation.umlclass.UmlClassCanvasElementIds.CLASS_PREFIX;
import static com.marcosmoreira.domainmodelstudio.presentation.umlclass.UmlClassCanvasElementIds.MODULE_PREFIX;
import static com.marcosmoreira.domainmodelstudio.presentation.umlclass.UmlClassCanvasElementIds.RELATION_PREFIX;
import static com.marcosmoreira.domainmodelstudio.presentation.umlclass.UmlClassCanvasElementIds.classLayoutId;
import static com.marcosmoreira.domainmodelstudio.presentation.umlclass.UmlClassCanvasElementIds.moduleLayoutId;
import static com.marcosmoreira.domainmodelstudio.presentation.umlclass.UmlClassCanvasElementIds.normalize;
import static com.marcosmoreira.domainmodelstudio.presentation.umlclass.UmlClassCanvasElementIds.rawIdAfterPrefix;
import static com.marcosmoreira.domainmodelstudio.presentation.umlclass.UmlClassCanvasElementIds.relationLayoutId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasAdapterInteractionState;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBounds;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasConnectorLabelPort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasConnectorSelectionHitPolicy;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasConnectorHitTestPort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasRenderFailurePort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasRenderFailureReport;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasProjectStylePort;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasDragPreviewPort;
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
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
/** Adaptador de UML Clases hacia el lienzo común reutilizable. */
public final class UmlClassCanvasAdapter implements InteractiveCanvasAdapter, CanvasConnectorLabelPort, CanvasConnectorHitTestPort, CanvasSelectionClipboardPort, CanvasProjectStylePort, CanvasRenderFailurePort, CanvasDragPreviewPort {
    private static final double EXPORT_PADDING = 96.0;
    private final UmlClassDiagramViewModel viewModel;
    private final CanvasAdapterInteractionState interactionState = new CanvasAdapterInteractionState();
    public UmlClassCanvasAdapter(UmlClassDiagramViewModel viewModel) {
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
    public boolean connectorLineHitTestingEnabled() {
        // UML Clases dibuja relaciones sobre módulos para que rombos y triángulos
        // sean visibles. La línea no debe bloquear clic/drag de clases o módulos.
        return false;
    }
    @Override
    public DiagramTypeId diagramTypeId() {
        return DiagramTypeId.UML_CLASS;
    }
    @Override
    public List<InteractiveCanvasNode> nodes() {
        List<InteractiveCanvasNode> result = new ArrayList<>();
        for (UmlModuleGroup module : viewModel.modules()) {
            result.add(new InteractiveCanvasNode(
                    moduleLayoutId(module.id()),
                    module.displayName(),
                    subtitleFor(module),
                    "uml-module",
                    true,
                    false));
        }
        for (UmlClassNode umlClass : viewModel.classes()) {
            result.add(new InteractiveCanvasNode(
                    classLayoutId(umlClass.id()),
                    umlClass.displayName(),
                    subtitleFor(umlClass),
                    "uml-class-" + umlClass.kind().name().toLowerCase(Locale.ROOT).replace('_', '-'),
                    true,
                    false));
        }
        return result;
    }
    @Override
    public List<InteractiveCanvasConnector> connectors() {
        List<InteractiveCanvasConnector> result = new ArrayList<>();
        for (UmlClassRelation relation : viewModel.relations()) {
            result.add(new InteractiveCanvasConnector(
                    relationLayoutId(relation.id()),
                    classLayoutId(relation.sourceClassId()),
                    classLayoutId(relation.targetClassId()),
                    firstNonBlank(relation.label(), relation.kind().displayName()),
                    "uml-relation-" + relation.kind().name().toLowerCase(Locale.ROOT).replace('_', '-'),
                    true));
        }
        return result;
    }
    @Override
    public Optional<NodeLayout> layoutForNode(String elementId) {
        String moduleId = rawIdAfterPrefix(elementId, MODULE_PREFIX);
        if (!moduleId.isBlank()) {
            return viewModel.modules().stream()
                    .filter(module -> module.id().equals(moduleId))
                    .findFirst()
                    .map(viewModel::layoutForModule);
        }
        String classId = rawIdAfterPrefix(elementId, CLASS_PREFIX);
        if (!classId.isBlank()) {
            return viewModel.classes().stream()
                    .filter(node -> node.id().equals(classId))
                    .findFirst()
                    .map(viewModel::layoutForClass);
        }
        return Optional.empty();
    }
    @Override
    public Optional<ConnectorLayout> layoutForConnector(String connectorId) {
        String normalized = normalize(connectorId);
        if (normalized.startsWith(RELATION_PREFIX)) {
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
    public void handleCanvasRenderFailure(CanvasRenderFailureReport report) {
        viewModel.handleCanvasRenderFailure(report);
    }
    @Override
    public void selectNode(String elementId, boolean additive) {
        String normalized = normalize(elementId);
        if (normalized.startsWith(MODULE_PREFIX)) {
            selectModuleNode(rawIdAfterPrefix(normalized, MODULE_PREFIX), additive);
            return;
        }
        if (normalized.startsWith(CLASS_PREFIX)) {
            selectClassNode(rawIdAfterPrefix(normalized, CLASS_PREFIX), additive);
        }
    }
    @Override
    public void selectConnector(String connectorId, boolean additive) {
        String normalized = normalize(connectorId);
        if (!normalized.startsWith(RELATION_PREFIX)) {
            return;
        }
        interactionState.selectConnector(normalized);
        viewModel.selectRelationByIdForCanvas(rawIdAfterPrefix(normalized, RELATION_PREFIX));
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
        for (UmlModuleGroup module : viewModel.modules()) {
            NodeLayout layout = viewModel.layoutForModule(module);
            if (CanvasBounds.from(layout).intersects(selectionBounds)) {
                selectedNodes.add(moduleLayoutId(module.id()));
            }
        }
        for (UmlClassNode umlClass : viewModel.classes()) {
            NodeLayout layout = viewModel.layoutForClass(umlClass);
            if (CanvasBounds.from(layout).intersects(selectionBounds)) {
                selectedNodes.add(classLayoutId(umlClass.id()));
            }
        }
        for (UmlClassRelation relation : viewModel.relations()) {
            String source = classLayoutId(relation.sourceClassId());
            String target = classLayoutId(relation.targetClassId());
            String connectorId = relationLayoutId(relation.id());
            if ((selectedNodes.contains(source) && selectedNodes.contains(target))
                    || connectorHasBendPointInside(connectorId, selectionBounds)
                    || connectorRouteTouches(connectorId, source, target, selectionBounds)) {
                selectedConnectors.add(connectorId);
            }
        }
        interactionState.selectNodesAndConnectors(selectedNodes, selectedConnectors);
        selectedNodes.stream()
                .filter(id -> normalize(id).startsWith(CLASS_PREFIX))
                .findFirst()
                .or(() -> selectedNodes.stream().findFirst())
                .ifPresent(this::selectFirstPropertyNode);
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
        String normalized = normalize(elementId);
        if (normalized.startsWith(MODULE_PREFIX)) {
            viewModel.moveModuleTo(rawIdAfterPrefix(normalized, MODULE_PREFIX), x, y);
            markDirty();
            return;
        }
        if (normalized.startsWith(CLASS_PREFIX)) {
            viewModel.moveClassTo(rawIdAfterPrefix(normalized, CLASS_PREFIX), x, y);
            markDirty();
        }
    }
    private void moveModuleAndVisibleClassesBy(String moduleId, double deltaX, double deltaY, Set<String> moved) {
        String moduleLayoutId = moduleLayoutId(moduleId);
        String normalizedLayoutId = normalize(moduleLayoutId);
        if (moved.add(normalizedLayoutId)) {
            layoutForNode(normalizedLayoutId).ifPresent(layout -> viewModel.moveModuleTo(
                    moduleId,
                    layout.x() + deltaX,
                    layout.y() + deltaY));
        }
        // Tanda 17: el coordinador de layout UML ya traslada las clases internas
        // cuando se mueve el módulo. El adapter no debe moverlas otra vez, porque
        // eso desincroniza la previsualización del drag con el commit final.
    }
    @Override
    public void moveSelectedNodesBy(double deltaX, double deltaY) {
        Set<String> selected = interactionState.selectedNodeIds();
        Set<String> moved = new LinkedHashSet<>();
        for (String nodeId : selected) {
            String moduleId = rawIdAfterPrefix(normalize(nodeId), MODULE_PREFIX);
            if (!moduleId.isBlank()) {
                moveModuleAndVisibleClassesBy(moduleId, deltaX, deltaY, moved);
                continue;
            }
            if (classMoveCoveredBySelectedModule(nodeId, selected) || moved.contains(normalize(nodeId))) {
                continue;
            }
            moveNodeBy(nodeId, deltaX, deltaY, moved);
        }
        markDirty();
    }
    private void moveNodeBy(String nodeId, double deltaX, double deltaY, Set<String> moved) {
        String normalized = normalize(nodeId);
        if (!moved.add(normalized)) {
            return;
        }
        layoutForNode(normalized).ifPresent(layout -> moveNode(normalized, layout.x() + deltaX, layout.y() + deltaY));
    }
    private boolean classMoveCoveredBySelectedModule(String nodeId, Set<String> selected) {
        String classId = rawIdAfterPrefix(normalize(nodeId), CLASS_PREFIX);
        if (classId.isBlank()) {
            return false;
        }
        return viewModel.moduleIdForClass(classId)
                .map(UmlClassCanvasElementIds::moduleLayoutId)
                .filter(selected::contains)
                .isPresent();
    }
    @Override
    public Set<String> previewNodeIdsForDraggedNode(String draggedNodeId, Set<String> selectedNodeIds) {
        Set<String> base = selectedNodeIds == null || selectedNodeIds.isEmpty()
                ? Set.of(normalize(draggedNodeId))
                : selectedNodeIds;
        Set<String> result = new LinkedHashSet<>(base);
        for (String selectedId : base) {
            String moduleId = rawIdAfterPrefix(normalize(selectedId), MODULE_PREFIX);
            if (!moduleId.isBlank()) {
                addClassIdsForModule(result, moduleId);
            }
        }
        return result;
    }
    private void addClassIdsForModule(Set<String> target, String moduleId) {
        for (UmlClassNode umlClass : viewModel.classes()) {
            if (umlClass.moduleId().equals(moduleId)) {
                target.add(classLayoutId(umlClass.id()));
            }
        }
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
        if (!normalized.startsWith(RELATION_PREFIX)) {
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
        for (UmlModuleGroup module : viewModel.modules()) {
            NodeLayout layout = viewModel.layoutForModule(module);
            minX = Math.min(minX, layout.x());
            minY = Math.min(minY, layout.y());
            maxX = Math.max(maxX, layout.x() + layout.width());
            maxY = Math.max(maxY, layout.y() + layout.height());
        }
        for (UmlClassNode umlClass : viewModel.classes()) {
            NodeLayout layout = viewModel.layoutForClass(umlClass);
            minX = Math.min(minX, layout.x());
            minY = Math.min(minY, layout.y());
            maxX = Math.max(maxX, layout.x() + layout.width());
            maxY = Math.max(maxY, layout.y() + layout.height());
        }
        if (minX == Double.MAX_VALUE) {
            return CanvasBounds.of(0, 0, 980, 680);
        }
        return CanvasBounds.of(
                Math.max(0.0, minX - EXPORT_PADDING),
                Math.max(0.0, minY - EXPORT_PADDING),
                Math.max(980.0, maxX - minX + EXPORT_PADDING * 2.0),
                Math.max(680.0, maxY - minY + EXPORT_PADDING * 2.0));
    }

    private void selectModuleNode(String moduleId, boolean additive) {
        String layoutId = moduleLayoutId(moduleId);
        interactionState.selectNode(layoutId, additive);
        viewModel.selectModuleById(moduleId);
    }

    private void selectClassNode(String classId, boolean additive) {
        String layoutId = classLayoutId(classId);
        interactionState.selectNode(layoutId, additive);
        viewModel.selectClassNodeById(classId);
    }

    private void selectFirstPropertyNode(String layoutId) {
        String normalized = normalize(layoutId);
        if (normalized.startsWith(MODULE_PREFIX)) {
            viewModel.selectModuleById(rawIdAfterPrefix(normalized, MODULE_PREFIX));
            return;
        }
        if (normalized.startsWith(CLASS_PREFIX)) {
            viewModel.selectClassNodeById(rawIdAfterPrefix(normalized, CLASS_PREFIX));
        }
    }

    private void syncSelectionFromViewModel() {
        if (interactionState.shouldPreserveManualBendPointSelection()) {
            return;
        }
        if (viewModel.selectedModuleProperty().get() != null) {
            String selectedId = moduleLayoutId(viewModel.selectedModuleProperty().get().id());
            interactionState.syncSingleNode(selectedId);
            return;
        }
        if (viewModel.selectedClassProperty().get() != null) {
            String selectedId = classLayoutId(viewModel.selectedClassProperty().get().id());
            interactionState.syncSingleNode(selectedId);
            return;
        }
        if (viewModel.selectedRelationProperty().get() != null) {
            String selectedId = relationLayoutId(viewModel.selectedRelationProperty().get().id());
            interactionState.syncSingleConnector(selectedId);
            return;
        }
        interactionState.clearSelection();
    }

    private static String subtitleFor(UmlModuleGroup module) {
        return firstNonBlank(module.path(), module.description(), "Paquete / módulo");
    }

    private static String subtitleFor(UmlClassNode node) {
        String detail = firstNonBlank(node.packageName(), node.responsibility(), "Sin paquete definido");
        return node.kind().displayName() + "\n" + detail;
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
