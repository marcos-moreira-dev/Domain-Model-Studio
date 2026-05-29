package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeTag;
import org.junit.jupiter.api.Test;

class MarkdownDiagramParserTest {

    private final MarkdownDiagramParser parser = new MarkdownDiagramParser();

    @Test
    void parsesBasicChenMarkdownIntoDiagramProject() throws Exception {
        DiagramProject project = parser.parse(validMarkdown(), "test.md");

        assertEquals("supermercado_v1", project.metadata().id());
        assertEquals("Modelo conceptual - Supermercado", project.metadata().title());
        assertEquals(2, project.model().entityCount());
        assertEquals(1, project.model().relationshipCount());
        assertTrue(project.model().entityById(com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId.of("producto")).isPresent());
        assertTrue(project.model().entities().getFirst().attributes().getFirst().hasTag(AttributeTag.PRIMARY_KEY));
    }

    @Test
    void rejectsUnknownAttributeTag() {
        String markdown = validMarkdown().replace("codigo_barras [unique]", "codigo_barras [raro]");

        assertThrows(MarkdownModelParsingException.class, () -> parser.parse(markdown, "bad.md"));
    }

    @Test
    void rejectsRelationshipWithUnknownEntity() {
        String markdown = validMarkdown().replace("to: Categoria", "to: Fantasma");

        assertThrows(MarkdownModelParsingException.class, () -> parser.parse(markdown, "bad.md"));
    }

    private static String validMarkdown() {
        return """
                ---
                id: supermercado_v1
                title: Modelo conceptual - Supermercado
                notation: chen
                version: 1.0.0
                ---

                # Entidades

                ## Producto
                id: producto
                module: inventario
                description: Producto vendido por el negocio.

                - pk id
                - nombre
                - codigo_barras [unique]

                ## Categoria
                id: categoria
                module: inventario

                - pk id
                - nombre

                # Relaciones

                ## Pertenece
                id: pertenece
                from: Producto
                to: Categoria
                from_cardinality: 0..M
                to_cardinality: 1
                description: Cada producto pertenece a una categoría.
                """;
    }
}
