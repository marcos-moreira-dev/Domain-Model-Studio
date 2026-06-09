package com.marcosmoreira.domainmodelstudio.application.assets;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Objects;

/** Elimina del catálogo una referencia de asset por id. */
public final class RemoveProjectAssetUseCase {

    public DiagramProject remove(DiagramProject project, String assetId) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        return project.withAssetCatalog(project.assetCatalog().withoutReference(assetId));
    }
}
