package com.marcosmoreira.domainmodelstudio.productization;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Guardarraíl fuente de la tanda JD-2 de JavaDoc en aplicación y servicios. */
class Jd2ApplicationJavadocServicesSourceTest {

    private static final Path ROOT = Path.of("");

    @Test
    void applicationPackagesShouldExplainLayerResponsibilities() throws IOException {
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/application/package-info.java",
                "Capa de aplicación", "sin depender de JavaFX", "orquestación de casos de uso");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/application/catalog/package-info.java",
                "fuente de verdad", "capacidades reales", "anti-fachada");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/application/logicalbusiness/derivation/package-info.java",
                "Infraestructura interna", "no hay importación automática", "fuente lógica canónica");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/application/visual/package-info.java",
                "layout visual persistente", "sin conocer JavaFX", "layout vive como dato");
    }

    @Test
    void criticalApplicationServicesShouldExposePedagogicalContracts() throws IOException {
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/application/catalog/DefaultDiagramTypeDefinitions.java",
                "Fuente oficial de definiciones", "estado", "workspace", "capacidades");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/application/logicalbusiness/LogicalBusinessValidationService.java",
                "Orquesta la validación de coherencia interna", "no genera", "hallazgos");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/application/logicalbusiness/LogicalBusinessTraceabilityService.java",
                "trazas internas navegables", "por qué existe un elemento", "pregunta bloquea");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/application/logicalbusiness/derivation/LogicalBusinessDerivationService.java",
                "borradores Markdown compatibles", "no guardan proyectos", "no sustituyen");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/application/visual/VisualLayoutService.java",
                "layout para los elementos visuales esperados", "Preserva posiciones manuales", "workspace especializado");
    }

    @Test
    void onboardingShouldIncludeApplicationReadingRoute() throws IOException {
        assertContains("docs/desarrollo/ONBOARDING_CODIGO_JAVADOC.md",
                "Aplicación: casos de uso y servicios", "DefaultCreateWorkspaceUseCase", "LogicalBusinessDerivationService");
        assertContains("docs/calidad/AUDITORIA_JAVADOC_JD2.md",
                "application/catalog", "No se documentaron getters/setters triviales");
        assertContains("docs/calidad/PLAN_TANDAS_JAVADOC.md",
                "Estado: aplicada en JD-2", "Tanda JD-3");
    }

    private static void assertContains(String relativePath, String... requiredFragments) throws IOException {
        String text = Files.readString(ROOT.resolve(relativePath));
        for (String fragment : requiredFragments) {
            assertTrue(text.contains(fragment), () -> relativePath + " no contiene: " + fragment);
        }
    }
}
