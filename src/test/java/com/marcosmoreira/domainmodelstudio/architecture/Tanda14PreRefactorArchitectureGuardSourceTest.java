package com.marcosmoreira.domainmodelstudio.architecture;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Guardarraíles previos al refactor masivo para no contaminar componentes transversales. */
class Tanda14PreRefactorArchitectureGuardSourceTest {

    private static final Path MAIN = Path.of("src", "main", "java");
    private static final Path BASE = MAIN.resolve("com/marcosmoreira/domainmodelstudio".replace('/', java.io.File.separatorChar));

    @Test
    void interactiveCanvasMustRemainTransversalAndAvoidDiagramSpecificImports() throws IOException {
        List<String> violations = new ArrayList<>();
        for (Path file : javaFiles(BASE.resolve("presentation").resolve("interactivecanvas"))) {
            String source = Files.readString(file, StandardCharsets.UTF_8);
            if (source.contains("presentation.freegraph") || source.contains("presentation.umlclass")) {
                violations.add(relative(file) + " importa presentaciones específicas desde interactivecanvas");
            }
            if (source.contains("domain.freegraph") || source.contains("domain.umlclass")) {
                violations.add(relative(file) + " importa dominios específicos desde interactivecanvas");
            }
        }
        assertNoViolations(violations);
    }

    @Test
    void specializedDiagramsMustNotDependOnTheProtectedConceptualCanvasPackage() throws IOException {
        List<String> violations = new ArrayList<>();
        for (Path folder : List.of(
                BASE.resolve("presentation").resolve("freegraph"),
                BASE.resolve("presentation").resolve("umlclass"),
                BASE.resolve("presentation").resolve("architecture"),
                BASE.resolve("presentation").resolve("behavior"))) {
            for (Path file : javaFiles(folder)) {
                String source = Files.readString(file, StandardCharsets.UTF_8);
                if (source.contains("presentation.canvas")) {
                    violations.add(relative(file) + " depende del canvas protegido del modelo conceptual");
                }
            }
        }
        assertNoViolations(violations);
    }

    @Test
    void productDocumentationMustKeepTheConceptualModelProtectionRule() throws IOException {
        Path baseline = Path.of("docs", "desarrollo", "TANDA_000_BASELINE_Y_REGLAS.md");
        Path plan = Path.of("docs", "desarrollo", "PLAN_TANDAS_ACTUAL.md");
        String baselineText = Files.readString(baseline, StandardCharsets.UTF_8).toLowerCase();
        String planText = Files.readString(plan, StandardCharsets.UTF_8).toLowerCase();

        assertTrue(baselineText.contains("modelo conceptual") && baselineText.contains("zona protegida"),
                "La regla de modelo conceptual protegido debe seguir documentada en la línea base.");
        assertTrue(planText.contains("refactor") && planText.contains("tanda 38") && planText.contains("tanda 39"),
                "El plan debe conservar la continuidad vigente del refactor y su cierre post-refactor.");
    }

    private static List<Path> javaFiles(Path folder) throws IOException {
        if (!Files.isDirectory(folder)) {
            return List.of();
        }
        try (Stream<Path> stream = Files.walk(folder)) {
            return stream.filter(path -> path.toString().endsWith(".java")).toList();
        }
    }

    private static void assertNoViolations(List<String> violations) {
        assertTrue(violations.isEmpty(), "Violaciones detectadas:\n" + String.join("\n", violations));
    }

    private static String relative(Path file) {
        return Path.of("").toAbsolutePath().relativize(file.toAbsolutePath()).toString().replace('\\', '/');
    }
}
