package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 43: instalable Windows y JavaDoc deben quedar auditados antes del cierre final. */
class Tanda43InstallerJavadocAuditSourceTest {

    private static final Path IMPL = Path.of("docs", "implementacion", "TANDA_043_INSTALABLE_JAVADOC_AUDITORIA.md");
    private static final Path INSTALLER_GUIDE = Path.of("docs", "testeo", "INSTALABLE_WINDOWS_RC_GUIA.md");
    private static final Path INSTALLER_REPORT = Path.of("docs", "testeo", "reportes", "REPORTE_INSTALABLE_WINDOWS_RC.md");
    private static final Path JAVADOC_AUDIT = Path.of("docs", "calidad", "AUDITORIA_JAVADOC_2026_05_24.md");
    private static final Path JAVADOC_PLAN = Path.of("docs", "calidad", "PLAN_TANDAS_JAVADOC.md");
    private static final Path SCRIPT_JAVADOC = Path.of("scripts", "31-generar-javadoc.bat");
    private static final Path RETIRED_SCRIPT_VALIDATE = Path.of("scripts", "32-validar-instalable-y-javadoc.bat");
    private static final Path SCRIPTS_README = Path.of("scripts", "README.md");
    private static final Path POM = Path.of("pom.xml");

    @Test
    void installerGuideShouldKeepAppImageMsiAndRcApprovalSeparated() throws IOException {
        String joined = read(IMPL) + "\n" + read(INSTALLER_GUIDE) + "\n" + read(INSTALLER_REPORT);

        for (String token : List.of(
                "scripts\\14-app-image-completa.bat",
                "scripts\\15-msi-completo.bat",
                "scripts\\16-release-candidate.bat",
                "dist\\staging\\APP_IMAGE_MANIFEST.txt",
                "dist\\installer\\MSI_MANIFEST.txt",
                "dist\\release\\RELEASE_CANDIDATE_MANIFEST.txt",
                "No aprobado hasta completar smoke manual",
                "app-image/MSI/smoke manual")) {
            assertTrue(joined.contains(token), () -> "Falta contrato instalable: " + token);
        }
    }

    @Test
    void javadocAuditShouldExposeTypeCoverageAndMethodDebt() throws IOException {
        String audit = read(JAVADOC_AUDIT);

        for (String token : List.of(
                "Cobertura de tipos públicos",
                "98.4%",
                "Cobertura de miembros públicos",
                "0.6%",
                "El instalable no queda bloqueado por JavaDoc",
                "sí conviene planificar tandas de documentación de código")) {
            assertTrue(audit.contains(token), () -> "Falta lectura JavaDoc: " + token);
        }
    }

    @Test
    void javadocPlanShouldPrioritizePedagogicalContractsByLayer() throws IOException {
        String plan = read(JAVADOC_PLAN);

        for (String token : List.of(
                "Tanda JD-1 — Contratos públicos de dominio",
                "Tanda JD-2 — Casos de uso y servicios de aplicación",
                "Tanda JD-3 — Infraestructura de importación, persistencia y exportación",
                "Tanda JD-4 — Presentation, workbenches, SideDock y canvas transversal",
                "Tanda JD-5 — Sitio JavaDoc y guardarraíl de cobertura",
                "No debe repetir el nombre del método")) {
            assertTrue(plan.contains(token), () -> "Falta tanda JavaDoc: " + token);
        }
    }

    @Test
    void scriptsAndPomShouldExposeJavadocGenerationWithoutExtraValidationWrappers() throws IOException {
        String joined = read(SCRIPT_JAVADOC) + "\n" + read(SCRIPTS_README) + "\n" + read(POM);

        assertFalse(Files.exists(RETIRED_SCRIPT_VALIDATE), "El wrapper JavaDoc/instalable histórico no debe seguir público.");
        for (String token : List.of(
                "javadoc:javadoc",
                "target\\site\\apidocs\\index.html",
                "maven-javadoc-plugin",
                "doclint",
                "31-generar-javadoc.bat")) {
            assertTrue(joined.contains(token), () -> "Falta entrada JavaDoc vigente: " + token);
        }
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
