package com.marcosmoreira.domainmodelstudio.application.theory;

import java.util.List;
import java.util.Objects;

/** Sección textual de un tema teórico, opcionalmente acompañada por figuras didácticas. */
public record TheorySection(String title, List<String> lines, List<TheoryFigureReference> figures) {

    public TheorySection(String title, List<String> lines) {
        this(title, lines, List.of());
    }

    public TheorySection {
        Objects.requireNonNull(title, "title");
        lines = List.copyOf(Objects.requireNonNull(lines, "lines"));
        figures = List.copyOf(Objects.requireNonNull(figures, "figures"));
    }

    public boolean hasFigures() {
        return !figures.isEmpty();
    }
}
