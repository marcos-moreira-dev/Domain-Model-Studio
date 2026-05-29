package com.marcosmoreira.domainmodelstudio.application.editing;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.Objects;

/** Caso de uso para ajustar posición y tamaño de un nodo sin tocar la semántica. */
public final class UpdateNodeLayoutUseCase {

    public DiagramProject update(
            DiagramProject project,
            NotationType notation,
            DiagramElementId elementId,
            double x,
            double y,
            double width,
            double height
    ) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        Objects.requireNonNull(elementId, "El ID del elemento no puede ser null");
        NotationType resolvedNotation = notation == null ? project.layouts().activeNotation() : notation;
        DiagramLayout layout = project.layouts()
                .layoutFor(resolvedNotation)
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para la notación: " + resolvedNotation));
        NodeLayout current = layout.nodeFor(elementId)
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para el elemento: " + elementId));
        NodeLayout updatedNode = current.movedTo(x, y).resizedTo(width, height);
        return project.withLayouts(project.layouts().withLayout(layout.withNode(updatedNode)));
    }
}
