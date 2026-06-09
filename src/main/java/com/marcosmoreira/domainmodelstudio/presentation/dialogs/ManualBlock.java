package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import com.marcosmoreira.domainmodelstudio.application.theory.TheoryFigureReference;
import java.util.List;
import java.util.Objects;

/** Bloque de contenido dentro de una sección del manual integrado. */
record ManualBlock(String title, List<String> lines, List<String> diagramLines, List<TheoryFigureReference> figures) {

    ManualBlock(String title, List<String> lines) {
        this(title, lines, List.of(), List.of());
    }

    ManualBlock(String title, List<String> lines, List<String> diagramLines) {
        this(title, lines, diagramLines, List.of());
    }

    ManualBlock {
        Objects.requireNonNull(title, "title");
        lines = List.copyOf(Objects.requireNonNull(lines, "lines"));
        diagramLines = List.copyOf(Objects.requireNonNull(diagramLines, "diagramLines"));
        figures = List.copyOf(Objects.requireNonNull(figures, "figures"));
    }

    boolean hasDiagram() {
        return !diagramLines.isEmpty();
    }

    boolean hasFigures() {
        return !figures.isEmpty();
    }
}
