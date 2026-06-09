package com.marcosmoreira.domainmodelstudio.presentation.conceptual.bridge;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.canvas.DiagramCanvasViewModel;
import java.util.Objects;
import java.util.Optional;

/**
 * Puente de layout conceptual hacia contratos transversales.
 *
 * <p>Lee y mueve el layout conceptual activo usando el ViewModel actual. Esta clase no introduce servicios visuales transversales ni migra el dibujo a otro motor; solo
 * expone una frontera limpia para la etapa híbrida.</p>
 */
public final class ConceptualLayoutBridge {

    private final DiagramCanvasViewModel canvasViewModel;
    private final ConceptualSelectionBridge selectionBridge;

    public ConceptualLayoutBridge(
            DiagramCanvasViewModel canvasViewModel,
            ConceptualSelectionBridge selectionBridge
    ) {
        this.canvasViewModel = Objects.requireNonNull(canvasViewModel, "canvasViewModel");
        this.selectionBridge = Objects.requireNonNull(selectionBridge, "selectionBridge");
    }

    public Optional<DiagramLayout> activeLayout() {
        DiagramProject project = canvasViewModel.currentProject();
        if (project == null) {
            return Optional.empty();
        }
        return project.layouts().layoutFor(canvasViewModel.activeNotation());
    }

    public Optional<NodeLayout> selectedNodeLayout() {
        return selectionBridge.snapshot()
                .primaryElementId()
                .flatMap(this::nodeLayoutFor);
    }

    public Optional<NodeLayout> nodeLayoutFor(DiagramElementId elementId) {
        if (elementId == null) {
            return Optional.empty();
        }
        return activeLayout().flatMap(layout -> layout.nodeFor(elementId));
    }

    public Optional<ConnectorLayout> connectorLayoutFor(DiagramElementId connectorId) {
        if (connectorId == null) {
            return Optional.empty();
        }
        return activeLayout().flatMap(layout -> layout.connectorById(connectorId));
    }

    public int nodeCount() {
        return activeLayout().map(layout -> layout.nodes().size()).orElse(0);
    }

    public int connectorCount() {
        return activeLayout().map(layout -> layout.connectors().size()).orElse(0);
    }

    public DiagramProject previewMoveSelectedBy(double deltaX, double deltaY) {
        return canvasViewModel.previewMoveSelectedElementBy(deltaX, deltaY);
    }

    public void moveSelectedBy(double deltaX, double deltaY) {
        canvasViewModel.moveSelectedElementBy(deltaX, deltaY);
    }
}
