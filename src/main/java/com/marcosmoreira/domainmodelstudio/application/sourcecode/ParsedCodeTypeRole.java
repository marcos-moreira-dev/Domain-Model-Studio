package com.marcosmoreira.domainmodelstudio.application.sourcecode;

/** Rol arquitectónico tentativo de un tipo detectado desde código fuente. */
public enum ParsedCodeTypeRole {
    BACKEND_CONTROLLER("backend-controller", "Controlador backend"),
    BACKEND_SERVICE("backend-service", "Servicio backend"),
    BACKEND_REPOSITORY("backend-repository", "Repositorio backend"),
    BACKEND_ENTITY("backend-entity", "Entidad backend"),
    FRONTEND_COMPONENT("frontend-component", "Componente frontend"),
    FRONTEND_SERVICE("frontend-service", "Servicio frontend"),
    FRONTEND_MODEL("frontend-model", "Modelo frontend"),
    DTO("dto", "DTO/contrato"),
    CONFIGURATION("configuration", "Configuración"),
    GUARD("guard", "Guard"),
    INTERCEPTOR("interceptor", "Interceptor"),
    MODULE("module", "Módulo"),
    DOMAIN_TYPE("domain-type", "Tipo de dominio"),
    UNKNOWN("unknown", "Sin clasificar");

    private final String id;
    private final String displayName;

    ParsedCodeTypeRole(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String id() {
        return id;
    }

    public String displayName() {
        return displayName;
    }
}
