package com.marcosmoreira.domainmodelstudio.presentation.rolespermissions;

import com.marcosmoreira.domainmodelstudio.application.rolespermissions.AddPermissionAssignmentUseCase;
import com.marcosmoreira.domainmodelstudio.application.rolespermissions.AddPermissionUseCase;
import com.marcosmoreira.domainmodelstudio.application.rolespermissions.AddRoleUseCase;
import com.marcosmoreira.domainmodelstudio.application.rolespermissions.RemoveRolesPermissionsItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.rolespermissions.RolesPermissionsValidationResult;
import com.marcosmoreira.domainmodelstudio.application.rolespermissions.UpdatePermissionAssignmentUseCase;
import com.marcosmoreira.domainmodelstudio.application.rolespermissions.UpdatePermissionUseCase;
import com.marcosmoreira.domainmodelstudio.application.rolespermissions.UpdateRoleUseCase;
import com.marcosmoreira.domainmodelstudio.application.rolespermissions.ValidateRolesPermissionsUseCase;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.ProjectChangeSupport;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionAssignment;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionScope;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RoleNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RoleStatus;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportPngAction;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** ViewModel del editor de matriz de roles y permisos. */
public final class RolesPermissionsViewModel {

    private final AddRoleUseCase addRoleUseCase;
    private final AddPermissionUseCase addPermissionUseCase;
    private final AddPermissionAssignmentUseCase addAssignmentUseCase;
    private final UpdateRoleUseCase updateRoleUseCase;
    private final UpdatePermissionUseCase updatePermissionUseCase;
    private final UpdatePermissionAssignmentUseCase updateAssignmentUseCase;
    private final RemoveRolesPermissionsItemUseCase removeItemUseCase;
    private final ValidateRolesPermissionsUseCase validateUseCase;
    private final Consumer<String> statusConsumer;

    private final ObservableList<RoleNode> roles = FXCollections.observableArrayList();
    private final ObservableList<PermissionNode> permissions = FXCollections.observableArrayList();
    private final ObservableList<PermissionAssignment> assignments = FXCollections.observableArrayList();
    private final ObjectProperty<RoleNode> selectedRole = new SimpleObjectProperty<>();
    private final ObjectProperty<PermissionNode> selectedPermission = new SimpleObjectProperty<>();
    private final ObjectProperty<PermissionAssignment> selectedAssignment = new SimpleObjectProperty<>();

    private DiagramProject currentProject;
    private RolesPermissionsDocument currentDocument;
    private final ProjectChangeSupport projectChangeSupport = new ProjectChangeSupport();
    private ExportPngAction pngExportAction = targetFile -> {
        throw new IllegalStateException("No hay matriz de roles y permisos lista para exportar PNG.");
    };

    public RolesPermissionsViewModel(
            AddRoleUseCase addRoleUseCase,
            AddPermissionUseCase addPermissionUseCase,
            AddPermissionAssignmentUseCase addAssignmentUseCase,
            UpdateRoleUseCase updateRoleUseCase,
            UpdatePermissionUseCase updatePermissionUseCase,
            UpdatePermissionAssignmentUseCase updateAssignmentUseCase,
            RemoveRolesPermissionsItemUseCase removeItemUseCase,
            ValidateRolesPermissionsUseCase validateUseCase,
            Consumer<String> statusConsumer
    ) {
        this.addRoleUseCase = Objects.requireNonNull(addRoleUseCase, "addRoleUseCase");
        this.addPermissionUseCase = Objects.requireNonNull(addPermissionUseCase, "addPermissionUseCase");
        this.addAssignmentUseCase = Objects.requireNonNull(addAssignmentUseCase, "addAssignmentUseCase");
        this.updateRoleUseCase = Objects.requireNonNull(updateRoleUseCase, "updateRoleUseCase");
        this.updatePermissionUseCase = Objects.requireNonNull(updatePermissionUseCase, "updatePermissionUseCase");
        this.updateAssignmentUseCase = Objects.requireNonNull(updateAssignmentUseCase, "updateAssignmentUseCase");
        this.removeItemUseCase = Objects.requireNonNull(removeItemUseCase, "removeItemUseCase");
        this.validateUseCase = Objects.requireNonNull(validateUseCase, "validateUseCase");
        this.statusConsumer = Objects.requireNonNull(statusConsumer, "statusConsumer");
    }

