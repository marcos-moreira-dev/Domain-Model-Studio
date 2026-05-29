package com.marcosmoreira.domainmodelstudio.presentation.freegraph;

import com.marcosmoreira.domainmodelstudio.application.freegraph.AddFreeGraphEdgeUseCase;
import com.marcosmoreira.domainmodelstudio.application.freegraph.AddFreeGraphNodeUseCase;
import com.marcosmoreira.domainmodelstudio.application.freegraph.RemoveFreeGraphItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.freegraph.UpdateFreeGraphEdgeUseCase;
import com.marcosmoreira.domainmodelstudio.application.freegraph.UpdateFreeGraphNodeUseCase;
import com.marcosmoreira.domainmodelstudio.application.freegraph.ValidateFreeGraphUseCase;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutService;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayerOrderCommand;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.ProjectChangeSupport;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphNode;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportPngAction;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualProjectPatchSupport;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualDiagramViewActions;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualLayerOrderViewModelSupport;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.BooleanSupplier;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualNodeSizeCommand;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualNodeSizeViewModelSupport;

/** Estado y operaciones transversales del ViewModel de Grafo libre. */
abstract class FreeGraphViewModelCore {

    protected final AddFreeGraphNodeUseCase addNodeUseCase;
    protected final AddFreeGraphEdgeUseCase addEdgeUseCase;
    protected final UpdateFreeGraphNodeUseCase updateNodeUseCase;
    protected final UpdateFreeGraphEdgeUseCase updateEdgeUseCase;
    protected final RemoveFreeGraphItemUseCase removeItemUseCase;
    protected final ValidateFreeGraphUseCase validateUseCase;
    protected final VisualLayoutService visualLayoutService = new VisualLayoutService();
    protected final Consumer<String> statusConsumer;
    protected final ObservableList<FreeGraphNode> nodes = FXCollections.observableArrayList();
    protected final ObservableList<FreeGraphEdge> edges = FXCollections.observableArrayList();
    protected final ObjectProperty<FreeGraphNode> selectedNode = new SimpleObjectProperty<>();
    protected final ObjectProperty<FreeGraphEdge> selectedEdge = new SimpleObjectProperty<>();
    protected final ObjectProperty<FreeGraphCanvasTool> activeCanvasTool = new SimpleObjectProperty<>(FreeGraphCanvasTool.SELECT);
    protected DiagramProject currentProject;
    protected FreeGraphDocument currentDocument;
    protected String pendingEdgeSourceNodeId = "";
    private final ProjectChangeSupport projectChangeSupport = new ProjectChangeSupport();
    private final VisualDiagramViewActions viewActions;

    FreeGraphViewModelCore(
            AddFreeGraphNodeUseCase addNodeUseCase,
            AddFreeGraphEdgeUseCase addEdgeUseCase,
            UpdateFreeGraphNodeUseCase updateNodeUseCase,
            UpdateFreeGraphEdgeUseCase updateEdgeUseCase,
            RemoveFreeGraphItemUseCase removeItemUseCase,
            ValidateFreeGraphUseCase validateUseCase,
            Consumer<String> statusConsumer
    ) {
        this.addNodeUseCase = Objects.requireNonNull(addNodeUseCase, "addNodeUseCase");
        this.addEdgeUseCase = Objects.requireNonNull(addEdgeUseCase, "addEdgeUseCase");
        this.updateNodeUseCase = Objects.requireNonNull(updateNodeUseCase, "updateNodeUseCase");
        this.updateEdgeUseCase = Objects.requireNonNull(updateEdgeUseCase, "updateEdgeUseCase");
        this.removeItemUseCase = Objects.requireNonNull(removeItemUseCase, "removeItemUseCase");
        this.validateUseCase = Objects.requireNonNull(validateUseCase, "validateUseCase");
        this.statusConsumer = Objects.requireNonNull(statusConsumer, "statusConsumer");
        this.viewActions = VisualDiagramViewActions.forFreeGraph(this::active, this.statusConsumer, "Grafo libre todavía no tiene una vista PNG registrada.");
    }

