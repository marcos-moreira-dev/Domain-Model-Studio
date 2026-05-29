package com.marcosmoreira.domainmodelstudio.domain.freegraph;

/** Dirección semántica de una relación en un grafo libre. */
public enum FreeGraphEdgeDirection {
    DIRECTED("Dirigida"),
    UNDIRECTED("No dirigida");

    private final String displayName;

    FreeGraphEdgeDirection(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
