package com.marcosmoreira.domainmodelstudio.productization;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Guardarraíl fuente de la tanda JD-1 de JavaDoc y onboarding. */
class Jd1DomainJavadocOnboardingSourceTest {

    private static final Path ROOT = Path.of("");

    @Test
    void domainContractsShouldExposePedagogicalJavadoc() throws IOException {
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/domain/logicalbusinessgraph/LogicalBusinessGraphDocument.java",
                "Agregado raíz inmutable", "Las operaciones {@code with...}", "semanticIssues");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/domain/logicalbusinessgraph/LogicalBusinessGraphRelationKind.java",
                "matriz de compatibilidad", "canConnect", "gramática del grafo lógico");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/domain/logicalbusiness/LogicalBusinessDocument.java",
                "fuente lógica canónica", "sin prometer generación automática", "coherencia interna");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/domain/layout/package-info.java",
                "valores persistibles", "guardar", "exportar");
    }

    @Test
    void onboardingShouldTeachCodeReadingAndJavadocGeneration() throws IOException {
        assertContains("docs/desarrollo/ONBOARDING_CODIGO_JAVADOC.md",
                "dominio → aplicación → infraestructura → presentación", "LogicalBusinessGraphDocument", "scripts\\31-generar-javadoc.bat");
        assertContains("docs/desarrollo/ONBOARDING.md",
                "Lectura del código y JavaDoc", "ONBOARDING_CODIGO_JAVADOC.md");
        assertContains("docs/calidad/AUDITORIA_JAVADOC_JD1.md",
                "JD-1 dominio y onboarding", "No documentar getters triviales");
    }

    @Test
    void javadocPlanShouldKeepRemainingRoadmapVisible() throws IOException {
        assertContains("docs/calidad/PLAN_TANDAS_JAVADOC.md",
                "Estado: aplicada en JD-1", "Tanda JD-2", "Tanda JD-7");
    }

    private static void assertContains(String relativePath, String... requiredFragments) throws IOException {
        String text = Files.readString(ROOT.resolve(relativePath));
        for (String fragment : requiredFragments) {
            assertTrue(text.contains(fragment), () -> relativePath + " no contiene: " + fragment);
        }
    }
}
