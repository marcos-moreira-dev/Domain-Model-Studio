package com.marcosmoreira.domainmodelstudio.domain.rolespermissions;

/** Estado funcional de un rol dentro del sistema administrativo. */
public enum RoleStatus {
    PLANNED("Planificado"), ACTIVE("Activo"), RESTRICTED("Restringido"), DEPRECATED("En retiro");
    private final String displayName;
    RoleStatus(String displayName) { this.displayName = displayName; }
    public String displayName() { return displayName; }
}
