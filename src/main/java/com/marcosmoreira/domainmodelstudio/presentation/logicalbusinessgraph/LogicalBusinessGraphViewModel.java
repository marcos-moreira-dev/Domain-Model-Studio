package com.marcosmoreira.domainmodelstudio.presentation.logicalbusinessgraph;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutService;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayerOrderCommand;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.ProjectChangeSupport;
import com.marcosmoreira.domainmodelstudio.domain.layout.BendPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphIssue;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNode;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphRelationKind;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportPngAction;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualDiagramViewActions;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualLayerOrderViewModelSupport;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasSelection;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualProjectPatchSupport;
import com.marcosmoreira.domainmodelstudio.presentation.visualtransfer.LogicalBusinessGraphSelectionTransferPayload;
import com.marcosmoreira.domainmodelstudio.presentation.visualtransfer.VisualSelectionClipboard;
import com.marcosmoreira.domainmodelstudio.presentation.visualtransfer.VisualSelectionTransferPayload;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualNodeSizeCommand;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualNodeSizeViewModelSupport;

/**
 * Estado visual del Grafo lógico del negocio sobre el canvas común.
 *
 * <p>El ViewModel mantiene sincronizados el {@code DiagramProject}, el documento lógico, la
 * selección actual, los issues semánticos y las acciones visuales. No define la semántica del
 * grafo: esa responsabilidad vive en dominio; aquí se exponen cambios observables para JavaFX y
 * se notifican parches persistibles al shell.</p>
 *
 * <p>Es una clase útil para estudiar la frontera presentación/aplicación: mover un nodo o editar
 * propiedades no muta controles JavaFX aislados, sino que produce un nuevo proyecto coherente
 * que puede guardarse, exportarse o validarse.</p>
 */
public final class LogicalBusinessGraphViewModel {

    private final Consumer<String> statusConsumer;
    private final VisualLayoutService visualLayoutService = new VisualLayoutService();
    private final VisualDiagramViewActions viewActions;
    private final ObservableList<LogicalBusinessGraphNode> nodes = FXCollections.observableArrayList();
    private final ObservableList<LogicalBusinessGraphEdge> edges = FXCollections.observableArrayList();
    private final ObservableList<LogicalBusinessGraphIssue> semanticIssues = FXCollections.observableArrayList();
    private final ObjectProperty<LogicalBusinessGraphNode> selectedNode = new SimpleObjectProperty<>();
    private final ObjectProperty<LogicalBusinessGraphEdge> selectedEdge = new SimpleObjectProperty<>();
    private final ProjectChangeSupport projectChangeSupport = new ProjectChangeSupport();
    private InteractiveCanvasSelection lastCanvasSelection = InteractiveCanvasSelection.empty();
    private DiagramProject currentProject;
    private LogicalBusinessGraphDocument currentDocument;

