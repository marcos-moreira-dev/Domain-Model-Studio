package com.marcosmoreira.domainmodelstudio.presentation.freegraph;

import com.marcosmoreira.domainmodelstudio.application.freegraph.AddFreeGraphEdgeUseCase;
import com.marcosmoreira.domainmodelstudio.application.freegraph.AddFreeGraphNodeUseCase;
import com.marcosmoreira.domainmodelstudio.application.freegraph.FreeGraphValidationResult;
import com.marcosmoreira.domainmodelstudio.application.freegraph.RemoveFreeGraphItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.freegraph.UpdateFreeGraphEdgeUseCase;
import com.marcosmoreira.domainmodelstudio.application.freegraph.UpdateFreeGraphNodeUseCase;
import com.marcosmoreira.domainmodelstudio.application.freegraph.ValidateFreeGraphUseCase;
import com.marcosmoreira.domainmodelstudio.application.visual.FreeGraphLayoutPolicy;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdgeDirection;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphKind;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphNode;
import com.marcosmoreira.domainmodelstudio.domain.layout.BendPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasSelection;
import com.marcosmoreira.domainmodelstudio.presentation.visualtransfer.FreeGraphSelectionTransferPayload;
import com.marcosmoreira.domainmodelstudio.presentation.visualtransfer.VisualSelectionClipboard;
import com.marcosmoreira.domainmodelstudio.presentation.visualtransfer.VisualSelectionTransferPayload;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/** ViewModel del editor de Grafo libre. */
public final class FreeGraphViewModel extends FreeGraphViewModelCore {

    private InteractiveCanvasSelection lastCanvasSelection = InteractiveCanvasSelection.empty();

    public FreeGraphViewModel(
            AddFreeGraphNodeUseCase addNodeUseCase,
            AddFreeGraphEdgeUseCase addEdgeUseCase,
            UpdateFreeGraphNodeUseCase updateNodeUseCase,
            UpdateFreeGraphEdgeUseCase updateEdgeUseCase,
            RemoveFreeGraphItemUseCase removeItemUseCase,
            ValidateFreeGraphUseCase validateUseCase,
            Consumer<String> statusConsumer
    ) {
        super(addNodeUseCase, addEdgeUseCase, updateNodeUseCase, updateEdgeUseCase,
                removeItemUseCase, validateUseCase, statusConsumer);
    }

    public void rememberCanvasSelection(InteractiveCanvasSelection selection) {
        lastCanvasSelection = selection == null ? InteractiveCanvasSelection.empty() : selection;
    }

    public boolean copyCurrentSelectionToClipboard() {
        return copySelectionToClipboard(lastCanvasSelection);
    }

