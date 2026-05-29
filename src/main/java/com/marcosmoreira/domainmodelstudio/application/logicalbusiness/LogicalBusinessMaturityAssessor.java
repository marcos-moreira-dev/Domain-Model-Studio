package com.marcosmoreira.domainmodelstudio.application.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessIssueSeverity;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessMaturity;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessMaturityLevel;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessValidationIssue;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Calcula una lectura práctica de coherencia interna y madurez documental del levantamiento lógico. */
public final class LogicalBusinessMaturityAssessor {

    private final LogicalBusinessValidationService validationService;

    public LogicalBusinessMaturityAssessor() {
        this(new LogicalBusinessValidationService());
    }

    public LogicalBusinessMaturityAssessor(LogicalBusinessValidationService validationService) {
        this.validationService = Objects.requireNonNull(validationService, "validationService");
    }

    public LogicalBusinessMaturity assess(LogicalBusinessDocument document) {
        List<LogicalBusinessValidationIssue> issues = validationService.validate(document);
        long blocking = validationService.count(issues, LogicalBusinessIssueSeverity.BLOCKING);
        long warnings = validationService.count(issues, LogicalBusinessIssueSeverity.WARNING);
        LogicalBusinessMaturityLevel level = levelFor(document, blocking, warnings);
        return new LogicalBusinessMaturity(level, strengthsFor(document), blockersFor(issues), nextStepsFor(document, issues));
    }

    private LogicalBusinessMaturityLevel levelFor(LogicalBusinessDocument document, long blocking, long warnings) {
        if (document.items().isEmpty() && document.entityCandidates().isEmpty()) {
            return LogicalBusinessMaturityLevel.INITIAL;
        }
        if (blocking > 0) {
            return LogicalBusinessMaturityLevel.PARTIAL;
        }
        if (hasCoreForSourceUse(document) && warnings <= 3) {
            return LogicalBusinessMaturityLevel.SOURCE_READY;
        }
        if (hasCoreForSourceUse(document)) {
            return LogicalBusinessMaturityLevel.CONSISTENT;
        }
        return LogicalBusinessMaturityLevel.PARTIAL;
    }

    private boolean hasCoreForSourceUse(LogicalBusinessDocument document) {
        return !document.itemsByKind(LogicalBusinessItemKind.RULE).isEmpty()
                && !document.itemsByKind(LogicalBusinessItemKind.ACTION).isEmpty()
                && !document.itemsByKind(LogicalBusinessItemKind.INVARIANT).isEmpty()
                && !document.entityCandidates().isEmpty();
    }

    private List<String> strengthsFor(LogicalBusinessDocument document) {
        List<String> strengths = new ArrayList<>();
        if (!document.itemsByKind(LogicalBusinessItemKind.RULE).isEmpty()) {
            strengths.add("Reglas de negocio identificadas.");
        }
        if (!document.itemsByKind(LogicalBusinessItemKind.ACTION).isEmpty()) {
            strengths.add("Acciones transformadoras identificadas.");
        }
        if (!document.entityCandidates().isEmpty()) {
            strengths.add("Entidades candidatas justificadas por la lógica.");
        }
        if (!document.pendingQuestions().isEmpty()) {
            strengths.add("Preguntas pendientes explícitas, no escondidas.");
        }
        return strengths;
    }

    private List<String> blockersFor(List<LogicalBusinessValidationIssue> issues) {
        return issues.stream()
                .filter(issue -> issue.severity() == LogicalBusinessIssueSeverity.BLOCKING)
                .map(issue -> issue.targetId() + ": " + issue.message())
                .toList();
    }

    private List<String> nextStepsFor(LogicalBusinessDocument document, List<LogicalBusinessValidationIssue> issues) {
        List<String> steps = new ArrayList<>();
        issues.stream().limit(5).map(issue -> issue.targetId() + ": " + issue.message()).forEach(steps::add);
        if (document.entityCandidates().isEmpty()) {
            steps.add("Identificar entidades candidatas desde acciones, reglas, evidencias o reportes.");
        }
        if (document.itemsByKind(LogicalBusinessItemKind.ACTION).isEmpty()) {
            steps.add("Registrar acciones transformadoras con precondiciones y postcondiciones.");
        }
        return steps.stream().distinct().toList();
    }
}
