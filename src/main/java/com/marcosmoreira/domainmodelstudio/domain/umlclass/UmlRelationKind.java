package com.marcosmoreira.domainmodelstudio.domain.umlclass;

/** Tipo de relación estructural entre clases UML. */
public enum UmlRelationKind {
    ASSOCIATION("Asociación"),
    INHERITANCE("Herencia"),
    IMPLEMENTATION("Implementación"),
    DEPENDENCY("Dependencia"),
    COMPOSITION("Composición"),
    AGGREGATION("Agregación");

    private final String displayName;

    UmlRelationKind(String displayName) { this.displayName = displayName; }

    public String displayName() { return displayName; }
}
