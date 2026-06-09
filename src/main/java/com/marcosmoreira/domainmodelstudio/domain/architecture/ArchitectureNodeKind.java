package com.marcosmoreira.domainmodelstudio.domain.architecture;

/** Tipos de elementos para C4 y despliegue técnico. */
public enum ArchitectureNodeKind {
    PERSON("Persona"),
    SOFTWARE_SYSTEM("Sistema"),
    EXTERNAL_SYSTEM("Sistema externo"),
    BOUNDARY("Límite"),
    CONTAINER("Contenedor"),
    APPLICATION("Aplicación"),
    API("API"),
    DATABASE("Base de datos"),
    EXTERNAL_SERVICE("Servicio externo"),
    ENVIRONMENT("Ambiente"),
    SERVER("Servidor"),
    CLIENT("Cliente"),
    SERVICE("Servicio"),
    NETWORK("Red"),
    ARTIFACT("Artefacto");

    private final String displayName;
    ArchitectureNodeKind(String displayName) { this.displayName = displayName; }
    public String displayName() { return displayName; }
    @Override public String toString() { return displayName; }
}
