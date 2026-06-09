package com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph;

/** Estado de madurez de un nodo del grafo lógico de negocio. */
public enum LogicalBusinessGraphNodeStatus {
    DRAFT("borrador"),
    IN_REVIEW("en revisión"),
    PARTIALLY_VALIDATED("validado parcialmente"),
    VALIDATED("validado"),
    BLOCKED("bloqueado"),
    DISCARDED("descartado");

    private final String displayName;

    LogicalBusinessGraphNodeStatus(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
