package com.marcosmoreira.domainmodelstudio.application.editing;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.layout.MarkerOrientation;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.Objects;

/**
 * Cambia la orientación manual de marcadores de extremos.
 *
 * <p>En Chen normalmente los conectores no tienen marcadores. En Crow's Foot, en cambio,
 * la pata de gallo puede necesitar orientación manual cuando el resultado automático queda
 * visualmente forzado.</p>
 */
public final class ChangeConnectorMarkerOrientationUseCase {

    public DiagramProject changeOrientations(
            DiagramProject project,
            NotationType notation,
            DiagramElementId connectorId,
            MarkerOrientation sourceOrientation,
            MarkerOrientation targetOrientation
    ) {
        Objects.requireNonNull(project, "project");
        Objects.requireNonNull(connectorId, "connectorId");
        NotationType effectiveNotation = notation == null ? project.metadata().activeNotation() : notation;
        DiagramLayout layout = project.layouts()
                .layoutFor(effectiveNotation)
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para notación: " + effectiveNotation));
        ConnectorLayout connector = layout.connectorById(connectorId)
                .orElseThrow(() -> new IllegalArgumentException("No existe conector: " + connectorId));
        DiagramLayout updatedLayout = layout.withConnector(
                connector.withMarkerOrientations(sourceOrientation, targetOrientation)
        );
        DiagramLayouts updatedLayouts = project.layouts().withLayout(updatedLayout);
        return project.withLayouts(updatedLayouts);
    }
}
