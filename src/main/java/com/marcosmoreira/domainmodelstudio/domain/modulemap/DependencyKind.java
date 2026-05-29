package com.marcosmoreira.domainmodelstudio.domain.modulemap;

/** Tipo de relación funcional entre módulos. */
public enum DependencyKind {
    USES("Usa"),
    PROVIDES_DATA("Entrega datos"),
    RECEIVES_DATA("Recibe datos"),
    NOTIFIES("Notifica"),
    AUTHORIZES("Autoriza"),
    REPORTS_FROM("Reporta desde"),
    OTHER("Otra");

    private final String displayName;

    DependencyKind(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
