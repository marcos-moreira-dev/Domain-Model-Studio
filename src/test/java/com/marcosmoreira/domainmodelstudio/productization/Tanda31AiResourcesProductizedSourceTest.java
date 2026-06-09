package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 31: Recursos IA debe ser un contrato productizado. */
class Tanda31AiResourcesProductizedSourceTest {

    @Test
    void operationalFlowShouldHaveGrammarDescriptorAndRegistryId() throws IOException {
        String definitions = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/application/catalog/definitions/BusinessProcessDiagramTypeDefinitions.java"));
        String descriptors = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/resources/definitions/CoreAiResourceDescriptors.java"));

        assertTrue(Files.exists(Path.of("src/main/resources/ai-resources/17_flujo_operativo_gramatica.md")));
        assertTrue(definitions.contains("\"flujo-operativo-gramatica\""));
        assertTrue(descriptors.contains("\"flujo-operativo-gramatica\""));
    }

    @Test
    void rootGrammarTextsShouldNotContradictCurrentImportability() throws IOException {
        String dataDictionary = Files.readString(Path.of("src/main/resources/ai-resources/03_diccionario_datos_gramatica.md"));
        String wireframes = Files.readString(Path.of("src/main/resources/ai-resources/11_wireframes_administrativos_gramatica.md"));

        assertTrue(dataDictionary.contains("Importable por la app: sí"));
        assertTrue(dataDictionary.contains("data-dictionary"));
        assertTrue(wireframes.contains("Importable por la app: sí"));
        assertTrue(wireframes.contains("admin-wireframes"));
        assertTrue(wireframes.contains("No debe confundirse"));
    }

    @Test
    void logicalBusinessResourcesShouldDeclareSampleKind() throws IOException {
        assertSampleKind("logical_business_intake_template.md", "template");
        assertSampleKind("logical_business_intake_optica_minimo.md", "minimal-example");
        assertSampleKind("logical_business_intake_optica_gordito.md", "full-example");
    }

    @Test
    void exportedIndexShouldExposeProductizedResourceContract() throws IOException {
        String exporter = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/resources/ClasspathAiResourceExporter.java"));

        assertTrue(exporter.contains("Tipo de recurso"));
        assertTrue(exporter.contains("Uso recomendado"));
        assertTrue(exporter.contains("Contrato de importación"));
        assertTrue(exporter.contains("borradores revisables"));
    }

    private static void assertSampleKind(String fileName, String expected) throws IOException {
        Path path = Path.of("src/main/resources/ai-resources/official-markdown/levantamiento-logico", fileName);
        String text = Files.readString(path);
        assertTrue(text.contains("sample_kind: \"" + expected + "\""), fileName);
    }
}
