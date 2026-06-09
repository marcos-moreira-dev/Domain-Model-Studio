package com.marcosmoreira.domainmodelstudio.application.theory;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Objects;

/** Recurso Markdown que contiene un tema académico editable del centro de ayuda. */
record TheoryTopicResource(
        TheoryTopicId topicId,
        DiagramTypeId diagramTypeId,
        String resourcePath
) {

    TheoryTopicResource {
        Objects.requireNonNull(topicId, "topicId");
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        resourcePath = Objects.requireNonNull(resourcePath, "resourcePath").strip();
        if (resourcePath.isBlank()) {
            throw new IllegalArgumentException("La ruta del recurso teórico no puede estar vacía.");
        }
    }
}
