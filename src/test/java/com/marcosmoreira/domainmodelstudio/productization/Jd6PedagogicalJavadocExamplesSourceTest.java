package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl fuente de JD-6: ejemplos pedagógicos JavaDoc. */
class Jd6PedagogicalJavadocExamplesSourceTest {

    private static final Path ROOT = Path.of("");

    @Test
    void jd6DocumentationShouldGuideStudyWithExamples() throws IOException {
        assertContains("docs/desarrollo/JAVADOC_EJEMPLOS_PEDAGOGICOS.md",
                "JavaDoc: ejemplos pedagógicos", "Ruta de lectura sugerida", "Preguntas de estudio");
        assertContains("docs/calidad/AUDITORIA_JAVADOC_JD6.md",
                "JD-6 aplicada", "Ejemplo pedagógico", "sin cambios");
        assertContains("docs/implementacion/TANDA_JD_006_JAVADOC_EJEMPLOS_PEDAGOGICOS.md",
                "No se modificó lógica funcional", "Ejemplo pedagógico");
        assertContains("docs/desarrollo/ONBOARDING_CODIGO_JAVADOC.md",
                "Ejemplos pedagógicos", "JAVADOC_EJEMPLOS_PEDAGOGICOS.md");
    }

    @Test
    void planAndScriptsShouldExposeJd6AsDocumentedLineWithoutDedicatedWrapper() throws IOException {
        assertContains("docs/calidad/PLAN_TANDAS_JAVADOC.md",
                "Estado: aplicada en JD-6", "JD-7", "JD-8", "JD-9");
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
