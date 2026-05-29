package com.marcosmoreira.domainmodelstudio.application.rolespermissions;

import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;

/** Agrega un permiso funcional al documento. */
public final class AddPermissionUseCase {
    public RolesPermissionsDocument add(RolesPermissionsDocument document, String displayName) {
        return document.withPermission(new PermissionNode(document.nextPermissionId(), displayName, null, "", "", "", ""));
    }
}
