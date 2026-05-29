package com.marcosmoreira.domainmodelstudio.presentation.architecture;

import com.marcosmoreira.domainmodelstudio.application.architecture.AddArchitectureEdgeUseCase;
import com.marcosmoreira.domainmodelstudio.application.architecture.AddArchitectureNodeUseCase;
import com.marcosmoreira.domainmodelstudio.application.architecture.ArchitectureDiagramValidationResult;
import com.marcosmoreira.domainmodelstudio.application.architecture.RemoveArchitectureItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.architecture.UpdateArchitectureEdgeUseCase;
import com.marcosmoreira.domainmodelstudio.application.architecture.UpdateArchitectureNodeUseCase;
import com.marcosmoreira.domainmodelstudio.application.architecture.ValidateArchitectureDiagramUseCase;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.application.visual.ArchitectureContainerLayoutSupport;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutService;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayerOrderCommand;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdge;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNode;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.ProjectChangeSupport;
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

/** ViewModel del editor funcional para C4 y despliegue técnico. */
public final class ArchitectureDiagramViewModel {

    private final AddArchitectureNodeUseCase addNodeUseCase;
    private final AddArchitectureEdgeUseCase addEdgeUseCase;
    private final UpdateArchitectureNodeUseCase updateNodeUseCase;
    private final UpdateArchitectureEdgeUseCase updateEdgeUseCase;
    private final RemoveArchitectureItemUseCase removeItemUseCase;
    private final ValidateArchitectureDiagramUseCase validateUseCase;
    private final VisualLayoutService visualLayoutService = new VisualLayoutService();
    private final ArchitectureContainerLayoutSupport containerLayoutSupport = new ArchitectureContainerLayoutSupport();
    private final Consumer<String> statusConsumer;
    private final ObservableList<ArchitectureNode> nodes = FXCollections.observableArrayList();
    private final ObservableList<ArchitectureEdge> edges = FXCollections.observableArrayList();
    private final ObservableList<ArchitectureNodeKind> availableNodeKinds = FXCollections.observableArrayList();
    private final ObservableList<ArchitectureEdgeKind> availableEdgeKinds = FXCollections.observableArrayList();
    private final ObjectProperty<ArchitectureNode> selectedNode = new SimpleObjectProperty<>();
    private final ObjectProperty<ArchitectureEdge> selectedEdge = new SimpleObjectProperty<>();
    private DiagramProject currentProject;
    private ArchitectureDiagramDocument currentDocument;
    private final ProjectChangeSupport projectChangeSupport = new ProjectChangeSupport();
    private final VisualDiagramViewActions viewActions;

