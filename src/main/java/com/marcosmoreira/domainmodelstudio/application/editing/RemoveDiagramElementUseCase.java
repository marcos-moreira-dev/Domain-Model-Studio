package com.marcosmoreira.domainmodelstudio.application.editing;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Elimina una entidad, atributo o relación conceptual y limpia sus trazas visuales. */
public final class RemoveDiagramElementUseCase {

    public DiagramProject remove(DiagramProject project, DiagramElementId elementId) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        Objects.requireNonNull(elementId, "El ID del elemento no puede ser null");
        DiagramModel updatedModel;
        List<DiagramElementId> removedIds = new ArrayList<>();
        removedIds.add(elementId);
        if (project.model().entityById(elementId).isPresent()) {
            project.model().entityById(elementId).ifPresent(entity ->
                    entity.attributes().forEach(attribute -> removedIds.add(attribute.id())));
            project.model().relationships().stream()
                    .filter(relationship -> relationship.fromEntityId().equals(elementId)
                            || relationship.toEntityId().equals(elementId))
                    .forEach(relationship -> removedIds.add(relationship.id()));
            updatedModel = project.model().withoutEntity(elementId);
        } else if (project.model().relationshipById(elementId).isPresent()) {
            updatedModel = project.model().withoutRelationship(elementId);
        } else if (project.model().attributeById(elementId).isPresent()) {
            updatedModel = project.model().withoutAttribute(elementId);
        } else {
            throw new IllegalArgumentException("No existe elemento para eliminar: " + elementId);
        }
        DiagramProject withModel = project.withModel(updatedModel);
        return withModel.withLayouts(ConceptualLayoutEditingSupport.withoutElements(project, removedIds));
    }
}
