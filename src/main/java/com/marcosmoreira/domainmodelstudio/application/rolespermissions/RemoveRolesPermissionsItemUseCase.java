package com.marcosmoreira.domainmodelstudio.application.rolespermissions;

import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;

/** Elimina roles, permisos o asignaciones. */
public final class RemoveRolesPermissionsItemUseCase {
    public RolesPermissionsDocument removeRole(RolesPermissionsDocument document, String id) { return document.withoutRole(id); }
    public RolesPermissionsDocument removePermission(RolesPermissionsDocument document, String id) { return document.withoutPermission(id); }
    public RolesPermissionsDocument removeAssignment(RolesPermissionsDocument document, String id) { return document.withoutAssignment(id); }
}
