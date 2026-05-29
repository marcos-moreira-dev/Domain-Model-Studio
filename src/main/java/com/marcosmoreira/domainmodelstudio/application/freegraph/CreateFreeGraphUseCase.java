package com.marcosmoreira.domainmodelstudio.application.freegraph;

import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphKind;

/** Crea documentos de Grafo libre para edición manual o importación futura desde Markdown. */
public final class CreateFreeGraphUseCase {

    public FreeGraphDocument createBlank(String projectName) {
        return FreeGraphDocument.blank(projectName);
    }

    public FreeGraphDocument createBlank(String projectName, FreeGraphKind graphKind) {
        return FreeGraphDocument.blank(projectName).withGraphKind(graphKind);
    }
}
