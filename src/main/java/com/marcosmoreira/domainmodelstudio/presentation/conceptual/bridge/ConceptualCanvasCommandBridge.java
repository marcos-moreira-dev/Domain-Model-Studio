package com.marcosmoreira.domainmodelstudio.presentation.conceptual.bridge;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.canvas.DiagramCanvasViewModel;
import java.util.Objects;

/**
 * Puente de comandos conceptuales.
 *
 * <p>Centraliza los comandos que el SideDock y la toolbar común pueden ejecutar sobre el
 * canvas conceptual actual. No reimplementa edición ER: delega en {@link DiagramCanvasViewModel}.</p>
 */
public final class ConceptualCanvasCommandBridge {

    private final DiagramCanvasViewModel canvasViewModel;
    private final ConceptualSelectionBridge selectionBridge;

    public ConceptualCanvasCommandBridge(
            DiagramCanvasViewModel canvasViewModel,
            ConceptualSelectionBridge selectionBridge
    ) {
        this.canvasViewModel = Objects.requireNonNull(canvasViewModel, "canvasViewModel");
        this.selectionBridge = Objects.requireNonNull(selectionBridge, "selectionBridge");
    }

    public DiagramProject currentProject() {
        return canvasViewModel.currentProject();
    }

    public ConceptualSelectionSnapshot selection() {
        return selectionBridge.snapshot();
    }

    public void beginAddEntity() {
        canvasViewModel.beginAddEntityTool();
    }

    public void beginAddRelationship() {
        canvasViewModel.beginAddRelationshipTool();
    }

    public void addAttributeToSelectedEntity() {
        canvasViewModel.addAttributeToSelectedEntity();
    }

    public void duplicateSelectedEntity() {
        canvasViewModel.duplicateSelectedEntity();
    }

    public void removeSelectedElement() {
        canvasViewModel.removeSelectedElement();
    }

    public boolean removeSelectedBendPoint() {
        return canvasViewModel.removeSelectedBendPoint();
    }

    public void selectAllVisibleElements() {
        canvasViewModel.selectAllVisibleElements();
    }

    public void clearSelection() {
        canvasViewModel.clearSelection();
    }

    public void moveSelectedBy(double deltaX, double deltaY) {
        canvasViewModel.moveSelectedElementBy(deltaX, deltaY);
    }

    public void select(DiagramElementId elementId) {
        selectionBridge.select(elementId);
    }
}
