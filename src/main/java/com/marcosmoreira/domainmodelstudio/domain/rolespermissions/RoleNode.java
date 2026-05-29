package com.marcosmoreira.domainmodelstudio.domain.rolespermissions;

/** Rol operativo o administrativo dentro de la aplicación. */
public record RoleNode(
        String id,
        String displayName,
        RoleStatus status,
        String responsibility,
        String description,
        String notes
) {
    public RoleNode {
        id = required(id, "id");
        displayName = defaultText(displayName, id);
        status = status == null ? RoleStatus.PLANNED : status;
        responsibility = normalize(responsibility);
        description = normalize(description);
        notes = normalize(notes);
    }

    public RoleNode withDetails(String displayName, RoleStatus status, String responsibility, String description, String notes) {
        return new RoleNode(id, displayName, status, responsibility, description, notes);
    }

    private static String required(String value, String label) {
        String normalized = normalize(value);
        if (normalized.isBlank()) throw new IllegalArgumentException("El " + label + " del rol no puede estar vacío.");
        return normalized;
    }
    private static String defaultText(String value, String fallback) { String normalized = normalize(value); return normalized.isBlank()? fallback: normalized; }
    private static String normalize(String value) { return value == null ? "" : value.strip(); }
}
