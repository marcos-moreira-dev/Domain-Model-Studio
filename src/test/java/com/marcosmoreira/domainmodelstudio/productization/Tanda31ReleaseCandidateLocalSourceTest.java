package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 31: la validación local Windows / RC debe quedar trazada. */
class Tanda31ReleaseCandidateLocalSourceTest {

    private static final Path IMPL = Path.of(
            "docs", "implementacion", "TANDA_031_VALIDACION_LOCAL_WINDOWS_RELEASE_CANDIDATE.md");
    private static final Path GUIDE = Path.of("docs", "testeo", "RELEASE_CANDIDATE_TANDA_31.md");
    private static final Path REPORT = Path.of(
            "docs", "testeo", "reportes", "REPORTE_RELEASE_CANDIDATE_TANDA_31.md");
    private static final Path RETIRED_SCRIPT = Path.of("scripts", "29-validar-tanda31-release-candidate-local.bat");
    private static final Path CURRENT_RC_SCRIPT = Path.of("scripts", "16-release-candidate.bat");
    private static final Path AUDIT = Path.of("docs", "diagnostico", "ESTADO_AUDITORIA_ACTUAL.md");
    private static final Path PLAN = Path.of("docs", "raiz", "PLAN_TANDAS_RESTANTES.md");
    private static final Path MAP = Path.of("docs", "documentacion", "MAPA_DOCUMENTACION_VIVA.md");
    private static final Path RELEASE = Path.of("docs", "release", "RELEASE_CANDIDATE_0_0_1.md");
    private static final Path NOTES = Path.of("docs", "release", "RELEASE_NOTES.md");
    private static final Path SCRIPTS_README = Path.of("scripts", "README.md");

    @Test
    void tanda31DocumentsShouldRecordLocalGreenEvidenceWithoutOverApprovingInstaller() throws IOException {
        String joined = read(IMPL) + "\n" + read(REPORT) + "\n" + read(AUDIT) + "\n" + read(NOTES);

        for (String token : List.of(
                "Tests run: 34, Failures: 0, Errors: 0, Skipped: 0",
                "Tests run: 1218, Failures: 0, Errors: 0, Skipped: 0",
                "BUILD SUCCESS",
                "base automatizada verde",
                "app-image/MSI/smoke manual",
                "RC instalable aprobado")) {
            assertTrue(joined.contains(token), () -> "Falta evidencia/criterio RC31: " + token);
        }
    }

    @Test
    void retiredFocusedScriptShouldBeReplacedByCurrentReleaseCandidateFlow() throws IOException {
        String rc = read(CURRENT_RC_SCRIPT);

        assertFalse(Files.exists(RETIRED_SCRIPT), "El script RC31 histórico no debe seguir como entrada pública.");
        for (String token : List.of(
                "scripts\\13-revalidacion-local-completa.bat",
                "scripts\\14-app-image-completa.bat",
                "scripts\\15-msi-completo.bat",
                "scripts\\internal\\verify-release-candidate.bat")) {
            assertTrue(rc.contains(token), () -> "Falta etapa del RC vigente: " + token);
        }
    }

    @Test
    void rcGuideShouldRequireAppImageMsiAndSmokeBeforeInstallerApproval() throws IOException {
        String guide = read(GUIDE);

        for (String token : List.of(
                "scripts\\14-app-image-completa.bat",
                "scripts\\15-msi-completo.bat",
                "scripts\\16-release-candidate.bat",
                "dist\\staging\\APP_IMAGE_MANIFEST.txt",
                "dist\\installer\\MSI_MANIFEST.txt",
                "dist\\release\\RELEASE_CANDIDATE_MANIFEST.txt",
                "No aprobado")) {
            assertTrue(guide.contains(token), () -> "Falta criterio en guía RC31: " + token);
        }
    }

    @Test
    void liveContinuityDocsShouldPointToCurrentRcSurface() throws IOException {
        String joined = read(AUDIT) + "\n" + read(PLAN) + "\n" + read(MAP) + "\n" + read(RELEASE) + "\n" + read(SCRIPTS_README);

        for (String token : List.of(
                "TANDA_031_VALIDACION_LOCAL_WINDOWS_RELEASE_CANDIDATE.md",
                "RELEASE_CANDIDATE_TANDA_31.md",
                "REPORTE_RELEASE_CANDIDATE_TANDA_31.md",
                "16-release-candidate.bat",
                "Tanda 9 — Deuda SRP focalizada")) {
            assertTrue(joined.contains(token), () -> "Falta continuidad RC31: " + token);
        }
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
