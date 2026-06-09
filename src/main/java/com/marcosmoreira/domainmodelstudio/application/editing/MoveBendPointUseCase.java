package com.marcosmoreira.domainmodelstudio.application.editing;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.Objects;

/** Mueve un punto intermedio existente de una línea poligonal. */
public final class MoveBendPointUseCase {

    public DiagramProject moveBy(
            DiagramProject project,
            NotationType notation,
            DiagramElementId connectorId,
            int bendPointIndex,
            double deltaX,
            double deltaY
    ) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        Objects.requireNonNull(connectorId, "El ID del conector no puede ser null");
        ensureFinite(deltaX, "deltaX");
        ensureFinite(deltaY, "deltaY");

        NotationType resolvedNotation = notation == null ? project.layouts().activeNotation() : notation;
        DiagramLayout layout = project.layouts()
                .layoutFor(resolvedNotation)
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para la notación: " + resolvedNotation));
        var connector = layout.connectorById(connectorId)
                .orElseThrow(() -> new IllegalArgumentException("No existe conector: " + connectorId));

        DiagramLayout updatedLayout = layout.withConnector(connector.withMovedBendPoint(bendPointIndex, deltaX, deltaY));
        DiagramLayouts updatedLayouts = project.layouts().withLayout(updatedLayout);
        return project.withLayouts(updatedLayouts);
    }

    private static void ensureFinite(double value, String name) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("El valor " + name + " debe ser finito");
        }
    }
}