    public void registerProjectChangeListener(Consumer<DiagramProject> listener) {
        projectChangeSupport.registerProjectChangeListener(listener);
    }

    public void registerPngExportAction(ExportPngAction action) {
        this.pngExportAction = action == null ? this.pngExportAction : action;
    }

    public void exportVisualAsPng(Path targetFile) throws IOException {
        pngExportAction.export(targetFile);
    }

    public ObservableList<RoleNode> roles() {
        return roles;
    }

    public ObservableList<PermissionNode> permissions() {
        return permissions;
    }

    public ObservableList<PermissionAssignment> assignments() {
        return assignments;
    }

    public ObjectProperty<RoleNode> selectedRoleProperty() {
        return selectedRole;
    }

    public ObjectProperty<PermissionNode> selectedPermissionProperty() {
        return selectedPermission;
    }

    public ObjectProperty<PermissionAssignment> selectedAssignmentProperty() {
        return selectedAssignment;
    }

    public RolesPermissionsDocument currentDocument() {
        return currentDocument;
    }

    public DiagramProject currentProject() {
        return currentProject;
    }

    public boolean active() {
        return currentProject != null
                && currentProject.metadata().diagramTypeId().equals(DiagramTypeId.ROLES_PERMISSIONS_MAP);
    }

    public void loadProject(DiagramProject project) {
        projectChangeSupport.runLoading(() -> {
            currentProject = Objects.requireNonNull(project, "project");
            currentDocument = project.rolesPermissions()
                    .orElseGet(() -> RolesPermissionsDocument.blank(project.metadata().title()));
            refreshLists();
            selectedRole.set(firstOrNull(roles));
            selectedPermission.set(firstOrNull(permissions));
            selectedAssignment.set(firstOrNull(assignments));
        });
    }

    public void clear() {
        projectChangeSupport.runLoading(() -> {
            currentProject = null;
            currentDocument = null;
            roles.clear();
            permissions.clear();
            assignments.clear();
            selectedRole.set(null);
            selectedPermission.set(null);
            selectedAssignment.set(null);
        });
    }

    public void addRole() {
        if (!ensureDocument("No hay matriz de roles y permisos abierta para agregar rol.")) {
            return;
        }
        applyDocument(addRoleUseCase.add(currentDocument, "Rol"), "Rol agregado.");
        selectedRole.set(lastOrNull(roles));
    }

    public void addPermission() {
        if (!ensureDocument("No hay matriz de roles y permisos abierta para agregar permiso.")) {
            return;
        }
        applyDocument(addPermissionUseCase.add(currentDocument, "Permiso"), "Permiso agregado.");
        selectedPermission.set(lastOrNull(permissions));
    }

    public void addAssignment() {
        if (!ensureDocument("No hay matriz de roles y permisos abierta para agregar asignación.")) {
            return;
        }
        if (roles.isEmpty() || permissions.isEmpty()) {
            statusConsumer.accept("Crea al menos un rol y un permiso antes de asignar.");
            return;
        }
        RoleNode role = selectedRole.get() == null ? roles.get(0) : selectedRole.get();
        PermissionNode permission = selectedPermission.get() == null ? permissions.get(0) : selectedPermission.get();
        applyDocument(addAssignmentUseCase.add(currentDocument, role.id(), permission.id()), "Permiso asignado a rol.");
        selectedAssignment.set(lastOrNull(assignments));
    }

