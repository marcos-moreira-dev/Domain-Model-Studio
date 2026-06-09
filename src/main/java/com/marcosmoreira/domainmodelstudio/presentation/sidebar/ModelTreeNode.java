package com.marcosmoreira.domainmodelstudio.presentation.sidebar;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.Optional;

/**
 * Valor semántico del árbol lateral.
 *
 * <p>Evita depender solo de texto visible. El texto puede cambiar, pero el ID del
 * elemento y la notación permiten sincronizar la selección con el diagrama de forma
 * estable.</p>
 */
public final class ModelTreeNode {

    private final String label;
    private final DiagramElementId elementId;
    private final NotationType notation;

    private ModelTreeNode(String label, DiagramElementId elementId, NotationType notation) {
        this.label = label == null || label.isBlank() ? "Elemento sin nombre" : label.trim();
        this.elementId = elementId;
        this.notation = notation;
    }

    public static ModelTreeNode group(String label) {
        return new ModelTreeNode(label, null, null);
    }

    public static ModelTreeNode element(String label, DiagramElementId elementId) {
        return new ModelTreeNode(label, elementId, null);
    }

    public static ModelTreeNode notation(String label, NotationType notation) {
        return new ModelTreeNode(label, null, notation);
    }

    public String label() {
        return label;
    }

    public Optional<DiagramElementId> elementId() {
        return Optional.ofNullable(elementId);
    }

    public Optional<NotationType> notation() {
        return Optional.ofNullable(notation);
    }

    public boolean selectable() {
        return elementId != null;
    }

    public boolean changesNotation() {
        return notation != null;
    }

    @Override
    public String toString() {
        return label;
    }
}
