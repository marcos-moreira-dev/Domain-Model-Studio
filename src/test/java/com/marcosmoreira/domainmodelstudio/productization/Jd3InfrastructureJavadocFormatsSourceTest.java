package com.marcosmoreira.domainmodelstudio.productization;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Guardarraíl fuente de la tanda JD-3 de JavaDoc en infraestructura y formatos. */
class Jd3InfrastructureJavadocFormatsSourceTest {

    private static final Path ROOT = Path.of("");

    @Test
    void infrastructurePackagesShouldExplainFormatBoundaries() throws IOException {
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/package-info.java",
                "Capa de infraestructura", "formatos concretos", "roundtrip");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown/package-info.java",
                "puente humano/IA", "diagram_type", "reimportarse sin perder semántica");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/package-info.java",
                "formato de proyecto", "roundtrip estricto", "Grafo lógico");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/resources/package-info.java",
                "gramáticas", "prompt maestro", "importable como proyecto");
    }

    @Test
    void criticalInfrastructureAdaptersShouldDocumentRoundtripContracts() throws IOException {
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown/DiagramMarkdownImportDispatcher.java",
                "Entrada única", "diagram_type", "capacidades reales");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown/LogicalBusinessGraphMarkdownParser.java",
                "Grafo lógico del negocio", "nodos tipados", "fallas de importación");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown/LogicalBusinessGraphMarkdownExporter.java",
                "plantilla mínima reimportable", "leyenda de abreviaciones", "ida y vuelta con IA");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/DmsProjectJsonReader.java",
                "Lector JSON manual", "payload es inconsistente", "proyecto degradado");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/DmsProjectJsonWriter.java",
                "formato durable", "layout visual", "diagnóstico de roundtrip");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/svg/specialized/SpecializedVisualSvgDiagramExporter.java",
                "salida reproducible", "no una captura accidental", "independiente de JavaFX");
    }

    @Test
    void onboardingShouldIncludeInfrastructureReadingRoute() throws IOException {
        assertContains("docs/desarrollo/ONBOARDING_CODIGO_JAVADOC.md",
                "Infraestructura: formatos, roundtrip y recursos", "DmsProjectJsonReader", "ClasspathAiResourceExporter");
        assertContains("docs/calidad/AUDITORIA_JAVADOC_JD3.md",
                "infrastructure/markdown", ".dms JSON", "No se documentaron getters/setters triviales");
        assertContains("docs/calidad/PLAN_TANDAS_JAVADOC.md",
                "Estado: aplicada en JD-3", "Tanda JD-8");
    }

    private static void assertContains(String relativePath, String... requiredFragments) throws IOException {
        String text = Files.readString(ROOT.resolve(relativePath));
        for (String fragment : requiredFragments) {
            assertTrue(text.contains(fragment), () -> relativePath + " no contiene: " + fragment);
        }
    }
}
