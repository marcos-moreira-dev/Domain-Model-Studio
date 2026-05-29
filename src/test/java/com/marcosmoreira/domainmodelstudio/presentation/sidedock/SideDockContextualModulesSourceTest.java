package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class SideDockContextualModulesSourceTest {

    private static final Path DIAGRAM_WORKBENCH = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/workbench/DiagramWorkbenchView.java");
    private static final Path STRUCTURED_WORKBENCH = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/workbench/StructuredWorkbenchView.java");
    private static final Path SIDE_DOCK_MODULES = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/workbench/WorkbenchSideDockModules.java");
    private static final Path MODULE_IDS = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/sidedock/SideDockModuleId.java");

    @Test
    void visualWorkbenchDoesNotRegisterEmptyViewPlaceholdersAndUsesRealOperationalHelp() throws IOException {
        String source = Files.readString(DIAGRAM_WORKBENCH, StandardCharsets.UTF_8);
        String modules = Files.readString(SIDE_DOCK_MODULES, StandardCharsets.UTF_8);

        assertFalse(source.contains("SideDockModuleId.VIEW"));
        assertFalse(source.contains("SideDockPlaceholderFactory"));
        assertTrue(modules.contains("SideDockModuleId.STRUCTURE"));
        assertTrue(modules.contains("SideDockModuleId.PROPERTIES"));
        assertTrue(modules.contains("WorkspaceKind.CONCEPTUAL_CANVAS"));
        assertTrue(modules.contains("operationalHelp"));
    }

    @Test
    void structuredWorkbenchDoesNotRegisterDocumentOrMatrixPlaceholders() throws IOException {
        String source = Files.readString(STRUCTURED_WORKBENCH, StandardCharsets.UTF_8);
        String modules = Files.readString(SIDE_DOCK_MODULES, StandardCharsets.UTF_8);

        assertFalse(source.contains("SideDockModuleId.PREVIEW"));
        assertFalse(source.contains("SideDockModuleId.MATRIX"));
        assertFalse(source.contains("SideDockPlaceholderFactory"));
        assertTrue(modules.contains("SideDockModuleId.ROLES"));
        assertTrue(modules.contains("SideDockModuleId.PERMISSIONS"));
        assertTrue(modules.contains("SideDockModuleId.SECTIONS"));
        assertTrue(modules.contains("SideDockModuleId.PROPERTIES"));
        assertTrue(modules.contains("WorkspaceKind.CONCEPTUAL_CANVAS"));
    }

    @Test
    void moduleCatalogDeclaresUsefulFutureModulesWithoutRenderingThemAsPlaceholders() throws IOException {
        String source = Files.readString(MODULE_IDS, StandardCharsets.UTF_8);

        assertTrue(source.contains("APPEARANCE"));
        assertTrue(source.contains("ORGANIZATION"));
        assertTrue(source.contains("VALIDATION"));
        assertTrue(source.contains("COMPONENTS"));
        assertTrue(source.contains("TIME"));
        assertTrue(source.contains("DOCUMENT"));
        assertTrue(source.contains("LAYERS"));
    }
}
