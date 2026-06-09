package com.marcosmoreira.domainmodelstudio.infrastructure.pdf;

/** Entrada enlazable para indices PDF con apariencia de tabla de contenido. */
record PdfIndexEntry(String label, String destinationId, int level) {

    PdfIndexEntry {
        label = label == null ? "" : label.strip();
        destinationId = destinationId == null ? "" : destinationId.strip();
        level = Math.max(0, Math.min(3, level));
    }

    static PdfIndexEntry linked(String label, String destinationId) {
        return linked(label, destinationId, 0);
    }

    static PdfIndexEntry linked(String label, String destinationId, int level) {
        return new PdfIndexEntry(label, destinationId, level);
    }

    boolean linked() {
        return !destinationId.isBlank();
    }
}
