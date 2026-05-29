package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl documental para la planificación de mejoras UML Secuencia. */
class Tanda28SequenceImprovementPlanSourceTest {

    @Test
    void sequenceImprovementPlanKeepsTheoryAndToolingAligned() throws IOException {
        String plan = read("docs/arquitectura/18_plan_uml_secuencia_fragmentos_combinados.md");
        assertAll(
                () -> assertTrue(plan.contains("fragmentos combinados")),
                () -> assertTrue(plan.contains("SequenceFragmentKind")),
                () -> assertTrue(plan.contains("SequenceFragmentOperand")),
                () -> assertTrue(plan.contains("ALT") && plan.contains("OPT") && plan.contains("LOOP") && plan.contains("PAR")),
                () -> assertTrue(plan.contains("guardas")),
                () -> assertTrue(plan.contains("SpecializedSvgSequenceWriter")),
                () -> assertTrue(plan.contains("ManualFigureUmlSequenceFigures")),
                () -> assertTrue(plan.contains("no adopta bendpoints libres"))
        );
    }

    @Test
    void rootPlanMovesReleaseCandidateAfterSequencePlanning() throws IOException {
        String root = read("docs/raiz/PLAN_TANDAS_RESTANTES.md");
        assertAll(
                () -> assertTrue(root.contains("Tanda 35 — Refactor conservador del canvas conceptual legacy")),
                () -> assertTrue(root.contains("Tanda 36 — Artefactos compatibles legacy del Levantamiento lógico")),
                () -> assertTrue(root.contains("Tanda 37 — CSS y recursos UI post-refactor")),
                () -> assertTrue(root.contains("Tanda 38 — Smoke integral post-refactor")),
                () -> assertTrue(root.contains("Tanda 39 — Release candidate post-refactor"))
        );
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
