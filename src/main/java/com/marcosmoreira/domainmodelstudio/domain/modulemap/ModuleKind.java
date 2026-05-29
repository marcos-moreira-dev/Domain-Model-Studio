package com.marcosmoreira.domainmodelstudio.domain.modulemap;

/** Tipo funcional de un módulo dentro del mapa administrativo. */
public enum ModuleKind {
    MAIN("Principal"),
    SUPPORT("Soporte"),
    REPORTING("Reportes"),
    CONFIGURATION("Configuración"),
    INTEGRATION("Integración"),
    SECURITY("Seguridad"),
    OTHER("Otro");

    private final String displayName;

    ModuleKind(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
