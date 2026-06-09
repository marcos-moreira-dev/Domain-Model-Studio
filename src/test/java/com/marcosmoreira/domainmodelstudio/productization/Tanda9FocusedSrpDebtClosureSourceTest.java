package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 9: la deuda SRP focalizada no debe abrir refactor sin bloqueo real. */
class Tanda9FocusedSrpDebtClosureSourceTest {

    private static final Path IMPL = Path.of("docs", "implementacion", "TANDA_009_DEUDA_SRP_FOCALIZADA_CIERRE.md");
    private static final Path AUDIT = Path.of("docs", "diagnostico", "ESTADO_AUDITORIA_ACTUAL.md");
    private static final Path PLAN = Path.of("docs", "raiz", "PLAN_TANDAS_RESTANTES.md");
    private static final Path MAP = Path.of("docs", "documentacion", "MAPA_DOCUMENTACION_VIVA.md");
    private static final Path RETIRED_SCRIPT = Path.of("scripts", "30-validar-tanda09-deuda-srp-focalizada.bat");
    private static final Path SCRIPTS_README = Path.of("scripts", "README.md");

    @Test
    void srpDebtClosureShouldBeDocumentedAsFocusedAuditWithoutFunctionalRefactor() throws IOException {
        String impl = read(IMPL);

        for (String token : List.of(
                "Tanda 9 — Deuda SRP focalizada",
                "sin refactor funcional",
                "sin bloqueo real",
                "ArchitectureBoundaryTest",
                "ArchitectureStrongAuditTest",
                "No se toca pantalla de inicio",
                "No se toca modelo conceptual",
                "No se toca canvas conceptual")) {
            assertTrue(impl.contains(token), () -> "Falta contrato SRP focalizado: " + token);
        }
    }

    @Test
    void liveContinuityShouldNotKeepTanda9AsFunctionalPendingWork() throws IOException {
        String plan = read(PLAN);
        String audit = read(AUDIT);
        String joined = plan + "\n" + audit;

        for (String token : List.of(
                "TANDA_009_DEUDA_SRP_FOCALIZADA_CIERRE.md",
                "Tanda 9 — Deuda SRP focalizada — aplicada",
                "sin bloqueo real",
                "no activada",
                "Sin tandas funcionales pendientes")) {
            assertTrue(joined.contains(token), () -> "Falta cierre vivo de Tanda 9: " + token);
        }

        assertFalse(plan.contains("La siguiente tanda normal solo debe ser **Tanda 9"),
                "El plan no debe seguir presentando Tanda 9 como siguiente tanda pendiente.");
    }

    @Test
    void mapShouldKeepFocusedAuditButScriptShouldBeRetired() throws IOException {
        String map = read(MAP);
        String readme = read(SCRIPTS_README);
        String joined = map + "\n" + readme;

        assertFalse(Files.exists(RETIRED_SCRIPT), "La validación SRP histórica no debe seguir como script público.");
        for (String token : List.of(
                "TANDA_009_DEUDA_SRP_FOCALIZADA_CIERRE.md",
                "ArchitectureBoundaryTest",
                "ArchitectureStrongAuditTest",
                "scripts de tandas pasadas")) {
            assertTrue(joined.contains(token), () -> "Falta trazabilidad documental Tanda 9: " + token);
        }
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