    public void registerProjectChangeListener(Consumer<DiagramProject> listener) {
        projectChangeSupport.registerProjectChangeListener(listener);
    }
    public void registerPngExportAction(ExportPngAction action) { viewActions.registerPngExportAction(action); }
    public void registerDiagramFitAction(Runnable action) { viewActions.registerDiagramFitAction(action); }
    public void registerDiagramCenterAction(Runnable action) { viewActions.registerDiagramCenterAction(action); }
    public void registerDiagramRefreshAction(Runnable action) { viewActions.registerDiagramRefreshAction(action); }
    public void registerDeleteSelectedBendPointAction(BooleanSupplier action) { viewActions.registerDeleteSelectedBendPointAction(action); }
    public void fitDiagramView() { viewActions.fitDiagramView(); }
    public void centerDiagramView() { viewActions.centerDiagramView(); }
    public boolean deleteSelectedBendPoint() { return viewActions.deleteSelectedBendPoint(); }
    public void exportVisualAsPng(Path targetFile) throws IOException { viewActions.exportVisualAsPng(targetFile); }

    public ObservableList<FreeGraphNode> nodes() {
        return nodes;
    }

    public ObservableList<FreeGraphEdge> edges() {
        return edges;
    }

    public ObjectProperty<FreeGraphNode> selectedNodeProperty() {
        return selectedNode;
    }

    public ObjectProperty<FreeGraphEdge> selectedEdgeProperty() {
        return selectedEdge;
    }

    public ObjectProperty<FreeGraphCanvasTool> activeCanvasToolProperty() {
        return activeCanvasTool;
    }

    public FreeGraphCanvasTool activeCanvasTool() {
        return activeCanvasTool.get();
    }

    public String pendingEdgeSourceNodeId() {
        return pendingEdgeSourceNodeId;
    }

    public void activateCanvasTool(FreeGraphCanvasTool tool) {
        FreeGraphCanvasTool safeTool = tool == null ? FreeGraphCanvasTool.SELECT : tool;
        activeCanvasTool.set(safeTool);
        pendingEdgeSourceNodeId = "";
        statusConsumer.accept(safeTool.displayName() + ": " + safeTool.helpText());
    }

    public void activateSelectionTool() {
        activateCanvasTool(FreeGraphCanvasTool.SELECT);
    }

    public void activateAddNodeTool() {
        activateCanvasTool(FreeGraphCanvasTool.ADD_NODE);
    }

    public void activateAddEdgeTool() {
        activateCanvasTool(FreeGraphCanvasTool.ADD_EDGE);
    }

    public void toggleAddNodeTool() {
        activateCanvasTool(activeCanvasTool.get() == FreeGraphCanvasTool.ADD_NODE
                ? FreeGraphCanvasTool.SELECT
                : FreeGraphCanvasTool.ADD_NODE);
    }

    public void toggleAddEdgeTool() {
        activateCanvasTool(activeCanvasTool.get() == FreeGraphCanvasTool.ADD_EDGE
                ? FreeGraphCanvasTool.SELECT
                : FreeGraphCanvasTool.ADD_EDGE);
    }

    public FreeGraphDocument currentDocument() {
        return currentDocument;
    }

    public DiagramProject currentProject() {
        return currentProject;
    }

    public boolean active() {
        return currentProject != null && currentDocument != null;
    }

    public NodeLayout layoutForNode(FreeGraphNode node) {
        Objects.requireNonNull(node, "node");
        ensureCurrentLayout();
        return visualLayoutService.nodeLayout(currentProject, VisualElementLayoutIds.freeGraphNode(node.id()))
                .orElseThrow(() -> new IllegalStateException("No existe layout para el nodo: " + node.id()));
    }

    public Optional<ConnectorLayout> layoutForEdge(FreeGraphEdge edge) {
        Objects.requireNonNull(edge, "edge");
        if (currentProject == null || currentDocument == null) {
            return Optional.empty();
        }
        ensureCurrentLayout();
        return visualLayoutService.connectorLayout(currentProject, VisualElementLayoutIds.freeGraphEdge(edge.id()));
    }

    public Optional<ConnectorLayout> layoutForConnector(DiagramElementId connectorId) {
        Objects.requireNonNull(connectorId, "connectorId");
        if (currentProject == null || currentDocument == null) {
            return Optional.empty();
        }
        ensureCurrentLayout();
        return visualLayoutService.connectorLayout(currentProject, connectorId);
    }

