package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl documental para el smoke UI mínimo previo a refactors grandes. */
class UiSmokeMinimumPlanSourceTest {

    private static final Path UI_SMOKE_PLAN = Path.of("docs/testeo/UI_SMOKE_MINIMO_EJECUTABLE.md");
    private static final Path UI_SMOKE_REPORT = Path.of("docs/testeo/reportes/REPORTE_SMOKE_UI_MINIMO.md");
    private static final Path RETIRED_UI_SMOKE_SCRIPT = Path.of("scripts/09-smoke-ui-minimo.bat");
    private static final Path UI_E2E_PLAN = Path.of("docs/testeo/PLAN_PRUEBAS_UI_E2E.md");
    private static final Path SCRIPTS_README = Path.of("scripts/README.md");

    @Test
    void uiSmokePlanShouldCoverCriticalRuntimeFlowsBeforeLargeRefactors() throws IOException {
        String plan = Files.readString(UI_SMOKE_PLAN, StandardCharsets.UTF_8);

        assertTrue(plan.contains("UI-SMOKE-001"));
        assertTrue(plan.contains("UI-SMOKE-002"));
        assertTrue(plan.contains("UI-SMOKE-003"));
        assertTrue(plan.contains("UI-SMOKE-004"));
        assertTrue(plan.contains("UI-SMOKE-005"));
        assertTrue(plan.contains("UI-SMOKE-007"));
        assertTrue(plan.contains("UI-SMOKE-008"));
        assertTrue(plan.contains("toolbar contextual"));
        assertTrue(plan.contains("SideDock contextual"));
        assertTrue(plan.contains("Guardar/reabrir `.dms`"));
        assertTrue(plan.contains("PNG no sale blanco"));
    }

    @Test
    void uiSmokePlanShouldNameFakesRequiredForFutureAutomation() throws IOException {
        String plan = Files.readString(UI_SMOKE_PLAN, StandardCharsets.UTF_8);

        assertTrue(plan.contains("FileChooser falso"));
        assertTrue(plan.contains("CodeEditorLauncher falso"));
        assertTrue(plan.contains("no debe abrir una aplicación real"));
        assertTrue(plan.contains("carpeta temporal conocida"));
    }

    @Test
    void uiSmokeReportShouldRemainDiscoverableWithoutDedicatedScriptWrapper() throws IOException {
        String uiPlan = Files.readString(UI_E2E_PLAN, StandardCharsets.UTF_8);
        String report = Files.readString(UI_SMOKE_REPORT, StandardCharsets.UTF_8);
        String readme = Files.readString(SCRIPTS_README, StandardCharsets.UTF_8);

        assertTrue(uiPlan.contains("UI_SMOKE_MINIMO_EJECUTABLE.md"));
        assertTrue(uiPlan.contains("REPORTE_SMOKE_UI_MINIMO.md"));
        assertTrue(report.contains("UI-SMOKE-001 Arranque y home"));
        assertTrue(report.contains("UI-SMOKE-011 Exportación por lote"));
        assertTrue(report.contains("Aprobado para refactor pequeño"));
        assertTrue(readme.contains("13-revalidacion-local-completa.bat"));
        assertFalse(Files.exists(RETIRED_UI_SMOKE_SCRIPT), "El smoke UI manual ya no debe tener wrapper público propio.");
    }
}
