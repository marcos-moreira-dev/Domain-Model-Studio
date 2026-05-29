package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionAssignment;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionScope;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RoleNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RoleStatus;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

final class RolesPermissionsMarkdownExporterTest {

    @Test
    void exportsMatrixDocumentWithLegendAndConditionalDecision() {
        RolesPermissionsDocument document = new RolesPermissionsDocument(
                "UENS",
                "1.0",
                LocalDate.of(2026, 1, 1),
                List.of(new RoleNode("secretaria", "Secretaría", RoleStatus.ACTIVE, "Opera matrícula", "", "")),
                List.of(new PermissionNode("matricula.editar", "Editar matrícula", PermissionScope.ACTION, "Matrícula", "Editar", "Edita matrícula vigente", "")),
                List.of(new PermissionAssignment("a1", "secretaria", "matricula.editar", true, "solo período activo", "")));
        DiagramProject project = DiagramProject.blank("uens", "UENS", DiagramTypeId.ROLES_PERMISSIONS_MAP)
                .withRolesPermissions(document);

        String markdown = new RolesPermissionsMarkdownExporter().export(project);

        assertTrue(markdown.contains("intended_output: \"matriz/documento administrativo\""));
        assertTrue(markdown.contains("# Matriz"));
        assertTrue(markdown.contains("△"));
        assertTrue(markdown.contains("Leyenda: ✓ permitido, △ condicionado, × denegado, — sin asignación explícita."));
    }
    @Test
    void exportedMarkdownRoundTripsWithoutTreatingSummaryAsRole() throws Exception {
        RolesPermissionsDocument document = sampleDocument();
        DiagramProject project = DiagramProject.blank("uens", "UENS", DiagramTypeId.ROLES_PERMISSIONS_MAP)
                .withRolesPermissions(document);

        String markdown = new RolesPermissionsMarkdownExporter().export(project);
        DiagramProject reparsed = new RolesPermissionsMarkdownParser().parse(markdown, "roles-exportados.md");

        RolesPermissionsDocument roundTripped = reparsed.rolesPermissions().orElseThrow();
        assertEquals(1, roundTripped.roles().size());
        assertEquals(1, roundTripped.permissions().size());
        assertEquals(1, roundTripped.assignments().size());
        assertFalse(roundTripped.roles().stream()
                .anyMatch(role -> role.displayName().equalsIgnoreCase("Resumen")));
        assertTrue(roundTripped.assignmentFor("secretaria", "matricula_editar").isPresent());
    }


    private static RolesPermissionsDocument sampleDocument() {
        return new RolesPermissionsDocument(
                "UENS",
                "1.0",
                LocalDate.of(2026, 1, 1),
                List.of(new RoleNode("secretaria", "Secretaría", RoleStatus.ACTIVE, "Opera matrícula", "", "")),
                List.of(new PermissionNode("matricula_editar", "Editar matrícula", PermissionScope.ACTION, "Matrícula", "Editar", "Edita matrícula vigente", "")),
                List.of(new PermissionAssignment("a1", "secretaria", "matricula_editar", true, "solo período activo", "")));
    }
}
