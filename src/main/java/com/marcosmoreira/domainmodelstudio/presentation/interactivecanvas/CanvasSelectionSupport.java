package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import java.util.Objects;
import java.util.Set;

/**
 * Soporte transversal para mantener selección normalizada en adaptadores del lienzo.
 *
 * <p>No conoce ViewModels ni tipos de diagrama; solo encapsula las operaciones mecánicas
 * sobre {@link InteractiveCanvasSelection}.</p>
 */
public final class CanvasSelectionSupport {

    private InteractiveCanvasSelection selection = InteractiveCanvasSelection.empty();

    public InteractiveCanvasSelection current() {
        return selection;
    }

    public void replace(InteractiveCanvasSelection selection) {
        this.selection = Objects.requireNonNull(selection, "selection");
    }

    public void selectNode(String nodeId, boolean additive) {
        selection = additive ? selection.toggledNode(nodeId) : selection.withSingleNode(nodeId);
    }

    public void selectConnector(String connectorId) {
        selection = selection.withSingleConnector(connectorId);
    }

    public void selectNodes(Set<String> nodeIds) {
        selection = selection.withNodes(nodeIds);
    }

    public void selectNodesAndConnectors(Set<String> nodeIds, Set<String> connectorIds) {
        selection = selection.withNodesAndConnectors(nodeIds, connectorIds);
    }

    public void selectBendPoint(String connectorId, int index) {
        selection = CanvasBendPointSelectionPolicy.selectBendPoint(connectorId, index);
    }

    public boolean shouldPreserveManualBendPointSelection() {
        return CanvasBendPointSelectionPolicy.preserveManualBendPointSelection(selection);
    }

    public void clear() {
        selection = InteractiveCanvasSelection.empty();
    }
}
