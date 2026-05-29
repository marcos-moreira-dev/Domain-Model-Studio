package com.marcosmoreira.domainmodelstudio.domain.rolespermissions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Documento semántico de roles, permisos y asignaciones de una aplicación administrativa. */
public final class RolesPermissionsDocument {

    private final String projectName;
    private final String version;
    private final LocalDate documentDate;
    private final List<RoleNode> roles;
    private final List<PermissionNode> permissions;
    private final List<PermissionAssignment> assignments;

    public RolesPermissionsDocument(
            String projectName,
            String version,
            LocalDate documentDate,
            List<RoleNode> roles,
            List<PermissionNode> permissions,
            List<PermissionAssignment> assignments
    ) {
        this.projectName = textOrDefault(projectName, "Roles y permisos");
        this.version = textOrDefault(version, "0.1.0");
        this.documentDate = documentDate == null ? LocalDate.now() : documentDate;
        this.roles = List.copyOf(roles == null ? List.of() : roles);
        this.permissions = List.copyOf(permissions == null ? List.of() : permissions);
        this.assignments = List.copyOf(assignments == null ? List.of() : assignments);
    }

    public static RolesPermissionsDocument blank(String projectName) {
        return new RolesPermissionsDocument(projectName, "0.1.0", LocalDate.now(), List.of(), List.of(), List.of());
    }

    public String projectName() {
        return projectName;
    }

    public String version() {
        return version;
    }

    public LocalDate documentDate() {
        return documentDate;
    }

    public List<RoleNode> roles() {
        return roles;
    }

    public List<PermissionNode> permissions() {
        return permissions;
    }

    public List<PermissionAssignment> assignments() {
        return assignments;
    }

    public Optional<RoleNode> roleById(String id) {
        String normalized = normalize(id);
        return roles.stream().filter(role -> role.id().equals(normalized)).findFirst();
    }

    public Optional<PermissionNode> permissionById(String id) {
        String normalized = normalize(id);
        return permissions.stream().filter(permission -> permission.id().equals(normalized)).findFirst();
    }

    public Optional<PermissionAssignment> assignmentFor(String roleId, String permissionId) {
        String role = normalize(roleId);
        String permission = normalize(permissionId);
        return assignments.stream()
                .filter(assignment -> assignment.roleId().equals(role) && assignment.permissionId().equals(permission))
                .findFirst();
    }

    public List<RolesPermissionsMatrixCell> matrixCells() {
        Map<String, PermissionAssignment> indexed = new HashMap<>();
        for (PermissionAssignment assignment : assignments) {
            indexed.put(assignmentKey(assignment.roleId(), assignment.permissionId()), assignment);
        }

        ArrayList<RolesPermissionsMatrixCell> cells = new ArrayList<>();
        for (RoleNode role : roles) {
            for (PermissionNode permission : permissions) {
                PermissionAssignment assignment = indexed.get(assignmentKey(role.id(), permission.id()));
                cells.add(new RolesPermissionsMatrixCell(
                        role,
                        permission,
                        assignment,
                        PermissionDecision.fromAssignment(assignment)));
            }
        }
        return List.copyOf(cells);
    }

    public long assignedCellCount() {
        return matrixCells().stream()
                .filter(cell -> cell.decision() != PermissionDecision.NOT_ASSIGNED)
                .count();
    }

    public long conditionalAssignmentCount() {
        return assignments.stream()
                .filter(assignment -> assignment.decision() == PermissionDecision.CONDITIONAL)
                .count();
    }

    public RolesPermissionsDocument withRole(RoleNode role) {
        var list = new ArrayList<>(roles);
        list.add(role);
        return copy(list, permissions, assignments);
    }

    public RolesPermissionsDocument withPermission(PermissionNode permission) {
        var list = new ArrayList<>(permissions);
        list.add(permission);
        return copy(roles, list, assignments);
    }

