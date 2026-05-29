package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import org.junit.jupiter.api.Test;

class ConceptualModelMarkdownExporterTest {

    private final MarkdownDiagramParser parser = new MarkdownDiagramParser();
    private final ConceptualModelMarkdownExporter exporter = new ConceptualModelMarkdownExporter();

    @Test
    void exportsMarkdownCompatibleWithCurrentParser() throws Exception {
        DiagramProject original = parser.parse(markdown(), "restaurante.md");

        String generated = exporter.export(original);
        DiagramProject roundTrip = parser.parse(generated, "restaurante_actualizado.md");

        assertTrue(generated.contains("# Entidades"));
        assertTrue(generated.contains("# Relaciones"));
        assertEquals(original.model().entityCount(), roundTrip.model().entityCount());
        assertEquals(original.model().relationshipCount(), roundTrip.model().relationshipCount());
        assertTrue(roundTrip.model().entityById(
                com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId.of("pedido")).isPresent());
    }

    private static String markdown() {
        return """
                ---
                id: restaurante_v1
                title: Modelo conceptual - Restaurante
                notation: chen
                version: 1.0.0
                ---

                # Entidades

                ## Pedido
                id: pedido
                module: ventas
                description: Pedido realizado por un cliente.

                - pk id
                - fecha
                - total

                ## Cliente
                id: cliente
                module: ventas

                - pk id
                - nombre

                # Relaciones

                ## Realiza
                id: realiza
                from: Cliente
                to: Pedido
                from_cardinality: 1
                to_cardinality: 0..M
                description: Un cliente realiza pedidos.
                """;
    }
}
