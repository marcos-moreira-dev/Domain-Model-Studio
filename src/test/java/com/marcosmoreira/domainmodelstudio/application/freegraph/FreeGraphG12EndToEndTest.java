package com.marcosmoreira.domainmodelstudio.application.freegraph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.infrastructure.json.DmsProjectJsonReader;
import com.marcosmoreira.domainmodelstudio.infrastructure.json.DmsProjectJsonWriter;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.FreeGraphMarkdownExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.FreeGraphMarkdownParser;
import com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized.SpecializedVisualSvgDiagramExporter;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Cierre G12: comprueba el flujo real de Grafo libre de punta a punta, sin JavaFX.
 */
class FreeGraphG12EndToEndTest {

    @Test
    void officialMinimalExampleShouldImportPersistRoundTripAndExportSvg() throws Exception {
        assertEndToEnd(
                Path.of("src/main/resources/ai-resources/official-markdown/diagramas/free_graph_minimo.md"),
                4,
                4,
                "Equipo recibido");
    }

    @Test
    void officialUensExampleShouldImportPersistRoundTripAndExportSvg() throws Exception {
        assertEndToEnd(
                Path.of("src/main/resources/ai-resources/official-markdown/diagramas/free_graph_uens_gordito.md"),
                10,
                8,
                "Secretaría");
    }

    private static void assertEndToEnd(Path markdownFile, int expectedNodes, int minimumEdges, String expectedTitle)
            throws Exception {
        FreeGraphMarkdownParser parser = new FreeGraphMarkdownParser();
        DiagramProject imported = parser.parse(markdownFile);

        assertEquals(DiagramTypeId.FREE_GRAPH, imported.metadata().diagramTypeId());
        FreeGraphDocument importedDocument = imported.freeGraph().orElseThrow();
        assertEquals(expectedNodes, importedDocument.nodeCount());
        assertTrue(importedDocument.edgeCount() >= minimumEdges);
        assertTrue(importedDocument.nodes().stream().anyMatch(node -> node.title().equals(expectedTitle)));
        assertTrue(imported.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.freeGraphNode(importedDocument.nodes().get(0).id()))
                .isPresent());
        assertEquals(importedDocument.edgeCount(), imported.layouts().activeLayout().connectorCount());

        String json = new DmsProjectJsonWriter().write(imported);
        DiagramProject reopened = new DmsProjectJsonReader().read(json);
        FreeGraphDocument reopenedDocument = reopened.freeGraph().orElseThrow();
        assertEquals(expectedNodes, reopenedDocument.nodeCount());
        assertEquals(importedDocument.edgeCount(), reopenedDocument.edgeCount());

        String exportedMarkdown = new FreeGraphMarkdownExporter().export(reopened);
        assertTrue(exportedMarkdown.contains("diagram_type: \"free-graph\""));
        assertTrue(exportedMarkdown.contains("# Nodos"));
        assertTrue(exportedMarkdown.contains("# Relaciones"));

        DiagramProject reparsed = parser.parse(exportedMarkdown, markdownFile.getFileName() + ".roundtrip.md");
        assertEquals(expectedNodes, reparsed.freeGraph().orElseThrow().nodeCount());
        assertEquals(importedDocument.edgeCount(), reparsed.freeGraph().orElseThrow().edgeCount());

        String svg = new SpecializedVisualSvgDiagramExporter().export(reparsed);
        assertTrue(svg.contains("type=free-graph"));
        assertTrue(svg.contains("node-free-graph"));
        assertTrue(svg.contains("connector-free-graph"));
        assertTrue(svg.contains("<g id=\"nodes\">"));
        assertTrue(svg.contains("<g id=\"connectors\">"));
        assertFalse(svg.isBlank());
    }
}
