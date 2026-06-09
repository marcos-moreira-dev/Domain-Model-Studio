package com.marcosmoreira.domainmodelstudio.application.rolespermissions;

import java.util.List;

/** Resultado de validación de roles y permisos. */
public record RolesPermissionsValidationResult(boolean ok, String summary, List<String> warnings) {
    public RolesPermissionsValidationResult { warnings = List.copyOf(warnings == null ? List.of() : warnings); }
    public static RolesPermissionsValidationResult valid() { return new RolesPermissionsValidationResult(true, "Roles y permisos sin advertencias.", List.of()); }
    public static RolesPermissionsValidationResult warnings(List<String> warnings) { return new RolesPermissionsValidationResult(false, warnings.size() + " advertencias en roles y permisos.", warnings); }
}
