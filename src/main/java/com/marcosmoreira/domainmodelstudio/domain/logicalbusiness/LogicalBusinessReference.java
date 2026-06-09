package com.marcosmoreira.domainmodelstudio.domain.logicalbusiness;

/** Referencia trazable entre dos elementos del levantamiento lógico. */
public record LogicalBusinessReference(
        String sourceId,
        String targetId,
        String reason
) {
    public LogicalBusinessReference {
        sourceId = LogicalBusinessText.require(sourceId, "sourceId");
        targetId = LogicalBusinessText.require(targetId, "targetId");
        reason = LogicalBusinessText.normalize(reason);
    }

    public static LogicalBusinessReference of(String sourceId, String targetId) {
        return new LogicalBusinessReference(sourceId, targetId, "");
    }
}
