package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.nio.file.Path;
import java.util.List;

/** Solicitud para importar un directorio de código fuente hacia UML Clases. */
public record SourceCodeImportRequest(
        Path projectRoot,
        List<SourceLanguageVersion> preferredLanguageVersions,
        boolean includeTests
) {
    public SourceCodeImportRequest {
        if (projectRoot == null) {
            throw new IllegalArgumentException("La carpeta raíz del proyecto fuente no puede ser nula.");
        }
        preferredLanguageVersions = List.copyOf(preferredLanguageVersions == null
                ? List.of()
                : preferredLanguageVersions);
    }

    public static SourceCodeImportRequest flexible(Path projectRoot) {
        return new SourceCodeImportRequest(projectRoot, List.of(), false);
    }
}
