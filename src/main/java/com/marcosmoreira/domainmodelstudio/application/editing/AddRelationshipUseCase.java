package com.marcosmoreira.domainmodelstudio.application.editing;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.Cardinality;
import com.marcosmoreira.domainmodelstudio.domain.er.ParticipationType;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipKind;
import java.util.Objects;

/** Crea una relación conceptual binaria entre dos entidades. */
public final class AddRelationshipUseCase {

    public DiagramProject add(DiagramProject project, DiagramElementId fromEntityId, DiagramElementId toEntityId) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        Objects.requireNonNull(fromEntityId, "La entidad origen no puede ser null");
        Objects.requireNonNull(toEntityId, "La entidad destino no puede ser null");
        project.model().entityById(fromEntityId)
                .orElseThrow(() -> new IllegalArgumentException("La entidad origen no existe: " + fromEntityId));
        project.model().entityById(toEntityId)
                .orElseThrow(() -> new IllegalArgumentException("La entidad destino no existe: " + toEntityId));
        DiagramElementId relationshipId = ConceptualEditingIds.uniqueElementId(project.model(), "nueva_relacion");
        RelationshipElement relationship = new RelationshipElement(
                relationshipId,
                "Nueva relación",
                fromEntityId,
                toEntityId,
                Cardinality.of("1"),
                Cardinality.of("1..M"),
                RelationshipKind.REGULAR,
                ParticipationType.UNSPECIFIED,
                ParticipationType.UNSPECIFIED,
                ""
        );
        DiagramModel updatedModel = project.model().withRelationship(relationship);
        DiagramProject withModel = project.withModel(updatedModel);
        return withModel.withLayouts(ConceptualLayoutEditingSupport.withRelationshipLayout(withModel, relationship));
    }
}
