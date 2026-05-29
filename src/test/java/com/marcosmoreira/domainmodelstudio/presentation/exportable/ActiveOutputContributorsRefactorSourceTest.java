package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl R2: la resolución de outputs exportables debe vivir en contributors por familia. */
class ActiveOutputContributorsRefactorSourceTest {

    @Test
    void resolverShouldDelegateToContributorRegistry() throws IOException {
        String source = read("ActiveOutputResolver.java");

        assertTrue(source.contains("ActiveOutputContributorRegistry"));
        assertTrue(source.contains("contributorRegistry.resolve(activeProject, exportFormatPolicy)"));
        assertFalse(source.contains("private Optional<ExportableOutput> moduleMapOutput"));
        assertFalse(source.contains("private Optional<ExportableOutput> umlClassOutput"));
        assertLineCountAtMost("ActiveOutputResolver.java", 90);
    }

    @Test
    void registryShouldRegisterAllOutputFamilies() throws IOException {
        String source = read("ActiveOutputContributorRegistry.java");

        assertTrue(source.contains("ConceptualActiveOutputContributor"));
        assertTrue(source.contains("DataDictionaryActiveOutputContributor"));
        assertTrue(source.contains("ModuleMapActiveOutputContributor"));
        assertTrue(source.contains("UmlClassActiveOutputContributor"));
        assertTrue(source.contains("RolesPermissionsActiveOutputContributor"));
        assertTrue(source.contains("ScreenFlowActiveOutputContributor"));
        assertTrue(source.contains("WireframeActiveOutputContributor"));
        assertTrue(source.contains("FreeGraphActiveOutputContributor"));
        assertTrue(source.contains("LogicalBusinessActiveOutputContributor"));
        assertTrue(source.contains("BehaviorActiveOutputContributor"));
        assertTrue(source.contains("ArchitectureActiveOutputContributor"));
    }

    @Test
    void contributorsShouldPreserveSpecialOutputKinds() throws IOException {
        assertContains("UmlClassActiveOutputContributor.java",
                "ExportableOutput.visualScoped",
                "viewModel.currentVisualExportProject()",
                "viewModel::exportVisualAsPng");
        assertContains("DataDictionaryActiveOutputContributor.java",
                "ExportableOutput.document",
                "formatsForDataDictionary");
        assertContains("RolesPermissionsActiveOutputContributor.java",
                "ExportableOutput.matrix",
                "formatsForRolesPermissions");
        assertContains("FreeGraphActiveOutputContributor.java",
                "DiagramTypeId.FREE_GRAPH",
                "viewModel::exportVisualAsPng");
        assertContains("LogicalBusinessActiveOutputContributor.java",
                "DiagramTypeId.LOGICAL_BUSINESS_INTAKE",
                "ExportableOutput.projectDocument",
                "formatsForLogicalBusiness");
    }

    private static void assertContains(String fileName, String... fragments) throws IOException {
        String source = read(fileName);
        for (String fragment : fragments) {
            assertTrue(source.contains(fragment), fileName + " debe contener: " + fragment);
        }
    }

    private static void assertLineCountAtMost(String fileName, int maxLines) throws IOException {
        long lines = Files.lines(path(fileName)).count();
        assertTrue(lines <= maxLines, fileName + " debe mantenerse pequeño; líneas actuales: " + lines);
    }

    private static String read(String fileName) throws IOException {
        return Files.readString(path(fileName));
    }

    private static Path path(String fileName) {
        return Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable", fileName);
    }
}
