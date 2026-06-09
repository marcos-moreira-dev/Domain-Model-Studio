package com.marcosmoreira.domainmodelstudio.application.sourcecode;

/** Rol arquitectónico tentativo de una raíz de código dentro de un sistema. */
public enum SourceRootKind {
    BACKEND("Backend"),
    FRONTEND("Frontend"),
    SHARED("Compartido"),
    LIBRARY("Librería"),
    UNKNOWN("Sin clasificar");

    private final String displayName;

    SourceRootKind(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
