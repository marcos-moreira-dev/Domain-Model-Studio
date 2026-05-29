package com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph;

import static com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphIssueSeverity.BLOCKING;
import static com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphIssueSeverity.INFO;
import static com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphIssueSeverity.WARNING;
import static com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeKind.*;
import static com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphRelationKind.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Cobertura de Tanda 42 para la validación integral del Grafo lógico del negocio. */
class LogicalBusinessGraphIntegralValidationTest {

    @Test
    void validationShouldReportBlockingIssuesForNonDerivableOrInvalidGraphs() {
        LogicalBusinessGraphDocument withoutMacroFlow = new LogicalBusinessGraphDocument(
                "Demo", "v0.1", LocalDate.now(),
                List.of(LogicalBusinessGraphNode.of("FL-001", FLOW, "Flujo aislado")),
                List.of(), "");

        assertTrue(hasIssue(withoutMacroFlow, BLOCKING, "macroflujo"));

        LogicalBusinessGraphDocument invalidRelation = new LogicalBusinessGraphDocument(
                "Demo", "v0.1", LocalDate.now(),
                List.of(
                        LogicalBusinessGraphNode.of("MF-001", MACRO_FLOW, "Gestión"),
                        LogicalBusinessGraphNode.of("FL-001", FLOW, "Flujo")),
                List.of(LogicalBusinessGraphEdge.of("rel-001", "MF-001", DEPENDS_ON, "MF-001")), "");

        assertTrue(hasIssue(invalidRelation, BLOCKING, "mismo nodo"));
        assertTrue(hasIssue(invalidRelation, BLOCKING, "no es semánticamente esperada"));
    }

    @Test
    void validationShouldWarnWhenBackboneAndLogicalTraceabilityAreWeak() {
        LogicalBusinessGraphDocument document = new LogicalBusinessGraphDocument(
                "Demo", "v0.1", LocalDate.now(),
                List.of(
                        LogicalBusinessGraphNode.of("MF-001", MACRO_FLOW, "Macro sin flujo"),
                        LogicalBusinessGraphNode.of("FL-001", FLOW, "Flujo sin caso"),
                        LogicalBusinessGraphNode.of("RN-001", BUSINESS_RULE, "Regla aislada"),
                        LogicalBusinessGraphNode.of("PRE-001", PRECONDITION, "Precondición aislada"),
                        LogicalBusinessGraphNode.of("INV-001", INVARIANT, "Invariante aislada"),
                        LogicalBusinessGraphNode.of("POST-001", POSTCONDITION, "Postcondición aislada"),
                        LogicalBusinessGraphNode.of("RISK-001", RISK, "Riesgo aislado"),
                        LogicalBusinessGraphNode.of("PEND-001", PENDING_QUESTION, "Pregunta aislada")),
                List.of(LogicalBusinessGraphEdge.of("rel-001", "MF-001", CONTAINS, "FL-001")), "");

        assertTrue(hasIssue(document, WARNING, "flujo no usa"));
        assertTrue(hasIssue(document, WARNING, "regla de negocio está aislada"));
        assertTrue(hasIssue(document, WARNING, "precondición no es requerida"));
        assertTrue(hasIssue(document, WARNING, "invariante no está protegida"));
        assertTrue(hasIssue(document, WARNING, "postcondición no está garantizada"));
        assertTrue(hasIssue(document, WARNING, "riesgo no bloquea"));
        assertTrue(hasIssue(document, WARNING, "pregunta pendiente no bloquea"));
    }

