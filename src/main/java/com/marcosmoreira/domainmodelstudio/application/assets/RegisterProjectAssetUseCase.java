package com.marcosmoreira.domainmodelstudio.application.assets;

import com.marcosmoreira.domainmodelstudio.domain.assets.ProjectAssetKind;
import com.marcosmoreira.domainmodelstudio.domain.assets.ProjectAssetReference;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Objects;

/** Registra un recurso relativo en el catálogo de assets del proyecto. */
public final class RegisterProjectAssetUseCase {

    public DiagramProject register(
            DiagramProject project,
            String id,
            ProjectAssetKind kind,
            String displayName,
            String relativePath,
            String mimeType,
            String purpose,
            String checksum,
            String notes
    ) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        ProjectAssetReference reference = new ProjectAssetReference(
                id,
                kind,
                displayName,
                relativePath,
                mimeType,
                purpose,
                checksum,
                notes
        );
        return project.withAssetCatalog(project.assetCatalog().withReference(reference));
    }
}
