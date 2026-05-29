package com.marcosmoreira.domainmodelstudio.application.rolespermissions;

import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionDecision;
import java.util.ArrayList;
import java.util.HashSet;

/** Valida consistencia básica de roles, permisos y asignaciones. */
public final class ValidateRolesPermissionsUseCase {
    public RolesPermissionsValidationResult validate(RolesPermissionsDocument document) {
        ArrayList<String> warnings = new ArrayList<>();
        if (document.roles().isEmpty()) warnings.add("No hay roles definidos.");
        if (document.permissions().isEmpty()) warnings.add("No hay permisos definidos.");
        for (var role : document.roles()) {
            boolean assigned = document.assignments().stream().anyMatch(a -> a.roleId().equals(role.id()));
            if (!assigned) warnings.add("El rol '" + role.displayName() + "' no tiene permisos asignados.");
        }
        HashSet<String> assignedCells = new HashSet<>();
        for (var assignment : document.assignments()) {
            if (document.roleById(assignment.roleId()).isEmpty()) warnings.add("Asignación con rol inexistente: " + assignment.roleId());
            if (document.permissionById(assignment.permissionId()).isEmpty()) warnings.add("Asignación con permiso inexistente: " + assignment.permissionId());
            if (!assignedCells.add(assignment.roleId() + "::" + assignment.permissionId())) {
                warnings.add("Asignación duplicada para rol '" + assignment.roleId() + "' y permiso '" + assignment.permissionId() + "'.");
            }
            if (PermissionDecision.fromAssignment(assignment) == PermissionDecision.CONDITIONAL && assignment.condition().isBlank()) {
                warnings.add("Asignación condicionada sin condición explícita: " + assignment.id());
            }
        }
        return warnings.isEmpty() ? RolesPermissionsValidationResult.valid() : RolesPermissionsValidationResult.warnings(warnings);
    }
}
