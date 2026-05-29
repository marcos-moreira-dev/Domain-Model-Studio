package com.marcosmoreira.domainmodelstudio.domain.rolespermissions;

/** Alcance funcional de un permiso. */
public enum PermissionScope {
    MODULE("Módulo"), SCREEN("Pantalla"), ACTION("Acción"), REPORT("Reporte"), SYSTEM("Sistema"), OTHER("Otro");
    private final String displayName;
    PermissionScope(String displayName) { this.displayName = displayName; }
    public String displayName() { return displayName; }
}
