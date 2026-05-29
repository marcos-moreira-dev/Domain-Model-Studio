package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 7 para importación, ejemplos y exportación Markdown de Levantamiento lógico. */
class LogicalBusinessImportExportUiSourceTest {

    private static final Path MAIN = Path.of("src/main/java");

    @Test
    void markdownDispatcherShouldImportLogicalBusinessThroughDedicatedAdapter() throws IOException {
        String dispatcher = read("com/marcosmoreira/domainmodelstudio/infrastructure/markdown/DiagramMarkdownImportDispatcher.java");
        String adapter = read("com/marcosmoreira/domainmodelstudio/infrastructure/markdown/logicalbusiness/LogicalBusinessProjectMarkdownParser.java");

        assertTrue(dispatcher.contains("DiagramTypeId.LOGICAL_BUSINESS_INTAKE"));
        assertTrue(dispatcher.contains("LogicalBusinessProjectMarkdownParser"));
        assertTrue(adapter.contains("implements MarkdownModelParser"));
        assertTrue(adapter.contains("withLogicalBusinessDocument"));
        assertTrue(adapter.contains("DiagramProject.blank"));
    }

    @Test
    void logicalBusinessShouldExposeMarkdownWithoutVisualFormats() throws IOException {
        String profiles = read("com/marcosmoreira/domainmodelstudio/application/catalog/definitions/DiagramCapabilityProfiles.java");
        String output = read("com/marcosmoreira/domainmodelstudio/presentation/exportable/LogicalBusinessActiveOutputContributor.java");
        String exporter = read("com/marcosmoreira/domainmodelstudio/infrastructure/markdown/logicalbusiness/LogicalBusinessMarkdownDiagramExporter.java");

        String capabilities = slice(profiles, "public static DiagramCapabilitySet logicalBusinessDocument", "public static DiagramCapabilitySet logicalBusinessGraphVisual");
        assertTrue(capabilities.contains("IMPORT_MARKDOWN"));
        assertTrue(capabilities.contains("EXPORT_MARKDOWN"));
        assertTrue(!capabilities.contains("EXPORT_PNG"));
        assertTrue(!capabilities.contains("EXPORT_SVG"));
        assertTrue(output.contains("ExportableOutput.projectDocument"));
        assertTrue(output.contains("formatsForLogicalBusiness"));
        assertTrue(exporter.contains("LogicalBusinessMarkdownExporter"));
    }

    @Test
    void officialSelectorShouldUseSingleUensLogicalBusinessExampleAndResourcesRemainExportable() throws IOException {
        String catalog = read("com/marcosmoreira/domainmodelstudio/application/examples/DefaultOfficialExampleCatalog.java");
        String definitions = read("com/marcosmoreira/domainmodelstudio/application/catalog/definitions/DiagramTypeDefinitionFactory.java");
        String descriptors = read("com/marcosmoreira/domainmodelstudio/infrastructure/resources/definitions/CoreAiResourceDescriptors.java")
                + "\n" + read("com/marcosmoreira/domainmodelstudio/infrastructure/resources/definitions/OfficialMinimalExampleAiResourceDescriptors.java");

        assertFalse(catalog.contains("logicalBusinessMinimalExample"));
        assertFalse(catalog.contains("logical_business_intake_optica_minimo.md"));
        assertTrue(definitions.contains("logical_business_intake_uens_gordito.md"));
        assertTrue(descriptors.contains("AiResourceDescriptorFactory.logicalBusiness"));
        assertTrue(descriptors.contains("logical_business_intake_optica_minimo.md"),
                "Óptica puede seguir como recurso IA/histórico, pero no como ejemplo oficial automático.");
        assertTrue(descriptors.contains("ejemplo-levantamiento-logico-uens-gordito"));
    }

    private static String read(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }

    private static String slice(String text, String startToken, String endToken) {
        int start = text.indexOf(startToken);
        int end = text.indexOf(endToken, start + startToken.length());
        if (start < 0 || end < 0) {
            return text;
        }
        return text.substring(start, end);
    }
}
