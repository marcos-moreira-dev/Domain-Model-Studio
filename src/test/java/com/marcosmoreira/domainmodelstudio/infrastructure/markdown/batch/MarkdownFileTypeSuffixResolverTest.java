package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import org.junit.jupiter.api.Test;

class MarkdownFileTypeSuffixResolverTest {

    private final MarkdownFileTypeSuffixResolver resolver = new MarkdownFileTypeSuffixResolver();

    @Test
    void resolvesKnownSpanishSuffixes() {
        assertEquals(DiagramTypeId.LOGICAL_BUSINESS_INTAKE, resolver.resolve("01_levantamiento-logico.md").orElseThrow());
        assertEquals(DiagramTypeId.DATA_DICTIONARY, resolver.resolve("03_diccionario-datos.markdown").orElseThrow());
        assertEquals(DiagramTypeId.UML_CLASS, resolver.resolve("04_uml-clases.md").orElseThrow());
    }

    @Test
    void returnsEmptyForUnknownNames() {
        assertTrue(resolver.resolve("notas-cliente.md").isEmpty());
    }
}
