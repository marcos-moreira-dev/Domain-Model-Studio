package com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeKind.*;
import static com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphRelationKind.*;

/**
 * Política semántica integral del grafo lógico del negocio.
 *
 * <p>La política no valida geometría ni detalles de UI. Su foco es calidad de
 * documentación lógica: backbone MF-FL-CU-ACC, relaciones válidas, reglas y
 * condiciones trazables, artefactos compatibles conectados, riesgos visibles y
 * preguntas pendientes que bloquean algo concreto.</p>
 
 *
 * <p><strong>Ejemplo pedagógico:</strong> si el documento contiene {@code MF-001}
 * sin ninguna relación {@code contiene} hacia un {@code FL-*}, la política no inventa
 * un flujo. Reporta una advertencia para que el analista decida si falta levantar
 * información o si el macroflujo aún está en borrador.</p>
 */
final class LogicalBusinessGraphValidationPolicy {

    List<LogicalBusinessGraphIssue> validate(LogicalBusinessGraphDocument document) {
        List<LogicalBusinessGraphIssue> issues = new ArrayList<>();
        validateMinimumBackbone(document, issues);
        validateRelations(document, issues);
        validateTraceableConditions(document, issues);
        validateDerivableArtifacts(document, issues);
        validateOperationalRisksAndQuestions(document, issues);
        validateGenericDependencyUsage(document, issues);
        return List.copyOf(issues);
    }

    private void validateMinimumBackbone(LogicalBusinessGraphDocument document,
                                         List<LogicalBusinessGraphIssue> issues) {
        if (document.nodesByKind(MACRO_FLOW).isEmpty()) {
            issues.add(LogicalBusinessGraphIssue.blocking("",
                    "El grafo lógico debe tener al menos un macroflujo para ser derivable."));
            return;
        }
        for (LogicalBusinessGraphNode macroFlow : document.nodesByKind(MACRO_FLOW)) {
            if (!hasOutgoingTo(document, macroFlow.code(), CONTAINS, Set.of(FLOW))) {
                issues.add(LogicalBusinessGraphIssue.warning(macroFlow.code(),
                        "El macroflujo no contiene ningún flujo o microflujo."));
            }
        }
        for (LogicalBusinessGraphNode flow : document.nodesByKind(FLOW)) {
            if (!hasOutgoingTo(document, flow.code(), EnumSet.of(USES, REUSES, EXECUTES), Set.of(USE_CASE, ACTION))) {
                issues.add(LogicalBusinessGraphIssue.warning(flow.code(),
                        "El flujo no usa ni ejecuta casos de uso o acciones transformadoras."));
            }
        }
    }

    private void validateRelations(LogicalBusinessGraphDocument document,
                                   List<LogicalBusinessGraphIssue> issues) {
        for (LogicalBusinessGraphEdge edge : document.edges()) {
            LogicalBusinessGraphNode source = document.nodeByCode(edge.sourceCode()).orElseThrow();
            LogicalBusinessGraphNode target = document.nodeByCode(edge.targetCode()).orElseThrow();
            if (edge.loop()) {
                issues.add(LogicalBusinessGraphIssue.blocking(edge.id(),
                        "La relación lógica no puede apuntar al mismo nodo."));
            }
            if (!edge.relationKind().canConnect(source.kind(), target.kind())) {
                issues.add(LogicalBusinessGraphIssue.blocking(edge.id(),
                        "La relación '" + edge.relationKind().code() + "' no es semánticamente esperada entre "
                                + source.kind().prefix() + " y " + target.kind().prefix() + "."));
            }
        }
    }

    private void validateTraceableConditions(LogicalBusinessGraphDocument document,
                                             List<LogicalBusinessGraphIssue> issues) {
        for (LogicalBusinessGraphNode rule : document.nodesByKind(BUSINESS_RULE)) {
            if (!hasOutgoing(document, rule.code(), Set.of(APPLIES, ENABLES, DERIVES_IN))) {
                issues.add(LogicalBusinessGraphIssue.warning(rule.code(),
                        "La regla de negocio está aislada; debería aplicar, habilitar o derivar algún elemento."));
            }
        }
        for (LogicalBusinessGraphNode precondition : document.nodesByKind(PRECONDITION)) {
            if (!hasIncoming(document, precondition.code(), Set.of(REQUIRES))) {
                issues.add(LogicalBusinessGraphIssue.warning(precondition.code(),
                        "La precondición no es requerida por ningún flujo, caso de uso o acción."));
            }
        }
        for (LogicalBusinessGraphNode invariant : document.nodesByKind(INVARIANT)) {
            if (!hasIncoming(document, invariant.code(), Set.of(PROTECTS))) {
                issues.add(LogicalBusinessGraphIssue.warning(invariant.code(),
                        "La invariante no está protegida por ningún flujo, caso de uso o acción."));
            }
        }
        for (LogicalBusinessGraphNode postcondition : document.nodesByKind(POSTCONDITION)) {
            if (!hasIncoming(document, postcondition.code(), Set.of(GUARANTEES))) {
                issues.add(LogicalBusinessGraphIssue.warning(postcondition.code(),
                        "La postcondición no está garantizada por ningún flujo, caso de uso o acción."));
            }
        }
    }