    public LogicalBusinessGraphViewModel(Consumer<String> statusConsumer) {
        this.statusConsumer = Objects.requireNonNull(statusConsumer, "statusConsumer");
        this.viewActions = VisualDiagramViewActions.forGenericDiagram(
                this::active,
                this.statusConsumer,
                "Grafo lógico del negocio todavía no tiene una vista PNG registrada.");
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

    public ObservableList<LogicalBusinessGraphNode> nodes() { return nodes; }
    public ObservableList<LogicalBusinessGraphEdge> edges() { return edges; }
    public ObservableList<LogicalBusinessGraphIssue> semanticIssues() { return semanticIssues; }
    public ObjectProperty<LogicalBusinessGraphNode> selectedNodeProperty() { return selectedNode; }
    public ObjectProperty<LogicalBusinessGraphEdge> selectedEdgeProperty() { return selectedEdge; }
    public LogicalBusinessGraphDocument currentDocument() { return currentDocument; }
    public DiagramProject currentProject() { return currentProject; }
    public boolean active() { return currentProject != null && currentDocument != null; }

    public boolean resizeSelectedElement(VisualNodeSizeCommand command) {
        LogicalBusinessGraphNode node = selectedNode.get();
        DiagramElementId layoutId = node == null ? null : VisualElementLayoutIds.logicalBusinessGraphNode(node.code());
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
            currentDocument = safeProject.logicalBusinessGraphDocument()
                    .orElseGet(() -> LogicalBusinessGraphDocument.blank(safeProject.metadata().title()));
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
            semanticIssues.clear();
            selectedNode.set(null);
            selectedEdge.set(null);
        });
    }

    public NodeLayout layoutForNode(LogicalBusinessGraphNode node) {
        Objects.requireNonNull(node, "node");
        ensureCurrentLayout();
        return visualLayoutService.nodeLayout(currentProject, VisualElementLayoutIds.logicalBusinessGraphNode(node.code()))
                .orElseThrow(() -> new IllegalStateException("No existe layout para el nodo lógico: " + node.code()));
    }

    public Optional<ConnectorLayout> layoutForConnector(DiagramElementId connectorId) {
        Objects.requireNonNull(connectorId, "connectorId");
        if (!active()) {
            return Optional.empty();
        }
        ensureCurrentLayout();
        return visualLayoutService.connectorLayout(currentProject, connectorId);
    }

    public void selectNodeByCode(String code) {
        String normalized = clean(code).toUpperCase();
        if (normalized.isBlank()) {
            return;
        }
        nodes.stream()
                .filter(node -> node.code().equals(normalized))
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
    }


    public String nodeLabel(String code) {
        String normalized = clean(code).toUpperCase();
        return nodes.stream()
                .filter(node -> node.code().equals(normalized))
                .findFirst()
                .map(node -> node.code() + " — " + node.title())
                .orElse(normalized);
    }

    public void applyDocumentDetails(String projectName, String version, LocalDate documentDate, String notes) {
        if (!ensureActiveDocument("No hay grafo lógico abierto para editar documento.")) {
            return;
        }
        currentDocument = currentDocument.withHeader(projectName, version, documentDate, notes);
        publishDocumentChange("Detalles del grafo lógico actualizados.");
    }

    public void applyNodeChanges(String title, String description, LogicalBusinessGraphNodeStatus status, String sourceReferencesText) {
        LogicalBusinessGraphNode selected = selectedNode.get();
        if (selected == null || !ensureActiveDocument("Selecciona un nodo lógico antes de editar.")) {
            return;
        }
        try {
            LogicalBusinessGraphNode updated = selected.withDetails(title, description, status, parseReferences(sourceReferencesText));
            currentDocument = currentDocument.withUpdatedNode(updated);
            publishDocumentChange("Nodo lógico actualizado.");
            selectNodeByCode(updated.code());
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo actualizar nodo lógico: " + exception.getMessage());
        }
    }

    public void applyEdgeChanges(LogicalBusinessGraphNode source, LogicalBusinessGraphRelationKind relation,
                                 LogicalBusinessGraphNode target, String description) {
        LogicalBusinessGraphEdge selected = selectedEdge.get();
        if (selected == null || !ensureActiveDocument("Selecciona una relación lógica antes de editar.")) {
            return;
        }
        if (source == null || target == null || relation == null) {
            statusConsumer.accept("Selecciona origen, relación y destino antes de aplicar.");
            return;
        }
        try {
            LogicalBusinessGraphEdge updated = new LogicalBusinessGraphEdge(
                    selected.id(), source.code(), relation, target.code(), description);
            currentDocument = currentDocument.withUpdatedEdge(updated);
            publishDocumentChange("Relación lógica actualizada.");
            selectEdgeById(updated.id());
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo actualizar relación lógica: " + exception.getMessage());
        }
    }


    public boolean reorderSelectedElement(VisualLayerOrderCommand command) {
        LogicalBusinessGraphNode node = selectedNode.get();
        DiagramElementId layoutId = node == null ? null : VisualElementLayoutIds.logicalBusinessGraphNode(node.code());
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

    public void moveNodeTo(String code, double x, double y) {
        if (!ensureProjectForLayout("No hay grafo lógico abierto.")) {
            return;
        }
        try {
            currentProject = visualLayoutService.moveNodeTo(
                    currentProject,
                    VisualElementLayoutIds.logicalBusinessGraphNode(clean(code).toUpperCase()),
                    x,
                    y);
            notifyProjectChanged();
            statusConsumer.accept("Nodo lógico movido.");
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo mover nodo lógico: " + exception.getMessage());
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



    public void rememberCanvasSelection(InteractiveCanvasSelection selection) {
        lastCanvasSelection = selection == null ? InteractiveCanvasSelection.empty() : selection;
    }

    public boolean copyCurrentSelectionToClipboard() {
        return copySelectionToClipboard(lastCanvasSelection);
    }

    public boolean copySelectionToClipboard(InteractiveCanvasSelection selection) {
        Optional<LogicalBusinessGraphSelectionTransferPayload> payload = selectedTransferPayload(selection);
        if (payload.isEmpty()) {
            statusConsumer.accept("No hay nodos o relaciones de Grafo lógico seleccionados para copiar.");
            return false;
        }
        LogicalBusinessGraphSelectionTransferPayload value = payload.orElseThrow();
        VisualSelectionClipboard.copy(value);
        statusConsumer.accept("Selección copiada desde Grafo lógico: "
                + value.nodeCount() + " nodo(s), " + value.connectorCount() + " relación(es).");
        return true;
    }

    public boolean pasteSelectionFromClipboard() {
        Optional<VisualSelectionTransferPayload> current = VisualSelectionClipboard.current();
        if (current.isEmpty()) {
            statusConsumer.accept("No hay selección visual copiada.");
            return false;
        }
        VisualSelectionTransferPayload payload = current.orElseThrow();
        if (!(payload instanceof LogicalBusinessGraphSelectionTransferPayload logicalPayload)) {
            statusConsumer.accept("La selección copiada pertenece a " + payload.diagramTypeId().value()
                    + "; solo puede pegarse en otro Grafo lógico.");
            return false;
        }
        return pasteSelection(logicalPayload);
    }

    Optional<LogicalBusinessGraphSelectionTransferPayload> selectedTransferPayload(InteractiveCanvasSelection selection) {
        if (!active() || selection == null || selection.isEmpty()) {
            return Optional.empty();
        }
        ensureCurrentLayout();
        Set<String> selectedCodes = rawNodeCodes(selection.selectedNodeIds());
        Set<String> selectedEdgeIds = rawEdgeIds(selection.selectedConnectorIds());
        for (LogicalBusinessGraphEdge edge : currentDocument.edges()) {
            if (selectedEdgeIds.contains(edge.id())) {
                selectedCodes.add(edge.sourceCode());
                selectedCodes.add(edge.targetCode());
            }
        }
        for (LogicalBusinessGraphEdge edge : currentDocument.edges()) {
            if (selectedCodes.contains(edge.sourceCode()) && selectedCodes.contains(edge.targetCode())) {
                selectedEdgeIds.add(edge.id());
            }
        }
        List<LogicalBusinessGraphNode> copiedNodes = currentDocument.nodes().stream()
                .filter(node -> selectedCodes.contains(node.code()))
                .toList();
        List<LogicalBusinessGraphEdge> copiedEdges = currentDocument.edges().stream()
                .filter(edge -> selectedEdgeIds.contains(edge.id()))
                .filter(edge -> selectedCodes.contains(edge.sourceCode()))
                .filter(edge -> selectedCodes.contains(edge.targetCode()))
                .toList();
        if (copiedNodes.isEmpty() && copiedEdges.isEmpty()) {
            return Optional.empty();
        }
        List<NodeLayout> nodeLayouts = copiedNodes.stream()
                .map(node -> currentProject.layouts().activeLayout().nodeFor(VisualElementLayoutIds.logicalBusinessGraphNode(node.code())))
                .flatMap(Optional::stream)
                .toList();
        List<ConnectorLayout> connectorLayouts = copiedEdges.stream()
                .map(edge -> currentProject.layouts().activeLayout().connectorById(VisualElementLayoutIds.logicalBusinessGraphEdge(edge.id())))
                .flatMap(Optional::stream)
                .toList();
        return Optional.of(new LogicalBusinessGraphSelectionTransferPayload(
                currentProject.metadata().title(),
                copiedNodes,
                copiedEdges,
                nodeLayouts,
                connectorLayouts));
    }

    public boolean pasteSelection(LogicalBusinessGraphSelectionTransferPayload payload) {
        if (payload == null || payload.empty()) {
            statusConsumer.accept("La selección de Grafo lógico está vacía.");
            return false;
        }
        if (!ensureProjectForLayout("No hay Grafo lógico abierto para pegar selección.")) {
            return false;
        }
        Map<String, String> nodeCodeMap = new LinkedHashMap<>();
        LogicalBusinessGraphDocument updatedDocument = currentDocument;
        for (LogicalBusinessGraphNode node : payload.nodes()) {
            String newCode = uniqueLogicalNodeCode(node, updatedDocument, nodeCodeMap.values());
            nodeCodeMap.put(node.code(), newCode);
            updatedDocument = updatedDocument.withNode(new LogicalBusinessGraphNode(
                    newCode,
                    node.kind(),
                    copyTitle(node.title()),
                    node.description(),
                    node.status(),
                    node.sourceReferenceIds()));
        }
        Map<String, String> edgeIdMap = new LinkedHashMap<>();
        for (LogicalBusinessGraphEdge edge : payload.edges()) {
            String newSource = nodeCodeMap.get(edge.sourceCode());
            String newTarget = nodeCodeMap.get(edge.targetCode());
            if (newSource == null || newTarget == null) {
                continue;
            }
            String newId = uniqueLogicalEdgeId(edge.id(), updatedDocument, edgeIdMap.values());
            edgeIdMap.put(edge.id(), newId);
            updatedDocument = updatedDocument.withEdge(new LogicalBusinessGraphEdge(
                    newId,
                    newSource,
                    edge.relationKind(),
                    newTarget,
                    edge.description()));
        }
        DiagramLayout updatedLayout = currentProject.layouts().activeLayout();
        for (NodeLayout layout : payload.nodeLayouts()) {
            String rawCode = rawLogicalNodeLayoutId(layout.elementId().value());
            String pastedCode = nodeCodeMap.get(rawCode);
            if (pastedCode == null) {
                continue;
            }
            updatedLayout = updatedLayout.withNode(new NodeLayout(
                    VisualElementLayoutIds.logicalBusinessGraphNode(pastedCode),
                    layout.position().translatedBy(32.0, 32.0),
                    layout.size(),
                    layout.visible(),
                    layout.locked(),
                    layout.zOrder()));
        }
        for (ConnectorLayout layout : payload.connectorLayouts()) {
            String rawEdgeId = rawLogicalEdgeLayoutId(layout.connectorId().value());
            String pastedEdgeId = edgeIdMap.get(rawEdgeId);
            if (pastedEdgeId == null) {
                continue;
            }
            String source = nodeCodeMap.get(rawLogicalNodeLayoutId(layout.sourceElementId().value()));
            String target = nodeCodeMap.get(rawLogicalNodeLayoutId(layout.targetElementId().value()));
            if (source == null || target == null) {
                continue;
            }
            updatedLayout = updatedLayout.withConnector(copyConnectorLayout(
                    layout,
                    VisualElementLayoutIds.logicalBusinessGraphEdge(pastedEdgeId),
                    VisualElementLayoutIds.logicalBusinessGraphNode(source),
                    VisualElementLayoutIds.logicalBusinessGraphNode(target)));
        }
        currentDocument = updatedDocument;
        currentProject = currentProject.withLogicalBusinessGraphDocument(updatedDocument)
                .withLayouts(currentProject.layouts().withLayout(updatedLayout));
        refreshLists();
        notifyProjectChanged();
        statusConsumer.accept("Selección pegada en Grafo lógico: "
                + nodeCodeMap.size() + " nodo(s), " + edgeIdMap.size() + " relación(es).");
        return true;
    }

    private static ConnectorLayout copyConnectorLayout(
            ConnectorLayout layout,
            DiagramElementId connectorId,
            DiagramElementId sourceId,
            DiagramElementId targetId
    ) {
        List<BendPoint> bendPoints = layout.bendPoints().stream()
                .map(point -> point.translatedBy(32.0, 32.0))
                .toList();
        return new ConnectorLayout(
                connectorId,
                sourceId,
                targetId,
                layout.sourceAnchor(),
                layout.targetAnchor(),
                layout.pathKind(),
                bendPoints,
                layout.sourceMarker(),
                layout.targetMarker(),
                layout.sourceMarkerOrientation(),
                layout.targetMarkerOrientation(),
                layout.labelOffsetX(),
                layout.labelOffsetY(),
                layout.visible());
    }

    private Set<String> rawNodeCodes(Set<String> layoutIds) {
        Set<String> result = new LinkedHashSet<>();
        for (String id : layoutIds == null ? Set.<String>of() : layoutIds) {
            String raw = rawLogicalNodeLayoutId(id);
            if (!raw.isBlank()) {
                result.add(raw);
            }
        }
        return result;
    }

    private Set<String> rawEdgeIds(Set<String> layoutIds) {
        Set<String> result = new LinkedHashSet<>();
        for (String id : layoutIds == null ? Set.<String>of() : layoutIds) {
            String raw = rawLogicalEdgeLayoutId(id);
            if (!raw.isBlank()) {
                result.add(raw);
            }
        }
        return result;
    }

    private static String rawLogicalNodeLayoutId(String layoutId) {
        return rawAfter(layoutId, "logical-business-graph-node:").toUpperCase();
    }

    private static String rawLogicalEdgeLayoutId(String layoutId) {
        return rawAfter(layoutId, "logical-business-graph-edge:");
    }

    private static String rawAfter(String value, String prefix) {
        String normalized = clean(value);
        return normalized.startsWith(prefix) ? normalized.substring(prefix.length()).strip() : "";
    }

    private static String uniqueLogicalNodeCode(
            LogicalBusinessGraphNode node,
            LogicalBusinessGraphDocument document,
            Iterable<String> pendingCodes
    ) {
        Set<String> pending = new LinkedHashSet<>();
        for (String pendingCode : pendingCodes == null ? List.<String>of() : pendingCodes) {
            pending.add(clean(pendingCode).toUpperCase());
        }
        LogicalBusinessGraphNodeKind kind = node.kind();
        String base = clean(node.code()).toUpperCase();
        if (!kind.matchesCode(base)) {
            base = kind.prefix() + "-COPIA";
        }
        int suffix = 1;
        String candidate;
        do {
            candidate = base + "-COPIA" + (suffix == 1 ? "" : "-" + suffix);
            suffix++;
        } while (document.nodeByCode(candidate).isPresent() || pending.contains(candidate));
        return candidate;
    }

    private static String uniqueLogicalEdgeId(
            String originalId,
            LogicalBusinessGraphDocument document,
            Iterable<String> pendingIds
    ) {
        Set<String> pending = new LinkedHashSet<>();
        for (String pendingId : pendingIds == null ? List.<String>of() : pendingIds) {
            pending.add(clean(pendingId));
        }
        String base = clean(originalId).isBlank() ? "relacion-copiada" : clean(originalId);
        int suffix = 1;
        String candidate;
        do {
            candidate = base + "_copia" + (suffix == 1 ? "" : "_" + suffix);
            suffix++;
        } while (document.edgeById(candidate).isPresent() || pending.contains(candidate));
        return candidate;
    }

    private static String copyTitle(String title) {
        String normalized = clean(title);
        return normalized.isBlank() ? "Copia" : normalized + " (copia)";
    }

    private boolean ensureActiveDocument(String message) {
        if (!active()) {
            statusConsumer.accept(message);
            return false;
        }
        return true;
    }

    private void publishDocumentChange(String statusMessage) {
        currentProject = currentProject.withLogicalBusinessGraphDocument(currentDocument);
        refreshLists();
        notifyProjectChanged();
        statusConsumer.accept(statusMessage);
    }

    private static List<String> parseReferences(String text) {
        String normalized = clean(text);
        if (normalized.isBlank()) {
            return List.of();
        }
        return Arrays.stream(normalized.split("[,;\n]"))
                .map(String::strip)
                .filter(value -> !value.isBlank())
                .toList();
    }

    private void refreshLists() {
        nodes.setAll(currentDocument == null ? List.of() : currentDocument.nodes());
        edges.setAll(currentDocument == null ? List.of() : currentDocument.edges());
        semanticIssues.setAll(currentDocument == null ? List.of() : currentDocument.semanticIssues());
    }

    private void ensureCurrentLayout() {
        if (currentProject == null) {
            throw new IllegalStateException("No hay proyecto activo para consultar layout visual.");
        }
        currentProject = visualLayoutService.ensureVisualLayout(currentProject);
    }

    private boolean ensureProjectForLayout(String message) {
        if (!active()) {
            statusConsumer.accept(message);
            return false;
        }
        ensureCurrentLayout();
        return true;
    }

    private void notifyProjectChanged() {
        projectChangeSupport.notifyChanged(currentProject);
    }

    private static String clean(String value) {
        return value == null ? "" : value.strip();
    }
}
