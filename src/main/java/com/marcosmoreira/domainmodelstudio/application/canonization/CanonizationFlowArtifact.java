package com.marcosmoreira.domainmodelstudio.application.canonization;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.nio.file.Path;
import java.util.Objects;

/** Artefacto importado desde una carpeta raíz y clasificado para lectura enterprise. */
public record CanonizationFlowArtifact(
        Path sourceFile,
        String displayName,
        DiagramTypeId diagramTypeId,
        CanonizationArtifactRole role
) {

    public CanonizationFlowArtifact {
        Objects.requireNonNull(sourceFile, "sourceFile");
        displayName = displayName == null || displayName.isBlank()
                ? sourceFile.getFileName().toString()
                : displayName.strip();
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        Objects.requireNonNull(role, "role");
    }

    public boolean sourceMother() {
        return role == CanonizationArtifactRole.SOURCE_MOTHER;
    }
}
