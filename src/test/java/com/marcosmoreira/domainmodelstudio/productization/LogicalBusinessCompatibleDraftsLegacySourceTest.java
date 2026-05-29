package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 36: los artefactos compatibles quedan como infraestructura interna legacy. */
class LogicalBusinessCompatibleDraftsLegacySourceTest {

    private static final Path MAIN = Path.of("src/main/java");
    private static final Path RESOURCES = Path.of("src/main/resources");

    @Test
    void logicalBusinessPresentationShouldNotExposeDerivationSelection() throws IOException {
        String viewModel = read("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessViewModel.java");
        String selectionKind = read("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessSelectionKind.java");
        String selection = read("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessSelection.java");
        String helpGuide = read("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessContextualHelpGuide.java");
        String inspector = read("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessInspectorSupport.java");

        assertFalse(viewModel.contains("LogicalBusinessDerivationService"));
        assertFalse(viewModel.contains("selectDerivation"));
        assertFalse(viewModel.contains("derivationDrafts"));
        assertFalse(selectionKind.contains("DERIVATION"));
        assertFalse(selection.contains("derivation("));
        assertFalse(helpGuide.contains("compatibleArtifactHelp"));
        assertFalse(inspector.contains("Artefacto compatible"));
    }

    @Test
    void compatibleDraftUiArtifactsShouldBeRemoved() throws IOException {
        assertFalse(MAIN.resolve("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessDerivationsPanel.java")
                .toFile().exists());
        assertFalse(RESOURCES.resolve("css/logical-business-derivations.css").toFile().exists());

        String css = Files.readString(RESOURCES.resolve("css/logical-business.css"), StandardCharsets.UTF_8);
        assertFalse(css.contains("logical-business-derivations.css"));
    }

    @Test
    void compatibleDraftInfrastructureShouldStayInternalAndExplicit() throws IOException {
        String service = read("com/marcosmoreira/domainmodelstudio/application/logicalbusiness/derivation/LogicalBusinessDerivationService.java");
        String draftText = read("com/marcosmoreira/domainmodelstudio/application/logicalbusiness/derivation/LogicalBusinessDraftText.java");
        String packageInfo = read("com/marcosmoreira/domainmodelstudio/application/logicalbusiness/derivation/package-info.java");

        assertTrue(service.contains("compatibleDrafts"));
        assertTrue(service.contains("compatibleDraft("));
        assertTrue(service.contains("@Deprecated"));
        assertTrue(draftText.contains("compatible-draft"));
        assertTrue(draftText.contains("borrador compatible revisable"));
        assertTrue(draftText.contains("source_mode"));
        assertTrue(packageInfo.contains("Infraestructura interna"));
        assertTrue(packageInfo.contains("no hay importación automática"));
        assertTrue(packageInfo.contains("fuente lógica canónica"));
    }

    private static String read(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
