package com.marcosmoreira.domainmodelstudio.domain.logicalbusiness;

/** Pregunta pendiente que puede bloquear reglas, entidades, atributos o decisiones internas. */
public record LogicalBusinessPendingQuestion(
        String id,
        String question,
        String affects,
        LogicalBusinessQuestionPriority priority,
        LogicalBusinessItemStatus status
) {
    public LogicalBusinessPendingQuestion {
        id = LogicalBusinessText.require(id, "id");
        question = LogicalBusinessText.require(question, "question");
        affects = LogicalBusinessText.normalize(affects);
        priority = priority == null ? LogicalBusinessQuestionPriority.MEDIUM : priority;
        status = status == null ? LogicalBusinessItemStatus.DRAFT : status;
        if (!LogicalBusinessItemKind.PENDING_QUESTION.matchesId(id)) {
            throw new IllegalArgumentException("La pregunta pendiente debe usar ID PEND-XXX: " + id);
        }
    }

    public LogicalBusinessPendingQuestion withEditableDetails(
            String updatedQuestion,
            String updatedAffects,
            LogicalBusinessQuestionPriority updatedPriority,
            LogicalBusinessItemStatus updatedStatus
    ) {
        return new LogicalBusinessPendingQuestion(id, updatedQuestion, updatedAffects, updatedPriority, updatedStatus);
    }
}

