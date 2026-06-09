package com.marcosmoreira.domainmodelstudio.application.rolespermissions;

import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionScope;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;

/** Actualiza un permiso. */
public final class UpdatePermissionUseCase {
    public RolesPermissionsDocument update(RolesPermissionsDocument document, String id, String displayName,
                                           PermissionScope scope, String moduleName, String actionName,
                                           String description, String notes) {
        return document.withUpdatedPermission(new PermissionNode(id, displayName, scope, moduleName, actionName, description, notes));
    }
}
