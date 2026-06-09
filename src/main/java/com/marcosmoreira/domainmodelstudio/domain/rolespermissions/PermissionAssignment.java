package com.marcosmoreira.domainmodelstudio.domain.rolespermissions;

/** Asignación explícita de un permiso a un rol. */
public record PermissionAssignment(
        String id,
        String roleId,
        String permissionId,
        boolean allowed,
        String condition,
        String notes
) {
    public PermissionAssignment {
        id = required(id, "id");
        roleId = required(roleId, "rol");
        permissionId = required(permissionId, "permiso");
        condition = normalize(condition);
        notes = normalize(notes);
    }

    public PermissionAssignment withDetails(String roleId, String permissionId, boolean allowed, String condition, String notes) {
        return new PermissionAssignment(id, roleId, permissionId, allowed, condition, notes);
    }

    public PermissionDecision decision() {
        return PermissionDecision.fromAssignment(this);
    }

    private static String required(String value, String label) {
        String normalized = normalize(value);
        if (normalized.isBlank()) throw new IllegalArgumentException("El campo " + label + " de la asignación no puede estar vacío.");
        return normalized;
    }
    private static String normalize(String value) { return value == null ? "" : value.strip(); }
}