    public RolesPermissionsDocument withAssignment(PermissionAssignment assignment) {
        var list = new ArrayList<>(assignments);
        list.add(assignment);
        return copy(roles, permissions, list);
    }

    public RolesPermissionsDocument withUpdatedRole(RoleNode role) {
        return copy(replaceRole(role), permissions, assignments);
    }

    public RolesPermissionsDocument withUpdatedPermission(PermissionNode permission) {
        return copy(roles, replacePermission(permission), assignments);
    }

    public RolesPermissionsDocument withUpdatedAssignment(PermissionAssignment assignment) {
        return copy(roles, permissions, replaceAssignment(assignment));
    }

    public RolesPermissionsDocument withoutRole(String id) {
        String normalized = normalize(id);
        return copy(
                roles.stream().filter(role -> !role.id().equals(normalized)).toList(),
                permissions,
                assignments.stream().filter(assignment -> !assignment.roleId().equals(normalized)).toList());
    }

    public RolesPermissionsDocument withoutPermission(String id) {
        String normalized = normalize(id);
        return copy(
                roles,
                permissions.stream().filter(permission -> !permission.id().equals(normalized)).toList(),
                assignments.stream().filter(assignment -> !assignment.permissionId().equals(normalized)).toList());
    }

    public RolesPermissionsDocument withoutAssignment(String id) {
        String normalized = normalize(id);
        return copy(
                roles,
                permissions,
                assignments.stream().filter(assignment -> !assignment.id().equals(normalized)).toList());
    }

    public String nextRoleId() {
        return nextId("role", roles.stream().map(RoleNode::id).toList());
    }

    public String nextPermissionId() {
        return nextId("perm", permissions.stream().map(PermissionNode::id).toList());
    }

    public String nextAssignmentId() {
        return nextId("assign", assignments.stream().map(PermissionAssignment::id).toList());
    }

    private RolesPermissionsDocument copy(
            List<RoleNode> roles,
            List<PermissionNode> permissions,
            List<PermissionAssignment> assignments
    ) {
        return new RolesPermissionsDocument(projectName, version, documentDate, roles, permissions, assignments);
    }

    private List<RoleNode> replaceRole(RoleNode role) {
        var output = new ArrayList<RoleNode>();
        boolean replaced = false;

        for (RoleNode current : roles) {
            if (current.id().equals(role.id())) {
                output.add(role);
                replaced = true;
            } else {
                output.add(current);
            }
        }

        if (!replaced) {
            throw new IllegalArgumentException("No existe rol: " + role.id());
        }
        return output;
    }

    private List<PermissionNode> replacePermission(PermissionNode permission) {
        var output = new ArrayList<PermissionNode>();
        boolean replaced = false;

        for (PermissionNode current : permissions) {
            if (current.id().equals(permission.id())) {
                output.add(permission);
                replaced = true;
            } else {
                output.add(current);
            }
        }

        if (!replaced) {
            throw new IllegalArgumentException("No existe permiso: " + permission.id());
        }
        return output;
    }

    private List<PermissionAssignment> replaceAssignment(PermissionAssignment assignment) {
        var output = new ArrayList<PermissionAssignment>();
        boolean replaced = false;

        for (PermissionAssignment current : assignments) {
            if (current.id().equals(assignment.id())) {
                output.add(assignment);
                replaced = true;
            } else {
                output.add(current);
            }
        }

        if (!replaced) {
            throw new IllegalArgumentException("No existe asignación: " + assignment.id());
        }
        return output;
    }

    private static String assignmentKey(String roleId, String permissionId) {
        return normalize(roleId) + "::" + normalize(permissionId);
    }

    private static String nextId(String prefix, List<String> existingIds) {
        int counter = existingIds.size() + 1;
        String id;
        do {
            id = prefix + "-" + counter++;
        } while (existingIds.contains(id));
        return id;
    }

    private static String textOrDefault(String value, String fallback) {
        String normalized = normalize(value);
        return normalized.isBlank() ? fallback : normalized;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
