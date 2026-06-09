package com.marcosmoreira.domainmodelstudio.application.editing;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import java.util.Objects;
import java.util.Set;

/** Agrega un atributo a una entidad conceptual existente. */
public final class AddAttributeUseCase {

    public DiagramProject add(DiagramProject project, DiagramElementId entityId) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        Objects.requireNonNull(entityId, "El ID de la entidad no puede ser null");
        EntityElement ownerBefore = project.model().entityById(entityId)
                .orElseThrow(() -> new IllegalArgumentException("Selecciona una entidad para agregar atributo."));
        DiagramElementId attributeId = ConceptualEditingIds.uniqueElementId(project.model(), ownerBefore.id().value() + "_nuevo_atributo");
        AttributeElement attribute = new AttributeElement(attributeId, "Nuevo atributo", Set.of(), "");
        DiagramModel updatedModel = project.model().withAttribute(entityId, attribute);
        DiagramProject withModel = project.withModel(updatedModel);
        EntityElement ownerAfter = withModel.model().entityById(entityId)
                .orElseThrow(() -> new IllegalStateException("La entidad dejó de existir después de agregar atributo."));
        return withModel.withLayouts(ConceptualLayoutEditingSupport.withAttributeNode(withModel, ownerAfter, attribute));
    }
}
