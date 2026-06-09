package com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LogicalBusinessGraphNodeKindTest {

    @Test
    void legendShouldExposeEveryAbbreviationWithReadableName() {
        String legend = java.util.Arrays.stream(LogicalBusinessGraphNodeKind.values())
                .map(LogicalBusinessGraphNodeKind::legendEntry)
                .reduce("", (left, right) -> left + "\n" + right);

        assertTrue(legend.contains("MF — Macroflujo"));
        assertTrue(legend.contains("FL — Flujo o microflujo"));
        assertTrue(legend.contains("CU — Caso de uso"));
        assertTrue(legend.contains("ACC — Acción transformadora"));
        assertTrue(legend.contains("RN — Regla de negocio"));
        assertTrue(legend.contains("PRE — Precondición"));
        assertTrue(legend.contains("INV — Invariante"));
        assertTrue(legend.contains("POST — Postcondición"));
        assertTrue(legend.contains("ENT — Entidad candidata"));
        assertTrue(legend.contains("EST — Estado"));
        assertTrue(legend.contains("REP — Reporte"));
        assertTrue(legend.contains("RISK — Riesgo"));
        assertTrue(legend.contains("PEND — Pregunta pendiente"));
    }

    @Test
    void nodeCodeShouldMatchItsSemanticKind() {
        LogicalBusinessGraphNode node = LogicalBusinessGraphNode.of(
                "CU-001", LogicalBusinessGraphNodeKind.USE_CASE, "Registrar estudiante");

        assertEquals("CU-001", node.code());
        assertEquals("CU CU-001 — Registrar estudiante", node.compactLabel());

        assertThrows(IllegalArgumentException.class, () -> LogicalBusinessGraphNode.of(
                "CU-001", LogicalBusinessGraphNodeKind.MACRO_FLOW, "Código inválido"));
    }
}
