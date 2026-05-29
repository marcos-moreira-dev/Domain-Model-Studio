package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MarkdownFrontMatterProbeTest {

    private final MarkdownFrontMatterProbe probe = new MarkdownFrontMatterProbe();

    @Test
    void readsOnlyRealFrontMatterAtDocumentStart() {
        MarkdownFrontMatterSnapshot snapshot = probe.read("""
                ---
                diagram_type: "uml-class"
                importable: true
                sample_kind: "project"
                ---

                # Clases
                """);

        assertTrue(snapshot.hasFrontMatter());
        assertEquals("uml-class", snapshot.value("diagram_type").orElseThrow());
        assertEquals(true, snapshot.booleanValue("importable").orElseThrow());
    }

    @Test
    void ignoresYamlBlocksThatAreNotAtTheStart() {
        MarkdownFrontMatterSnapshot snapshot = probe.read("""
                # Gramática

                ```yaml
                ---
                diagram_type: "uml-class"
                ---
                ```
                """);

        assertFalse(snapshot.hasFrontMatter());
        assertTrue(snapshot.value("diagram_type").isEmpty());
    }
}
