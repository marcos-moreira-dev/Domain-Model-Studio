package com.marcosmoreira.domainmodelstudio.application.editing;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.BendPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.Objects;

/**
 * Agrega un punto intermedio editable a un conector de una notación concreta.
 *
 * <p>El caso de uso actualiza únicamente layout visual. No cambia entidades,
 * relaciones ni cardinalidades del modelo conceptual.</p>
 */
public final class AddBendPointUseCase {

    public DiagramProject add(
            DiagramProject project,
            NotationType notation,
            DiagramElementId connectorId,
            double x,
            double y
    ) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        Objects.requireNonNull(connectorId, "El ID del conector no puede ser null");
        ensureFinite(x, "x");
        ensureFinite(y, "y");

        NotationType resolvedNotation = notation == null ? project.layouts().activeNotation() : notation;
        DiagramLayout layout = project.layouts()
                .layoutFor(resolvedNotation)
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para la notación: " + resolvedNotation));
        var connector = layout.connectorById(connectorId)
                .orElseThrow(() -> new IllegalArgumentException("No existe conector: " + connectorId));

        DiagramLayout updatedLayout = layout.withConnector(connector.withBendPoint(BendPoint.of(x, y)));
        DiagramLayouts updatedLayouts = project.layouts().withLayout(updatedLayout);
        return project.withLayouts(updatedLayouts);
    }

    private static void ensureFinite(double value, String name) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("El valor " + name + " debe ser finito");
        }
    }
}
