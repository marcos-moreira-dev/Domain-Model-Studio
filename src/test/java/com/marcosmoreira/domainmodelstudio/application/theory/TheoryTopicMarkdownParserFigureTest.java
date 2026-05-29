package com.marcosmoreira.domainmodelstudio.application.theory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import org.junit.jupiter.api.Test;

class TheoryTopicMarkdownParserFigureTest {

    @Test
    void shouldParseFigureReferencesWithoutMixingThemWithTextLines() {
        TheoryTopicResource resource = new TheoryTopicResource(
                TheoryTopicId.UML_USE_CASE,
                DiagramTypeId.UML_USE_CASE,
                "help/topics/uml-use-case.md");
        String markdown = """
                # UML Casos de uso

                ## Elementos principales
                ![Actor y caso de uso](figure:uml-use-case-actor)
                - Actor externo.
                - Caso de uso observable.
                """;

        TheoryTopic topic = new TheoryTopicMarkdownParser().parse(resource, markdown);

        TheorySection section = topic.sections().get(0);
        assertEquals(2, section.lines().size());
        assertTrue(section.hasFigures());
        assertEquals("uml-use-case-actor", section.figures().get(0).figureId());
        assertEquals("Actor y caso de uso", section.figures().get(0).caption());
    }
}
