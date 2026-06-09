package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdgeDirection;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphKind;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphNode;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class FreeGraphMarkdownParserExporterTest {

    @Test
    void shouldImportOfficialFreeGraphShape() throws Exception {
        DiagramProject project = new FreeGraphMarkdownParser().parse(freeGraphMarkdown(), "grafo.md");

        assertEquals(DiagramTypeId.FREE_GRAPH, project.metadata().diagramTypeId());
        FreeGraphDocument document = project.freeGraph().orElseThrow();
        assertEquals(FreeGraphKind.MIXED, document.graphKind());
        assertEquals(3, document.nodeCount());
        assertEquals(2, document.edgeCount());
        assertEquals("Sistema", document.nodeById("sistema").orElseThrow().title());
        assertEquals("Cliente", document.nodeById("cliente").orElseThrow().title());
        assertEquals(FreeGraphEdgeDirection.UNDIRECTED, document.edges().get(1).direction());
        assertTrue(project.layouts().activeLayout().nodeFor(VisualElementLayoutIds.freeGraphNode("sistema")).isPresent());
        assertEquals(2, project.layouts().activeLayout().connectorCount());
    }

    @Test
    void shouldExportImportableMarkdownAndRoundTrip() throws Exception {
        FreeGraphDocument document = new FreeGraphDocument(
                "Grafo de soporte",
                "borrador",
                LocalDate.now(),
                FreeGraphKind.DIRECTED,
                List.of(
                        new FreeGraphNode("ticket", "Ticket", "Solicitud registrada.", 0),
                        new FreeGraphNode("tecnico", "Técnico", "Persona que atiende.", 1)),
                List.of(new FreeGraphEdge("rel_ticket_tecnico", "ticket", "tecnico",
                        FreeGraphEdgeDirection.DIRECTED, "asignado a", "")),
                "Notas de prueba.");
        DiagramProject project = DiagramProject.blank("grafo_soporte", "Grafo de soporte", DiagramTypeId.FREE_GRAPH)
                .withFreeGraph(document);

        String markdown = new FreeGraphMarkdownExporter().export(project);
        DiagramProject reopened = new FreeGraphMarkdownParser().parse(markdown, "memoria.md");

        assertTrue(markdown.contains("diagram_type: \"free-graph\""));
        assertTrue(markdown.contains("graph_kind: \"directed\""));
        assertTrue(markdown.contains("# Nodos"));
        assertTrue(markdown.contains("# Relaciones"));
        assertEquals(2, reopened.freeGraph().orElseThrow().nodeCount());
        assertEquals(1, reopened.freeGraph().orElseThrow().edgeCount());
        assertEquals(FreeGraphKind.DIRECTED, reopened.freeGraph().orElseThrow().graphKind());
    }

    @Test
    void shouldRejectEdgesToUnknownNodes() {
        MarkdownModelParsingException exception = assertThrows(
                MarkdownModelParsingException.class,
                () -> new FreeGraphMarkdownParser().parse("""
                        ---
                        diagram_type: "free-graph"
                        name: "Grafo roto"
                        graph_kind: "mixed"
                        ---

                        # Nodos
                        ## A
                        id: a

                        # Relaciones
                        - a -> b: falta b
                        """, "roto.md"));

        assertTrue(exception.getMessage().contains("nodo inexistente"));
    }

    private static String freeGraphMarkdown() {
        return """
                ---
                dms_version: "1"
                diagram_type: "free-graph"
                name: "Grafo de ejemplo"
                graph_kind: "mixed"
                importable: true
                ---

                # Nodos

                ## Sistema
                id: sistema
                contenido: Núcleo del producto.

                ## Cliente
                id: cliente
                contenido: Persona que usa el sistema.

                ## Reporte
                id: reporte
                contenido: Documento producido por el sistema.

                # Relaciones

                - cliente -> sistema: usa
                - sistema -- reporte: produce
                """;
    }
}
