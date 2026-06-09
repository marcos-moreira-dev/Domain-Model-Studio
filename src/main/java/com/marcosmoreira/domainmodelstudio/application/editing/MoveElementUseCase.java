package com.marcosmoreira.domainmodelstudio.application.editing;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.Objects;

/**
 * Caso de uso para mover un elemento visual dentro del layout de una notación.
 *
 * <p>La edición de posición no cambia la semántica del modelo: una entidad sigue siendo la
 * misma entidad, una relación sigue apuntando a las mismas entidades y los atributos no cambian.
 * Solo se actualiza el layout visual correspondiente a la notación activa. Esta separación es
 * importante para que Chen y Crow's Foot puedan compartir dominio, pero conservar acomodos
 * diferentes.</p>
 */
public final class MoveElementUseCase {

    public DiagramProject moveBy(
            DiagramProject project,
            NotationType notation,
            DiagramElementId elementId,
            double deltaX,
            double deltaY
    ) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        Objects.requireNonNull(elementId, "El ID del elemento no puede ser null");
        ensureFinite(deltaX, "deltaX");
        ensureFinite(deltaY, "deltaY");

        NotationType resolvedNotation = notation == null ? project.layouts().activeNotation() : notation;
        DiagramLayout layout = project.layouts()
                .layoutFor(resolvedNotation)
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para la notación: " + resolvedNotation));
        NodeLayout node = layout.nodeFor(elementId)
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para el elemento: " + elementId));

        if (node.locked()) {
            return project;
        }

        DiagramLayout updatedLayout = layout.withNode(node.translatedBy(deltaX, deltaY));
        DiagramLayouts updatedLayouts = project.layouts().withLayout(updatedLayout);
        return project.withLayouts(updatedLayouts);
    }

    private static void ensureFinite(double value, String name) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("El valor " + name + " debe ser finito");
        }
    }
}
