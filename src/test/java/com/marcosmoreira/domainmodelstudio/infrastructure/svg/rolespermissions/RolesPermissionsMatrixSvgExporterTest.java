package com.marcosmoreira.domainmodelstudio.infrastructure.svg.rolespermissions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionAssignment;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionScope;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RoleNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RoleStatus;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

final class RolesPermissionsMatrixSvgExporterTest {

    @Test
    void exportsVectorMatrixWithoutRasterImage() {
        RolesPermissionsDocument document = new RolesPermissionsDocument(
                "UENS",
                "0.1.0",
                LocalDate.of(2026, 1, 1),
                List.of(new RoleNode("admin", "Administrador", RoleStatus.ACTIVE, "Gestiona", "", "")),
                List.of(new PermissionNode("students.read", "Ver estudiantes", PermissionScope.SCREEN, "Estudiantes", "Leer", "", "")),
                List.of(new PermissionAssignment("a1", "admin", "students.read", true, "", "")));

        String svg = new RolesPermissionsMatrixSvgExporter().export(document);

        assertTrue(svg.contains("<svg"));
        assertTrue(svg.contains("roles-permissions-matrix"));
        assertTrue(svg.contains("Administrador"));
        assertTrue(svg.contains("Ver estudiantes"));
        assertTrue(svg.contains("✓"));
        assertFalse(svg.contains("<image"));
        assertFalse(svg.contains("data:image"));
    }
}
