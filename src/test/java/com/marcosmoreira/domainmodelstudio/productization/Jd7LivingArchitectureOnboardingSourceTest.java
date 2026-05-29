package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl fuente de JD-7: onboarding vivo de arquitectura. */
class Jd7LivingArchitectureOnboardingSourceTest {

    private static final Path ROOT = Path.of("");

    @Test
    void jd7DocumentationAndPlanShouldBeRegistered() throws IOException {
        assertContains("docs/calidad/AUDITORIA_JAVADOC_JD7.md",
                "JD-7 aplicada", "No se modificó lógica funcional");
        assertContains("docs/implementacion/TANDA_JD_007_ONBOARDING_VIVO_ARQUITECTURA.md",
                "Dominio → aplicación → infraestructura → presentación", "No se modificó lógica funcional");
        assertContains("docs/calidad/PLAN_TANDAS_JAVADOC.md",
                "Estado: aplicada en JD-7", "JD-8", "JD-9");
        assertContains("docs/documentacion/MAPA_DOCUMENTACION_VIVA.md",
                "Onboarding vivo de arquitectura — JD-7", "ONBOARDING_ARQUITECTURA_RUTA_ESTUDIO.md");
    }

    @Test
    void javadocPublicScriptShouldRemainSingleEntryPointAfterJd7() throws IOException {
        assertContains("scripts/31-generar-javadoc.bat",
                "javadoc:javadoc", "target\\site\\apidocs\\index.html");
        assertContains("scripts/README.md",
                "31-generar-javadoc.bat", "scripts de tandas pasadas");
    }

    private static void assertContains(String relativePath, String... requiredFragments) throws IOException {
        String text = Files.readString(ROOT.resolve(relativePath));
        for (String fragment : requiredFragments) {
            assertTrue(text.contains(fragment), () -> relativePath + " no contiene: " + fragment);
        }
    }
}