    public void selectNodeById(String nodeId) {
        String normalized = clean(nodeId);
        if (normalized.isBlank()) {
            return;
        }
        nodes.stream()
                .filter(node -> node.id().equals(normalized))
                .findFirst()
                .ifPresent(node -> {
                    selectedNode.set(node);
                    selectedEdge.set(null);
                });
    }

    public void selectEdgeById(String edgeId) {
        String normalized = clean(edgeId);
        if (normalized.isBlank()) {
            return;
        }
        edges.stream()
                .filter(edge -> edge.id().equals(normalized))
                .findFirst()
                .ifPresent(edge -> {
                    selectedEdge.set(edge);
                    selectedNode.set(null);
                });
    }

    public void clearPropertySelection() {
        selectedNode.set(null);
        selectedEdge.set(null);
        if (activeCanvasTool.get() != FreeGraphCanvasTool.ADD_EDGE) {
            pendingEdgeSourceNodeId = "";
        }
    }

    public boolean resizeSelectedElement(VisualNodeSizeCommand command) {
        FreeGraphNode node = selectedNode.get();
        DiagramElementId layoutId = node == null ? null : VisualElementLayoutIds.freeGraphNode(node.id());
        boolean resized = VisualNodeSizeViewModelSupport.resizeSelectedNode(
                visualLayoutService, currentProject, layoutId, command,
                project -> currentProject = project, this::notifyProjectChanged, statusConsumer);
        if (resized) {
            viewActions.refreshDiagramView();
        }
        return resized;
    }

    public void loadProject(DiagramProject project) {
        projectChangeSupport.runLoading(() -> {
            DiagramProject safeProject = Objects.requireNonNull(project, "project");
            currentProject = visualLayoutService.ensureVisualLayout(safeProject);
            currentDocument = safeProject.freeGraph()
                    .orElseGet(() -> FreeGraphDocument.blank(safeProject.metadata().title()));
            refreshLists();
            selectedNode.set(nodes.isEmpty() ? null : nodes.get(0));
            selectedEdge.set(null);
            pendingEdgeSourceNodeId = "";
        });
    }

    public void clear() {
        projectChangeSupport.runLoading(() -> {
            currentProject = null;
            currentDocument = null;
            nodes.clear();
            edges.clear();
            selectedNode.set(null);
            selectedEdge.set(null);
            pendingEdgeSourceNodeId = "";
        });
    }

    public void moveNodeTo(String nodeId, double x, double y) {
        moveNodeTo(nodeId, x, y, true);
    }

    public void moveNodeTo(String nodeId, double x, double y, boolean announce) {
        if (!ensureProjectForLayout("No hay grafo abierto.")) {
            return;
        }
        try {
            currentProject = visualLayoutService.moveNodeTo(currentProject, VisualElementLayoutIds.freeGraphNode(nodeId), x, y);
            notifyProjectChanged();
            if (announce) {
                statusConsumer.accept("Nodo movido.");
            }
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo mover nodo: " + exception.getMessage());
        }
    }


    public boolean reorderSelectedElement(VisualLayerOrderCommand command) {
        FreeGraphNode node = selectedNode.get();
        DiagramElementId layoutId = node == null ? null : VisualElementLayoutIds.freeGraphNode(node.id());
        boolean reordered = VisualLayerOrderViewModelSupport.reorderSelectedNode(
                visualLayoutService,
                currentProject,
                layoutId,
                command,
                project -> currentProject = project,
                this::notifyProjectChanged,
                statusConsumer);
        if (reordered) {
            viewActions.refreshDiagramView();
        }
        return reordered;
    }

    public Optional<Integer> addConnectorBendPoint(DiagramElementId connectorId, double x, double y) {
        if (!ensureProjectForLayout("No hay grafo abierto.")) {
            return Optional.empty();
        }
        try {
            currentProject = visualLayoutService.addBendPoint(currentProject, connectorId, x, y);
            notifyProjectChanged();
            int index = visualLayoutService.bendPointIndexAt(currentProject, connectorId, x, y).orElse(-1);
            statusConsumer.accept("Punto intermedio agregado.");
            return index < 0 ? Optional.empty() : Optional.of(index);
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo agregar punto intermedio: " + exception.getMessage());
            return Optional.empty();
        }
    }

