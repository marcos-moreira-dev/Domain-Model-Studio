package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParser;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionAssignment;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionDecision;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionScope;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RoleNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RoleStatus;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/** Importa Markdown oficial de roles y permisos hacia un proyecto editable. */
public final class RolesPermissionsMarkdownParser implements MarkdownModelParser {

    @Override
    public DiagramProject parse(Path markdownFile) throws IOException, MarkdownModelParsingException {
        Objects.requireNonNull(markdownFile, "markdownFile");
        return parse(Files.readString(markdownFile, StandardCharsets.UTF_8), markdownFile.toString());
    }

    @Override
    public DiagramProject parse(String markdownContent, String sourceName) throws MarkdownModelParsingException {
        Objects.requireNonNull(markdownContent, "markdownContent");
        MarkdownImportDocument importDocument = MarkdownImportDocument.parse(markdownContent);
        MarkdownFrontMatter frontMatter = importDocument.frontMatter();
        String title = frontMatter.valueOrDefault("name", "Roles y permisos importado");
        Map<String, RoleNode> roles = new LinkedHashMap<>();
        Map<String, PermissionNode> permissions = new LinkedHashMap<>();
        List<PermissionAssignment> assignments = new ArrayList<>();
        parseBody(importDocument.body(), roles, permissions, assignments);
        if (roles.isEmpty()) {
            throw new MarkdownModelParsingException("El documento de roles y permisos no contiene roles reconocibles.");
        }
        if (permissions.isEmpty()) {
            throw new MarkdownModelParsingException("El documento de roles y permisos no contiene permisos reconocibles.");
        }
        RolesPermissionsDocument document;
        try {
            document = new RolesPermissionsDocument(
                    title,
                    frontMatter.valueOrDefault("version", "borrador"),
                    LocalDate.now(),
                    new ArrayList<>(roles.values()),
                    new ArrayList<>(permissions.values()),
                    assignments);
        } catch (IllegalArgumentException exception) {
            throw new MarkdownModelParsingException("No se pudo construir roles y permisos: " + exception.getMessage(), exception);
        }
        return MarkdownTextUtils.withSourceMarkdownPath(
                DiagramProject.blank(stableProjectId(title), title, DiagramTypeId.ROLES_PERMISSIONS_MAP)
                        .withRolesPermissions(document),
                sourceName);
    }

    private static void parseBody(
            String body,
            Map<String, RoleNode> roles,
            Map<String, PermissionNode> permissions,
            List<PermissionAssignment> assignments
    ) throws MarkdownModelParsingException {
        Section section = Section.NONE;
        String pendingRoleTitle = "";
        String pendingRoleId = "";
        String pendingPurpose = "";
        int assignmentCounter = 1;
        AssignmentTableColumns assignmentColumns = AssignmentTableColumns.legacy();
        for (String rawLine : body.split("\\R")) {
            String line = rawLine.strip();
            if (line.isBlank() || line.startsWith(">")) {
                continue;
            }
            String lower = line.toLowerCase(Locale.ROOT);
            if (line.startsWith("# ")) {
                if (!pendingRoleTitle.isBlank()) {
                    flushRole(roles, pendingRoleTitle, pendingRoleId, pendingPurpose);
                    pendingRoleTitle = pendingRoleId = pendingPurpose = "";
                }
                String heading = normalizeHeading(line.substring(2));
                if (heading.equals("roles")) {
                    section = Section.ROLES;
                } else if (heading.equals("permisos")) {
                    section = Section.PERMISSIONS;
                } else if (heading.equals("asignaciones")) {
                    section = Section.ASSIGNMENTS;
                } else {
                    section = Section.NONE;
                }
                continue;
            }
            if (section == Section.ROLES && line.startsWith("## ")) {
                if (!pendingRoleTitle.isBlank()) {
                    flushRole(roles, pendingRoleTitle, pendingRoleId, pendingPurpose);
                }
                pendingRoleTitle = line.substring(3).strip();
                pendingRoleId = "";
                pendingPurpose = "";
                continue;
            }
            if (section == Section.ROLES && !pendingRoleTitle.isBlank() && MarkdownTextUtils.isPropertyLine(line)) {
                String key = normalizeKey(MarkdownTextUtils.keyBeforeColon(line));
                String value = MarkdownTextUtils.valueAfterColon(line);
                if (key.equals("id")) {
                    pendingRoleId = value;
                } else if (key.equals("proposito") || key.equals("propósito") || key.equals("responsabilidad")) {
                    pendingPurpose = value;
                }
                continue;
            }
            if (section == Section.PERMISSIONS && line.startsWith("- ")) {
                PermissionNode permission = parsePermission(line.substring(2).strip());
                putPermission(permissions, permission);
                continue;
            }
            if (section == Section.ASSIGNMENTS && line.startsWith("|") && !isTableSeparator(line)) {
                if (isAssignmentHeader(line)) {
                    assignmentColumns = AssignmentTableColumns.fromHeader(line);
                    continue;
                }
                assignments.add(parseAssignment(line, roles, permissions, assignmentCounter++, assignmentColumns));
            }
        }
        if (!pendingRoleTitle.isBlank()) {
            flushRole(roles, pendingRoleTitle, pendingRoleId, pendingPurpose);
        }
    }