    @Test
    void validationShouldWarnWhenDerivedArtifactsHaveNoSource() {
        LogicalBusinessGraphDocument document = new LogicalBusinessGraphDocument(
                "Demo", "v0.1", LocalDate.now(),
                List.of(
                        LogicalBusinessGraphNode.of("MF-001", MACRO_FLOW, "Gestión"),
                        LogicalBusinessGraphNode.of("FL-001", FLOW, "Flujo"),
                        LogicalBusinessGraphNode.of("CU-001", USE_CASE, "Caso"),
                        LogicalBusinessGraphNode.of("ACC-001", ACTION, "Acción sin efectos"),
                        LogicalBusinessGraphNode.of("ENT-001", ENTITY, "Entidad aislada"),
                        LogicalBusinessGraphNode.of("EST-001", STATE, "Estado aislado"),
                        LogicalBusinessGraphNode.of("REP-001", REPORT, "Reporte aislado")),
                List.of(
                        LogicalBusinessGraphEdge.of("rel-001", "MF-001", CONTAINS, "FL-001"),
                        LogicalBusinessGraphEdge.of("rel-002", "FL-001", USES, "CU-001"),
                        LogicalBusinessGraphEdge.of("rel-003", "CU-001", EXECUTES, "ACC-001")), "");

        assertTrue(hasIssue(document, WARNING, "acción transformadora no declara"));
        assertTrue(hasIssue(document, WARNING, "entidad candidata está aislada"));
        assertTrue(hasIssue(document, WARNING, "estado no está conectado"));
        assertTrue(hasIssue(document, WARNING, "reporte no tiene fuente"));
    }

    @Test
    void validationShouldAcceptACompactButTraceableGraph() {
        LogicalBusinessGraphDocument document = new LogicalBusinessGraphDocument(
                "Demo", "v0.1", LocalDate.now(),
                List.of(
                        LogicalBusinessGraphNode.of("MF-001", MACRO_FLOW, "Gestión"),
                        LogicalBusinessGraphNode.of("FL-001", FLOW, "Registrar"),
                        LogicalBusinessGraphNode.of("CU-001", USE_CASE, "Registrar estudiante"),
                        LogicalBusinessGraphNode.of("ACC-001", ACTION, "Crear expediente"),
                        LogicalBusinessGraphNode.of("PRE-001", PRECONDITION, "Datos mínimos"),
                        LogicalBusinessGraphNode.of("INV-001", INVARIANT, "No duplicar"),
                        LogicalBusinessGraphNode.of("POST-001", POSTCONDITION, "Estudiante registrado"),
                        LogicalBusinessGraphNode.of("ENT-001", ENTITY, "Estudiante"),
                        LogicalBusinessGraphNode.of("REP-001", REPORT, "Reporte"),
                        LogicalBusinessGraphNode.of("RISK-001", RISK, "Riesgo"),
                        LogicalBusinessGraphNode.of("PEND-001", PENDING_QUESTION, "Pregunta")),
                List.of(
                        LogicalBusinessGraphEdge.of("rel-001", "MF-001", CONTAINS, "FL-001"),
                        LogicalBusinessGraphEdge.of("rel-002", "FL-001", USES, "CU-001"),
                        LogicalBusinessGraphEdge.of("rel-003", "CU-001", EXECUTES, "ACC-001"),
                        LogicalBusinessGraphEdge.of("rel-004", "ACC-001", REQUIRES, "PRE-001"),
                        LogicalBusinessGraphEdge.of("rel-005", "ACC-001", PROTECTS, "INV-001"),
                        LogicalBusinessGraphEdge.of("rel-006", "ACC-001", GUARANTEES, "POST-001"),
                        LogicalBusinessGraphEdge.of("rel-007", "ACC-001", CREATES, "ENT-001"),
                        LogicalBusinessGraphEdge.of("rel-008", "FL-001", GENERATES, "REP-001"),
                        LogicalBusinessGraphEdge.of("rel-009", "RISK-001", BLOCKS, "FL-001"),
                        LogicalBusinessGraphEdge.of("rel-010", "PEND-001", BLOCKS, "REP-001")), "");

        assertFalse(document.semanticIssues().stream().anyMatch(issue -> issue.severity() != INFO));
    }

    private static boolean hasIssue(LogicalBusinessGraphDocument document,
                                    LogicalBusinessGraphIssueSeverity severity,
                                    String messageFragment) {
        return document.semanticIssues().stream()
                .anyMatch(issue -> issue.severity() == severity
                        && issue.message().toLowerCase().contains(messageFragment.toLowerCase()));
    }
}
