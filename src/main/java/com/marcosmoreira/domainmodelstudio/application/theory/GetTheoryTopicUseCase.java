package com.marcosmoreira.domainmodelstudio.application.theory;

import java.util.Objects;
import java.util.Optional;

/** Obtiene teoría para manual, ayuda contextual o workspace de preparación. */
public final class GetTheoryTopicUseCase {

    private final TheoryCatalog catalog;

    public GetTheoryTopicUseCase(TheoryCatalog catalog) {
        this.catalog = Objects.requireNonNull(catalog, "catalog");
    }

    public Optional<TheoryTopic> execute(TheoryTopicId id) {
        return catalog.findById(Objects.requireNonNull(id, "id"));
    }
}
