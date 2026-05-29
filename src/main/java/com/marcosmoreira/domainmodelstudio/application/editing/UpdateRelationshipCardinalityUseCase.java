package com.marcosmoreira.domainmodelstudio.application.editing;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.Cardinality;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorMarker;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.Objects;

/** Actualiza las cardinalidades conceptuales de una relación. */
public final class UpdateRelationshipCardinalityUseCase {

    public DiagramProject update(
            DiagramProject project,
            DiagramElementId relationshipId,
            String fromCardinality,
            String toCardinality
    ) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        Objects.requireNonNull(relationshipId, "El ID de la relación no puede ser null");
        RelationshipElement relationship = project.model().relationshipById(relationshipId)
                .orElseThrow(() -> new IllegalArgumentException("No existe relación: " + relationshipId));
        RelationshipElement updatedRelationship = relationship.withCardinalities(
                Cardinality.of(fromCardinality),
                Cardinality.of(toCardinality)
        );
        DiagramProject withModel = project.withModel(project.model().withUpdatedRelationship(updatedRelationship));
        return withModel.withLayouts(updateCrowsFootMarkers(withModel, updatedRelationship));
    }

    private DiagramLayouts updateCrowsFootMarkers(DiagramProject project, RelationshipElement relationship) {
        DiagramLayout crowsFootLayout = project.layouts().layoutFor(NotationType.CROWS_FOOT).orElse(null);
        if (crowsFootLayout == null) {
            return project.layouts();
        }
        DiagramElementId connectorId = DiagramElementId.of("conn_crowsfoot_" + relationship.id().value());
        ConnectorLayout connector = crowsFootLayout.connectorById(connectorId).orElse(null);
        if (connector == null) {
            return project.layouts();
        }
        ConnectorLayout updatedConnector = connector.withMarkers(
                markerFor(relationship.fromCardinality()),
                markerFor(relationship.toCardinality())
        );
        return project.layouts().withLayout(crowsFootLayout.withConnector(updatedConnector));
    }

    private ConnectorMarker markerFor(Cardinality cardinality) {
        if (cardinality.isMany()) {
            return cardinality.isOptional() ? ConnectorMarker.OPTIONAL_MANY : ConnectorMarker.MANY;
        }
        return cardinality.isOptional() ? ConnectorMarker.OPTIONAL_ONE : ConnectorMarker.ONE;
    }
}
