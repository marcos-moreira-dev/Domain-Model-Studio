package com.marcosmoreira.domainmodelstudio.application.logicalbusiness.derivation;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import java.util.List;
import java.util.Objects;

/** Vista de lectura del documento para escritores de borradores compatibles revisables. */
final class LogicalBusinessDerivationContext {

    private final LogicalBusinessDocument document;

    LogicalBusinessDerivationContext(LogicalBusinessDocument document) {
        this.document = Objects.requireNonNull(document, "document");
    }

    String projectName() {
        return document.projectName();
    }

    String domainName() {
        return document.projectName();
    }

    List<LogicalBusinessItem> rules() {
        return items(LogicalBusinessItemKind.RULE);
    }

    List<LogicalBusinessItem> invariants() {
        return items(LogicalBusinessItemKind.INVARIANT);
    }

    List<LogicalBusinessItem> actions() {
        return items(LogicalBusinessItemKind.ACTION);
    }

    List<LogicalBusinessItem> macroFlows() {
        return items(LogicalBusinessItemKind.MACRO_FLOW);
    }

    List<LogicalBusinessItem> flows() {
        return items(LogicalBusinessItemKind.FLOW);
    }

    List<LogicalBusinessItem> preconditions() {
        return items(LogicalBusinessItemKind.PRECONDITION);
    }

    List<LogicalBusinessItem> postconditions() {
        return items(LogicalBusinessItemKind.POSTCONDITION);
    }

    List<LogicalBusinessItem> states() {
        return items(LogicalBusinessItemKind.STATE);
    }

    List<LogicalBusinessItem> reports() {
        return items(LogicalBusinessItemKind.REPORT);
    }

    List<LogicalBusinessItem> risks() {
        return items(LogicalBusinessItemKind.RISK);
    }

    List<LogicalBusinessItem> pendingQuestionItems() {
        return items(LogicalBusinessItemKind.PENDING_QUESTION);
    }

    List<com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion> pendingQuestions() {
        return document.pendingQuestions();
    }

    List<LogicalBusinessItem> useCases() {
        return items(LogicalBusinessItemKind.USE_CASE);
    }

    List<LogicalBusinessItem> actors() {
        return items(LogicalBusinessItemKind.ACTOR);
    }

    List<LogicalBusinessItem> items(LogicalBusinessItemKind kind) {
        return document.itemsByKind(kind);
    }

    List<LogicalBusinessItem> primaryFlowItems() {
        List<LogicalBusinessItem> useCases = useCases();
        return useCases.isEmpty() ? actions() : useCases;
    }

    List<LogicalBusinessEntityCandidate> entities() {
        return document.entityCandidates();
    }

    List<String> warnings() {
        return List.of(
                "Borrador compatible preparado desde la fuente lógica: revisar nombres, relaciones y omisiones antes de importar.",
                "No reemplaza la validación humana del levantamiento lógico ni la entrevista con el cliente."
        );
    }
}
