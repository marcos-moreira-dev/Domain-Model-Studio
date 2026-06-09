package com.marcosmoreira.domainmodelstudio.application.theory;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;
import java.util.Objects;

/** Tema teórico asociado a un tipo de diagrama. */
public record TheoryTopic(
        TheoryTopicId id,
        DiagramTypeId diagramTypeId,
        String title,
        List<TheorySection> sections
) {

    public TheoryTopic {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        Objects.requireNonNull(title, "title");
        sections = List.copyOf(Objects.requireNonNull(sections, "sections"));
    }
}
