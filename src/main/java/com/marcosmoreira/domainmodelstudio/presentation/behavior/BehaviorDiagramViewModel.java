package com.marcosmoreira.domainmodelstudio.presentation.behavior;
import com.marcosmoreira.domainmodelstudio.application.behavior.AddBehaviorEdgeUseCase;
import com.marcosmoreira.domainmodelstudio.application.behavior.AddBehaviorNodeUseCase;
import com.marcosmoreira.domainmodelstudio.application.behavior.BehaviorDiagramValidationResult;
import com.marcosmoreira.domainmodelstudio.application.behavior.MoveSequenceMessageUseCase;
import com.marcosmoreira.domainmodelstudio.application.behavior.RemoveBehaviorItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.behavior.UpdateBehaviorEdgeUseCase;
import com.marcosmoreira.domainmodelstudio.application.behavior.UpdateBehaviorNodeUseCase;
import com.marcosmoreira.domainmodelstudio.application.behavior.ValidateBehaviorDiagramUseCase;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutService;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayerOrderCommand;
import com.marcosmoreira.domainmodelstudio.application.visual.UseCaseBoundaryLayoutSupport;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.ProjectChangeSupport;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.BooleanSupplier;
import java.io.IOException;
import java.nio.file.Path;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportPngAction;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualProjectPatchSupport;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualDiagramViewActions;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualLayerOrderViewModelSupport;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualNodeSizeCommand;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualNodeSizeViewModelSupport;
/** ViewModel común de BPMN básico y UML de comportamiento. */
public final class BehaviorDiagramViewModel {
    private final AddBehaviorNodeUseCase addNodeUseCase;
    private final AddBehaviorEdgeUseCase addEdgeUseCase;
    private final UpdateBehaviorNodeUseCase updateNodeUseCase;
    private final UpdateBehaviorEdgeUseCase updateEdgeUseCase;
    private final RemoveBehaviorItemUseCase removeItemUseCase;
    private final ValidateBehaviorDiagramUseCase validateUseCase;
    private final VisualLayoutService visualLayoutService = new VisualLayoutService();
    private final UseCaseBoundaryLayoutSupport useCaseBoundaryLayoutSupport = new UseCaseBoundaryLayoutSupport();
    private final MoveSequenceMessageUseCase moveSequenceMessageUseCase = new MoveSequenceMessageUseCase();
    private final Consumer<String> statusConsumer;
    private final ObservableList<BehaviorNode> nodes = FXCollections.observableArrayList();
    private final ObservableList<BehaviorEdge> edges = FXCollections.observableArrayList();
    private final ObservableList<BehaviorNodeKind> availableNodeKinds = FXCollections.observableArrayList();
    private final ObservableList<BehaviorEdgeKind> availableEdgeKinds = FXCollections.observableArrayList();
    private final ObjectProperty<BehaviorNode> selectedNode = new SimpleObjectProperty<>();
    private final ObjectProperty<BehaviorEdge> selectedEdge = new SimpleObjectProperty<>();
    private DiagramProject currentProject;
    private BehaviorDiagramDocument currentDocument;
    private final ProjectChangeSupport projectChangeSupport = new ProjectChangeSupport();
    private final VisualDiagramViewActions viewActions;
    public BehaviorDiagramViewModel(
            AddBehaviorNodeUseCase addNodeUseCase,
            AddBehaviorEdgeUseCase addEdgeUseCase,
            UpdateBehaviorNodeUseCase updateNodeUseCase,
            UpdateBehaviorEdgeUseCase updateEdgeUseCase,
            RemoveBehaviorItemUseCase removeItemUseCase,
            ValidateBehaviorDiagramUseCase validateUseCase,
            Consumer<String> statusConsumer
    ) {
        this.addNodeUseCase = Objects.requireNonNull(addNodeUseCase, "addNodeUseCase");
        this.addEdgeUseCase = Objects.requireNonNull(addEdgeUseCase, "addEdgeUseCase");
        this.updateNodeUseCase = Objects.requireNonNull(updateNodeUseCase, "updateNodeUseCase");
        this.updateEdgeUseCase = Objects.requireNonNull(updateEdgeUseCase, "updateEdgeUseCase");
        this.removeItemUseCase = Objects.requireNonNull(removeItemUseCase, "removeItemUseCase");
        this.validateUseCase = Objects.requireNonNull(validateUseCase, "validateUseCase");
        this.statusConsumer = Objects.requireNonNull(statusConsumer, "statusConsumer");
        this.viewActions = VisualDiagramViewActions.forGenericDiagram(this::active, this.statusConsumer, "El diagrama de comportamiento todavía no tiene una vista PNG registrada.");
    }
    public void registerProjectChangeListener(Consumer<DiagramProject> listener) {
        projectChangeSupport.registerProjectChangeListener(listener);
    }
    public void registerPngExportAction(ExportPngAction action) { viewActions.registerPngExportAction(action); }
    public void registerDiagramFitAction(Runnable action) { viewActions.registerDiagramFitAction(action); }
    public void registerDiagramCenterAction(Runnable action) { viewActions.registerDiagramCenterAction(action); }
    public void registerDiagramRefreshAction(Runnable action) { viewActions.registerDiagramRefreshAction(action); }
    public void registerDeleteSelectedBendPointAction(BooleanSupplier action) { viewActions.registerDeleteSelectedBendPointAction(action); }
    public void registerVisualCommentToolAction(Runnable action) { viewActions.registerVisualCommentToolAction(action); }
    public void registerVisualCommentLayerOrderAction(java.util.function.Function<VisualLayerOrderCommand, Boolean> action) { viewActions.registerVisualCommentLayerOrderAction(action); }
    public void registerVisualCommentSizeAction(java.util.function.Function<VisualNodeSizeCommand, Boolean> action) { viewActions.registerVisualCommentSizeAction(action); }
    public void fitDiagramView() { viewActions.fitDiagramView(); }
    public void centerDiagramView() { viewActions.centerDiagramView(); }
    public boolean deleteSelectedBendPoint() { return viewActions.deleteSelectedBendPoint(); }
    public void activateVisualCommentTool() { viewActions.activateVisualCommentTool(); }
    public void exportVisualAsPng(Path targetFile) throws IOException { viewActions.exportVisualAsPng(targetFile); }
    public ObservableList<BehaviorNode> nodes() { return nodes; }
    public ObservableList<BehaviorEdge> edges() { return edges; }
    public ObservableList<BehaviorNodeKind> availableNodeKinds() { return availableNodeKinds; }
    public ObservableList<BehaviorEdgeKind> availableEdgeKinds() { return availableEdgeKinds; }
    public ObjectProperty<BehaviorNode> selectedNodeProperty() { return selectedNode; }
    public ObjectProperty<BehaviorEdge> selectedEdgeProperty() { return selectedEdge; }
    public DiagramProject currentProject() { return currentProject; }
    public BehaviorDiagramDocument currentDocument() { return currentDocument; }
    public boolean active() { return currentProject != null && currentDocument != null; }
    public BehaviorDiagramKind currentKind() { return currentDocument == null ? BehaviorDiagramKind.BPMN_BASIC : currentDocument.diagramKind(); }
    public String title() { return currentDocument == null ? "Diagrama de comportamiento" : currentDocument.diagramKind().displayName(); }
    public void loadProject(DiagramProject project) {
        projectChangeSupport.runLoading(() -> {
            currentProject = visualLayoutService.ensureVisualLayout(Objects.requireNonNull(project, "project"));
            BehaviorDiagramKind kind = BehaviorDiagramKind.fromDiagramTypeId(currentProject.metadata().diagramTypeId());
            currentDocument = currentProject.behaviorDiagram().orElseGet(() -> BehaviorDiagramDocument.blank(currentProject.metadata().title(), kind));
            refreshLists();
            selectedNode.set(nodes.isEmpty() ? null : nodes.get(0));
            selectedEdge.set(null);
        });
    }
    public void clear() {
        projectChangeSupport.runLoading(() -> {
            currentProject = null;
            currentDocument = null;
            nodes.clear();
            edges.clear();
            availableNodeKinds.clear();
            availableEdgeKinds.clear();
            selectedNode.set(null);
            selectedEdge.set(null);
        });
    }
    public void addNode(BehaviorNodeKind kind) {
        if (!ensureDocument("No hay diagrama abierto para agregar elementos.")) return;
        try {
            applyDocument(addNodeUseCase.add(currentDocument, kind, kind == null ? currentKind().defaultNodeKind().displayName() : kind.displayName()), "Elemento agregado.");
            selectLastNode();
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo agregar elemento: " + exception.getMessage());
        }
    }
    public void addEdge(BehaviorEdgeKind kind) {
        if (!ensureDocument("No hay diagrama abierto para agregar relaciones.")) return;
        BehaviorNode source = selectedNode.get();
        BehaviorNode target = firstDifferentNode(source == null ? "" : source.id());
        if (source == null || target == null) {
            statusConsumer.accept("Crea y selecciona al menos dos elementos para agregar una relación.");
            return;
        }
        try {
            applyDocument(addEdgeUseCase.add(currentDocument, source.id(), target.id(), kind), "Relación agregada.");
            selectLastEdge();
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo agregar relación: " + exception.getMessage());
        }
    }
    public void removeSelected() {
        if (!ensureDocument("No hay diagrama abierto para eliminar elementos.")) return;
        BehaviorEdge edge = selectedEdge.get();
        BehaviorNode node = selectedNode.get();
        try {
            if (edge != null) {
                applyDocument(removeItemUseCase.removeEdge(currentDocument, edge.id()), "Relación eliminada.");
                selectedEdge.set(null);
                return;
            }
            if (node != null) {
                applyDocument(removeItemUseCase.removeNode(currentDocument, node.id()), "Elemento eliminado.");
                selectedNode.set(nodes.isEmpty() ? null : nodes.get(0));
            }
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo eliminar: " + exception.getMessage());
        }
    }
    public void moveSelectedSequenceMessageUp() {
        moveSelectedSequenceMessage(MoveSequenceMessageUseCase.Direction.UP);
    }
    public void moveSelectedSequenceMessageDown() {
        moveSelectedSequenceMessage(MoveSequenceMessageUseCase.Direction.DOWN);
    }
    public boolean sequenceDiagramActive() {
        return currentKind() == BehaviorDiagramKind.UML_SEQUENCE;
    }
    private void moveSelectedSequenceMessage(MoveSequenceMessageUseCase.Direction direction) {
        if (!ensureDocument("No hay secuencia abierta para reordenar mensajes.")) return;
        if (currentKind() != BehaviorDiagramKind.UML_SEQUENCE) {
            statusConsumer.accept("El orden temporal solo aplica a UML Secuencia.");
            return;
        }
        BehaviorEdge edge = selectedEdge.get();
        if (edge == null) {
            statusConsumer.accept("Selecciona un mensaje para cambiar su orden temporal.");
            return;
        }
        try {
            applyDocument(moveSequenceMessageUseCase.move(currentDocument, edge.id(), direction), "Orden temporal actualizado.");
            restoreEdgeSelection(edge.id());
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo reordenar mensaje: " + exception.getMessage());
        }
    }
    public void applyNodeChanges(BehaviorNodeKind kind, String displayName, String owner, String description, String notes, int orderIndex) {
        BehaviorNode node = selectedNode.get();
        if (node == null || !ensureDocument("No hay elemento seleccionado.")) return;
        try {
            applyDocument(updateNodeUseCase.update(currentDocument, node.id(), kind, displayName, owner, description, notes, orderIndex), "Elemento actualizado.");
            restoreNodeSelection(node.id());
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo actualizar elemento: " + exception.getMessage());
        }
    }
    public void applyEdgeChanges(BehaviorNode source, BehaviorNode target, BehaviorEdgeKind kind, String label, String condition, String notes) {
        BehaviorEdge edge = selectedEdge.get();
        if (edge == null || !ensureDocument("No hay relación seleccionada.")) return;
        if (source == null || target == null) {
            statusConsumer.accept("Selecciona origen y destino para la relación.");
            return;
        }
        try {
            applyDocument(updateEdgeUseCase.update(currentDocument, edge.id(), source.id(), target.id(), kind, label, condition, notes), "Relación actualizada.");
            restoreEdgeSelection(edge.id());
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo actualizar relación: " + exception.getMessage());
        }
    }
    public NodeLayout layoutForNode(BehaviorNode node) {
        Objects.requireNonNull(node, "node");
        ensureCurrentLayout();
        return visualLayoutService.nodeLayout(currentProject, VisualElementLayoutIds.behaviorNode(node.id()))
                .orElseThrow(() -> new IllegalStateException("No existe layout para el elemento de comportamiento: " + node.id()));
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
        if (nodeId == null || nodeId.isBlank()) {
            return;
        }
        selectedNode.set(nodes.stream().filter(node -> node.id().equals(nodeId.strip())).findFirst().orElse(null));
        if (selectedNode.get() != null) {
            selectedEdge.set(null);
        }
    }
    public void selectEdgeById(String edgeId) {
        if (edgeId == null || edgeId.isBlank()) {
            return;
        }
        selectedEdge.set(edges.stream().filter(edge -> edge.id().equals(edgeId.strip())).findFirst().orElse(null));
        if (selectedEdge.get() != null) {
            selectedNode.set(null);
        }
    }
    public void clearPropertySelection() {
        selectedNode.set(null);
        selectedEdge.set(null);
    }
    public Optional<Integer> addConnectorBendPoint(DiagramElementId connectorId, double x, double y) {
        if (!ensureProjectForLayout("No hay diagrama de comportamiento abierto.")) {
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
        if (!ensureProjectForLayout("No hay diagrama de comportamiento abierto.")) {
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
    public void moveConnectorLabelBy(DiagramElementId connectorId, double deltaX, double deltaY) {
        Objects.requireNonNull(connectorId, "connectorId");
        if (currentProject != null) {
            currentProject = visualLayoutService.moveConnectorLabelBy(currentProject, connectorId, deltaX, deltaY);
            notifyProjectChanged();
        }
    }
    public void removeConnectorBendPoint(DiagramElementId connectorId, int bendPointIndex) {
        if (!ensureProjectForLayout("No hay diagrama de comportamiento abierto.")) {
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
    public void moveNodeTo(String nodeId, double x, double y) {
        if (!ensureProjectForLayout("No hay diagrama de comportamiento abierto.")) {
            return;
        }
        try {
            DiagramElementId layoutId = VisualElementLayoutIds.behaviorNode(nodeId);
            currentProject = currentKind() == BehaviorDiagramKind.UML_USE_CASE
                    ? useCaseBoundaryLayoutSupport.moveNode(currentProject, currentKind(), nodes, nodeId, x, y)
                    : visualLayoutService.moveNodeTo(currentProject, layoutId, x, y);
            if (currentKind() == BehaviorDiagramKind.UML_SEQUENCE
                    && BehaviorSequenceFragmentManualLayout.isFragmentNode(nodes, nodeId)) {
                currentProject = BehaviorSequenceFragmentManualLayout.lockPosition(currentProject, layoutId);
                statusConsumer.accept("Fragmento UML Secuencia movido manualmente.");
            } else {
                statusConsumer.accept("Elemento y contención visual actualizados.");
            }
            notifyProjectChanged();
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo mover elemento: " + exception.getMessage());
        }
    }
    public void resizeSequenceFragmentTo(String nodeId, double width, double height) {
        if (!ensureProjectForLayout("No hay diagrama de secuencia abierto.")) return;
        try {
            currentProject = BehaviorSequenceFragmentManualLayout.resize(
                    currentProject, currentKind(), nodes, nodeId, width, height, visualLayoutService, statusConsumer);
            notifyProjectChanged();
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo redimensionar fragmento: " + exception.getMessage());
        }
    }
    public boolean reorderSelectedElement(VisualLayerOrderCommand command) {
        if (viewActions.reorderSelectedVisualComment(command)) {
            viewActions.refreshDiagramView();
            return true;
        }
        BehaviorNode node = selectedNode.get();
        DiagramElementId layoutId = node == null ? null : VisualElementLayoutIds.behaviorNode(node.id());
        boolean reordered = VisualLayerOrderViewModelSupport.reorderSelectedNode(visualLayoutService, currentProject, layoutId, command,
                project -> currentProject = project, this::notifyProjectChanged, statusConsumer);
        if (reordered) {
            viewActions.refreshDiagramView();
        }
        return reordered;
    }
    public boolean resizeSelectedElement(VisualNodeSizeCommand command) {
        if (viewActions.resizeSelectedVisualComment(command)) {
            viewActions.refreshDiagramView();
            return true;
        }
        BehaviorNode node = selectedNode.get();
        DiagramElementId layoutId = node == null ? null : VisualElementLayoutIds.behaviorNode(node.id());
        boolean resized = VisualNodeSizeViewModelSupport.resizeSelectedNode(
                visualLayoutService, currentProject, layoutId, command,
                project -> currentProject = project, this::notifyProjectChanged, statusConsumer);
        if (resized) {
            viewActions.refreshDiagramView();
        }
        return resized;
    }

    public void reorganizeLayout() { if (!ensureProjectForLayout("No hay diagrama activo para autoorganizar.")) return; currentProject = visualLayoutService.regenerateVisualLayout(currentProject.withBehaviorDiagram(currentDocument)); notifyProjectChanged(); statusConsumer.accept("Diagrama autoorganizado según su semántica visual."); }
    public BehaviorDiagramValidationResult validateDocument() {
        if (!ensureDocument("No hay diagrama abierto para validar.")) {
            return new BehaviorDiagramValidationResult(List.of("No hay diagrama abierto."));
        }
        BehaviorDiagramValidationResult result = validateUseCase.validate(currentDocument);
        statusConsumer.accept(result.summary());
        return result;
    }
    public String nodeLabel(String nodeId) {
        return currentDocument == null ? nodeId : currentDocument.nodeById(nodeId).map(BehaviorNode::displayName).orElse(nodeId);
    }
    private void applyDocument(BehaviorDiagramDocument updatedDocument, String statusMessage) {
        currentDocument = updatedDocument;
        refreshLists();
        if (currentProject != null) {
            currentProject = visualLayoutService.ensureVisualLayout(currentProject.withBehaviorDiagram(updatedDocument));
            projectChangeSupport.notifyChanged(currentProject);
        }
        statusConsumer.accept(statusMessage);
    }
    private void ensureCurrentLayout() {
        if (currentProject != null) {
            currentProject = visualLayoutService.ensureVisualLayout(currentProject);
        }
    }
    private boolean ensureProjectForLayout(String message) {
        if (currentProject == null || currentDocument == null) {
            statusConsumer.accept(message);
            return false;
        }
        ensureCurrentLayout();
        return true;
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
    private void notifyProjectChanged() {
        projectChangeSupport.notifyChanged(currentProject);
    }
    private void refreshLists() {
        nodes.setAll(currentDocument == null ? List.of() : currentDocument.nodes());
        edges.setAll(currentDocument == null ? List.of() : currentDocument.edges());
        availableNodeKinds.setAll(currentDocument == null ? List.of() : currentDocument.diagramKind().nodeKinds());
        availableEdgeKinds.setAll(currentDocument == null ? List.of() : currentDocument.diagramKind().edgeKinds());
    }
    private BehaviorNode firstDifferentNode(String nodeId) {
        return nodes.stream().filter(node -> !node.id().equals(nodeId)).findFirst().orElse(null);
    }
    private void selectLastNode() { if (!nodes.isEmpty()) selectedNode.set(nodes.get(nodes.size() - 1)); }
    private void selectLastEdge() { if (!edges.isEmpty()) selectedEdge.set(edges.get(edges.size() - 1)); }
    private boolean restoreNodeSelection(String nodeId) {
        for (BehaviorNode node : nodes) if (node.id().equals(nodeId)) { selectedNode.set(node); return true; }
        return false;
    }
    private boolean restoreEdgeSelection(String edgeId) {
        for (BehaviorEdge edge : edges) if (edge.id().equals(edgeId)) { selectedEdge.set(edge); return true; }
        return false;
    }
    private boolean ensureDocument(String message) {
        if (currentDocument == null) {
            statusConsumer.accept(message);
            return false;
        }
        return true;
    }
}
