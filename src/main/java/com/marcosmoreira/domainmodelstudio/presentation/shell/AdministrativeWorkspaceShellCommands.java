package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponentKind;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreenTemplateKind;
import com.marcosmoreira.domainmodelstudio.presentation.rolespermissions.RolesPermissionsViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.screenflow.ScreenFlowViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.wireframe.WireframeViewModel;
import java.util.Objects;

/** Comandos de workspaces administrativos no conceptuales: roles, pantallas y wireframes. */
final class AdministrativeWorkspaceShellCommands {

    private final RolesPermissionsViewModel rolesPermissionsViewModel;
    private final ScreenFlowViewModel screenFlowViewModel;
    private final WireframeViewModel wireframeViewModel;
    private final ProjectValidationCoordinator validationCoordinator;
    private final SpecializedProjectSynchronizer projectSynchronizer;
    private final EditorActivationGuard activationGuard;

    AdministrativeWorkspaceShellCommands(
            RolesPermissionsViewModel rolesPermissionsViewModel,
            ScreenFlowViewModel screenFlowViewModel,
            WireframeViewModel wireframeViewModel,
            ProjectValidationCoordinator validationCoordinator,
            SpecializedProjectSynchronizer projectSynchronizer,
            EditorActivationGuard activationGuard
    ) {
        this.rolesPermissionsViewModel = Objects.requireNonNull(rolesPermissionsViewModel, "rolesPermissionsViewModel");
        this.screenFlowViewModel = Objects.requireNonNull(screenFlowViewModel, "screenFlowViewModel");
        this.wireframeViewModel = Objects.requireNonNull(wireframeViewModel, "wireframeViewModel");
        this.validationCoordinator = Objects.requireNonNull(validationCoordinator, "validationCoordinator");
        this.projectSynchronizer = Objects.requireNonNull(projectSynchronizer, "projectSynchronizer");
        this.activationGuard = Objects.requireNonNull(activationGuard, "activationGuard");
    }

    void synchronizeRolesPermissionsEdit(DiagramProject updatedProject) {
        projectSynchronizer.synchronize(updatedProject, "Roles y permisos actualizado");
    }

    void synchronizeScreenFlowEdit(DiagramProject updatedProject) {
        projectSynchronizer.synchronize(updatedProject, "Flujo de pantallas actualizado");
    }

    void synchronizeWireframeEdit(DiagramProject updatedProject) {
        projectSynchronizer.synchronize(updatedProject, "Wireframes actualizados");
    }

    void requestAddRole() {
        if (!ensureRolesPermissionsActive("agregar roles")) {
            return;
        }
        rolesPermissionsViewModel.addRole();
    }

    void requestAddPermission() {
        if (!ensureRolesPermissionsActive("agregar permisos")) {
            return;
        }
        rolesPermissionsViewModel.addPermission();
    }

    void requestAddPermissionAssignment() {
        if (!ensureRolesPermissionsActive("asignar permisos")) {
            return;
        }
        rolesPermissionsViewModel.addAssignment();
    }

    void requestRemoveRolesPermissionsItem() {
        if (!ensureRolesPermissionsActive("eliminar elementos")) {
            return;
        }
        rolesPermissionsViewModel.removeSelected();
    }

    void requestValidateRolesPermissions() {
        validationCoordinator.validateRolesPermissions();
    }

    void requestAddScreen() {
        if (!ensureScreenFlowActive("agregar pantallas")) {
            return;
        }
        screenFlowViewModel.addScreen();
    }

    void requestAddScreenTransition() {
        if (!ensureScreenFlowActive("agregar transiciones")) {
            return;
        }
        screenFlowViewModel.addTransition();
    }

    void requestRemoveScreenFlowItem() {
        if (!ensureScreenFlowActive("eliminar elementos")) {
            return;
        }
        screenFlowViewModel.removeSelected();
    }

    void requestValidateScreenFlow() {
        validationCoordinator.validateScreenFlow();
    }

    void requestAddWireframeScreen() {
        if (!ensureWireframeActive("agregar pantallas")) {
            return;
        }
        wireframeViewModel.addScreen();
    }

    void requestAddWireframeSection() {
        requestAddWireframeComponent(WireframeComponentKind.SECTION);
    }

    void requestAddWireframeForm() {
        requestAddWireframeComponent(WireframeComponentKind.FORM);
    }

    void requestAddWireframeTable() {
        requestAddWireframeComponent(WireframeComponentKind.TABLE);
    }

    void requestAddWireframeField() {
        requestAddWireframeComponent(WireframeComponentKind.FIELD);
    }

    void requestAddWireframeButton() {
        requestAddWireframeComponent(WireframeComponentKind.BUTTON);
    }

    void requestRemoveWireframeItem() {
        if (!ensureWireframeActive("eliminar elementos")) {
            return;
        }
        wireframeViewModel.removeSelected();
    }

    void requestValidateWireframe() {
        validationCoordinator.validateWireframe();
    }

    void requestApplyWireframeTemplate(WireframeScreenTemplateKind templateKind) {
        if (!ensureWireframeActive("insertar plantillas")) {
            return;
        }
        wireframeViewModel.applyTemplate(templateKind);
    }

    private void requestAddWireframeComponent(WireframeComponentKind kind) {
        if (!ensureWireframeActive("agregar componentes")) {
            return;
        }
        wireframeViewModel.addComponent(kind);
    }

    private boolean ensureRolesPermissionsActive(String action) {
        return activationGuard.ensureActive(rolesPermissionsViewModel.active(), "Roles y permisos", action);
    }

    private boolean ensureScreenFlowActive(String action) {
        return activationGuard.ensureActive(screenFlowViewModel.active(), "Flujo de pantallas", action);
    }

    private boolean ensureWireframeActive(String action) {
        return activationGuard.ensureActive(wireframeViewModel.active(), "Wireframes", action);
    }
}
