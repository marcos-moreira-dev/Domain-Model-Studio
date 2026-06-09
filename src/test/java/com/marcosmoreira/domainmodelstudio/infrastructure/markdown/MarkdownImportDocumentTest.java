package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MarkdownImportDocumentTest {

    @Test
    void splitsFrontMatterAndBodyForImportableMarkdown() {
        MarkdownImportDocument document = MarkdownImportDocument.parse("""
                ---
                diagram_type: "uml-class"
                name: "Colegio"
                version: borrador
                ---

                # Clases
                ## Estudiante
                """);

        assertEquals("uml-class", document.frontMatter().value("diagram_type").orElseThrow());
        assertEquals("Colegio", document.frontMatter().value("name").orElseThrow());
        assertEquals("borrador", document.frontMatter().value("version").orElseThrow());
        assertTrue(document.body().contains("# Clases"));
        assertFalse(document.body().contains("diagram_type"));
    }

    @Test
    void keepsWholeDocumentAsBodyWhenThereIsNoFrontMatter() {
        String markdown = """
                # Entidades
                ## Estudiante
                """;

        MarkdownImportDocument document = MarkdownImportDocument.parse(markdown);

        assertTrue(document.frontMatter().value("diagram_type").isEmpty());
        assertEquals(markdown, document.body());
    }

    @Test
    void treatsUnclosedFrontMatterAsRegularBody() {
        String markdown = """
                ---
                diagram_type: "uml-class"
                # Clases
                """;

        MarkdownImportDocument document = MarkdownImportDocument.parse(markdown);

        assertTrue(document.frontMatter().value("diagram_type").isEmpty());
        assertEquals(markdown, document.body());
    }
}