    public boolean copySelectionToClipboard(InteractiveCanvasSelection selection) {
        Optional<FreeGraphSelectionTransferPayload> payload = selectedTransferPayload(selection);
        if (payload.isEmpty()) {
            statusConsumer.accept("No hay nodos o relaciones de Grafo libre seleccionados para copiar.");
            return false;
        }
        FreeGraphSelectionTransferPayload value = payload.orElseThrow();
        VisualSelectionClipboard.copy(value);
        statusConsumer.accept("Selección copiada desde Grafo libre: "
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
        if (!(payload instanceof FreeGraphSelectionTransferPayload freeGraphPayload)) {
            statusConsumer.accept("La selección copiada pertenece a " + payload.diagramTypeId().value()
                    + "; solo puede pegarse en otro Grafo libre.");
            return false;
        }
        return pasteSelection(freeGraphPayload);
    }

    Optional<FreeGraphSelectionTransferPayload> selectedTransferPayload(InteractiveCanvasSelection selection) {
        if (!active() || selection == null || selection.isEmpty()) {
            return Optional.empty();
        }
        ensureCurrentLayout();
        Set<String> selectedNodeIds = rawNodeIds(selection.selectedNodeIds());
        Set<String> selectedEdgeIds = rawEdgeIds(selection.selectedConnectorIds());
        for (FreeGraphEdge edge : currentDocument.edges()) {
            if (selectedEdgeIds.contains(edge.id())) {
                selectedNodeIds.add(edge.sourceNodeId());
                selectedNodeIds.add(edge.targetNodeId());
            }
        }
        for (FreeGraphEdge edge : currentDocument.edges()) {
            if (selectedNodeIds.contains(edge.sourceNodeId()) && selectedNodeIds.contains(edge.targetNodeId())) {
                selectedEdgeIds.add(edge.id());
            }
        }
        List<FreeGraphNode> copiedNodes = currentDocument.nodes().stream()
                .filter(node -> selectedNodeIds.contains(node.id()))
                .toList();
        List<FreeGraphEdge> copiedEdges = currentDocument.edges().stream()
                .filter(edge -> selectedEdgeIds.contains(edge.id()))
                .filter(edge -> selectedNodeIds.contains(edge.sourceNodeId()))
                .filter(edge -> selectedNodeIds.contains(edge.targetNodeId()))
                .toList();
        if (copiedNodes.isEmpty() && copiedEdges.isEmpty()) {
            return Optional.empty();
        }
        List<NodeLayout> nodeLayouts = copiedNodes.stream()
                .map(node -> currentProject.layouts().activeLayout().nodeFor(VisualElementLayoutIds.freeGraphNode(node.id())))
                .flatMap(Optional::stream)
                .toList();
        List<ConnectorLayout> connectorLayouts = copiedEdges.stream()
                .map(edge -> currentProject.layouts().activeLayout().connectorById(VisualElementLayoutIds.freeGraphEdge(edge.id())))
                .flatMap(Optional::stream)
                .toList();
        return Optional.of(new FreeGraphSelectionTransferPayload(
                currentProject.metadata().title(),
                copiedNodes,
                copiedEdges,
                nodeLayouts,
                connectorLayouts));
    }

    public boolean pasteSelection(FreeGraphSelectionTransferPayload payload) {
        if (payload == null || payload.empty()) {
            statusConsumer.accept("La selección de Grafo libre está vacía.");
            return false;
        }
        if (!ensureProjectForLayout("No hay Grafo libre abierto para pegar selección.")) {
            return false;
        }
        Map<String, String> nodeIdMap = new LinkedHashMap<>();
        FreeGraphDocument updatedDocument = currentDocument;
        int orderIndex = updatedDocument.nodes().size();
        for (FreeGraphNode node : payload.nodes()) {
            String newId = uniqueFreeGraphNodeId(node.id(), updatedDocument, nodeIdMap.values());
            nodeIdMap.put(node.id(), newId);
            updatedDocument = updatedDocument.withNode(new FreeGraphNode(
                    newId,
                    copyTitle(node.title()),
                    node.content(),
                    orderIndex++));
        }
        Map<String, String> edgeIdMap = new LinkedHashMap<>();
        for (FreeGraphEdge edge : payload.edges()) {
            String newSource = nodeIdMap.get(edge.sourceNodeId());
            String newTarget = nodeIdMap.get(edge.targetNodeId());
            if (newSource == null || newTarget == null) {
                continue;
            }
            String newId = uniqueFreeGraphEdgeId(edge.id(), updatedDocument, edgeIdMap.values());
            edgeIdMap.put(edge.id(), newId);
            updatedDocument = updatedDocument.withEdge(new FreeGraphEdge(
                    newId,
                    newSource,
                    newTarget,
                    edge.direction(),
                    edge.label(),
                    edge.notes()));
        }
        DiagramLayout updatedLayout = currentProject.layouts().activeLayout();
        for (NodeLayout layout : payload.nodeLayouts()) {
            String rawNodeId = rawFreeGraphNodeLayoutId(layout.elementId().value());
            String pastedNodeId = nodeIdMap.get(rawNodeId);
            if (pastedNodeId == null) {
                continue;
            }
            updatedLayout = updatedLayout.withNode(new NodeLayout(
                    VisualElementLayoutIds.freeGraphNode(pastedNodeId),
                    layout.position().translatedBy(32.0, 32.0),
                    layout.size(),
                    layout.visible(),
                    layout.locked(),
                    layout.zOrder()));
        }
        for (ConnectorLayout layout : payload.connectorLayouts()) {
            String rawEdgeId = rawFreeGraphEdgeLayoutId(layout.connectorId().value());
            String pastedEdgeId = edgeIdMap.get(rawEdgeId);
            if (pastedEdgeId == null) {
                continue;
            }
            String source = nodeIdMap.get(rawFreeGraphNodeLayoutId(layout.sourceElementId().value()));
            String target = nodeIdMap.get(rawFreeGraphNodeLayoutId(layout.targetElementId().value()));
            if (source == null || target == null) {
                continue;
            }
            updatedLayout = updatedLayout.withConnector(copyConnectorLayout(
                    layout,
                    VisualElementLayoutIds.freeGraphEdge(pastedEdgeId),
                    VisualElementLayoutIds.freeGraphNode(source),
                    VisualElementLayoutIds.freeGraphNode(target)));
        }
        currentDocument = updatedDocument;
        currentProject = currentProject.withFreeGraph(updatedDocument)
                .withLayouts(currentProject.layouts().withLayout(updatedLayout));
        refreshLists();
        notifyProjectChanged();
        statusConsumer.accept("Selección pegada en Grafo libre: "
                + nodeIdMap.size() + " nodo(s), " + edgeIdMap.size() + " relación(es).");
        return true;
    }

    private static ConnectorLayout copyConnectorLayout(
            ConnectorLayout layout,
            com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId connectorId,
            com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId sourceId,
            com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId targetId
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

    private Set<String> rawNodeIds(Set<String> layoutIds) {
        Set<String> result = new LinkedHashSet<>();
        for (String id : layoutIds == null ? Set.<String>of() : layoutIds) {
            String raw = rawFreeGraphNodeLayoutId(id);
            if (!raw.isBlank()) {
                result.add(raw);
            }
        }
        return result;
    }

    private Set<String> rawEdgeIds(Set<String> layoutIds) {
        Set<String> result = new LinkedHashSet<>();
        for (String id : layoutIds == null ? Set.<String>of() : layoutIds) {
            String raw = rawFreeGraphEdgeLayoutId(id);
            if (!raw.isBlank()) {
                result.add(raw);
            }
        }
        return result;
    }

    private static String rawFreeGraphNodeLayoutId(String layoutId) {
        return rawAfter(layoutId, "free-graph-node:");
    }

    private static String rawFreeGraphEdgeLayoutId(String layoutId) {
        return rawAfter(layoutId, "free-graph-edge:");
    }

    private static String rawAfter(String value, String prefix) {
        String normalized = clean(value);
        return normalized.startsWith(prefix) ? normalized.substring(prefix.length()).strip() : "";
    }

    private static String uniqueFreeGraphNodeId(String originalId, FreeGraphDocument document, Iterable<String> pendingIds) {
        return uniqueId(originalId, candidate -> document.nodeById(candidate).isEmpty(), pendingIds);
    }

    private static String uniqueFreeGraphEdgeId(String originalId, FreeGraphDocument document, Iterable<String> pendingIds) {
        return uniqueId(originalId, candidate -> document.edgeById(candidate).isEmpty(), pendingIds);
    }

    private static String uniqueId(String originalId, java.util.function.Predicate<String> available, Iterable<String> pendingIds) {
        Set<String> pending = new LinkedHashSet<>();
        for (String pendingId : pendingIds == null ? List.<String>of() : pendingIds) {
            pending.add(clean(pendingId));
        }
        String base = clean(originalId).isBlank() ? "copiado" : clean(originalId);
        int suffix = 1;
        String candidate;
        do {
            candidate = base + "_copia" + (suffix == 1 ? "" : "_" + suffix);
            suffix++;
        } while (!available.test(candidate) || pending.contains(candidate));
        return candidate;
    }

    private static String copyTitle(String title) {
        String normalized = clean(title);
        return normalized.isBlank() ? "Copia" : normalized + " (copia)";
    }

    public boolean handleBackgroundCanvasClick(double x, double y, boolean additive, int clickCount) {
        if (activeCanvasTool.get() == FreeGraphCanvasTool.ADD_EDGE && !pendingEdgeSourceNodeId.isBlank()) {
            pendingEdgeSourceNodeId = "";
            selectedNode.set(null);
            selectedEdge.set(null);
            statusConsumer.accept("Creación de relación cancelada.");
            return true;
        }
        if (activeCanvasTool.get() != FreeGraphCanvasTool.ADD_NODE) {
            return false;
        }
        if (clickCount > 1) {
            return true;
        }
        addNodeAt(x, y);
        return true;
    }

    public boolean handleNodeCanvasClick(String nodeId) {
        String normalizedNodeId = clean(nodeId);
        if (normalizedNodeId.isBlank() || activeCanvasTool.get() != FreeGraphCanvasTool.ADD_EDGE) {
            return false;
        }
        if (!ensureDocument("No hay grafo abierto.")) {
            return true;
        }
        if (currentDocument.nodeById(normalizedNodeId).isEmpty()) {
            statusConsumer.accept("No existe el nodo seleccionado para crear relación.");
            return true;
        }
        if (pendingEdgeSourceNodeId.isBlank()) {
            pendingEdgeSourceNodeId = normalizedNodeId;
            selectNodeById(normalizedNodeId);
            statusConsumer.accept("Origen seleccionado: " + nodeLabel(normalizedNodeId)
                    + ". Haz clic en el nodo destino; puede ser el mismo nodo para una autorrelación.");
            return true;
        }
        addEdgeBetween(pendingEdgeSourceNodeId, normalizedNodeId);
        pendingEdgeSourceNodeId = "";
        return true;
    }

    public void addNode() {
        if (!ensureDocument("No hay grafo abierto.")) {
            return;
        }
        try {
            applyDocument(addNodeUseCase.addNode(currentDocument, "Nodo"), "Nodo agregado.");
            selectLastNode();
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo agregar nodo: " + exception.getMessage());
        }
    }

    public void addEdge() {
        if (!ensureDocument("No hay grafo abierto.")) {
            return;
        }
        FreeGraphNode source = selectedNode.get();
        if (source == null) {
            statusConsumer.accept("Selecciona un nodo para agregar relación.");
            return;
        }
        FreeGraphNode target = firstDifferentNode(source.id());
        if (target == null) {
            target = source;
        }
        try {
            applyDocument(addEdgeUseCase.addEdge(currentDocument, source.id(), target.id(),
                    defaultDirection(), target == source ? "autorrelación" : "relaciona"), "Relación agregada.");
            selectLastEdge();
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo agregar relación: " + exception.getMessage());
        }
    }

    public void addNodeAt(double x, double y) {
        if (!ensureProjectForLayout("No hay grafo abierto.")) {
            return;
        }
        try {
            currentDocument = addNodeUseCase.addNode(currentDocument, "Nodo " + (nodes.size() + 1), "");
            refreshLists();
            FreeGraphNode createdNode = nodes.isEmpty() ? null : nodes.get(nodes.size() - 1);
            if (createdNode == null) {
                statusConsumer.accept("No se pudo identificar el nodo creado.");
                return;
            }
            selectedNode.set(createdNode);
            selectedEdge.set(null);
            pendingEdgeSourceNodeId = "";
            currentProject = visualLayoutService.ensureVisualLayout(currentProject.withFreeGraph(currentDocument));
            currentProject = visualLayoutService.moveNodeTo(
                    currentProject,
                    VisualElementLayoutIds.freeGraphNode(createdNode.id()),
                    Math.max(0.0, x - FreeGraphLayoutPolicy.NODE_WIDTH / 2.0),
                    Math.max(0.0, y - FreeGraphLayoutPolicy.NODE_HEIGHT / 2.0));
            notifyProjectChanged();
            statusConsumer.accept("Nodo agregado en el lienzo.");
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo agregar nodo: " + exception.getMessage());
        }
    }

    public void addEdgeBetween(String sourceNodeId, String targetNodeId) {
        if (!ensureDocument("No hay grafo abierto.")) {
            return;
        }
        try {
            FreeGraphDocument updatedDocument = addEdgeUseCase.addEdge(
                    currentDocument,
                    sourceNodeId,
                    targetNodeId,
                    defaultDirection(),
                    sourceNodeId.equals(targetNodeId) ? "autorrelación" : "");
            applyDocument(updatedDocument, sourceNodeId.equals(targetNodeId)
                    ? "Autorrelación agregada."
                    : "Relación agregada desde el lienzo.");
            selectLastEdge();
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo agregar relación: " + exception.getMessage());
        }
    }

    public void removeSelected() {
        if (!ensureDocument("No hay grafo abierto.")) {
            return;
        }
        FreeGraphEdge edge = selectedEdge.get();
        FreeGraphNode node = selectedNode.get();
        try {
            if (edge != null) {
                applyDocument(removeItemUseCase.removeEdge(currentDocument, edge.id()), "Relación eliminada.");
                selectedEdge.set(null);
                return;
            }
            if (node != null) {
                applyDocument(removeItemUseCase.removeNode(currentDocument, node.id()), "Nodo eliminado.");
                selectedNode.set(nodes.isEmpty() ? null : nodes.get(0));
            }
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo eliminar: " + exception.getMessage());
        }
    }

    public void applyNodeChanges(String title, String content) {
        FreeGraphNode node = selectedNode.get();
        if (node == null || !ensureDocument("No hay nodo seleccionado.")) {
            return;
        }
        try {
            applyDocument(updateNodeUseCase.updateNode(currentDocument, node.id(), title, content), "Nodo actualizado.");
            restoreNodeSelection(node.id());
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo actualizar nodo: " + exception.getMessage());
        }
    }

    public void applyEdgeChanges(FreeGraphNode source, FreeGraphNode target, FreeGraphEdgeDirection direction, String label, String notes) {
        FreeGraphEdge edge = selectedEdge.get();
        if (edge == null || !ensureDocument("No hay relación seleccionada.")) {
            return;
        }
        if (source == null || target == null) {
            statusConsumer.accept("Selecciona origen y destino para la relación.");
            return;
        }
        try {
            applyDocument(updateEdgeUseCase.updateEdge(currentDocument, edge.id(), source.id(), target.id(),
                    direction == null ? edge.direction() : direction, label, notes), "Relación actualizada.");
            restoreEdgeSelection(edge.id());
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo actualizar relación: " + exception.getMessage());
        }
    }

    public void applyDocumentDetails(String projectName, String version, LocalDate documentDate, FreeGraphKind graphKind, String notes) {
        if (!ensureDocument("No hay grafo abierto.")) {
            return;
        }
        try {
            applyDocument(currentDocument.withDocumentDetails(projectName, version, documentDate, graphKind, notes),
                    "Detalles del grafo actualizados.");
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudieron actualizar detalles: " + exception.getMessage());
        }
    }

    public FreeGraphValidationResult validateDocument() {
        if (!ensureDocument("No hay grafo abierto para validar.")) {
            return new FreeGraphValidationResult(List.of("No hay grafo abierto."));
        }
        FreeGraphValidationResult result = validateUseCase.validate(currentDocument);
        statusConsumer.accept(result.summary());
        return result;
    }

    public void reorganizeLayout() {
        if (!ensureProjectForLayout("No hay grafo abierto para autoorganizar.")) {
            return;
        }
        currentProject = visualLayoutService.regenerateVisualLayout(currentProject.withFreeGraph(currentDocument));
        notifyProjectChanged();
        statusConsumer.accept("Grafo libre autoorganizado.");
    }

    public String nodeLabel(String nodeId) {
        return currentDocument == null ? nodeId : currentDocument.nodeById(nodeId)
                .map(FreeGraphNode::title)
                .orElse(nodeId);
    }

    private FreeGraphEdgeDirection defaultDirection() {
        return currentDocument.graphKind() == FreeGraphKind.UNDIRECTED
                ? FreeGraphEdgeDirection.UNDIRECTED
                : FreeGraphEdgeDirection.DIRECTED;
    }
}