    public void moveConnectorBendPointTo(DiagramElementId connectorId, int bendPointIndex, double x, double y) {
        if (!ensureProjectForLayout("No hay grafo abierto.")) {
            return;
        }
        try {
            currentProject = visualLayoutService.moveBendPointTo(currentProject, connectorId, bendPointIndex, x, y);
            notifyProjectChanged();
            statusConsumer.accept("Punto intermedio actualizado.");
        } catch (IllegalArgumentException | IndexOutOfBoundsException exception) {
            statusConsumer.accept("No se pudo mover punto intermedio: " + exception.getMessage());
        }
    }

    public void removeConnectorBendPoint(DiagramElementId connectorId, int bendPointIndex) {
        if (!ensureProjectForLayout("No hay grafo abierto.")) {
            return;
        }
        try {
            currentProject = visualLayoutService.removeBendPoint(currentProject, connectorId, bendPointIndex);
            notifyProjectChanged();
            statusConsumer.accept("Punto intermedio eliminado.");
        } catch (IllegalArgumentException | IndexOutOfBoundsException exception) {
            statusConsumer.accept("No se pudo eliminar punto intermedio: " + exception.getMessage());
        }
    }

    public void moveConnectorLabelBy(DiagramElementId connectorId, double deltaX, double deltaY) {
        if (!ensureProjectForLayout("No hay grafo abierto.")) {
            return;
        }
        try {
            currentProject = visualLayoutService.moveConnectorLabelBy(currentProject, connectorId, deltaX, deltaY);
            notifyProjectChanged();
            statusConsumer.accept("Etiqueta de relación actualizada.");
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo mover etiqueta: " + exception.getMessage());
        }
    }

    public void patchCurrentProject(java.util.function.UnaryOperator<DiagramProject> patch, String statusMessage) {
        VisualProjectPatchSupport.apply(
                currentProject,
                patch,
                statusConsumer,
                statusMessage,
                updatedProject -> currentProject = updatedProject,
                this::notifyProjectChanged
        );
    }

    protected void applyDocument(FreeGraphDocument updatedDocument, String statusMessage) {
        currentDocument = Objects.requireNonNull(updatedDocument, "updatedDocument");
        refreshLists();
        if (currentProject != null) {
            currentProject = visualLayoutService.ensureVisualLayout(currentProject.withFreeGraph(updatedDocument));
            notifyProjectChanged();
        }
        statusConsumer.accept(statusMessage);
    }

    protected void refreshLists() {
        nodes.setAll(currentDocument == null ? List.of() : currentDocument.nodes());
        edges.setAll(currentDocument == null ? List.of() : currentDocument.edges());
    }

    protected void selectLastNode() {
        if (!nodes.isEmpty()) {
            selectedNode.set(nodes.get(nodes.size() - 1));
            selectedEdge.set(null);
        }
    }

    protected void selectLastEdge() {
        if (!edges.isEmpty()) {
            selectedEdge.set(edges.get(edges.size() - 1));
            selectedNode.set(null);
        }
    }

    protected FreeGraphNode firstDifferentNode(String nodeId) {
        return nodes.stream().filter(node -> !node.id().equals(nodeId)).findFirst().orElse(null);
    }

    protected boolean restoreNodeSelection(String nodeId) {
        for (FreeGraphNode node : nodes) {
            if (node.id().equals(nodeId)) {
                selectedNode.set(node);
                selectedEdge.set(null);
                return true;
            }
        }
        return false;
    }

    protected boolean restoreEdgeSelection(String edgeId) {
        for (FreeGraphEdge edge : edges) {
            if (edge.id().equals(edgeId)) {
                selectedEdge.set(edge);
                selectedNode.set(null);
                return true;
            }
        }
        return false;
    }

    protected void ensureCurrentLayout() {
        if (currentProject == null) {
            throw new IllegalStateException("No hay proyecto activo para consultar layout visual.");
        }
        currentProject = visualLayoutService.ensureVisualLayout(currentProject);
    }

    protected void notifyProjectChanged() {
        projectChangeSupport.notifyChanged(currentProject);
    }

    protected boolean ensureProjectForLayout(String message) {
        if (currentProject == null || currentDocument == null) {
            statusConsumer.accept(message);
            return false;
        }
        currentProject = visualLayoutService.ensureVisualLayout(currentProject);
        return true;
    }

    protected boolean ensureDocument(String message) {
        if (currentDocument == null) {
            statusConsumer.accept(message);
            return false;
        }
        return true;
    }

    protected static String clean(String value) {
        return value == null ? "" : value.strip();
    }
}
