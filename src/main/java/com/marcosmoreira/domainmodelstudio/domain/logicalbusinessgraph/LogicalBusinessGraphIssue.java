package com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph;

/**
 * Hallazgo semántico o estructural detectado en el grafo lógico del negocio.
 *
 * @param severity nivel de impacto del hallazgo
 * @param elementId código de nodo o ID de relación asociado al hallazgo
 * @param message explicación orientada al usuario
 */
public record LogicalBusinessGraphIssue(
        LogicalBusinessGraphIssueSeverity severity,
        String elementId,
        String message
) {
    public LogicalBusinessGraphIssue {
        severity = severity == null ? LogicalBusinessGraphIssueSeverity.WARNING : severity;
        elementId = LogicalBusinessGraphText.normalize(elementId);
        message = LogicalBusinessGraphText.require(message, "message");
    }

    /**
     * Crea una advertencia que no bloquea apertura ni exportación, pero requiere revisión.
     */
    public static LogicalBusinessGraphIssue warning(String elementId, String message) {
        return new LogicalBusinessGraphIssue(LogicalBusinessGraphIssueSeverity.WARNING, elementId, message);
    }

    /**
     * Crea un hallazgo bloqueante para inconsistencias estructurales o semánticas graves.
     */
    public static LogicalBusinessGraphIssue blocking(String elementId, String message) {
        return new LogicalBusinessGraphIssue(LogicalBusinessGraphIssueSeverity.BLOCKING, elementId, message);
    }

    /**
     * Crea una observación informativa para mejorar calidad sin exigir corrección inmediata.
     */
    public static LogicalBusinessGraphIssue info(String elementId, String message) {
        return new LogicalBusinessGraphIssue(LogicalBusinessGraphIssueSeverity.INFO, elementId, message);
    }
}
