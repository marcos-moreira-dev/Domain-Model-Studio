package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayerOrderCommand;
import com.marcosmoreira.domainmodelstudio.application.visualcomment.VisualCommentProjectService;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.style.ElementStyle;
import com.marcosmoreira.domainmodelstudio.domain.visualcomment.VisualComment;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;
/** Adapter decorador que agrega notas visuales persistentes a un canvas interactivo. */
public final class VisualCommentCanvasAdapter implements
        InteractiveCanvasAdapter,
        CanvasProjectStylePort,
        CanvasBackgroundClickPort,
        CanvasResizePort,
        CanvasConnectorLabelPort,
        CanvasConnectorHitTestPort,
        CanvasDragPreviewPort,
        CanvasLivePreviewPort,
        CanvasSelectionClipboardPort,
        CanvasRenderFailurePort,
        CanvasNodeDoubleClickPort,
        CanvasVisualCommentPort {
    private final InteractiveCanvasAdapter delegate;
    private final CanvasProjectStylePort projectPort;
    private final VisualCommentProjectService commentService = new VisualCommentProjectService();
    private final VisualCommentDialog commentDialog = new VisualCommentDialog();
    private final Set<String> selectedCommentIds = new LinkedHashSet<>();
    private boolean commentPlacementActive;
    public VisualCommentCanvasAdapter(InteractiveCanvasAdapter delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
        if (!(delegate instanceof CanvasProjectStylePort port)) {
            throw new IllegalArgumentException("Las notas visuales requieren un adapter con proyecto editable.");
        }
        this.projectPort = port;
    }
    public void activateCommentPlacement() {
        if (currentStyleProject() == null) {
            return;
        }
        commentPlacementActive = true;
    }
    public boolean commentPlacementActive() { return commentPlacementActive; }
    public boolean hasSelectedVisualComment() { return !selectedCommentIds.isEmpty(); }
    public boolean deleteSelectedVisualComment() {
        if (selectedCommentIds.isEmpty()) {
            return false;
        }
        List<String> selected = List.copyOf(selectedCommentIds);
        patchStyleProject(project -> {
            DiagramProject updated = project;
            for (String commentId : selected) {
                updated = commentService.removeComment(updated, commentId);
            }
            return updated;
        }, "Nota visual eliminada.");
        selectedCommentIds.clear();
        return true;
    }
    public boolean reorderSelectedVisualComment(VisualLayerOrderCommand command) {
        if (selectedCommentIds.isEmpty()) {
            return false;
        }
        patchStyleProject(
                project -> commentService.reorderComments(project, selectedCommentIds, command),
                "Capa de nota visual actualizada.");
        return true;
    }
    public boolean resizeSelectedVisualComment(double factor) {
        if (selectedCommentIds.size() != 1 || !Double.isFinite(factor)) {
            return false;
        }
        String selected = selectedCommentIds.iterator().next();
        Optional<NodeLayout> layout = layoutForNode(selected);
        if (layout.isEmpty()) {
            return false;
        }
        NodeLayout current = layout.get();
        patchStyleProject(
                project -> commentService.resizeCommentTo(
                        project,
                        selected,
                        current.width() * factor,
                        current.height() * factor),
                "Tamano de nota visual actualizado.");
        return true;
    }
    @Override
    public DiagramTypeId diagramTypeId() {
        return delegate.diagramTypeId();
    }
    @Override
    public List<InteractiveCanvasNode> nodes() {
        List<InteractiveCanvasNode> result = new ArrayList<>(delegate.nodes());
        DiagramProject project = currentStyleProject();
        if (project == null) {
            return result;
        }
        for (VisualComment comment : project.visualComments().comments()) {
            String layoutId = VisualElementLayoutIds.visualComment(comment.id()).value();
            if (project.layouts().activeLayout().nodeFor(DiagramElementId.of(layoutId)).isPresent()) {
                result.add(new InteractiveCanvasNode(
                        layoutId,
                        comment.visibleTitle(),
                        comment.visibleDescription(),
                        VisualCommentRenderKit.NODE_KIND,
                        true,
                        false));
            }
        }
        return result;
    }
    @Override
    public List<InteractiveCanvasConnector> connectors() {
        return delegate.connectors();
    }
    @Override
    public Optional<NodeLayout> layoutForNode(String elementId) {
        if (isVisualComment(elementId)) {
            DiagramProject project = currentStyleProject();
            if (project == null) {
                return Optional.empty();
            }
            return project.layouts().activeLayout().nodeFor(DiagramElementId.of(elementId));
        }
        return delegate.layoutForNode(elementId);
    }
    @Override
    public Optional<ConnectorLayout> layoutForConnector(String connectorId) {
        return delegate.layoutForConnector(connectorId);
    }
    @Override
    public CanvasBounds contentBounds() {
        CanvasBounds bounds = delegate.contentBounds();
        for (String commentId : commentLayoutIds()) {
            bounds = CanvasBounds.union(bounds, layoutForNode(commentId).map(CanvasBounds::from).orElse(null));
        }
        return bounds == null ? CanvasBounds.of(0.0, 0.0, 880.0, 600.0) : bounds;
    }
    @Override
    public InteractiveCanvasSelection selection() {
        InteractiveCanvasSelection base = delegate.selection();
        if (selectedCommentIds.isEmpty()) {
            return base;
        }
        Set<String> nodes = new LinkedHashSet<>(base.selectedNodeIds());
        nodes.addAll(selectedCommentIds);
        return new InteractiveCanvasSelection(nodes, base.selectedConnectorIds(), base.selectedBendPoint().orElse(null));
    }
    @Override
    public void selectNode(String elementId, boolean additive) {
        if (isVisualComment(elementId)) {
            if (!additive) {
                delegate.clearSelection();
                selectedCommentIds.clear();
            }
            if (additive && selectedCommentIds.contains(elementId)) {
                selectedCommentIds.remove(elementId);
            } else {
                selectedCommentIds.add(elementId);
            }
            return;
        }
        if (!additive) {
            selectedCommentIds.clear();
        }
        delegate.selectNode(elementId, additive);
    }
    @Override
    public void selectConnector(String connectorId, boolean additive) {
        if (!additive) {
            selectedCommentIds.clear();
        }
        delegate.selectConnector(connectorId, additive);
    }
    @Override
    public void selectNodesInside(CanvasBounds selectionBounds, boolean additive) {
        selectElementsInside(selectionBounds, additive);
    }
    @Override
    public void selectElementsInside(CanvasBounds selectionBounds, boolean additive) {
        if (!additive) {
            selectedCommentIds.clear();
        }
        delegate.selectElementsInside(selectionBounds, additive);
        for (String commentId : commentLayoutIds()) {
            if (layoutForNode(commentId).map(CanvasBounds::from).filter(bounds -> bounds.intersects(selectionBounds)).isPresent()) {
                selectedCommentIds.add(commentId);
            }
        }
    }
    @Override
    public void clearSelection() {
        selectedCommentIds.clear();
        delegate.clearSelection();
    }
    @Override
    public void moveNode(String elementId, double x, double y) {
        if (isVisualComment(elementId)) {
            patchStyleProject(project -> commentService.moveCommentTo(project, elementId, x, y), "Nota visual movida.");
            markDirty();
            return;
        }
        delegate.moveNode(elementId, x, y);
    }
    @Override
    public void moveSelectedNodesBy(double deltaX, double deltaY) {
        if (!selectedCommentIds.isEmpty()) {
            patchStyleProject(
                    project -> commentService.moveCommentsBy(project, selectedCommentIds, deltaX, deltaY),
                    "Nota visual movida.");
            markDirty();
        }
        if (!delegate.selection().selectedNodeIds().isEmpty()) {
            delegate.moveSelectedNodesBy(deltaX, deltaY);
        }
    }
    @Override
    public void moveSelectedConnectorBendPointsBy(double deltaX, double deltaY) {
        delegate.moveSelectedConnectorBendPointsBy(deltaX, deltaY);
    }
    @Override
    public boolean handleBackgroundClick(double x, double y, boolean additive, int clickCount) {
        if (commentPlacementActive) {
            if (clickCount > 1) {
                return true;
            }
            final String[] createdId = new String[1];
            patchStyleProject(project -> {
                VisualCommentProjectService.AddResult result = commentService.addComment(project, x, y);
                createdId[0] = result.layoutId();
                return result.project();
            }, "Nota visual agregada.");
            commentPlacementActive = false;
            clearSelection();
            if (createdId[0] != null) {
                selectNode(createdId[0], false);
            }
            markDirty();
            return true;
        }
        return delegate instanceof CanvasBackgroundClickPort port
                && port.handleBackgroundClick(x, y, additive, clickCount);
    }
    @Override
    public boolean handleNodeDoubleClick(String nodeId, String role) {
        if (!isVisualComment(nodeId)) {
            return delegate instanceof CanvasNodeDoubleClickPort port && port.handleNodeDoubleClick(nodeId, role);
        }
        DiagramProject project = currentStyleProject();
        if (project == null) {
            return false;
        }
        String rawId = VisualElementLayoutIds.rawVisualCommentId(nodeId);
        Optional<VisualComment> comment = project.visualComments().commentById(rawId);
        if (comment.isEmpty()) {
            return false;
        }
        Optional<VisualCommentDialog.Result> result = VisualCommentRenderKit.DESCRIPTION_ROLE.equals(role)
                ? commentDialog.editDescription(comment.get())
                : commentDialog.editTitle(comment.get());
        if (result.isEmpty()) {
            return true;
        }
        VisualCommentDialog.Result value = result.get();
        if (value.delete()) {
            patchStyleProject(updated -> commentService.removeComment(updated, nodeId), "Nota visual eliminada.");
            selectedCommentIds.clear();
            return true;
        }
        patchStyleProject(updated -> VisualCommentRenderKit.DESCRIPTION_ROLE.equals(role)
                        ? commentService.updateDescription(updated, nodeId, value.description())
                        : commentService.updateTitle(updated, nodeId, value.title()),
                "Nota visual actualizada.");
        selectedCommentIds.clear();
        selectedCommentIds.add(nodeId);
        return true;
    }
    @Override
    public void resizeNode(String elementId, double width, double height) {
        if (isVisualComment(elementId)) {
            patchStyleProject(project -> commentService.resizeCommentTo(project, elementId, width, height),
                    "Tamano de nota visual actualizado.");
            markDirty();
            return;
        }
        if (delegate instanceof CanvasResizePort port) {
            port.resizeNode(elementId, width, height);
        }
    }
    @Override
    public boolean supportsNodeResize() {
        return true;
    }
    @Override
    public boolean supportsNodeResize(String elementId) {
        if (isVisualComment(elementId)) {
            return true;
        }
        return delegate instanceof CanvasResizePort port && port.supportsNodeResize(elementId);
    }
    @Override
    public void addBendPoint(String connectorId, double x, double y) {
        delegate.addBendPoint(connectorId, x, y);
    }
    @Override
    public void moveBendPoint(String connectorId, int index, double x, double y) {
        delegate.moveBendPoint(connectorId, index, x, y);
    }
    @Override
    public void selectBendPoint(String connectorId, int index) {
        selectedCommentIds.clear();
        delegate.selectBendPoint(connectorId, index);
    }
    @Override
    public void removeSelectedBendPoint() {
        delegate.removeSelectedBendPoint();
    }
    @Override
    public boolean supportsBendPoints() {
        return delegate.supportsBendPoints();
    }
    @Override
    public void markDirty() {
        delegate.markDirty();
    }
    @Override
    public DiagramProject currentStyleProject() {
        return projectPort.currentStyleProject();
    }
    @Override
    public void patchStyleProject(UnaryOperator<DiagramProject> patch, String statusMessage) {
        projectPort.patchStyleProject(patch, statusMessage);
    }
    @Override
    public Optional<ElementStyle> explicitStyleForElement(String elementId) {
        if (isVisualComment(elementId)) {
            return Optional.empty();
        }
        return projectPort.explicitStyleForElement(elementId);
    }
    @Override
    public ElementStyle resolvedStyleForElement(String elementId) {
        if (isVisualComment(elementId)) {
            return ElementStyle.defaultElement();
        }
        return projectPort.resolvedStyleForElement(elementId);
    }
    @Override
    public void applyElementStyle(String elementId, ElementStyle style) {
        if (!isVisualComment(elementId)) {
            projectPort.applyElementStyle(elementId, style);
        }
    }
    @Override
    public void resetElementStyle(String elementId) {
        if (!isVisualComment(elementId)) {
            projectPort.resetElementStyle(elementId);
        }
    }
    @Override
    public void moveConnectorLabelBy(String connectorId, double deltaX, double deltaY) {
        if (delegate instanceof CanvasConnectorLabelPort port) {
            port.moveConnectorLabelBy(connectorId, deltaX, deltaY);
        }
    }
    @Override
    public boolean supportsConnectorLabels() {
        return delegate instanceof CanvasConnectorLabelPort port && port.supportsConnectorLabels();
    }
    @Override
    public boolean connectorLineHitTestingEnabled() {
        return delegate instanceof CanvasConnectorHitTestPort port && port.connectorLineHitTestingEnabled();
    }
    @Override
    public Set<String> previewNodeIdsForDraggedNode(String draggedNodeId, Set<String> selectedNodeIds) {
        if (isVisualComment(draggedNodeId)) {
            return selectedCommentIds.isEmpty() ? Set.of(draggedNodeId) : Set.copyOf(selectedCommentIds);
        }
        return delegate instanceof CanvasDragPreviewPort port
                ? port.previewNodeIdsForDraggedNode(draggedNodeId, selectedNodeIds)
                : Set.copyOf(selectedNodeIds == null ? Set.<String>of() : selectedNodeIds);
    }
    @Override
    public void beginPreview(String elementId, double canvasX, double canvasY) {
        if (!isVisualComment(elementId) && delegate instanceof CanvasLivePreviewPort port) {
            port.beginPreview(elementId, canvasX, canvasY);
        }
    }
    @Override
    public void updatePreview(String elementId, double canvasX, double canvasY) {
        if (!isVisualComment(elementId) && delegate instanceof CanvasLivePreviewPort port) {
            port.updatePreview(elementId, canvasX, canvasY);
        }
    }
    @Override
    public void commitPreview() {
        if (delegate instanceof CanvasLivePreviewPort port) {
            port.commitPreview();
        }
    }
    @Override
    public void cancelPreview() {
        if (delegate instanceof CanvasLivePreviewPort port) {
            port.cancelPreview();
        }
    }
    @Override
    public boolean supportsLivePreview() {
        return delegate instanceof CanvasLivePreviewPort port && port.supportsLivePreview();
    }
    @Override
    public boolean copySelectionToClipboard() {
        return selectedCommentIds.isEmpty()
                && delegate instanceof CanvasSelectionClipboardPort port
                && port.copySelectionToClipboard();
    }
    @Override
    public boolean pasteSelectionFromClipboard() {
        return delegate instanceof CanvasSelectionClipboardPort port && port.pasteSelectionFromClipboard();
    }
    @Override
    public void handleCanvasRenderFailure(CanvasRenderFailureReport report) {
        if (delegate instanceof CanvasRenderFailurePort port) {
            port.handleCanvasRenderFailure(report);
        }
    }
    private List<String> commentLayoutIds() {
        DiagramProject project = currentStyleProject();
        if (project == null) {
            return List.of();
        }
        return project.visualComments().comments().stream()
                .map(comment -> VisualElementLayoutIds.visualComment(comment.id()).value())
                .toList();
    }
    private boolean isVisualComment(String elementId) {
        return VisualElementLayoutIds.isVisualComment(elementId);
    }
}
