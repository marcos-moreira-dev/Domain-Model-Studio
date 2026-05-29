package com.marcosmoreira.domainmodelstudio.domain.behavior;

/** Tipos de elementos visibles para diagramas de proceso y UML de comportamiento. */
public enum BehaviorNodeKind {
    START_EVENT("Inicio"),
    END_EVENT("Fin"),
    ACTIVITY("Actividad"),
    DECISION("Decisión"),
    LANE("Carril"),
    ACTOR("Actor"),
    USE_CASE("Caso de uso"),
    SYSTEM_BOUNDARY("Sistema"),
    ACTION("Acción"),
    FORK("Bifurcación"),
    JOIN("Unión"),
    PARTICIPANT("Participante"),
    ACTIVATION("Activación"),
    FRAGMENT("Fragmento"),
    STATE("Estado"),
    INITIAL_STATE("Inicio"),
    FINAL_STATE("Final"),
    NOTE("Nota");

    private final String displayName;

    BehaviorNodeKind(String displayName) { this.displayName = displayName; }
    public String displayName() { return displayName; }

    @Override public String toString() { return displayName; }
}
