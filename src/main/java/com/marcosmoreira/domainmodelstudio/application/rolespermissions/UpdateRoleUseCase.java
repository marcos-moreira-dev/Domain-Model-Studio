package com.marcosmoreira.domainmodelstudio.application.rolespermissions;

import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RoleNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RoleStatus;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;

/** Actualiza un rol. */
public final class UpdateRoleUseCase {
    public RolesPermissionsDocument update(RolesPermissionsDocument document, String id, String displayName,
                                           RoleStatus status, String responsibility, String description, String notes) {
        return document.withUpdatedRole(new RoleNode(id, displayName, status, responsibility, description, notes));
    }
}
