package com.marcosmoreira.domainmodelstudio.application.editing;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.layout.AnchorSide;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.Objects;

/**
 * Cambia las anclas explícitas de un conector.
 *
 * <p>Este caso de uso existe para que la UI no modifique directamente el layout. Es
 * especialmente importante antes de Crow's Foot, porque la orientación de los extremos y
 * de la pata de gallo depende del lado real por donde entra o sale la línea.</p>
 */
public final class ChangeConnectorAnchorsUseCase {

    public DiagramProject changeAnchors(
            DiagramProject project,
            NotationType notation,
            DiagramElementId connectorId,
            AnchorSide sourceAnchor,
            AnchorSide targetAnchor
    ) {
        Objects.requireNonNull(project, "project");
        Objects.requireNonNull(connectorId, "connectorId");
        NotationType effectiveNotation = notation == null ? project.metadata().activeNotation() : notation;
        DiagramLayout layout = project.layouts()
                .layoutFor(effectiveNotation)
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para notación: " + effectiveNotation));
        ConnectorLayout connector = layout.connectorById(connectorId)
                .orElseThrow(() -> new IllegalArgumentException("No existe conector: " + connectorId));
        DiagramLayout updatedLayout = layout.withConnector(connector.withAnchors(sourceAnchor, targetAnchor));
        DiagramLayouts updatedLayouts = project.layouts().withLayout(updatedLayout);
        return project.withLayouts(updatedLayouts);
    }
}
