package com.marcosmoreira.domainmodelstudio.application.editing;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Objects;

/** Caso de uso para cambiar el nombre visible/semántico de un elemento del modelo. */
public final class RenameElementUseCase {

    public DiagramProject rename(DiagramProject project, DiagramElementId elementId, String updatedName) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        Objects.requireNonNull(elementId, "El ID del elemento no puede ser null");
        DiagramModel updatedModel = project.model().withRenamedElement(elementId, updatedName);
        return project.withModel(updatedModel);
    }
}
