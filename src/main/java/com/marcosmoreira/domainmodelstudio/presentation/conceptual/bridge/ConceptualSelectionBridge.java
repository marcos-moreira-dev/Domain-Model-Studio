package com.marcosmoreira.domainmodelstudio.presentation.conceptual.bridge;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.canvas.DiagramCanvasViewModel;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * Puente transversal de selección del modelo conceptual.
 *
 * <p>Expone la selección del canvas conceptual legacy como un contrato consumible por
 * SideDock, toolbar y futuras migraciones híbridas, sin sustituir el render Chen/Crow's Foot.</p>
 */
public final class ConceptualSelectionBridge {

    private final DiagramCanvasViewModel canvasViewModel;

    public ConceptualSelectionBridge(DiagramCanvasViewModel canvasViewModel) {
        this.canvasViewModel = Objects.requireNonNull(canvasViewModel, "canvasViewModel");
    }

    public ConceptualSelectionSnapshot snapshot() {
        DiagramProject project = canvasViewModel.currentProject();
        if (project == null) {
            return ConceptualSelectionSnapshot.empty();
        }
        boolean bendPointSelected = canvasViewModel.selectedBendPoint() != null;
        DiagramElementId selected = canvasViewModel.selectedElementId();
        ConceptualSelectedElementKind kind = bendPointSelected
                ? ConceptualSelectedElementKind.CONNECTOR_BEND_POINT
                : resolveKind(project, selected);
        return new ConceptualSelectionSnapshot(
                Optional.ofNullable(selected),
                canvasViewModel.selectedElementIds(),
                kind,
                bendPointSelected
        );
    }

    public boolean hasSelection() {
        return snapshot().hasSelection();
    }

    public boolean hasEntitySelection() {
        return snapshot().kind() == ConceptualSelectedElementKind.ENTITY;
    }

    public boolean hasRelationshipSelection() {
        return snapshot().kind() == ConceptualSelectedElementKind.RELATIONSHIP;
    }

    public boolean hasAttributeSelection() {
        return snapshot().kind() == ConceptualSelectedElementKind.ATTRIBUTE;
    }

    public void select(DiagramElementId elementId) {
        canvasViewModel.selectElement(elementId);
    }

    public void selectAll(Collection<DiagramElementId> elementIds, boolean extendCurrentSelection) {
        canvasViewModel.selectElements(elementIds, extendCurrentSelection);
    }

    public void clearSelection() {
        canvasViewModel.clearSelection();
    }

    private ConceptualSelectedElementKind resolveKind(DiagramProject project, DiagramElementId selected) {
        if (selected == null) {
            return ConceptualSelectedElementKind.NONE;
        }
        if (project.model().entityById(selected).isPresent()) {
            return ConceptualSelectedElementKind.ENTITY;
        }
        if (project.model().attributeById(selected).isPresent()) {
            return ConceptualSelectedElementKind.ATTRIBUTE;
        }
        if (project.model().relationshipById(selected).isPresent()) {
            return ConceptualSelectedElementKind.RELATIONSHIP;
        }
        return ConceptualSelectedElementKind.UNKNOWN;
    }
}
