package com.marcosmoreira.domainmodelstudio.application.freegraph;

import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphKind;
import java.time.LocalDate;
import java.util.Objects;

/** Actualiza metadatos generales del documento de Grafo libre. */
public final class UpdateFreeGraphDetailsUseCase {

    public FreeGraphDocument updateDetails(
            FreeGraphDocument document,
            String projectName,
            String version,
            LocalDate documentDate,
            FreeGraphKind graphKind,
            String notes
    ) {
        Objects.requireNonNull(document, "document");
        return document.withDocumentDetails(projectName, version, documentDate, graphKind, notes);
    }

    public FreeGraphDocument updateKind(FreeGraphDocument document, FreeGraphKind graphKind) {
        Objects.requireNonNull(document, "document");
        return document.withGraphKind(graphKind);
    }

    public FreeGraphDocument updateNotes(FreeGraphDocument document, String notes) {
        Objects.requireNonNull(document, "document");
        return document.withNotes(notes);
    }
}
