package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import java.util.Optional;
import java.util.Set;

/**
 * Estado transversal mínimo para adaptadores del canvas común.
 *
 * <p>Evita que cada adaptador especializado mantenga a mano una selección, un flag
 * dirty y la mecánica de puntos intermedios. No conoce ViewModels ni semántica de
 * diagramas; solo concentra operaciones de interacción compartidas.</p>
 */
public final class CanvasAdapterInteractionState {

    private final CanvasSelectionSupport selectionSupport = new CanvasSelectionSupport();
    private final CanvasDirtyState dirtyState = new CanvasDirtyState();
    private final CanvasBendPointEditingSupport bendPointSupport =
            new CanvasBendPointEditingSupport(selectionSupport, dirtyState);

    public InteractiveCanvasSelection selection() {
        return selectionSupport.current();
    }

    public Set<String> selectedNodeIds() {
        return selection().selectedNodeIds();
    }

    public Set<String> selectedConnectorIds() {
        return selection().selectedConnectorIds();
    }

    public Optional<SelectedBendPoint> selectedBendPoint() {
        return selection().selectedBendPoint();
    }

    public boolean shouldPreserveManualBendPointSelection() {
        return selectionSupport.shouldPreserveManualBendPointSelection();
    }

    public void selectNode(String nodeId, boolean additive) {
        selectionSupport.selectNode(nodeId, additive);
    }

    public void selectConnector(String connectorId) {
        selectionSupport.selectConnector(connectorId);
    }

    public void selectNodes(Set<String> nodeIds) {
        selectionSupport.selectNodes(nodeIds);
    }

    public void selectNodesAndConnectors(Set<String> nodeIds, Set<String> connectorIds) {
        selectionSupport.selectNodesAndConnectors(nodeIds, connectorIds);
    }

    public void selectBendPoint(String connectorId, int index) {
        bendPointSupport.selectBendPoint(connectorId, index);
    }

    public void markEditedBendPoint(String connectorId, int index) {
        bendPointSupport.markEditedBendPoint(connectorId, index);
    }

    public void clearSelection() {
        selectionSupport.clear();
    }

    public void clearAfterRemoval() {
        bendPointSupport.clearAfterRemoval();
    }

    public void markDirty() {
        dirtyState.markDirty();
    }

    public boolean dirty() {
        return dirtyState.dirty();
    }

    public void syncSingleNode(String nodeId) {
        if (!selection().isNodeSelected(nodeId)) {
            selectionSupport.selectNode(nodeId, false);
        }
    }

    public void syncSingleConnector(String connectorId) {
        if (!selection().isConnectorSelected(connectorId)) {
            selectionSupport.selectConnector(connectorId);
        }
    }

    public void clearIfNoBendPoint() {
        if (selection().selectedBendPoint().isEmpty()) {
            selectionSupport.clear();
        }
    }
}
