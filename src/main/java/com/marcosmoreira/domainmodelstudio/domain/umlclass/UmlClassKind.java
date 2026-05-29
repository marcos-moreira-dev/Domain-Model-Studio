package com.marcosmoreira.domainmodelstudio.domain.umlclass;

/** Tipo funcional de una clase representada en UML Clases. */
public enum UmlClassKind {
    CLASS("Clase"),
    ABSTRACT_CLASS("Clase abstracta"),
    INTERFACE("Interfaz"),
    ENUM("Enum"),
    RECORD("Record"),
    SERVICE("Servicio"),
    CONTROLLER("Controlador"),
    REPOSITORY("Repositorio"),
    DTO("DTO"),
    COMPONENT("Componente");

    private final String displayName;

    UmlClassKind(String displayName) { this.displayName = displayName; }

    public String displayName() { return displayName; }
}
