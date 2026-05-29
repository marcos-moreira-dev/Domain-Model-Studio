package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

/** Regresión para que Grafo libre pueda guardarse y reabrirse como payload .dms real. */
class DmsProjectJsonFreeGraphTest {

    @Test
    void shouldPersistFreeGraphInsideDmsProject() {
        FreeGraphDocument document = new FreeGraphDocument(
                "Mapa mental",
                "borrador",
                LocalDate.of(2026, 1, 1),
                FreeGraphKind.MIXED,
                List.of(
                        new FreeGraphNode("problema", "Problema", "Situación inicial", 0),
                        new FreeGraphNode("causa", "Causa", "Factor que explica el problema", 1)
                ),
                List.of(new FreeGraphEdge("problema-causa", "problema", "causa",
                        FreeGraphEdgeDirection.DIRECTED, "se explica por", "Relación causal")),
                "Notas generales");
        DiagramProject project = DiagramProject.blank("grafo_libre", "Grafo libre", DiagramTypeId.FREE_GRAPH)
                .withFreeGraph(document);

        String json = new DmsProjectJsonWriter().write(project);
        DiagramProject reopened = new DmsProjectJsonReader().read(json);

        assertTrue(json.contains("\"freeGraph\""));
        assertEquals(DiagramTypeId.FREE_GRAPH, reopened.metadata().diagramTypeId());
        assertTrue(reopened.freeGraph().isPresent());
        assertEquals(FreeGraphKind.MIXED, reopened.freeGraph().get().graphKind());
        assertEquals(2, reopened.freeGraph().get().nodeCount());
        assertEquals(1, reopened.freeGraph().get().edgeCount());
        assertEquals("Problema", reopened.freeGraph().get().nodeById("problema").orElseThrow().title());
        assertEquals("se explica por", reopened.freeGraph().get().edgeById("problema-causa").orElseThrow().label());
    }
}