    private static void flushRole(
            Map<String, RoleNode> roles,
            String title,
            String rawId,
            String purpose
    ) throws MarkdownModelParsingException {
        String id = rawId == null || rawId.isBlank() ? MarkdownTextUtils.toStableId(title) : MarkdownTextUtils.toStableId(rawId);
        if (roles.containsKey(id)) {
            throw new MarkdownModelParsingException("Rol duplicado en Markdown: " + id);
        }
        roles.put(id, new RoleNode(id, title, RoleStatus.PLANNED, purpose, purpose, ""));
    }

    private static PermissionNode parsePermission(String text) throws MarkdownModelParsingException {
        int colon = text.indexOf(':');
        String rawId = colon >= 0 ? text.substring(0, colon).strip() : text;
        String description = colon >= 0 ? text.substring(colon + 1).strip() : "";
        String id = MarkdownTextUtils.toStableId(rawId);
        if (id.isBlank()) {
            throw new MarkdownModelParsingException("Permiso sin identificador: " + text);
        }
        String module = inferModule(id);
        String action = inferAction(id);
        return new PermissionNode(id, displayName(rawId), PermissionScope.ACTION, module, action, description, "");
    }

    private static PermissionAssignment parseAssignment(
            String tableLine,
            Map<String, RoleNode> roles,
            Map<String, PermissionNode> permissions,
            int counter,
            AssignmentTableColumns columns
    ) throws MarkdownModelParsingException {
        List<String> cells = tableCells(tableLine);
        if (cells.size() < 2) {
            throw new MarkdownModelParsingException("Asignación incompleta en tabla: " + tableLine);
        }
        String roleId = resolveRoleId(cell(cells, columns.roleIndex(), 0), roles);
        String permissionId = resolvePermissionId(cell(cells, columns.permissionIndex(), 1), permissions);
        String decisionText = cell(cells, columns.decisionIndex(), -1);
        PermissionDecision decision = decisionText.isBlank()
                ? PermissionDecision.ALLOWED
                : PermissionDecision.fromText(decisionText);
        String condition = cell(cells, columns.conditionIndex(), -1);
        String scope = cell(cells, columns.scopeIndex(), -1);
        String notes = cell(cells, columns.notesIndex(), -1);

        if (decision == PermissionDecision.CONDITIONAL && condition.isBlank()) {
            condition = conditionFromExplicitDecision(scope, notes);
        }
        if (!condition.isBlank() && decision == PermissionDecision.ALLOWED) {
            decision = PermissionDecision.CONDITIONAL;
        }
        notes = mergeScopeIntoNotes(scope, notes);
        return new PermissionAssignment("assign_" + counter, roleId, permissionId,
                decision.allowedValue(), condition, notes);
    }

    private static String cell(List<String> cells, int preferredIndex, int fallbackIndex) {
        int index = preferredIndex >= 0 ? preferredIndex : fallbackIndex;
        return index >= 0 && index < cells.size() ? cells.get(index).strip() : "";
    }

    private static String mergeScopeIntoNotes(String scope, String notes) {
        String normalizedScope = scope == null ? "" : scope.strip();
        String normalizedNotes = notes == null ? "" : notes.strip();
        if (normalizedScope.isBlank()) {
            return normalizedNotes;
        }
        String scopeText = "Alcance: " + normalizedScope;
        if (normalizedNotes.isBlank()) {
            return scopeText;
        }
        return scopeText + ". " + normalizedNotes;
    }

    private static String conditionFromExplicitDecision(String scope, String notes) {
        String normalizedScope = scope == null ? "" : scope.strip();
        if (!normalizedScope.isBlank()) {
            return "Condicionado por alcance: " + normalizedScope;
        }
        String normalizedNotes = notes == null ? "" : notes.strip();
        if (!normalizedNotes.isBlank()) {
            return normalizedNotes;
        }
        return "Condicionado por política administrativa.";
    }


    private static String resolveRoleId(String raw, Map<String, RoleNode> roles) throws MarkdownModelParsingException {
        String stable = MarkdownTextUtils.toStableId(raw);
        if (roles.containsKey(stable)) {
            return stable;
        }
        for (RoleNode role : roles.values()) {
            if (role.displayName().equalsIgnoreCase(raw.strip())) {
                return role.id();
            }
        }
        throw new MarkdownModelParsingException("La asignación referencia un rol inexistente: " + raw);
    }

