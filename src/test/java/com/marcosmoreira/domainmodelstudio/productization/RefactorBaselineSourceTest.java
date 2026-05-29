package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 25: el refactor integral debe partir de baseline y no de impulsos masivos. */
class RefactorBaselineSourceTest {

    private static final Path BASELINE = Path.of(
            "docs", "desarrollo", "refactor", "BASELINE_REFACTOR_TANDA_025.md");
    private static final Path PLAN = Path.of(
            "docs", "desarrollo", "refactor", "PLAN_REFACTOR_SOLID.md");
    private static final Path MAP = Path.of(
            "docs", "desarrollo", "refactor", "MAPA_SEGURO_REFACTOR.md");
    private static final Path TANDA = Path.of(
            "docs", "desarrollo", "TANDA_025_BASELINE_REFACTOR_APLICACION.md");

    @Test
    void baselineShouldFreezeCurrentRefactorScopeAndMetrics() throws IOException {
        String baseline = read(BASELINE);

        assertTrue(baseline.contains("baseline vigente para refactor post-Levantamiento lógico"));
        assertTrue(baseline.contains("1192"));
        assertTrue(baseline.contains("110142"));
        assertTrue(baseline.contains("550"));
        assertTrue(baseline.contains("916"));
        assertTrue(baseline.contains("45"));
        assertTrue(baseline.contains("No se refactoriza una clase por estar grande"));
        assertTrue(baseline.contains("si no hay beneficio claro, no se toca"));
    }

    @Test
    void baselineShouldDefineSafeImplementationOrder() throws IOException {
        String baseline = read(BASELINE);

        assertInOrder(baseline,
                "Tanda 26 — Limpieza controlada de scripts",
                "Tanda 27 — Limpieza documental viva",
                "Tanda 28 — `ApplicationServices` por familias",
                "Tanda 29 — Shell/comandos",
                "Tanda 30 — Catálogos, capacidades y recursos oficiales",
                "Tanda 31 — Persistencia `.dms`",
                "Tanda 32 — Parsers/exporters Markdown");
    }

    @Test
    void refactorPlanShouldNotTreatConceptualModelAsDogmaticallyFrozen() throws IOException {
        String plan = read(PLAN);

        assertTrue(plan.contains("El refactor no debe cambiar comportamiento visible"));
        assertTrue(plan.contains("si no es necesario, no se toca"));
        assertTrue(plan.contains("El modelo conceptual ya no se trata como intocable por principio"));
        assertTrue(plan.contains("diagnóstico primero, extracción después si conviene"));
        assertFalse(plan.contains("zona sagrada"), "El refactor vigente no debe usar lenguaje dogmático para zonas protegidas.");
    }

    @Test
    void safeMapShouldDefineBoundariesBeforeTouchingCode() throws IOException {
        String map = read(MAP);

        assertTrue(map.contains("incremental, medible y reversible"));
        assertTrue(map.contains("Tocar primero"));
        assertTrue(map.contains("Tocar solo con diagnóstico específico"));
        assertTrue(map.contains("application` no debe depender de `presentation"));
        assertTrue(map.contains("Un Markdown histórico vuelve a aparecer como plan vigente"));
    }

    @Test
    void tanda25DocumentShouldDeclareNoFunctionalChange() throws IOException {
        String tanda = read(TANDA);

        assertTrue(tanda.contains("Baseline técnico de refactor integral"));
        assertTrue(tanda.contains("No se toca"));
        assertTrue(tanda.contains("UX visible"));
        assertTrue(tanda.contains("Parser/exporter Markdown"));
        assertTrue(tanda.contains("Tanda 26 — Limpieza controlada de scripts"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    private static void assertInOrder(String text, String... fragments) {
        int last = -1;
        for (String fragment : fragments) {
            int index = text.indexOf(fragment);
            assertTrue(index >= 0, () -> "No se encontró: " + fragment);
            assertTrue(index > last, () -> "Fuera de orden: " + fragment);
            last = index;
        }
    }
}
