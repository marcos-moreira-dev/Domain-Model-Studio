package com.marcosmoreira.domainmodelstudio.domain.behavior;

/** Tipos de relación para diagramas de comportamiento. */
public enum BehaviorEdgeKind {
    FLOW("Flujo"),
    ASSOCIATION("Asociación"),
    INCLUDE("Include"),
    EXTEND("Extend"),
    GENERALIZATION("Generalización"),
    MESSAGE("Mensaje síncrono"),
    ASYNC_MESSAGE("Mensaje asíncrono"),
    RETURN_MESSAGE("Respuesta"),
    TRANSITION("Transición");

    private final String displayName;
    BehaviorEdgeKind(String displayName) { this.displayName = displayName; }
    public String displayName() { return displayName; }
    @Override public String toString() { return displayName; }
}
