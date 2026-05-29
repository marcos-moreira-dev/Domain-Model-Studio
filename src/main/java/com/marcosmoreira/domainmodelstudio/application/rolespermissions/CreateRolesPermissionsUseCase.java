package com.marcosmoreira.domainmodelstudio.application.rolespermissions;

import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;

/** Crea un documento vacío de roles y permisos. */
public final class CreateRolesPermissionsUseCase {
    public RolesPermissionsDocument createBlank(String projectName) { return RolesPermissionsDocument.blank(projectName); }
}
