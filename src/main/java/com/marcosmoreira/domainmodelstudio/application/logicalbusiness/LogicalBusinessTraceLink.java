package com.marcosmoreira.domainmodelstudio.application.logicalbusiness;

/** Enlace de traza interna entre dos elementos del mismo levantamiento lógico. */
public record LogicalBusinessTraceLink(
        String sourceId,
        String relation,
        String targetId,
        String reading
) {
    public LogicalBusinessTraceLink {
        sourceId = clean(sourceId);
        relation = clean(relation).isBlank() ? "referencia" : clean(relation);
        targetId = clean(targetId);
        reading = clean(reading);
    }

    public boolean touches(String id) {
        String cleaned = clean(id);
        return sourceId.equals(cleaned) || targetId.equals(cleaned);
    }

    private static String clean(String value) {
        return value == null ? "" : value.strip();
    }
}