    private static String resolvePermissionId(String raw, Map<String, PermissionNode> permissions) throws MarkdownModelParsingException {
        String stable = MarkdownTextUtils.toStableId(raw);
        if (permissions.containsKey(stable)) {
            return stable;
        }
        for (PermissionNode permission : permissions.values()) {
            if (permission.displayName().equalsIgnoreCase(raw.strip())) {
                return permission.id();
            }
        }
        throw new MarkdownModelParsingException("La asignación referencia un permiso inexistente: " + raw);
    }

    private static void putPermission(Map<String, PermissionNode> permissions, PermissionNode permission)
            throws MarkdownModelParsingException {
        if (permissions.containsKey(permission.id())) {
            throw new MarkdownModelParsingException("Permiso duplicado en Markdown: " + permission.id());
        }
        permissions.put(permission.id(), permission);
    }

    private static List<String> tableCells(String line) {
        String clean = line.strip();
        if (clean.startsWith("|")) {
            clean = clean.substring(1);
        }
        if (clean.endsWith("|")) {
            clean = clean.substring(0, clean.length() - 1);
        }
        List<String> cells = new ArrayList<>();
        for (String cell : clean.split("\\|", -1)) {
            cells.add(cell.strip());
        }
        return cells;
    }

    private static boolean isTableSeparator(String line) {
        return line.replace("|", "").replace("-", "").replace(":", "").strip().isBlank();
    }

    private static boolean isAssignmentHeader(String line) {
        List<String> cells = tableCells(line);
        if (cells.size() < 2) {
            return false;
        }
        String first = normalizeKey(cells.get(0));
        String second = normalizeKey(cells.get(1));
        return (first.equals("rol") || first.equals("role"))
                && (second.equals("permiso") || second.equals("permission"));
    }

    private static String inferModule(String id) {
        int underscore = id.indexOf('_');
        int dash = id.indexOf('-');
        int split = underscore >= 0 ? underscore : dash;
        return split > 0 ? displayName(id.substring(0, split)) : "General";
    }

    private static String inferAction(String id) {
        int underscore = id.lastIndexOf('_');
        int dash = id.lastIndexOf('-');
        int split = Math.max(underscore, dash);
        return split >= 0 && split < id.length() - 1 ? displayName(id.substring(split + 1)) : displayName(id);
    }

    private static String displayName(String value) {
        String text = value == null ? "" : value.strip().replace('_', ' ').replace('-', ' ');
        if (text.isBlank()) {
            return "Permiso";
        }
        String[] parts = text.split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isBlank()) {
                continue;
            }
            if (!builder.isEmpty()) {
                builder.append(' ');
            }
            builder.append(part.substring(0, 1).toUpperCase(Locale.ROOT));
            if (part.length() > 1) {
                builder.append(part.substring(1));
            }
        }
        return builder.toString();
    }


    private static String stableProjectId(String title) {
        String id = MarkdownTextUtils.toStableId(title);
        return id.isBlank() ? "roles_permissions_importado" : id;
    }

    private static String normalizeKey(String value) {
        return value == null ? "" : value.strip().toLowerCase(Locale.ROOT);
    }

    private static String normalizeHeading(String value) {
        if (value == null) {
            return "";
        }
        return java.text.Normalizer.normalize(value, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replace('_', ' ')
                .strip()
                .replaceAll("\\s+", " ");
    }


    private record AssignmentTableColumns(
            int roleIndex,
            int permissionIndex,
            int decisionIndex,
            int conditionIndex,
            int scopeIndex,
            int notesIndex
    ) {
        private static AssignmentTableColumns legacy() {
            return new AssignmentTableColumns(0, 1, -1, -1, 2, 3);
        }

        private static AssignmentTableColumns fromHeader(String line) {
            List<String> cells = tableCells(line);
            int role = 0;
            int permission = 1;
            int decision = -1;
            int condition = -1;
            int scope = -1;
            int notes = -1;
            for (int index = 0; index < cells.size(); index++) {
                String key = normalizeHeading(cells.get(index));
                if (key.equals("rol") || key.equals("role")) {
                    role = index;
                } else if (key.equals("permiso") || key.equals("permission")) {
                    permission = index;
                } else if (key.contains("decision") || key.contains("estado") || key.contains("acceso")) {
                    decision = index;
                } else if (key.contains("condicion") || key.contains("restriccion") || key.contains("limite") || key.contains("guarda")) {
                    condition = index;
                } else if (key.contains("alcance") || key.contains("ambito")) {
                    scope = index;
                } else if (key.contains("observacion") || key.contains("nota") || key.contains("comentario")) {
                    notes = index;
                }
            }
            return new AssignmentTableColumns(role, permission, decision, condition, scope, notes);
        }
    }

    private enum Section {
        NONE,
        ROLES,
        PERMISSIONS,
        ASSIGNMENTS
    }
}
