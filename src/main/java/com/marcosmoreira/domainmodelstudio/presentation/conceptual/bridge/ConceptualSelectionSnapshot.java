package com.marcosmoreira.domainmodelstudio.presentation.conceptual.bridge;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Instantánea neutral de la selección conceptual expuesta a SideDock, toolbar y bridges. */
public record ConceptualSelectionSnapshot(
        Optional<DiagramElementId> primaryElementId,
        Set<DiagramElementId> selectedElementIds,
        ConceptualSelectedElementKind kind,
        boolean bendPointSelected
) {
    public ConceptualSelectionSnapshot {
        primaryElementId = primaryElementId == null ? Optional.empty() : primaryElementId;
        selectedElementIds = Set.copyOf(selectedElementIds == null ? Set.of() : selectedElementIds);
        kind = Objects.requireNonNullElse(kind, ConceptualSelectedElementKind.NONE);
    }

    public static ConceptualSelectionSnapshot empty() {
        return new ConceptualSelectionSnapshot(
                Optional.empty(),
                Set.of(),
                ConceptualSelectedElementKind.NONE,
                false
        );
    }

    public boolean hasSelection() {
        return primaryElementId.isPresent() || !selectedElementIds.isEmpty() || bendPointSelected;
    }

    public int selectionCount() {
        return selectedElementIds.size();
    }
}
