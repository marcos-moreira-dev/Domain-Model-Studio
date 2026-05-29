package com.marcosmoreira.domainmodelstudio.domain.rolespermissions;

import java.util.Objects;
import java.util.Optional;

/** Celda semántica de la matriz rol × permiso. */
public record RolesPermissionsMatrixCell(
        RoleNode role,
        PermissionNode permission,
        PermissionAssignment assignment,
        PermissionDecision decision
) {
    public RolesPermissionsMatrixCell {
        Objects.requireNonNull(role, "role");
        Objects.requireNonNull(permission, "permission");
        decision = decision == null ? PermissionDecision.fromAssignment(assignment) : decision;
    }

    public Optional<PermissionAssignment> assignmentOptional() {
        return Optional.ofNullable(assignment);
    }

    public String detailText() {
        if (assignment == null) {
            return "Sin asignación explícita.";
        }
        if (!assignment.condition().isBlank()) {
            return assignment.condition();
        }
        if (!assignment.notes().isBlank()) {
            return assignment.notes();
        }
        return decision.displayName();
    }
}
