package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class Tanda28SideDockInternalContentSourceTest {

    private static final Path WORKBENCH_SUPPORT = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/workbench/WorkbenchPanelSupport.java");
    private static final Path MODULE_MAP_STRUCTURE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapStructurePanel.java");
    private static final Path ARCHITECTURE_STRUCTURE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureStructurePanel.java");
    private static final Path BEHAVIOR_STRUCTURE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorStructurePanel.java");
    private static final Path ROLES_STRUCTURE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/rolespermissions/RolesPermissionsStructurePanel.java");

    @Test
    void workbenchPanelsExposeExplicitEmptySelectionCard() throws IOException {
        String source = Files.readString(WORKBENCH_SUPPORT, StandardCharsets.UTF_8);

        assertTrue(source.contains("emptySelectionCard"));
        assertTrue(source.contains("Sin selección"));
        assertTrue(source.contains("diagram-workbench-focus-card"));
    }

    @Test
    void visualStructuresExposeRelationshipsAsStructureNotOnlyProperties() throws IOException {
        String module = Files.readString(MODULE_MAP_STRUCTURE, StandardCharsets.UTF_8);
        String architecture = Files.readString(ARCHITECTURE_STRUCTURE, StandardCharsets.UTF_8);
        String behavior = Files.readString(BEHAVIOR_STRUCTURE, StandardCharsets.UTF_8);

        assertTrue(module.contains("dependencyList"));
        assertTrue(module.contains("tab(\"Dependencias\""));
        assertTrue(architecture.contains("edgeList"));
        assertTrue(architecture.contains("tab(\"Relaciones\""));
        assertTrue(behavior.contains("flowList"));
        assertTrue(behavior.contains("Flujos y transiciones"));
    }

    @Test
    void rolesPermissionsStructureSeparatesAssignmentsFromRolesAndPermissions() throws IOException {
        String source = Files.readString(ROLES_STRUCTURE, StandardCharsets.UTF_8);

        assertTrue(source.contains("assignmentList"));
        assertTrue(source.contains("tab(\"Asignaciones\""));
        assertTrue(source.contains("roleLabel(item.roleId())"));
        assertTrue(source.contains("permissionLabel(item.permissionId())"));
    }
}
