package com.marcosmoreira.domainmodelstudio.presentation.selection;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Selección compartida entre área de trabajo, estructura e inspector.
 *
 * <p>La selección es un estado de presentación, no del dominio. Permite sincronizar
 * estructura lateral, diagrama y propiedades sin crear dependencias directas entre vistas.</p>
 */
public final class DiagramSelectionModel {

    private final ObjectProperty<DiagramElementId> selectedElementId = new SimpleObjectProperty<>();
    private final ObjectProperty<Set<DiagramElementId>> selectedElementIds =
            new SimpleObjectProperty<>(Set.of());

    public ObjectProperty<DiagramElementId> selectedElementIdProperty() {
        return selectedElementId;
    }

    public ObjectProperty<Set<DiagramElementId>> selectedElementIdsProperty() {
        return selectedElementIds;
    }

    public DiagramElementId selectedElementId() {
        return selectedElementId.get();
    }

    public Set<DiagramElementId> selectedElementIds() {
        return Set.copyOf(selectedElementIds.get());
    }

    public boolean isSelected(DiagramElementId elementId) {
        return elementId != null && selectedElementIds.get().contains(elementId);
    }

    public int selectionCount() {
        return selectedElementIds.get().size();
    }

    public void select(DiagramElementId elementId) {
        if (elementId == null) {
            clearSelection();
            return;
        }
        LinkedHashSet<DiagramElementId> next = new LinkedHashSet<>();
        next.add(elementId);
        applySelection(next, elementId);
    }

    public void toggle(DiagramElementId elementId) {
        if (elementId == null) {
            return;
        }
        LinkedHashSet<DiagramElementId> next = new LinkedHashSet<>(selectedElementIds.get());
        DiagramElementId nextPrimary = elementId;
        if (next.contains(elementId)) {
            next.remove(elementId);
            nextPrimary = next.stream().findFirst().orElse(null);
        } else {
            next.add(elementId);
        }
        applySelection(next, nextPrimary);
    }

    public void selectAll(Collection<DiagramElementId> elementIds) {
        LinkedHashSet<DiagramElementId> next = new LinkedHashSet<>();
        if (elementIds != null) {
            for (DiagramElementId elementId : elementIds) {
                if (elementId != null) {
                    next.add(elementId);
                }
            }
        }
        applySelection(next, next.stream().findFirst().orElse(null));
    }

    public void addAll(Collection<DiagramElementId> elementIds) {
        if (elementIds == null || elementIds.isEmpty()) {
            return;
        }
        LinkedHashSet<DiagramElementId> next = new LinkedHashSet<>(selectedElementIds.get());
        for (DiagramElementId elementId : elementIds) {
            if (elementId != null) {
                next.add(elementId);
            }
        }
        DiagramElementId primary = selectedElementId.get();
        if (primary == null || !next.contains(primary)) {
            primary = next.stream().findFirst().orElse(null);
        }
        applySelection(next, primary);
    }

    public void clearSelection() {
        applySelection(new LinkedHashSet<>(), null);
    }

    private void applySelection(LinkedHashSet<DiagramElementId> next, DiagramElementId primary) {
        selectedElementIds.set(Set.copyOf(next));
        selectedElementId.set(primary);
    }
}