    private void validateDerivableArtifacts(LogicalBusinessGraphDocument document,
                                            List<LogicalBusinessGraphIssue> issues) {
        for (LogicalBusinessGraphNode action : document.nodesByKind(ACTION)) {
            if (!hasOutgoing(document, action.code(), Set.of(REQUIRES, PROTECTS, GUARANTEES,
                    CREATES, MODIFIES, CONSULTS, GENERATES, FEEDS, DERIVES_IN))) {
                issues.add(LogicalBusinessGraphIssue.warning(action.code(),
                        "La acción transformadora no declara condiciones, efectos ni artefactos compatibles."));
            }
        }
        for (LogicalBusinessGraphNode entity : document.nodesByKind(ENTITY)) {
            if (entity.sourceReferenceIds().isEmpty()
                    && !hasIncoming(document, entity.code(), Set.of(CREATES, MODIFIES, CONSULTS, FEEDS, DERIVES_IN))
                    && !hasOutgoing(document, entity.code(), Set.of(FEEDS, DEPENDS_ON))) {
                issues.add(LogicalBusinessGraphIssue.warning(entity.code(),
                        "La entidad candidata está aislada; debería crearse, modificarse, consultarse, alimentar o derivarse."));
            }
        }
        for (LogicalBusinessGraphNode report : document.nodesByKind(REPORT)) {
            if (report.sourceReferenceIds().isEmpty()
                    && !hasIncoming(document, report.code(), Set.of(GENERATES, FEEDS, DERIVES_IN, DEPENDS_ON))
                    && !hasOutgoing(document, report.code(), Set.of(DEPENDS_ON, FEEDS))) {
                issues.add(LogicalBusinessGraphIssue.warning(report.code(),
                        "El reporte no tiene fuente lógica; debería generarse, alimentarse o derivarse."));
            }
        }
        for (LogicalBusinessGraphNode state : document.nodesByKind(STATE)) {
            if (state.sourceReferenceIds().isEmpty()
                    && !hasIncoming(document, state.code(), Set.of(CREATES, MODIFIES, GENERATES, DERIVES_IN))
                    && !hasOutgoing(document, state.code(), Set.of(FEEDS, ENABLES, DEPENDS_ON))) {
                issues.add(LogicalBusinessGraphIssue.warning(state.code(),
                        "El estado no está conectado a una acción, derivación o condición de avance."));
            }
        }
    }

    private void validateOperationalRisksAndQuestions(LogicalBusinessGraphDocument document,
                                                      List<LogicalBusinessGraphIssue> issues) {
        for (LogicalBusinessGraphNode risk : document.nodesByKind(RISK)) {
            if (!hasOutgoing(document, risk.code(), Set.of(BLOCKS))) {
                issues.add(LogicalBusinessGraphIssue.warning(risk.code(),
                        "El riesgo no bloquea ni afecta ningún elemento lógico."));
            }
        }
        for (LogicalBusinessGraphNode question : document.nodesByKind(PENDING_QUESTION)) {
            if (!hasOutgoing(document, question.code(), Set.of(BLOCKS))) {
                issues.add(LogicalBusinessGraphIssue.warning(question.code(),
                        "La pregunta pendiente no bloquea ningún elemento lógico."));
            }
        }
    }

    private void validateGenericDependencyUsage(LogicalBusinessGraphDocument document,
                                                List<LogicalBusinessGraphIssue> issues) {
        long genericDependencies = document.edges().stream()
                .filter(edge -> edge.relationKind() == DEPENDS_ON)
                .count();
        if (document.edges().size() >= 4 && genericDependencies * 2 > document.edges().size()) {
            issues.add(LogicalBusinessGraphIssue.info("",
                    "El grafo usa muchas relaciones depende_de; conviene reemplazarlas por relaciones semánticas más precisas cuando sea posible."));
        }
    }

    private boolean hasOutgoing(LogicalBusinessGraphDocument document, String code,
                                Set<LogicalBusinessGraphRelationKind> relations) {
        return document.outgoingEdgesOf(code).stream()
                .anyMatch(edge -> relations.contains(edge.relationKind()));
    }

    private boolean hasIncoming(LogicalBusinessGraphDocument document, String code,
                                Set<LogicalBusinessGraphRelationKind> relations) {
        return document.incomingEdgesOf(code).stream()
                .anyMatch(edge -> relations.contains(edge.relationKind()));
    }

    private boolean hasOutgoingTo(LogicalBusinessGraphDocument document, String code,
                                  LogicalBusinessGraphRelationKind relation,
                                  Set<LogicalBusinessGraphNodeKind> targetKinds) {
        return hasOutgoingTo(document, code, Set.of(relation), targetKinds);
    }

    private boolean hasOutgoingTo(LogicalBusinessGraphDocument document, String code,
                                  Set<LogicalBusinessGraphRelationKind> relations,
                                  Set<LogicalBusinessGraphNodeKind> targetKinds) {
        return document.outgoingEdgesOf(code).stream()
                .filter(edge -> relations.contains(edge.relationKind()))
                .map(edge -> document.nodeByCode(edge.targetCode()).orElseThrow())
                .anyMatch(target -> targetKinds.contains(target.kind()));
    }
}
