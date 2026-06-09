package com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph;

import static com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeKind.*;
import static com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphRelationKind.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class LogicalBusinessGraphDocumentTest {

    @Test
    void documentShouldIndexNodesEdgesAndAcceptSemanticHappyPath() {
        LogicalBusinessGraphDocument document = sampleDocument();

        assertEquals(5, document.nodes().size());
        assertEquals(4, document.edges().size());
        assertTrue(document.nodeByCode("fl-001").isPresent());
        assertEquals(1, document.outgoingEdgesOf("MF-001").size());
        assertEquals(1, document.incomingEdgesOf("INV-001").size());
        assertTrue(document.semanticIssues().isEmpty());
    }

    @Test
    void documentShouldRejectDuplicateNodesAndUnknownReferences() {
        LogicalBusinessGraphNode mf = LogicalBusinessGraphNode.of("MF-001", MACRO_FLOW, "Gestión académica");
        LogicalBusinessGraphNode duplicated = LogicalBusinessGraphNode.of("MF-001", MACRO_FLOW, "Duplicado");

        assertThrows(IllegalArgumentException.class, () -> new LogicalBusinessGraphDocument(
                "Demo", "v0.1", LocalDate.now(), List.of(mf, duplicated), List.of(), ""));

        assertThrows(IllegalArgumentException.class, () -> new LogicalBusinessGraphDocument(
                "Demo", "v0.1", LocalDate.now(), List.of(mf),
                List.of(LogicalBusinessGraphEdge.of("R-001", "MF-001", CONTAINS, "FL-999")), ""));
    }

    @Test
    void semanticIssuesShouldWarnUnexpectedRelationKinds() {
        LogicalBusinessGraphDocument document = new LogicalBusinessGraphDocument(
                "Demo", "v0.1", LocalDate.now(),
                List.of(
                        LogicalBusinessGraphNode.of("MF-001", MACRO_FLOW, "Gestión académica"),
                        LogicalBusinessGraphNode.of("CU-001", USE_CASE, "Registrar estudiante")),
                List.of(LogicalBusinessGraphEdge.of("R-001", "CU-001", CONTAINS, "MF-001")), "");

        assertTrue(document.semanticIssues().stream()
                .anyMatch(issue -> issue.severity() == LogicalBusinessGraphIssueSeverity.BLOCKING
                        && issue.message().contains("no es semánticamente esperada")));
    }

    @Test
    void documentShouldSupportImmutableEditingOperations() {
        LogicalBusinessGraphDocument document = LogicalBusinessGraphDocument.blank("Demo")
                .withNode(LogicalBusinessGraphNode.of("MF-001", MACRO_FLOW, "Gestión académica"))
                .withNode(LogicalBusinessGraphNode.of("FL-001", FLOW, "Registrar matrícula"))
                .withEdge(LogicalBusinessGraphEdge.of("R-001", "MF-001", CONTAINS, "FL-001"));

        LogicalBusinessGraphDocument updated = document.withUpdatedNode(new LogicalBusinessGraphNode(
                "FL-001", FLOW, "Matrícula nueva", "Microflujo revisado",
                LogicalBusinessGraphNodeStatus.IN_REVIEW, List.of("FL-001")));

        assertEquals("Registrar matrícula", document.nodeByCode("FL-001").orElseThrow().title());
        assertEquals("Matrícula nueva", updated.nodeByCode("FL-001").orElseThrow().title());
        assertTrue(updated.withoutNode("FL-001").edges().isEmpty());
    }

    private static LogicalBusinessGraphDocument sampleDocument() {
        return new LogicalBusinessGraphDocument("UENS", "v0.1", LocalDate.now(),
                List.of(
                        LogicalBusinessGraphNode.of("MF-001", MACRO_FLOW, "Gestión académica"),
                        LogicalBusinessGraphNode.of("FL-001", FLOW, "Registro de calificaciones"),
                        LogicalBusinessGraphNode.of("CU-001", USE_CASE, "Registrar calificación"),
                        LogicalBusinessGraphNode.of("ACC-001", ACTION, "Guardar calificación"),
                        LogicalBusinessGraphNode.of("INV-001", INVARIANT, "Nota dentro de escala válida")),
                List.of(
                        LogicalBusinessGraphEdge.of("R-001", "MF-001", CONTAINS, "FL-001"),
                        LogicalBusinessGraphEdge.of("R-002", "FL-001", USES, "CU-001"),
                        LogicalBusinessGraphEdge.of("R-003", "CU-001", EXECUTES, "ACC-001"),
                        LogicalBusinessGraphEdge.of("R-004", "ACC-001", PROTECTS, "INV-001")), "");
    }
}
