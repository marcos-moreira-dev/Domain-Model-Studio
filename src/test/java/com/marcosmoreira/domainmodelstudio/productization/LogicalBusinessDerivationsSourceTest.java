package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class LogicalBusinessDerivationsSourceTest {

    private static final Path ROOT = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio");

    @Test
    void compatibleDraftsAreInternalContributorsNotAutomaticImports() throws IOException {
        String service = Files.readString(ROOT.resolve(
                "application/logicalbusiness/derivation/LogicalBusinessDerivationService.java"));
        String yaml = Files.readString(ROOT.resolve(
                "application/logicalbusiness/derivation/LogicalBusinessDraftText.java"));
        String viewModel = Files.readString(ROOT.resolve(
                "presentation/logicalbusiness/LogicalBusinessViewModel.java"));

        assertTrue(service.contains("LogicalBusinessDerivationWriter"));
        assertTrue(service.contains("compatibleDrafts"));
        assertTrue(yaml.contains("sample_kind: \\\"compatible-draft\\\""));
        assertTrue(yaml.contains("source_mode: \\\"levantamiento-logico-como-fuente\\\""));
        assertTrue(yaml.contains("auto_import: false"));
        assertFalse(viewModel.contains("LogicalBusinessDerivationService"));
        assertFalse(viewModel.contains("derivationDrafts"));
    }

    @Test
    void plannedDraftWritersRemainSeparatedByTarget() throws IOException {
        String directory = ROOT.resolve("application/logicalbusiness/derivation").toString();

        assertSmallWriter(directory, "LogicalBusinessFreeGraphDraftWriter.java");
        assertSmallWriter(directory, "LogicalBusinessConceptualModelDraftWriter.java");
        assertSmallWriter(directory, "LogicalBusinessDataDictionaryDraftWriter.java");
        assertSmallWriter(directory, "LogicalBusinessRolesPermissionsDraftWriter.java");
        assertSmallWriter(directory, "LogicalBusinessScreenFlowDraftWriter.java");
        assertSmallWriter(directory, "LogicalBusinessWireframeDraftWriter.java");
    }

    private void assertSmallWriter(String directory, String fileName) throws IOException {
        Path file = Path.of(directory, fileName);
        long lines = Files.readAllLines(file).size();
        assertTrue(lines < 140, fileName + " debe mantenerse pequeño y revisable; líneas: " + lines);
    }
}
