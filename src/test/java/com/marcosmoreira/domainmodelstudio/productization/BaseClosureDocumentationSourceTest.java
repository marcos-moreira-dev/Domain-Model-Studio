package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl documental para el cierre final liviano de base antes de refactors o nuevos tipos. */
class BaseClosureDocumentationSourceTest {

    private static final Path CLOSURE_CHECKLIST = Path.of("docs/testeo/CHECKLIST_CIERRE_BASE.md");
    private static final Path POST_TANDA_29_STATUS = Path.of("docs/diagnostico/ESTADO_BASE_POST_TANDA_029.md");
    private static final Path RETIRED_CLOSURE_SCRIPT = Path.of("scripts/10-cierre-base.bat");
    private static final Path SCRIPTS_README = Path.of("scripts/README.md");
    private static final Path PLAN_TANDAS = Path.of("docs/desarrollo/PLAN_TANDAS_ACTUAL.md");

    @Test
    void closureChecklistShouldCoverTestsSmokePackagingAndDecision() throws IOException {
        String checklist = Files.readString(CLOSURE_CHECKLIST, StandardCharsets.UTF_8);

        assertTrue(checklist.contains("CIERRE-002"));
        assertTrue(checklist.contains("CIERRE-003"));
        assertTrue(checklist.contains("CIERRE-005"));
        assertTrue(checklist.contains("CIERRE-006"));
        assertTrue(checklist.contains("CIERRE-008"));
        assertTrue(checklist.contains("scripts\\02-ejecutar-tests.bat"));
        assertTrue(checklist.contains("Base aprobada para refactor pequeño"));
        assertTrue(checklist.contains("Base no aprobada"));
    }

    @Test
    void closureChecklistShouldProtectRiskyAreasBeforeLargeRefactors() throws IOException {
        String checklist = Files.readString(CLOSURE_CHECKLIST, StandardCharsets.UTF_8);
        String status = Files.readString(POST_TANDA_29_STATUS, StandardCharsets.UTF_8);

        assertTrue(checklist.contains("MainShellCommandHandler"));
        assertTrue(checklist.contains("InteractiveCanvasSurfaceView"));
        assertTrue(checklist.contains("UmlClassDiagramViewModel"));
        assertTrue(checklist.contains("SpecializedVisualSvgWriter"));
        assertTrue(checklist.contains("ClientBatchExportCoordinator"));
        assertTrue(status.contains("refactorizar shell/canvas sin smoke UI"));
        assertTrue(status.contains("tocar modelo conceptual protegido"));
        assertTrue(status.contains("confiar en PNG sin revisión visual"));
    }

    @Test
    void closureScriptShouldBeRetiredIntoCurrentRevalidationFlow() throws IOException {
        String scriptsReadme = Files.readString(SCRIPTS_README, StandardCharsets.UTF_8);
        String plan = Files.readString(PLAN_TANDAS, StandardCharsets.UTF_8);

        assertFalse(Files.exists(RETIRED_CLOSURE_SCRIPT), "El cierre histórico no debe seguir como script público.");
        assertTrue(scriptsReadme.contains("13-revalidacion-local-completa.bat"));
        assertTrue(scriptsReadme.contains("16-release-candidate.bat"));
        assertTrue(scriptsReadme.contains("scripts de tandas pasadas"));

        assertTrue(plan.contains("Tanda 38"));
        assertTrue(plan.contains("Tanda 39"));
        assertTrue(plan.contains("refactor"));
    }
}
