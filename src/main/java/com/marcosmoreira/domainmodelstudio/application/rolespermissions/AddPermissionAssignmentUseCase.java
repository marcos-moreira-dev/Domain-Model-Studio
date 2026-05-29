package com.marcosmoreira.domainmodelstudio.application.rolespermissions;

import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionAssignment;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;

/** Asigna un permiso a un rol. */
public final class AddPermissionAssignmentUseCase {
    public RolesPermissionsDocument add(RolesPermissionsDocument document, String roleId, String permissionId) {
        return document.withAssignment(new PermissionAssignment(document.nextAssignmentId(), roleId, permissionId, true, "", ""));
    }
}
