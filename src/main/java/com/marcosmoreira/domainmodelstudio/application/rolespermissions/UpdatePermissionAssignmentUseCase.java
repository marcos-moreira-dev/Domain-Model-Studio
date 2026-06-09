package com.marcosmoreira.domainmodelstudio.application.rolespermissions;

import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionAssignment;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;

/** Actualiza una asignación rol-permiso. */
public final class UpdatePermissionAssignmentUseCase {
    public RolesPermissionsDocument update(RolesPermissionsDocument document, String id, String roleId, String permissionId,
                                           boolean allowed, String condition, String notes) {
        return document.withUpdatedAssignment(new PermissionAssignment(id, roleId, permissionId, allowed, condition, notes));
    }
}
