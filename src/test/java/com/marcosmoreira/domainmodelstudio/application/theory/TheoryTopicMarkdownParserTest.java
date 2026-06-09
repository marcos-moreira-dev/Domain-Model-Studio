package com.marcosmoreira.domainmodelstudio.application.theory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import org.junit.jupiter.api.Test;

class TheoryTopicMarkdownParserTest {

    @Test
    void shouldParseMarkdownTopicIntoTheorySections() {
        TheoryTopicResource resource = new TheoryTopicResource(
                TheoryTopicId.CONCEPTUAL_MODEL,
                DiagramTypeId.CONCEPTUAL_MODEL,
                "help/topics/test.md");
        String markdown = """
                # Modelo conceptual

                ## Qué es
                - Línea uno.
                - Línea dos.

                ## Errores comunes
                - Error de prueba.
                """;

        TheoryTopic topic = new TheoryTopicMarkdownParser().parse(resource, markdown);

        assertEquals(TheoryTopicId.CONCEPTUAL_MODEL, topic.id());
        assertEquals(DiagramTypeId.CONCEPTUAL_MODEL, topic.diagramTypeId());
        assertEquals("Modelo conceptual", topic.title());
        assertEquals(2, topic.sections().size());
        assertEquals("Qué es", topic.sections().get(0).title());
        assertEquals("LIST::Línea uno.", topic.sections().get(0).lines().get(0));
    }

    @Test
    void shouldRejectTopicWithoutSections() {
        TheoryTopicResource resource = new TheoryTopicResource(
                TheoryTopicId.CONCEPTUAL_MODEL,
                DiagramTypeId.CONCEPTUAL_MODEL,
                "help/topics/test.md");

        assertThrows(IllegalArgumentException.class,
                () -> new TheoryTopicMarkdownParser().parse(resource, "# Modelo conceptual\n"));
    }
}
