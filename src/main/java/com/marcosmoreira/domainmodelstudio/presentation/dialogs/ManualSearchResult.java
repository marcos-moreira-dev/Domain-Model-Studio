package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import java.util.Objects;

/** Resultado navegable de búsqueda dentro de la ayuda académica. */
record ManualSearchResult(String categoryTitle, ManualSection section, int score) {

    ManualSearchResult {
        categoryTitle = Objects.requireNonNull(categoryTitle, "categoryTitle").strip();
        Objects.requireNonNull(section, "section");
        if (categoryTitle.isBlank()) {
            throw new IllegalArgumentException("La categoría del resultado no puede estar vacía.");
        }
    }

    String title() {
        return section.title();
    }

    String summary() {
        return section.summary();
    }

    String displayText() {
        return title() + " — " + categoryTitle;
    }
}
