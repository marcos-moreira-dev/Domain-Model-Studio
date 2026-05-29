package com.marcosmoreira.domainmodelstudio.application.theory;

import java.util.Objects;

/** Referencia a una figura didáctica asociada a una sección teórica. */
public record TheoryFigureReference(String figureId, String caption) {

    public TheoryFigureReference {
        figureId = Objects.requireNonNull(figureId, "figureId").strip();
        caption = Objects.requireNonNull(caption, "caption").strip();
        if (figureId.isBlank()) {
            throw new IllegalArgumentException("El identificador de figura no puede estar vacío.");
        }
        if (!figureId.matches("[a-z0-9-]+")) {
            throw new IllegalArgumentException("El identificador de figura solo admite minúsculas, números y guiones: " + figureId);
        }
        if (caption.isBlank()) {
            throw new IllegalArgumentException("La leyenda de figura no puede estar vacía.");
        }
    }
}
