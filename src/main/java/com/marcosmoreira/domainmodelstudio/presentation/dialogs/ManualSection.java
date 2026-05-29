package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import java.util.List;
import java.util.Objects;

/** Sección visible del manual integrado. */
record ManualSection(String title, String summary, List<ManualBlock> blocks) {

    ManualSection {
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(summary, "summary");
        blocks = List.copyOf(Objects.requireNonNull(blocks, "blocks"));
    }
}
