package com.marcosmoreira.domainmodelstudio.application.editing;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Objects;

/** Caso de uso para editar la descripción semántica de un elemento del modelo. */
public final class UpdateElementDescriptionUseCase {

    public DiagramProject update(DiagramProject project, DiagramElementId elementId, String updatedDescription) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        Objects.requireNonNull(elementId, "El ID del elemento no puede ser null");
        DiagramModel updatedModel = project.model().withUpdatedDescription(elementId, updatedDescription);
        return project.withModel(updatedModel);
    }
}
