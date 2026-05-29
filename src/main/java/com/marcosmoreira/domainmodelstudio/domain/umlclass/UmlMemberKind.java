package com.marcosmoreira.domainmodelstudio.domain.umlclass;

/** Tipo de miembro dentro de una clase UML. */
public enum UmlMemberKind {
    ATTRIBUTE("Atributo"),
    METHOD("Método"),
    CONSTRUCTOR("Constructor");

    private final String displayName;

    UmlMemberKind(String displayName) { this.displayName = displayName; }

    public String displayName() { return displayName; }
}
