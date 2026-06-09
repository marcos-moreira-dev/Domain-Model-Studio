package com.marcosmoreira.domainmodelstudio.infrastructure.svg.rolespermissions;

import com.marcosmoreira.domainmodelstudio.application.export.SvgDiagramExporter;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionAssignment;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionDecision;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RoleNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** Exporta roles/permisos como matriz vectorial, no como grafo ni captura raster. */
public final class RolesPermissionsMatrixSvgExporter implements SvgDiagramExporter {

    private static final double MARGIN = 32.0;
    private static final double TITLE_HEIGHT = 58.0;
    private static final double ROLE_WIDTH = 190.0;
    private static final double PERMISSION_WIDTH = 150.0;
    private static final double HEADER_HEIGHT = 78.0;
    private static final double ROW_HEIGHT = 50.0;
    private static final double FOOTER_HEIGHT = 34.0;

    @Override
    public String export(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        RolesPermissionsDocument document = project.rolesPermissions()
                .orElseThrow(() -> new IllegalArgumentException("El proyecto no contiene matriz de roles y permisos."));
        return export(document);
    }

    public String export(RolesPermissionsDocument document) {
        Objects.requireNonNull(document, "document");
        int roleCount = Math.max(1, document.roles().size());
        int permissionCount = Math.max(1, document.permissions().size());
        double width = Math.max(860.0, MARGIN * 2 + ROLE_WIDTH + permissionCount * PERMISSION_WIDTH);
        double height = Math.max(520.0, MARGIN * 2 + TITLE_HEIGHT + HEADER_HEIGHT + roleCount * ROW_HEIGHT + FOOTER_HEIGHT);
        StringBuilder svg = new StringBuilder(48_000);
        appendHeader(svg, width, height, document);
        if (document.roles().isEmpty() || document.permissions().isEmpty()) {
            appendEmptyState(svg, width, height);
        } else {
            appendMatrix(svg, document);
        }
        appendFooter(svg, height, document);
        svg.append("</svg>\n");
        return svg.toString();
    }

