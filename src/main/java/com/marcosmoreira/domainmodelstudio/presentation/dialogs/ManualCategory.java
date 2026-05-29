package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import java.util.List;
import java.util.Objects;

/** Categoría visible del manual integrado. */
record ManualCategory(String title, String summary, List<ManualSection> sections) {

    ManualCategory {
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(summary, "summary");
        sections = List.copyOf(Objects.requireNonNull(sections, "sections"));
    }
}
