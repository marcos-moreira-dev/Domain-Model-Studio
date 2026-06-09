package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 38A: JavaDoc post-refactor solo donde aporta contrato vigente. */
class JavadocPostRefactorSourceTest {

    private static final Path ROOT = Path.of("");

    @Test
    void postRefactorJavadocTandaShouldDeclareLimitedScope() throws IOException {
        assertContains("docs/desarrollo/TANDA_038A_JAVADOC_POST_REFACTOR.md",
                "Revisar JavaDoc únicamente donde el refactor de Tandas 28–37 cambió fronteras arquitectónicas reales",
                "No modifica lógica funcional",
                "scripts\\31-generar-javadoc.bat",
                "smoke integral post-refactor");
        assertContains("docs/calidad/AUDITORIA_JAVADOC_POST_REFACTOR.md",
                "aplicada en Tanda 38A",
                "application.services",
                "infrastructure.markdown",
                "presentation.workbench",
                "application.logicalbusiness.derivation");
    }

    @Test
    void packageJavadocsShouldExplainRefactoredBoundaries() throws IOException {
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/application/services/package-info.java",
                "Fachadas de servicios de aplicación agrupadas por familia funcional",
                "ApplicationServices",
                "getters",
                "triviales");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/application/catalog/package-info.java",
                "application.catalog.definitions",
                "agregador público");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/package-info.java",
                "no se cambian claves JSON",
                "no se introducen migraciones");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown/package-info.java",
                "sin cambiar las gramáticas",
                "sin cambiar ejemplos oficiales",
                "sin cambiar comportamiento visible");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/workbench/package-info.java",
                "ProjectChangeSupport",
                "superclase");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/package-info.java",
                "Canvas conceptual legacy",
                "no cambió formato",
                "ConceptualAnchorResolver");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/application/umlclass/package-info.java",
                "SourceCodeUmlSummarySelectionPolicy",
                "proyectos grandes");
    }

    @Test
    void classJavadocsShouldNameTheNoBehaviorChangeContracts() throws IOException {
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown/MarkdownImportDocument.java",
                "sin cambiar las gramáticas",
                "sin cambiar ejemplos oficiales",
                "sin cambiar comportamiento");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/DmsProjectSpecializedPayload.java",
                "no define un nuevo schema",
                "mismas claves opcionales");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/workbench/ProjectChangeSupport.java",
                "No decide dirty state",
                "Tanda 33");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/application/umlclass/SourceCodeUmlSummarySelectionPolicy.java",
                "no cambia el parsing Java/TypeScript",
                "layout visual");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/bootstrap/ApplicationServicesFactory.java",
                "La capa de presentación recibe la fachada ya armada");
    }

    @Test
    void publicJavadocEntryPointShouldStaySingleAndCurrent() throws IOException {
        assertContains("scripts/README.md",
                "31-generar-javadoc.bat",
                "target\\site\\apidocs\\index.html");
        assertContains("docs/desarrollo/JAVADOC_SITIO_GUIA.md",
                "scripts\\31-generar-javadoc.bat",
                "scripts\\02-ejecutar-tests.bat",
                "Tanda 38A revisa JavaDoc post-refactor");
        String guide = Files.readString(ROOT.resolve("docs/desarrollo/JAVADOC_SITIO_GUIA.md"));
        assertFalse(guide.contains("37-validar-jd5-javadoc-sitio-cobertura.bat"));
        assertContains("docs/calidad/PLAN_TANDAS_JAVADOC.md",
                "Tanda 38A — Revisión JavaDoc post-refactor",
                "scripts/31-generar-javadoc.bat",
                "Suite global: scripts/02-ejecutar-tests.bat");
    }

    @Test
    void mainSourcesAndAiResourcesShouldNotKeepOldLogicalBusinessPromises() throws IOException {
        assertMainTreeDoesNotContain("src/main/java", "fuente madre");
        assertMainTreeDoesNotContain("src/main/java", "vistas derivadas");
        assertMainTreeDoesNotContain("src/main/java", "derivaciones revisables");
        assertMainTreeDoesNotContain("src/main/resources", "fuente madre");
        assertMainTreeDoesNotContain("src/main/resources", "vistas derivadas");
        assertMainTreeDoesNotContain("src/main/resources", "derivaciones revisables");
    }

    private static void assertContains(String relativePath, String... requiredFragments) throws IOException {
        String text = Files.readString(ROOT.resolve(relativePath));
        for (String fragment : requiredFragments) {
            assertTrue(text.contains(fragment), () -> relativePath + " no contiene: " + fragment);
        }
    }

    private static void assertMainTreeDoesNotContain(String relativeRoot, String forbidden) throws IOException {
        try (var paths = Files.walk(ROOT.resolve(relativeRoot))) {
            for (Path path : paths.filter(Files::isRegularFile).toList()) {
                String name = path.getFileName().toString();
                if ((name.endsWith(".java") || name.endsWith(".md"))
                        && Files.readString(path).contains(forbidden)) {
                    assertFalse(true, path + " contiene lenguaje obsoleto: " + forbidden);
                }
            }
        }
    }
}