    private void appendHeader(StringBuilder svg, double width, double height, RolesPermissionsDocument document) {
        svg.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        svg.append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"").append(format(width))
                .append("\" height=\"").append(format(height)).append("\" viewBox=\"0 0 ")
                .append(format(width)).append(' ').append(format(height)).append("\">\n");
        svg.append("  <metadata>Domain Model Studio | roles-permissions-matrix | vector=true | output=matrix</metadata>\n");
        svg.append("  <defs>\n");
        svg.append("    <style><![CDATA[\n");
        svg.append("      .bg{fill:#F6F8FB;} .panel{fill:#FFFFFF;stroke:#D7DEE8;stroke-width:1.2;}\n");
        svg.append("      .title{font:700 20px Segoe UI,Arial,sans-serif;fill:#223044;}\n");
        svg.append("      .subtitle{font:12px Segoe UI,Arial,sans-serif;fill:#637083;}\n");
        svg.append("      .header{fill:#E9EEF5;stroke:#C9D3E0;stroke-width:1;}\n");
        svg.append("      .role{fill:#F9FBFD;stroke:#C9D3E0;stroke-width:1;}\n");
        svg.append("      .cell{fill:#FFFFFF;stroke:#DDE4EE;stroke-width:1;}\n");
        svg.append("      .allowed{fill:#DDEFE7;stroke:#9BC9B3;stroke-width:1.3;}\n");
        svg.append("      .conditional{fill:#FFF4D6;stroke:#D1A43C;stroke-width:1.3;}\n");
        svg.append("      .denied{fill:#F4E1E1;stroke:#D6A7A7;stroke-width:1.3;}\n");
        svg.append("      .empty{fill:#F3F6FA;stroke:#DDE4EE;stroke-width:1;}\n");
        svg.append("      .label{font:12px Segoe UI,Arial,sans-serif;fill:#223044;}\n");
        svg.append("      .small{font:10px Segoe UI,Arial,sans-serif;fill:#637083;}\n");
        svg.append("      .mark{font:700 19px Segoe UI,Arial,sans-serif;fill:#223044;}\n");
        svg.append("    ]]></style>\n");
        svg.append("  </defs>\n");
        svg.append("  <rect class=\"bg\" x=\"0\" y=\"0\" width=\"100%\" height=\"100%\"/>\n");
        svg.append("  <text class=\"title\" x=\"").append(format(MARGIN)).append("\" y=\"").append(format(MARGIN + 22)).append("\">")
                .append(escape("Roles y permisos — " + document.projectName())).append("</text>\n");
        svg.append("  <text class=\"subtitle\" x=\"").append(format(MARGIN)).append("\" y=\"").append(format(MARGIN + 43)).append("\">")
                .append(escape("Matriz estructurada exportable; no es canvas libre ni grafo de nodos."))
                .append("</text>\n");
    }

    private void appendEmptyState(StringBuilder svg, double width, double height) {
        double x = MARGIN;
        double y = MARGIN + TITLE_HEIGHT;
        double w = width - MARGIN * 2;
        double h = height - y - MARGIN - FOOTER_HEIGHT;
        svg.append("  <rect class=\"panel\" x=\"").append(format(x)).append("\" y=\"").append(format(y))
                .append("\" width=\"").append(format(w)).append("\" height=\"").append(format(h)).append("\"/>\n");
        svg.append("  <text class=\"label\" x=\"").append(format(x + 24)).append("\" y=\"").append(format(y + 40)).append("\">")
                .append("Agrega roles y permisos para construir la matriz visual.").append("</text>\n");
    }

    private void appendMatrix(StringBuilder svg, RolesPermissionsDocument document) {
        Map<String, PermissionAssignment> assignments = new HashMap<>();
        for (PermissionAssignment assignment : document.assignments()) {
            assignments.put(key(assignment.roleId(), assignment.permissionId()), assignment);
        }
        double startX = MARGIN;
        double startY = MARGIN + TITLE_HEIGHT;
        double matrixWidth = ROLE_WIDTH + document.permissions().size() * PERMISSION_WIDTH;
        double matrixHeight = HEADER_HEIGHT + document.roles().size() * ROW_HEIGHT;
        svg.append("  <rect class=\"panel\" x=\"").append(format(startX)).append("\" y=\"").append(format(startY))
                .append("\" width=\"").append(format(matrixWidth)).append("\" height=\"").append(format(matrixHeight)).append("\"/>\n");
        cell(svg, startX, startY, ROLE_WIDTH, HEADER_HEIGHT, "header", "Rol / permiso", "");
        for (int column = 0; column < document.permissions().size(); column++) {
            PermissionNode permission = document.permissions().get(column);
            double x = startX + ROLE_WIDTH + column * PERMISSION_WIDTH;
            cell(svg, x, startY, PERMISSION_WIDTH, HEADER_HEIGHT, "header", truncate(permission.displayName(), 24), permission.scope().displayName());
        }
        for (int row = 0; row < document.roles().size(); row++) {
            RoleNode role = document.roles().get(row);
            double y = startY + HEADER_HEIGHT + row * ROW_HEIGHT;
            cell(svg, startX, y, ROLE_WIDTH, ROW_HEIGHT, "role", truncate(role.displayName(), 28), role.status().displayName());
            for (int column = 0; column < document.permissions().size(); column++) {
                PermissionNode permission = document.permissions().get(column);
                double x = startX + ROLE_WIDTH + column * PERMISSION_WIDTH;
                PermissionAssignment assignment = assignments.get(key(role.id(), permission.id()));
                PermissionDecision decision = PermissionDecision.fromAssignment(assignment);
                switch (decision) {
                    case ALLOWED -> cell(svg, x, y, PERMISSION_WIDTH, ROW_HEIGHT, "allowed", "✓", "permitido");
                    case CONDITIONAL -> cell(svg, x, y, PERMISSION_WIDTH, ROW_HEIGHT, "conditional", "△", emptyFallback(assignment.condition(), "condicionado"));
                    case DENIED -> cell(svg, x, y, PERMISSION_WIDTH, ROW_HEIGHT, "denied", "×", emptyFallback(assignment.condition(), "denegado"));
                    case NOT_APPLICABLE -> cell(svg, x, y, PERMISSION_WIDTH, ROW_HEIGHT, "empty", "N/A", "no aplica");
                    case NOT_ASSIGNED -> cell(svg, x, y, PERMISSION_WIDTH, ROW_HEIGHT, "cell", "—", "");
                }
            }
        }
    }

    private void appendFooter(StringBuilder svg, double height, RolesPermissionsDocument document) {
        svg.append("  <text class=\"small\" x=\"").append(format(MARGIN)).append("\" y=\"").append(format(height - MARGIN + 3)).append("\">")
                .append(escape("roles=" + document.roles().size() + " · permisos=" + document.permissions().size()
                        + " · asignaciones=" + document.assignments().size()))
                .append("</text>\n");
    }

    private void cell(StringBuilder svg, double x, double y, double width, double height, String styleClass, String main, String secondary) {
        svg.append("  <rect class=\"").append(styleClass).append("\" x=\"").append(format(x)).append("\" y=\"").append(format(y))
                .append("\" width=\"").append(format(width)).append("\" height=\"").append(format(height)).append("\"/>\n");
        String mainClass = ("✓".equals(main) || "×".equals(main) || "△".equals(main)) ? "mark" : "label";
        svg.append("  <text class=\"").append(mainClass).append("\" x=\"").append(format(x + width / 2.0)).append("\" y=\"")
                .append(format(y + height / 2.0 - (secondary.isBlank() ? -5.0 : 2.0))).append("\" text-anchor=\"middle\">")
                .append(escape(main)).append("</text>\n");
        if (!secondary.isBlank()) {
            svg.append("  <text class=\"small\" x=\"").append(format(x + width / 2.0)).append("\" y=\"")
                    .append(format(y + height / 2.0 + 18.0)).append("\" text-anchor=\"middle\">")
                    .append(escape(truncate(secondary, 22))).append("</text>\n");
        }
    }

    private static String key(String roleId, String permissionId) {
        return normalize(roleId) + "::" + normalize(permissionId);
    }

    private static String emptyFallback(String value, String fallback) {
        String normalized = normalize(value);
        return normalized.isBlank() ? fallback : normalized;
    }

    private static String truncate(String value, int max) {
        String normalized = normalize(value);
        if (normalized.length() <= max) {
            return normalized;
        }
        return normalized.substring(0, Math.max(1, max - 1)) + "…";
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }

    private static String format(double value) {
        return String.format(java.util.Locale.ROOT, "%.1f", value);
    }

    private static String escape(String value) {
        return normalize(value)
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
