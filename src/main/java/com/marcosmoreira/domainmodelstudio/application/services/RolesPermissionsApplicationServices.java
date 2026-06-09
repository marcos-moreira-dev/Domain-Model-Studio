package com.marcosmoreira.domainmodelstudio.application.services;

import com.marcosmoreira.domainmodelstudio.application.rolespermissions.AddPermissionAssignmentUseCase;
import com.marcosmoreira.domainmodelstudio.application.rolespermissions.AddPermissionUseCase;
import com.marcosmoreira.domainmodelstudio.application.rolespermissions.AddRoleUseCase;
import com.marcosmoreira.domainmodelstudio.application.rolespermissions.CreateRolesPermissionsUseCase;
import com.marcosmoreira.domainmodelstudio.application.rolespermissions.RemoveRolesPermissionsItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.rolespermissions.UpdatePermissionAssignmentUseCase;
import com.marcosmoreira.domainmodelstudio.application.rolespermissions.UpdatePermissionUseCase;
import com.marcosmoreira.domainmodelstudio.application.rolespermissions.UpdateRoleUseCase;
import com.marcosmoreira.domainmodelstudio.application.rolespermissions.ValidateRolesPermissionsUseCase;
import java.util.Objects;

/** Fachada de edición y validación de Roles y permisos.
 *
 * <p>Mantiene la matriz documental aislada de diagramas visuales y de código fuente.</p> */
public final class RolesPermissionsApplicationServices {

    private final CreateRolesPermissionsUseCase createRolesPermissionsUseCase;
    private final AddRoleUseCase addRoleUseCase;
    private final AddPermissionUseCase addPermissionUseCase;
    private final AddPermissionAssignmentUseCase addPermissionAssignmentUseCase;
    private final UpdateRoleUseCase updateRoleUseCase;
    private final UpdatePermissionUseCase updatePermissionUseCase;
    private final UpdatePermissionAssignmentUseCase updatePermissionAssignmentUseCase;
    private final RemoveRolesPermissionsItemUseCase removeRolesPermissionsItemUseCase;
    private final ValidateRolesPermissionsUseCase validateRolesPermissionsUseCase;

    public RolesPermissionsApplicationServices(
            CreateRolesPermissionsUseCase createRolesPermissionsUseCase,
            AddRoleUseCase addRoleUseCase,
            AddPermissionUseCase addPermissionUseCase,
            AddPermissionAssignmentUseCase addPermissionAssignmentUseCase,
            UpdateRoleUseCase updateRoleUseCase,
            UpdatePermissionUseCase updatePermissionUseCase,
            UpdatePermissionAssignmentUseCase updatePermissionAssignmentUseCase,
            RemoveRolesPermissionsItemUseCase removeRolesPermissionsItemUseCase,
            ValidateRolesPermissionsUseCase validateRolesPermissionsUseCase
    ) {
        this.createRolesPermissionsUseCase = Objects.requireNonNull(createRolesPermissionsUseCase, "createRolesPermissionsUseCase");
        this.addRoleUseCase = Objects.requireNonNull(addRoleUseCase, "addRoleUseCase");
        this.addPermissionUseCase = Objects.requireNonNull(addPermissionUseCase, "addPermissionUseCase");
        this.addPermissionAssignmentUseCase = Objects.requireNonNull(addPermissionAssignmentUseCase, "addPermissionAssignmentUseCase");
        this.updateRoleUseCase = Objects.requireNonNull(updateRoleUseCase, "updateRoleUseCase");
        this.updatePermissionUseCase = Objects.requireNonNull(updatePermissionUseCase, "updatePermissionUseCase");
        this.updatePermissionAssignmentUseCase = Objects.requireNonNull(updatePermissionAssignmentUseCase, "updatePermissionAssignmentUseCase");
        this.removeRolesPermissionsItemUseCase = Objects.requireNonNull(removeRolesPermissionsItemUseCase, "removeRolesPermissionsItemUseCase");
        this.validateRolesPermissionsUseCase = Objects.requireNonNull(validateRolesPermissionsUseCase, "validateRolesPermissionsUseCase");
    }

    public CreateRolesPermissionsUseCase createRolesPermissionsUseCase() {
        return createRolesPermissionsUseCase;
    }

    public AddRoleUseCase addRoleUseCase() {
        return addRoleUseCase;
    }

    public AddPermissionUseCase addPermissionUseCase() {
        return addPermissionUseCase;
    }

    public AddPermissionAssignmentUseCase addPermissionAssignmentUseCase() {
        return addPermissionAssignmentUseCase;
    }

    public UpdateRoleUseCase updateRoleUseCase() {
        return updateRoleUseCase;
    }

    public UpdatePermissionUseCase updatePermissionUseCase() {
        return updatePermissionUseCase;
    }

    public UpdatePermissionAssignmentUseCase updatePermissionAssignmentUseCase() {
        return updatePermissionAssignmentUseCase;
    }

    public RemoveRolesPermissionsItemUseCase removeRolesPermissionsItemUseCase() {
        return removeRolesPermissionsItemUseCase;
    }

    public ValidateRolesPermissionsUseCase validateRolesPermissionsUseCase() {
        return validateRolesPermissionsUseCase;
    }

}