    public ArchitectureDiagramViewModel(
            AddArchitectureNodeUseCase addNodeUseCase,
            AddArchitectureEdgeUseCase addEdgeUseCase,
            UpdateArchitectureNodeUseCase updateNodeUseCase,
            UpdateArchitectureEdgeUseCase updateEdgeUseCase,
            RemoveArchitectureItemUseCase removeItemUseCase,
            ValidateArchitectureDiagramUseCase validateUseCase,
            Consumer<String> statusConsumer
    ) {
        this.addNodeUseCase = Objects.requireNonNull(addNodeUseCase, "addNodeUseCase");
        this.addEdgeUseCase = Objects.requireNonNull(addEdgeUseCase, "addEdgeUseCase");
        this.updateNodeUseCase = Objects.requireNonNull(updateNodeUseCase, "updateNodeUseCase");
        this.updateEdgeUseCase = Objects.requireNonNull(updateEdgeUseCase, "updateEdgeUseCase");
        this.removeItemUseCase = Objects.requireNonNull(removeItemUseCase, "removeItemUseCase");
        this.validateUseCase = Objects.requireNonNull(validateUseCase, "validateUseCase");
        this.statusConsumer = Objects.requireNonNull(statusConsumer, "statusConsumer");
        this.viewActions = VisualDiagramViewActions.forGenericDiagram(this::active, this.statusConsumer, "El diagrama de arquitectura todavía no tiene una vista PNG registrada.");
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

    public ObservableList<ArchitectureNode> nodes() { return nodes; }
    public ObservableList<ArchitectureEdge> edges() { return edges; }
    public ObservableList<ArchitectureNodeKind> availableNodeKinds() { return availableNodeKinds; }
    public ObservableList<ArchitectureEdgeKind> availableEdgeKinds() { return availableEdgeKinds; }
    public ObjectProperty<ArchitectureNode> selectedNodeProperty() { return selectedNode; }
    public ObjectProperty<ArchitectureEdge> selectedEdgeProperty() { return selectedEdge; }
    public DiagramProject currentProject() { return currentProject; }
    public ArchitectureDiagramDocument currentDocument() { return currentDocument; }
    public boolean active() { return currentProject != null && currentDocument != null; }
    public ArchitectureDiagramKind currentKind() { return currentDocument == null ? ArchitectureDiagramKind.C4_CONTEXT : currentDocument.diagramKind(); }
    public String title() { return currentDocument == null ? "Diagrama de arquitectura" : currentDocument.diagramKind().displayName(); }

    public void loadProject(DiagramProject project) {
        projectChangeSupport.runLoading(() -> {
            currentProject = visualLayoutService.ensureVisualLayout(Objects.requireNonNull(project, "project"));
            ArchitectureDiagramKind kind = ArchitectureDiagramKind.fromDiagramTypeId(currentProject.metadata().diagramTypeId());
            currentDocument = currentProject.architectureDiagram().orElseGet(() -> ArchitectureDiagramDocument.blank(currentProject.metadata().title(), kind));
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

    public void addNode(ArchitectureNodeKind kind) {
        if (!ensureDocument("No hay diagrama abierto para agregar elementos.")) return;
        ArchitectureNodeKind resolved = kind == null ? currentKind().defaultNodeKind() : kind;
        try {
            applyDocument(addNodeUseCase.add(currentDocument, resolved, resolved.displayName()), "Elemento agregado.");
            selectLastNode();
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo agregar elemento: " + exception.getMessage());
        }
    }

    public void addEdge(ArchitectureEdgeKind kind) {
        if (!ensureDocument("No hay diagrama abierto para agregar relaciones.")) return;
        ArchitectureNode source = selectedNode.get();
        ArchitectureNode target = firstDifferentNode(source == null ? "" : source.id());
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
        ArchitectureEdge edge = selectedEdge.get();
        ArchitectureNode node = selectedNode.get();
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

    public void applyNodeChanges(ArchitectureNodeKind kind, String displayName, String technology, String owner,
                                 String environment, String description, String notes, int orderIndex) {
        ArchitectureNode node = selectedNode.get();
        if (node == null || !ensureDocument("No hay elemento seleccionado.")) return;
        try {
            applyDocument(updateNodeUseCase.update(currentDocument, node.id(), kind, displayName, technology, owner,
                    environment, description, notes, orderIndex), "Elemento actualizado.");
            restoreNodeSelection(node.id());
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo actualizar elemento: " + exception.getMessage());
        }
    }

    public void applyEdgeChanges(ArchitectureNode source, ArchitectureNode target, ArchitectureEdgeKind kind,
                                 String label, String protocol, String notes) {
        ArchitectureEdge edge = selectedEdge.get();
        if (edge == null || !ensureDocument("No hay relación seleccionada.")) return;
        if (source == null || target == null) {
            statusConsumer.accept("Selecciona origen y destino para la relación.");
            return;
        }
        try {
            applyDocument(updateEdgeUseCase.update(currentDocument, edge.id(), source.id(), target.id(), kind,
                    label, protocol, notes), "Relación actualizada.");
            restoreEdgeSelection(edge.id());
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo actualizar relación: " + exception.getMessage());
        }
    }

    public NodeLayout layoutForNode(ArchitectureNode node) {
        Objects.requireNonNull(node, "node");
        ensureCurrentLayout();
        return visualLayoutService.nodeLayout(currentProject, VisualElementLayoutIds.architectureNode(node.id()))
                .orElseThrow(() -> new IllegalStateException("No existe layout para el elemento de arquitectura: " + node.id()));
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
        if (!ensureProjectForLayout("No hay diagrama de arquitectura abierto.")) {
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
        if (!ensureProjectForLayout("No hay diagrama de arquitectura abierto.")) {
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
        if (!ensureProjectForLayout("No hay diagrama de arquitectura abierto.")) {
            return;
        }
        try {
            currentProject = visualLayoutService.moveConnectorLabelBy(currentProject, connectorId, deltaX, deltaY);
            notifyProjectChanged();
            statusConsumer.accept("Etiqueta de relación actualizada.");
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo mover etiqueta de relación: " + exception.getMessage());
        }
    }

    public void removeConnectorBendPoint(DiagramElementId connectorId, int bendPointIndex) {
        if (!ensureProjectForLayout("No hay diagrama de arquitectura abierto.")) {
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
        if (!ensureProjectForLayout("No hay diagrama de arquitectura abierto.")) {
            return;
        }
        try {
            currentProject = containerLayoutSupport.moveNode(currentProject, nodeId, x, y, nodes());
            notifyProjectChanged();
            statusConsumer.accept("Posición del elemento actualizada.");
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo mover elemento: " + exception.getMessage());
        }
    }


    public boolean reorderSelectedElement(VisualLayerOrderCommand command) {
        ArchitectureNode node = selectedNode.get();
        DiagramElementId layoutId = node == null ? null : VisualElementLayoutIds.architectureNode(node.id());
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

    public boolean resizeSelectedElement(VisualNodeSizeCommand command) {
        ArchitectureNode node = selectedNode.get();
        DiagramElementId layoutId = node == null ? null : VisualElementLayoutIds.architectureNode(node.id());
        boolean resized = VisualNodeSizeViewModelSupport.resizeSelectedNode(
                visualLayoutService, currentProject, layoutId, command,
                project -> currentProject = project, this::notifyProjectChanged, statusConsumer);
        if (resized) {
            viewActions.refreshDiagramView();
        }
        return resized;
    }

    public void reorganizeLayout() { if (!ensureProjectForLayout("No hay diagrama de arquitectura activo para autoorganizar.")) return; currentProject = visualLayoutService.regenerateVisualLayout(currentProject.withArchitectureDiagram(currentDocument)); notifyProjectChanged(); statusConsumer.accept("Diagrama de arquitectura autoorganizado según el nivel C4 activo."); }

    public ArchitectureDiagramValidationResult validateDocument() {
        if (!ensureDocument("No hay diagrama abierto para validar.")) {
            return new ArchitectureDiagramValidationResult(List.of("No hay diagrama abierto."));
        }
        ArchitectureDiagramValidationResult result = validateUseCase.validate(currentDocument);
        statusConsumer.accept(result.summary());
        return result;
    }

    public String nodeLabel(String nodeId) {
        return currentDocument == null ? nodeId : currentDocument.nodeById(nodeId).map(ArchitectureNode::displayName).orElse(nodeId);
    }

    private void applyDocument(ArchitectureDiagramDocument updatedDocument, String statusMessage) {
        currentDocument = updatedDocument;
        refreshLists();
        if (currentProject != null) {
            currentProject = visualLayoutService.ensureVisualLayout(currentProject.withArchitectureDiagram(updatedDocument));
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

    private ArchitectureNode firstDifferentNode(String nodeId) {
        return nodes.stream().filter(node -> !node.id().equals(nodeId)).findFirst().orElse(null);
    }

    private void selectLastNode() { if (!nodes.isEmpty()) selectedNode.set(nodes.get(nodes.size() - 1)); }
    private void selectLastEdge() { if (!edges.isEmpty()) selectedEdge.set(edges.get(edges.size() - 1)); }

    private boolean restoreNodeSelection(String nodeId) {
        for (ArchitectureNode node : nodes) if (node.id().equals(nodeId)) { selectedNode.set(node); return true; }
        return false;
    }

    private boolean restoreEdgeSelection(String edgeId) {
        for (ArchitectureEdge edge : edges) if (edge.id().equals(edgeId)) { selectedEdge.set(edge); return true; }
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
