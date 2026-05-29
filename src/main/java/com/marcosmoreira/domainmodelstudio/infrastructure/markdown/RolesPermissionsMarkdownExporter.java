package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.export.MarkdownDiagramExporter;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionAssignment;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionDecision;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RoleNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/** Exporta roles y permisos como matriz/documento administrativo importable. */
public final class RolesPermissionsMarkdownExporter implements MarkdownDiagramExporter {

    @Override
    public String export(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        RolesPermissionsDocument document = project.rolesPermissions()
                .orElseThrow(() -> new IllegalArgumentException("El proyecto no contiene roles y permisos."));
        StringBuilder markdown = new StringBuilder();
        appendFrontMatter(markdown, document);
        appendIntroduction(markdown, document);
        appendRoles(markdown, document);
        appendPermissions(markdown, document);
        appendAssignments(markdown, document);
        appendMatrix(markdown, document);
        return markdown.toString();
    }

    private static void appendFrontMatter(StringBuilder markdown, RolesPermissionsDocument document) {
        markdown.append("---\n");
        markdown.append("dms_version: \"1\"\n");
        markdown.append("diagram_type: \"roles-permissions-map\"\n");
        markdown.append("name: \"").append(escape(document.projectName())).append("\"\n");
        markdown.append("version: \"").append(escape(document.version())).append("\"\n");
        markdown.append("sample_kind: \"exported\"\n");
        markdown.append("status: \"importable\"\n");
        markdown.append("importable: true\n");
        markdown.append("intended_output: \"matriz/documento administrativo\"\n");
        markdown.append("---\n\n");
    }

    private static void appendIntroduction(StringBuilder markdown, RolesPermissionsDocument document) {
        markdown.append("# Matriz de roles y permisos\n\n");
        markdown.append("Este documento organiza roles, permisos y asignaciones como una matriz administrativa. ")
                .append("No representa un canvas libre ni un grafo de nodos; su objetivo es revisar responsabilidades, ")
                .append("acciones permitidas, restricciones y permisos críticos por rol.\n\n");
        markdown.append("## Resumen\n\n");
        markdown.append("| Indicador | Valor |\n");
        markdown.append("|---|---:|\n");
        markdown.append("| Roles | ").append(document.roles().size()).append(" |\n");
        markdown.append("| Permisos | ").append(document.permissions().size()).append(" |\n");
        markdown.append("| Asignaciones explícitas | ").append(document.assignments().size()).append(" |\n");
        markdown.append("| Asignaciones condicionadas | ").append(document.conditionalAssignmentCount()).append(" |\n\n");
    }

    private static void appendRoles(StringBuilder markdown, RolesPermissionsDocument document) {
        markdown.append("# Roles\n\n");
        if (document.roles().isEmpty()) {
            markdown.append("Sin roles documentados todavía.\n\n");
            return;
        }
        for (RoleNode role : document.roles()) {
            markdown.append("## ").append(role.displayName()).append("\n");
            markdown.append("id: ").append(role.id()).append("\n");
            markdown.append("estado: ").append(role.status().displayName()).append("\n");
            if (!role.responsibility().isBlank()) {
                markdown.append("responsabilidad: ").append(role.responsibility()).append("\n");
            }
            if (!role.description().isBlank()) {
                markdown.append("descripción: ").append(role.description()).append("\n");
            }
            if (!role.notes().isBlank()) {
                markdown.append("notas: ").append(role.notes()).append("\n");
            }
            markdown.append("\n");
        }
    }

    private static void appendPermissions(StringBuilder markdown, RolesPermissionsDocument document) {
        markdown.append("# Permisos\n\n");
        if (document.permissions().isEmpty()) {
            markdown.append("Sin permisos documentados todavía.\n\n");
            return;
        }
        markdown.append("| Permiso | Módulo | Acción | Alcance | Descripción |\n");
        markdown.append("|---|---|---|---|---|\n");
        document.permissions().stream()
                .sorted(Comparator.comparing(PermissionNode::moduleName).thenComparing(PermissionNode::displayName))
                .forEach(permission -> markdown.append("| ")
                        .append(permission.displayName()).append(" | ")
                        .append(optional(permission.moduleName())).append(" | ")
                        .append(optional(permission.actionName())).append(" | ")
                        .append(permission.scope().displayName()).append(" | ")
                        .append(optional(permission.description())).append(" |\n"));
        markdown.append("\n");
        markdown.append("<!-- Sección importable de permisos. Mantener una línea por permiso. -->\n");
        for (PermissionNode permission : document.permissions()) {
            String description = permission.description().isBlank()
                    ? permission.scope().displayName() + " · " + optional(permission.moduleName())
                    : permission.description();
            markdown.append("- ").append(permission.id()).append(": ").append(description).append("\n");
        }
        markdown.append("\n");
    }

    private static void appendAssignments(StringBuilder markdown, RolesPermissionsDocument document) {
        markdown.append("# Asignaciones\n\n");
        markdown.append("| Rol | Permiso | Decisión | Condición | Observación |\n");
        markdown.append("|---|---|---|---|---|\n");
        for (PermissionAssignment assignment : document.assignments()) {
            PermissionDecision decision = assignment.decision();
            markdown.append("| ").append(assignment.roleId())
                    .append(" | ").append(assignment.permissionId())
                    .append(" | ").append(decision.displayName())
                    .append(" | ").append(optional(assignment.condition()))
                    .append(" | ").append(optional(assignment.notes()))
                    .append(" |\n");
        }
        markdown.append("\n");
    }

    private static void appendMatrix(StringBuilder markdown, RolesPermissionsDocument document) {
        markdown.append("# Matriz\n\n");
        if (document.roles().isEmpty() || document.permissions().isEmpty()) {
            markdown.append("La matriz requiere al menos un rol y un permiso.\n");
            return;
        }
        Map<String, PermissionAssignment> assignments = document.assignments().stream()
                .collect(Collectors.toMap(
                        assignment -> key(assignment.roleId(), assignment.permissionId()),
                        assignment -> assignment,
                        (left, right) -> right));
        markdown.append("| Permiso / Rol ");
        for (RoleNode role : document.roles()) {
            markdown.append("| ").append(role.displayName()).append(' ');
        }
        markdown.append("|\n|---");
        for (int i = 0; i < document.roles().size(); i++) {
            markdown.append("|:---:");
        }
        markdown.append("|\n");
        for (PermissionNode permission : document.permissions()) {
            markdown.append("| ").append(permission.displayName()).append(' ');
            for (RoleNode role : document.roles()) {
                PermissionDecision decision = PermissionDecision.fromAssignment(assignments.get(key(role.id(), permission.id())));
                markdown.append("| ").append(decision.matrixSymbol()).append(' ');
            }
            markdown.append("|\n");
        }
        markdown.append("\nLeyenda: ✓ permitido, △ condicionado, × denegado, — sin asignación explícita.\n");
    }

    private static String roleName(RolesPermissionsDocument document, String roleId) {
        return document.roleById(roleId).map(RoleNode::displayName).orElse(roleId);
    }

    private static String permissionName(RolesPermissionsDocument document, String permissionId) {
        return document.permissionById(permissionId).map(PermissionNode::displayName).orElse(permissionId);
    }

    private static String key(String roleId, String permissionId) {
        return normalize(roleId) + "::" + normalize(permissionId);
    }

    private static String optional(String value) {
        String normalized = normalize(value);
        return normalized.isBlank() ? "-" : normalized;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
