package com.marcosmoreira.domainmodelstudio.domain.modulemap;

/** Estado de planificación de un módulo funcional. */
public enum ModuleStatus {
    PLANNED("Planificado"),
    IN_PROGRESS("En curso"),
    ACTIVE("Activo"),
    DEFERRED("Postergado"),
    OUT_OF_SCOPE("Fuera de alcance");

    private final String displayName;

    ModuleStatus(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
