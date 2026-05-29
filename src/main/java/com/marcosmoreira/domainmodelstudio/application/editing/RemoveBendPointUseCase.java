package com.marcosmoreira.domainmodelstudio.application.editing;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.Objects;

/** Elimina un punto intermedio de una línea editable. */
public final class RemoveBendPointUseCase {

    public DiagramProject remove(
            DiagramProject project,
            NotationType notation,
            DiagramElementId connectorId,
            int bendPointIndex
    ) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        Objects.requireNonNull(connectorId, "El ID del conector no puede ser null");

        NotationType resolvedNotation = notation == null ? project.layouts().activeNotation() : notation;
        DiagramLayout layout = project.layouts()
                .layoutFor(resolvedNotation)
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para la notación: " + resolvedNotation));
        var connector = layout.connectorById(connectorId)
                .orElseThrow(() -> new IllegalArgumentException("No existe conector: " + connectorId));

        DiagramLayout updatedLayout = layout.withConnector(connector.withoutBendPoint(bendPointIndex));
        DiagramLayouts updatedLayouts = project.layouts().withLayout(updatedLayout);
        return project.withLayouts(updatedLayouts);
    }
}
