package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl fuente de JD-9: ADRs pedagógicos y decisiones de diseño. */
class Jd9PedagogicalArchitecturalDecisionRecordsSourceTest {

    private static final Path ROOT = Path.of("");

    @Test
    void jd9StudyDocumentsShouldStayLinked() throws IOException {
        assertContains("docs/desarrollo/ADR_DECISIONES_DISENO_PEDAGOGICAS.md",
                "ADRs pedagógicos", "decisiones de diseño");
        assertContains("docs/desarrollo/ONBOARDING_CODIGO_JAVADOC.md",
                "ADRs pedagógicos", "ADR_DECISIONES_DISENO_PEDAGOGICAS.md");
        assertContains("docs/desarrollo/ONBOARDING_ARQUITECTURA_RUTA_ESTUDIO.md",
                "JD-9", "decisiones de diseño");
        assertContains("docs/desarrollo/GUIA_CASOS_USO_COMPLETOS.md",
                "ADR_DECISIONES_DISENO_PEDAGOGICAS.md", "por qué");
    }

    @Test
    void packageJavadocsShouldMentionJd9DecisionReading() throws IOException {
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/domain/package-info.java",
                "Ruta JD-9", "decisión de diseño");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/application/package-info.java",
                "Ruta JD-9", "alternativa descartada");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/package-info.java",
                "Ruta JD-9", "formato");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/package-info.java",
                "Ruta JD-9", "SideDock");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/bootstrap/package-info.java",
                "Ruta JD-9", "ensamblaje");
    }

    @Test
    void jd9DocumentationAndScriptPolicyShouldBeRegistered() throws IOException {
        assertContains("docs/calidad/AUDITORIA_JAVADOC_JD9.md",
                "JD-9 aplicada", "sin cambios funcionales");
        assertContains("docs/implementacion/TANDA_JD_009_ADRS_PEDAGOGICOS_DECISIONES_DISENO.md",
                "Decisiones cubiertas", "sin cambios funcionales");
        assertContains("docs/calidad/PLAN_TANDAS_JAVADOC.md",
                "Estado: aplicada en JD-9", "línea JavaDoc queda cerrada");
        assertContains("docs/documentacion/MAPA_DOCUMENTACION_VIVA.md",
                "ADRs pedagógicos y decisiones de diseño — JD-9", "ADR_DECISIONES_DISENO_PEDAGOGICAS.md");
        assertContains("docs/implementacion/00_indice_implementacion.md",
                "TANDA_JD_009_ADRS_PEDAGOGICOS_DECISIONES_DISENO.md");
        assertContains("scripts/README.md",
                "31-generar-javadoc.bat", "scripts de tandas pasadas");
    }

    @Test
    void jd9UsesSharedJavadocGenerationEntryPoint() throws IOException {
        assertContains("scripts/31-generar-javadoc.bat",
                "javadoc:javadoc", "target\\site\\apidocs\\index.html");
    }

    private static void assertContains(String relativePath, String... requiredFragments) throws IOException {
        String text = Files.readString(ROOT.resolve(relativePath));
        for (String fragment : requiredFragments) {
            assertTrue(text.contains(fragment), () -> relativePath + " no contiene: " + fragment);
        }
    }
}
