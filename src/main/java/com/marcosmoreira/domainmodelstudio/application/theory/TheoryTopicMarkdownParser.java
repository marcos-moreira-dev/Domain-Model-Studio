package com.marcosmoreira.domainmodelstudio.application.theory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Parser mínimo para temas teóricos Markdown del centro de ayuda. */
final class TheoryTopicMarkdownParser {

    TheoryTopic parse(TheoryTopicResource resource, String markdown) {
        Objects.requireNonNull(resource, "resource");
        Objects.requireNonNull(markdown, "markdown");

        String title = null;
        String currentSectionTitle = null;
        List<String> currentLines = new ArrayList<>();
        List<TheoryFigureReference> currentFigures = new ArrayList<>();
        List<TheorySection> sections = new ArrayList<>();

        boolean insideCodeFence = false;
        for (String rawLine : markdown.split("\\R")) {
            String line = rawLine.strip();
            if (line.startsWith("```")) {
                insideCodeFence = !insideCodeFence;
                continue;
            }
            if (line.isBlank()) {
                continue;
            }
            if (!insideCodeFence && line.startsWith("# ")) {
                title = line.substring(2).strip();
                continue;
            }
            if (!insideCodeFence && line.startsWith("## ")) {
                flushSection(sections, currentSectionTitle, currentLines, currentFigures);
                currentSectionTitle = line.substring(3).strip();
                currentLines = new ArrayList<>();
                currentFigures = new ArrayList<>();
                continue;
            }
            if (currentSectionTitle != null) {
                Optional<TheoryFigureReference> figure = parseFigureReference(line);
                if (figure.isPresent()) {
                    currentFigures.add(figure.get());
                } else {
                    currentLines.add(normalizeContentLine(line, insideCodeFence));
                }
            }
        }
        flushSection(sections, currentSectionTitle, currentLines, currentFigures);

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("El tema académico " + resource.resourcePath() + " no tiene título H1.");
        }
        if (sections.isEmpty()) {
            throw new IllegalArgumentException("El tema académico " + resource.resourcePath() + " no tiene secciones H2.");
        }
        return new TheoryTopic(resource.topicId(), resource.diagramTypeId(), title, sections);
    }

    private static String normalizeContentLine(String line, boolean insideCodeFence) {
        if (insideCodeFence) {
            return "EXAMPLE::" + line;
        }
        if (line.startsWith("### ")) {
            return "SUBHEADING::" + line.substring(4).strip();
        }
        if (line.startsWith("#### ")) {
            return "SUBHEADING::" + line.substring(5).strip();
        }
        if (line.startsWith("- ")) {
            return "LIST::" + line.substring(2).strip();
        }
        if (line.matches("\\d+\\.\\s+.*")) {
            return "LIST::" + line;
        }
        return "PARAGRAPH::" + line;
    }

    private static Optional<TheoryFigureReference> parseFigureReference(String line) {
        if (!line.startsWith("![") || !line.contains("](figure:")) {
            return Optional.empty();
        }
        int captionStart = 2;
        int captionEnd = line.indexOf("](figure:");
        int idStart = captionEnd + "](figure:".length();
        int idEnd = line.indexOf(')', idStart);
        if (captionEnd <= captionStart || idEnd <= idStart) {
            throw new IllegalArgumentException("Referencia de figura inválida: " + line);
        }
        String caption = line.substring(captionStart, captionEnd).strip();
        String figureId = line.substring(idStart, idEnd).strip();
        return Optional.of(new TheoryFigureReference(figureId, caption));
    }

    private static void flushSection(
            List<TheorySection> sections,
            String title,
            List<String> lines,
            List<TheoryFigureReference> figures
    ) {
        if (title == null) {
            return;
        }
        if (title.isBlank()) {
            throw new IllegalArgumentException("Una sección teórica no puede tener título vacío.");
        }
        if (lines.isEmpty() && figures.isEmpty()) {
            throw new IllegalArgumentException("La sección teórica '" + title + "' no puede estar vacía.");
        }
        sections.add(new TheorySection(title, lines, figures));
    }
}
