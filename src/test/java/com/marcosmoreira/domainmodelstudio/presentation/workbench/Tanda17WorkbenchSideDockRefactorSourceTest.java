package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class Tanda17WorkbenchSideDockRefactorSourceTest {

    private static final Path DIAGRAM_WORKBENCH = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/workbench/DiagramWorkbenchView.java");
    private static final Path STRUCTURED_WORKBENCH = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/workbench/StructuredWorkbenchView.java");
    private static final Path MODULES = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/workbench/WorkbenchSideDockModules.java");
    private static final List<Path> VISUAL_CONTRIBUTORS = List.of(
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureWorkbenchContributor.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorWorkbenchContributor.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphWorkbenchContributor.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapWorkbenchContributor.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowWorkbenchContributor.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassWorkbenchContributor.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeWorkbenchContributor.java")
    );

    @Test
    void visualAndStructuredWorkbenchesShareSideDockLayoutAndVisibilityPolicies() throws IOException {
        String visual = Files.readString(DIAGRAM_WORKBENCH, StandardCharsets.UTF_8);
        String structured = Files.readString(STRUCTURED_WORKBENCH, StandardCharsets.UTF_8);

        assertTrue(visual.contains("WorkbenchSideDockModules.registerVisualModules"));
        assertTrue(structured.contains("WorkbenchSideDockModules.registerStructuredModules"));
        assertTrue(visual.contains("WorkbenchSideDockLayout.mount"));
        assertTrue(structured.contains("WorkbenchSideDockLayout.mount"));
        assertTrue(visual.contains("WorkbenchSideDockVisibility.setVisible"));
        assertTrue(structured.contains("WorkbenchSideDockVisibility.setVisible"));
        assertFalse(visual.contains("StaticSideDockModule.of"));
        assertFalse(structured.contains("StaticSideDockModule.of"));
    }

    @Test
    void moduleRegistrationKeepsRealModulesWithoutPlaceholderLeakage() throws IOException {
        String source = Files.readString(MODULES, StandardCharsets.UTF_8);

        assertTrue(source.contains("SideDockModuleId.STRUCTURE"));
        assertTrue(source.contains("SideDockModuleId.PROPERTIES"));
        assertTrue(source.contains("SideDockModuleId.ROLES"));
        assertTrue(source.contains("SideDockModuleId.PERMISSIONS"));
        assertTrue(source.contains("SideDockModuleId.SECTIONS"));
        assertTrue(source.contains("operationalHelp"));
        assertTrue(source.contains("WorkspaceKind.CONCEPTUAL_CANVAS"));
        assertFalse(source.contains("SideDockPlaceholderFactory"));
    }

    @Test
    void visualContributorsUseCentralizedAppearanceModule() throws IOException {
        for (Path file : VISUAL_CONTRIBUTORS) {
            String source = Files.readString(file, StandardCharsets.UTF_8);
            assertTrue(source.contains("StandardSideDockModules.appearance"), file + " debe usar fábrica transversal.");
            assertFalse(source.contains("SideDockModuleId.APPEARANCE"), file + " no debe duplicar el módulo Apariencia.");
            assertFalse(source.contains("StaticSideDockModule.of"), file + " no debe construir Apariencia a mano.");
        }
    }
}
