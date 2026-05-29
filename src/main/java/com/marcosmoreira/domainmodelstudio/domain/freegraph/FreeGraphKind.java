package com.marcosmoreira.domainmodelstudio.domain.freegraph;

/** Tipo general de grafo libre que determina la dirección permitida en sus relaciones. */
public enum FreeGraphKind {
    DIRECTED("Dirigido"),
    UNDIRECTED("No dirigido"),
    MIXED("Mixto");

    private final String displayName;

    FreeGraphKind(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
