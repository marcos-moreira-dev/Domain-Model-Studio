package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl PKG-1: app-image/MSI/RC deben dejar evidencia auditable antes del RC final. */
class PackagingAuditSourceTest {

    private static final Path REQUIRE_ENV = Path.of("scripts", "internal", "require-packaging-env.bat");
    private static final Path METADATA_PS = Path.of("scripts", "internal", "write-artifact-metadata.ps1");
    private static final Path PREPARE = Path.of("scripts", "internal", "prepare-jpackage-input.bat");
    private static final Path APP_IMAGE = Path.of("scripts", "internal", "create-app-image.bat");
    private static final Path MSI = Path.of("scripts", "internal", "create-msi-installer.bat");
    private static final Path RC = Path.of("scripts", "internal", "verify-release-candidate.bat");
    private static final Path PUBLIC_MSI = Path.of("scripts", "15-msi-completo.bat");
    private static final Path PUBLIC_RC = Path.of("scripts", "16-release-candidate.bat");
    private static final Path DOC = Path.of("docs", "desarrollo", "TANDA_PKG_001_EMPAQUETADO_AUDITADO.md");

    @Test
    void packagingEnvironmentShouldRequireJava21JpackageAndPowerShell() throws IOException {
        String script = read(REQUIRE_ENV);

        for (String token : List.of(
                "java --version",
                "javac --version",
                "mvn -version",
                "jpackage --version",
                "where powershell",
                "JDK 21 completo",
                "Java 21 + jpackage")) {
            assertTrue(script.contains(token), () -> "Falta validación de entorno: " + token);
        }
    }

    @Test
    void manifestsShouldIncludeHashesSizesAndUtcTimestamps() throws IOException {
        String metadata = read(METADATA_PS);
        String joined = read(APP_IMAGE) + "\n" + read(MSI) + "\n" + read(RC);

        for (String token : List.of("Get-FileHash", "SHA256", "_BYTES", "_LAST_WRITE_UTC")) {
            assertTrue(metadata.contains(token), () -> "Falta metadato PowerShell: " + token);
            assertTrue(joined.contains(token) || joined.contains("write-artifact-metadata.ps1"),
                    () -> "Los scripts de empaquetado deben usar metadata: " + token);
        }
        for (String label : List.of("APP_EXE", "MSI_FILE", "RC_MSI_FILE", "APP_IMAGE_MANIFEST")) {
            assertTrue(joined.contains("-Label \"" + label + "\""), () -> "Falta hash de artefacto: " + label);
        }
    }

    @Test
    void packagingScriptsShouldPersistStageLogs() throws IOException {
        String prepare = read(PREPARE);
        String app = read(APP_IMAGE);
        String msi = read(MSI);
        String rc = read(RC);

        for (String token : List.of(
                "dist\\logs\\maven-package.log",
                "dist\\logs\\jpackage-app-image.log",
                "dist\\logs\\jpackage-msi.log")) {
            assertTrue((prepare + app + msi + rc).contains(token), () -> "Falta log de etapa: " + token);
        }
        assertTrue(rc.contains("dist\\release\\logs"), "El RC debe copiar logs al paquete de cierre.");
        assertTrue(rc.contains("copy /y \"%%~fF\" \"%RELEASE_LOG_DIR%\\\""), "El RC debe copiar logs existentes.");
    }

    @Test
    void releaseCandidateFlowShouldAvoidDuplicateAppImageGeneration() throws IOException {
        String msi = read(PUBLIC_MSI);
        String rc = read(PUBLIC_RC);

        assertTrue(msi.contains("DMS_REUSE_APP_IMAGE"));
        assertTrue(msi.contains("scripts\\14-app-image-completa.bat"));
        assertTrue(rc.contains("set DMS_REUSE_APP_IMAGE=1"));
        assertTrue(rc.contains("scripts\\14-app-image-completa.bat"));
        assertTrue(rc.contains("scripts\\15-msi-completo.bat"));
    }

    @Test
    void documentationShouldExposeAuditablePackagingContract() throws IOException {
        String joined = read(DOC) + "\n"
                + read(Path.of("scripts", "README.md")) + "\n"
                + read(Path.of("docs", "tecnico", "EMPAQUETADO_WINDOWS.md")) + "\n"
                + read(Path.of("docs", "testeo", "INSTALABLE_WINDOWS_RC_GUIA.md")) + "\n"
                + read(Path.of("docs", "testeo", "reportes", "REPORTE_INSTALABLE_WINDOWS_RC.md")) + "\n"
                + read(Path.of("docs", "release", "RELEASE_CANDIDATE_0_0_1.md"));

        for (String token : List.of(
                "dist\\logs\\maven-package.log",
                "dist\\logs\\jpackage-app-image.log",
                "dist\\logs\\jpackage-msi.log",
                "dist\\release\\logs",
                "*_SHA256",
                "*_BYTES",
                "*_LAST_WRITE_UTC")) {
            assertTrue(joined.contains(token), () -> "Falta contrato documental PKG-1: " + token);
        }
        assertFalse(joined.contains("aprobado automáticamente"), "El MSI no debe aprobarse sin smoke manual.");
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
