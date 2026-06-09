package com.marcosmoreira.domainmodelstudio.infrastructure.pdf;

import java.util.List;

/** Fila de tabla PDF con destino interno opcional para indices navegables. */
record PdfTableRow(List<String> cells, String destinationId) {

    PdfTableRow {
        cells = List.copyOf(cells == null ? List.of() : cells);
        destinationId = destinationId == null ? "" : destinationId.strip();
    }

    static PdfTableRow linked(List<String> cells, String destinationId) {
        return new PdfTableRow(cells, destinationId);
    }

    static PdfTableRow unlinked(List<String> cells) {
        return new PdfTableRow(cells, "");
    }

    boolean linked() {
        return !destinationId.isBlank();
    }
}
