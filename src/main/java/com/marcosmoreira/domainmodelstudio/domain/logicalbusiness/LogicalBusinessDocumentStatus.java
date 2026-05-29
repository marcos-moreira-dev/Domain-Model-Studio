package com.marcosmoreira.domainmodelstudio.domain.logicalbusiness;

/** Estado general del expediente lógico de negocio. */
public enum LogicalBusinessDocumentStatus {
    DRAFT("borrador"),
    PARTIALLY_VALIDATED("validado parcialmente"),
    VALIDATED("validado"),
    ARCHIVED("archivado");

    private final String displayName;

    LogicalBusinessDocumentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
