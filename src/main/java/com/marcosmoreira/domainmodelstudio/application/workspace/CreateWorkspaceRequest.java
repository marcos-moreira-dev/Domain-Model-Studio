package com.marcosmoreira.domainmodelstudio.application.workspace;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Objects;

/** Solicitud para abrir o crear un proyecto de un tipo de diagrama. */
public record CreateWorkspaceRequest(DiagramTypeId diagramTypeId, String projectName) {

    public CreateWorkspaceRequest {
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        projectName = projectName == null || projectName.isBlank() ? "Proyecto sin título" : projectName.strip();
    }
}
