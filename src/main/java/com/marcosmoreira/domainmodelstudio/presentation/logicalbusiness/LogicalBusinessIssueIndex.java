package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessIssueSeverity;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessValidationIssue;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Índice ligero de hallazgos para marcar el árbol sin cargar el panel de validación. */
final class LogicalBusinessIssueIndex {

    private final Map<String, LogicalBusinessIssueSeverity> severities = new HashMap<>();

    private LogicalBusinessIssueIndex() {
    }

    static LogicalBusinessIssueIndex from(List<LogicalBusinessValidationIssue> issues) {
        LogicalBusinessIssueIndex index = new LogicalBusinessIssueIndex();
        if (issues != null) {
            issues.forEach(index::put);
        }
        return index;
    }

    LogicalBusinessIssueSeverity severity(String targetId) {
        if (targetId == null || targetId.isBlank()) {
            return null;
        }
        return severities.get(targetId.strip());
    }

    boolean hasBlocking() {
        return severities.values().stream().anyMatch(LogicalBusinessIssueSeverity.BLOCKING::equals);
    }

    boolean hasWarnings() {
        return severities.values().stream().anyMatch(LogicalBusinessIssueSeverity.WARNING::equals);
    }

    private void put(LogicalBusinessValidationIssue issue) {
        if (issue.targetId().isBlank()) {
            return;
        }
        severities.merge(issue.targetId(), issue.severity(), LogicalBusinessIssueIndex::maxSeverity);
    }

    private static LogicalBusinessIssueSeverity maxSeverity(
            LogicalBusinessIssueSeverity current,
            LogicalBusinessIssueSeverity incoming
    ) {
        Map<LogicalBusinessIssueSeverity, Integer> weight = new EnumMap<>(LogicalBusinessIssueSeverity.class);
        weight.put(LogicalBusinessIssueSeverity.INFO, 1);
        weight.put(LogicalBusinessIssueSeverity.WARNING, 2);
        weight.put(LogicalBusinessIssueSeverity.BLOCKING, 3);
        return weight.get(incoming) > weight.get(current) ? incoming : current;
    }
}
