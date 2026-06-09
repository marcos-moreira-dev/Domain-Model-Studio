package com.marcosmoreira.domainmodelstudio.application.editing;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.Objects;

/** Mueve la etiqueta visual de una relación sin separarla de su línea base. */
public final class MoveConnectorLabelUseCase {

    private static final double MAXIMUM_LABEL_DISTANCE_FROM_LINE = 240.0;

    public DiagramProject moveBy(
            DiagramProject project,
            NotationType notation,
            DiagramElementId connectorId,
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

        DiagramLayout updatedLayout = layout.withConnector(
                connector.withMovedLabelOffset(deltaX, deltaY, MAXIMUM_LABEL_DISTANCE_FROM_LINE)
        );
        DiagramLayouts updatedLayouts = project.layouts().withLayout(updatedLayout);
        return project.withLayouts(updatedLayouts);
    }

    private static void ensureFinite(double value, String name) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("El valor " + name + " debe ser finito");
        }
    }
}
