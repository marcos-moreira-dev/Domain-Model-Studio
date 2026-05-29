package com.marcosmoreira.domainmodelstudio.domain.logicalbusiness;

/** Hallazgo de validación semántica del levantamiento lógico. */
public record LogicalBusinessValidationIssue(
        LogicalBusinessIssueSeverity severity,
        String targetId,
        String message
) {
    public LogicalBusinessValidationIssue {
        severity = severity == null ? LogicalBusinessIssueSeverity.WARNING : severity;
        targetId = LogicalBusinessText.normalize(targetId);
        message = LogicalBusinessText.require(message, "message");
    }

    public static LogicalBusinessValidationIssue blocking(String targetId, String message) {
        return new LogicalBusinessValidationIssue(LogicalBusinessIssueSeverity.BLOCKING, targetId, message);
    }
}
