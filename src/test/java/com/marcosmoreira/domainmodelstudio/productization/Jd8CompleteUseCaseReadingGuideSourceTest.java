package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl fuente de JD-8: guía de lectura por casos de uso completos. */
class Jd8CompleteUseCaseReadingGuideSourceTest {

    private static final Path ROOT = Path.of("");

    @Test
    void jd8DocumentationAndScriptPolicyShouldBeRegistered() throws IOException {
        assertContains("docs/desarrollo/GUIA_CASOS_USO_COMPLETOS.md",
                "casos de uso completos", "Ruta JD-8");
        assertContains("docs/calidad/AUDITORIA_JAVADOC_JD8.md",
                "JD-8 aplicada", "No modifica lógica funcional");
        assertContains("docs/implementacion/TANDA_JD_008_GUIA_CASOS_USO_COMPLETOS.md",
                "Recorridos incluidos", "No modifica lógica funcional");
        assertContains("docs/calidad/PLAN_TANDAS_JAVADOC.md",
                "Estado: aplicada en JD-8", "JD-9");
        assertContains("docs/documentacion/MAPA_DOCUMENTACION_VIVA.md",
                "Guía de lectura por casos de uso completos — JD-8", "GUIA_CASOS_USO_COMPLETOS.md");
        assertContains("scripts/README.md",
                "31-generar-javadoc.bat", "scripts de tandas pasadas");
    }

    @Test
    void packageJavadocsShouldMentionJd8UseCaseReading() throws IOException {
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/domain/package-info.java",
                "Ruta JD-8", "casos de uso completos");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/application/package-info.java",
                "Ruta JD-8", "intención del usuario");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/package-info.java",
                "Ruta JD-8", "Markdown");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/package-info.java",
                "Ruta JD-8", "SideDock");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/bootstrap/package-info.java",
                "Ruta JD-8", "conecta cada contrato");
    }

    @Test
    void jd8UsesSharedJavadocGenerationEntryPoint() throws IOException {
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
