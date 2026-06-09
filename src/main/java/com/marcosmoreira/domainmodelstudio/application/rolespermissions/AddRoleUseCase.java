package com.marcosmoreira.domainmodelstudio.application.rolespermissions;

import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RoleNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;

/** Agrega un rol operativo al documento. */
public final class AddRoleUseCase {
    public RolesPermissionsDocument add(RolesPermissionsDocument document, String displayName) {
        return document.withRole(new RoleNode(document.nextRoleId(), displayName, null, "", "", ""));
    }
}
