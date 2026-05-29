package com.marcosmoreira.domainmodelstudio.domain.rolespermissions;

/** Permiso observable que habilita una acción, pantalla, módulo o reporte. */
public record PermissionNode(
        String id,
        String displayName,
        PermissionScope scope,
        String moduleName,
        String actionName,
        String description,
        String notes
) {
    public PermissionNode {
        id = required(id, "id");
        displayName = defaultText(displayName, id);
        scope = scope == null ? PermissionScope.ACTION : scope;
        moduleName = normalize(moduleName);
        actionName = normalize(actionName);
        description = normalize(description);
        notes = normalize(notes);
    }

    public PermissionNode withDetails(String displayName, PermissionScope scope, String moduleName,
                                      String actionName, String description, String notes) {
        return new PermissionNode(id, displayName, scope, moduleName, actionName, description, notes);
    }

    private static String required(String value, String label) {
        String normalized = normalize(value);
        if (normalized.isBlank()) throw new IllegalArgumentException("El " + label + " del permiso no puede estar vacío.");
        return normalized;
    }
    private static String defaultText(String value, String fallback) { String normalized = normalize(value); return normalized.isBlank()? fallback: normalized; }
    private static String normalize(String value) { return value == null ? "" : value.strip(); }
}