    public void removeSelected() {
        if (!ensureDocument("No hay matriz de roles y permisos abierta para eliminar.")) {
            return;
        }
        if (selectedAssignment.get() != null) {
            applyDocument(removeItemUseCase.removeAssignment(currentDocument, selectedAssignment.get().id()), "Asignación eliminada.");
            selectedAssignment.set(null);
            return;
        }
        if (selectedPermission.get() != null) {
            applyDocument(removeItemUseCase.removePermission(currentDocument, selectedPermission.get().id()), "Permiso eliminado.");
            selectedPermission.set(firstOrNull(permissions));
            return;
        }
        if (selectedRole.get() != null) {
            applyDocument(removeItemUseCase.removeRole(currentDocument, selectedRole.get().id()), "Rol eliminado.");
            selectedRole.set(firstOrNull(roles));
            return;
        }
        statusConsumer.accept("Selecciona un rol, permiso o asignación para eliminar.");
    }

    public void applyRoleChanges(String name, RoleStatus status, String responsibility, String description, String notes) {
        if (selectedRole.get() == null || !ensureDocument("No hay rol seleccionado.")) {
            return;
        }
        String id = selectedRole.get().id();
        applyDocument(updateRoleUseCase.update(currentDocument, id, name, status, responsibility, description, notes),
                "Rol actualizado.");
        selectRole(id);
    }

    public void applyPermissionChanges(
            String name,
            PermissionScope scope,
            String module,
            String action,
            String description,
            String notes
    ) {
        if (selectedPermission.get() == null || !ensureDocument("No hay permiso seleccionado.")) {
            return;
        }
        String id = selectedPermission.get().id();
        applyDocument(updatePermissionUseCase.update(currentDocument, id, name, scope, module, action, description, notes),
                "Permiso actualizado.");
        selectPermission(id);
    }

    public void applyAssignmentChanges(RoleNode role, PermissionNode permission, boolean allowed, String condition, String notes) {
        if (selectedAssignment.get() == null || role == null || permission == null
                || !ensureDocument("No hay asignación seleccionada.")) {
            return;
        }
        String id = selectedAssignment.get().id();
        applyDocument(updateAssignmentUseCase.update(currentDocument, id, role.id(), permission.id(), allowed, condition, notes),
                "Asignación actualizada.");
        selectAssignment(id);
    }

    public RolesPermissionsValidationResult validateDocument() {
        if (!ensureDocument("No hay matriz de roles y permisos abierta para validar.")) {
            return RolesPermissionsValidationResult.warnings(List.of("No hay matriz de roles y permisos abierta."));
        }
        RolesPermissionsValidationResult result = validateUseCase.validate(currentDocument);
        statusConsumer.accept(result.summary());
        return result;
    }

    private boolean ensureDocument(String message) {
        if (currentDocument == null) {
            statusConsumer.accept(message);
            return false;
        }
        return true;
    }

    private void applyDocument(RolesPermissionsDocument document, String status) {
        currentDocument = document;
        refreshLists();
        if (currentProject != null) {
            currentProject = currentProject.withRolesPermissions(document);
            projectChangeSupport.notifyChanged(currentProject);
        }
        statusConsumer.accept(status);
    }

    private void refreshLists() {
        roles.setAll(currentDocument == null ? List.of() : currentDocument.roles());
        permissions.setAll(currentDocument == null ? List.of() : currentDocument.permissions());
        assignments.setAll(currentDocument == null ? List.of() : currentDocument.assignments());
    }

    private void selectRole(String id) {
        selectedRole.set(roles.stream().filter(role -> role.id().equals(id)).findFirst().orElse(null));
    }

    private void selectPermission(String id) {
        selectedPermission.set(permissions.stream().filter(permission -> permission.id().equals(id)).findFirst().orElse(null));
    }

    private void selectAssignment(String id) {
        selectedAssignment.set(assignments.stream().filter(assignment -> assignment.id().equals(id)).findFirst().orElse(null));
    }

    private static <T> T firstOrNull(ObservableList<T> items) {
        return items.isEmpty() ? null : items.get(0);
    }

    private static <T> T lastOrNull(ObservableList<T> items) {
        return items.isEmpty() ? null : items.get(items.size() - 1);
    }
}
