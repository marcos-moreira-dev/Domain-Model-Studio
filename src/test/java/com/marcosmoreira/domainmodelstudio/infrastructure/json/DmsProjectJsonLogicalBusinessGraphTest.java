package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNode;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphRelationKind;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Regresión para que Grafo lógico pueda guardarse y reabrirse como payload .dms real. */
class DmsProjectJsonLogicalBusinessGraphTest {

    @Test
    void shouldPersistLogicalBusinessGraphInsideDmsProject() {
        LogicalBusinessGraphDocument document = new LogicalBusinessGraphDocument(
                "UENS",
                "v0.1",
                LocalDate.of(2026, 1, 1),
                List.of(
                        LogicalBusinessGraphNode.of("MF-001", LogicalBusinessGraphNodeKind.MACRO_FLOW,
                                "Gestión académica"),
                        LogicalBusinessGraphNode.of("FL-001", LogicalBusinessGraphNodeKind.FLOW,
                                "Calificaciones")
                ),
                List.of(LogicalBusinessGraphEdge.of("MF-001-FL-001", "MF-001",
                        LogicalBusinessGraphRelationKind.CONTAINS, "FL-001")),
                "Notas del grafo lógico");
        DiagramProject project = DiagramProject.blank("grafo_logico", "Grafo lógico", DiagramTypeId.LOGICAL_BUSINESS_GRAPH)
                .withLogicalBusinessGraphDocument(document);

        String json = new DmsProjectJsonWriter().write(project);
        DiagramProject reopened = new DmsProjectJsonReader().read(json);

        assertTrue(json.contains("\"logicalBusinessGraphDocument\""));
        assertEquals(DiagramTypeId.LOGICAL_BUSINESS_GRAPH, reopened.metadata().diagramTypeId());
        assertTrue(reopened.logicalBusinessGraphDocument().isPresent());
        assertEquals(2, reopened.logicalBusinessGraphDocument().get().nodes().size());
        assertEquals(1, reopened.logicalBusinessGraphDocument().get().edges().size());
        assertEquals("Gestión académica", reopened.logicalBusinessGraphDocument().get()
                .nodeByCode("MF-001").orElseThrow().title());
    }
}
