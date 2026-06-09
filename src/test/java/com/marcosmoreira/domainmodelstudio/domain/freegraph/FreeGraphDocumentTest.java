package com.marcosmoreira.domainmodelstudio.domain.freegraph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class FreeGraphDocumentTest {

    @Test
    void blankDocumentKeepsProjectNameAndStartsEmpty() {
        FreeGraphDocument document = FreeGraphDocument.blank("Mapa mental del semestre");

        assertEquals("Mapa mental del semestre", document.projectName());
        assertEquals("borrador", document.version());
        assertEquals(FreeGraphKind.MIXED, document.graphKind());
        assertTrue(document.isEmpty());
        assertEquals(0, document.nodeCount());
        assertEquals(0, document.edgeCount());
    }

    @Test
    void rejectsNodeWithoutId() {
        assertThrows(IllegalArgumentException.class, () -> new FreeGraphNode(" ", "Nodo", "", 0));
    }

    @Test
    void rejectsNodeWithoutTitle() {
        assertThrows(IllegalArgumentException.class, () -> new FreeGraphNode("n1", " ", "", 0));
    }

    @Test
    void rejectsNegativeOrderIndex() {
        assertThrows(IllegalArgumentException.class, () -> new FreeGraphNode("n1", "Nodo", "", -1));
    }

    @Test
    void rejectsDuplicateNodeIds() {
        FreeGraphNode first = new FreeGraphNode("n1", "Nodo A", "", 0);
        FreeGraphNode second = new FreeGraphNode("n1", "Nodo B", "", 1);

        assertThrows(IllegalArgumentException.class, () -> new FreeGraphDocument(
                "Grafo", "borrador", LocalDate.of(2026, 5, 18), FreeGraphKind.MIXED,
                List.of(first, second), List.of(), ""));
    }

    @Test
    void rejectsEdgeWithoutSourceOrTarget() {
        assertThrows(IllegalArgumentException.class,
                () -> new FreeGraphEdge("e1", "", "n2", FreeGraphEdgeDirection.DIRECTED, "", ""));
        assertThrows(IllegalArgumentException.class,
                () -> new FreeGraphEdge("e1", "n1", "", FreeGraphEdgeDirection.DIRECTED, "", ""));
    }

    @Test
    void rejectsDuplicateEdgeIds() {
        FreeGraphNode a = node("a", "A", 0);
        FreeGraphNode b = node("b", "B", 1);
        FreeGraphEdge first = FreeGraphEdge.directed("e1", "a", "b", "usa");
        FreeGraphEdge second = FreeGraphEdge.undirected("e1", "a", "b", "conecta");

        assertThrows(IllegalArgumentException.class, () -> FreeGraphDocument.mixed("Grafo",
                List.of(a, b), List.of(first, second)));
    }

    @Test
    void rejectsEdgePointingToMissingNode() {
        FreeGraphNode a = node("a", "A", 0);
        FreeGraphEdge missingTarget = FreeGraphEdge.directed("e1", "a", "b", "usa");

        assertThrows(IllegalArgumentException.class, () -> FreeGraphDocument.directed("Grafo",
                List.of(a), List.of(missingTarget)));
    }


    @Test
    void acceptsSelfLoopEdgesAsMathematicalGraphRelations() {
        FreeGraphNode node = node("a", "A", 0);
        FreeGraphEdge loop = FreeGraphEdge.directed("aa", "a", "a", "autorrelación");

        FreeGraphDocument document = FreeGraphDocument.directed("Grafo", List.of(node), List.of(loop));

        assertEquals(1, document.edgeCount());
        assertTrue(document.edges().getFirst().loop());
        assertEquals(1, document.incidentEdgesOf("a").size());
    }

    @Test
    void directedDocumentNormalizesEdgesToDirected() {
        FreeGraphNode a = node("a", "A", 0);
        FreeGraphNode b = node("b", "B", 1);
        FreeGraphEdge edge = FreeGraphEdge.undirected("e1", "a", "b", "relación");

        FreeGraphDocument document = FreeGraphDocument.directed("Grafo", List.of(a, b), List.of(edge));

        assertEquals(FreeGraphKind.DIRECTED, document.graphKind());
        assertEquals(FreeGraphEdgeDirection.DIRECTED, document.edges().getFirst().direction());
    }

    @Test
    void undirectedDocumentNormalizesEdgesToUndirected() {
        FreeGraphNode a = node("a", "A", 0);
        FreeGraphNode b = node("b", "B", 1);
        FreeGraphEdge edge = FreeGraphEdge.directed("e1", "a", "b", "relación");

        FreeGraphDocument document = FreeGraphDocument.undirected("Grafo", List.of(a, b), List.of(edge));

        assertEquals(FreeGraphKind.UNDIRECTED, document.graphKind());
        assertEquals(FreeGraphEdgeDirection.UNDIRECTED, document.edges().getFirst().direction());
    }

    @Test
    void mixedDocumentPreservesEdgeDirections() {
        FreeGraphNode a = node("a", "A", 0);
        FreeGraphNode b = node("b", "B", 1);
        FreeGraphNode c = node("c", "C", 2);
        FreeGraphEdge directed = FreeGraphEdge.directed("e1", "a", "b", "usa");
        FreeGraphEdge undirected = FreeGraphEdge.undirected("e2", "b", "c", "asocia");

        FreeGraphDocument document = FreeGraphDocument.mixed("Grafo", List.of(a, b, c),
                List.of(directed, undirected));

        assertEquals(FreeGraphEdgeDirection.DIRECTED, document.edges().get(0).direction());
        assertEquals(FreeGraphEdgeDirection.UNDIRECTED, document.edges().get(1).direction());
    }

    @Test
    void removesNodeAndIncidentEdgesTogether() {
        FreeGraphNode a = node("a", "A", 0);
        FreeGraphNode b = node("b", "B", 1);
        FreeGraphNode c = node("c", "C", 2);
        FreeGraphEdge ab = FreeGraphEdge.directed("ab", "a", "b", "usa");
        FreeGraphEdge bc = FreeGraphEdge.directed("bc", "b", "c", "llama");
        FreeGraphDocument document = FreeGraphDocument.directed("Grafo", List.of(a, b, c), List.of(ab, bc));

        FreeGraphDocument updated = document.withoutNode("b");

        assertEquals(2, updated.nodeCount());
        assertEquals(0, updated.edgeCount());
        assertTrue(updated.nodeById("b").isEmpty());
    }

    @Test
    void canAddAndUpdateNodesAndEdgesImmutably() {
        FreeGraphDocument document = FreeGraphDocument.blank("Grafo")
                .withNode(node("a", "A", 0))
                .withNode(node("b", "B", 1))
                .withEdge(FreeGraphEdge.directed("ab", "a", "b", "usa"));

        FreeGraphDocument updated = document
                .withUpdatedNode(new FreeGraphNode("a", "Nodo A", "Contenido", 0))
                .withUpdatedEdge(new FreeGraphEdge("ab", "a", "b", FreeGraphEdgeDirection.UNDIRECTED,
                        "conecta", "nota"));

        assertEquals("A", document.nodeById("a").orElseThrow().title());
        assertEquals("Nodo A", updated.nodeById("a").orElseThrow().title());
        assertEquals(FreeGraphEdgeDirection.UNDIRECTED, updated.edgeById("ab").orElseThrow().direction());
    }

    private FreeGraphNode node(String id, String title, int orderIndex) {
        return new FreeGraphNode(id, title, "", orderIndex);
    }
}
