package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarrail de Tanda 39: MSI solo despues de app-image validada y con smoke manual. */
class Tanda39MsiPackagingSourceTest {

    private static final Path CREATE_MSI = Path.of("scripts", "internal", "create-msi-installer.bat");
    private static final Path VERIFY_MSI = Path.of("scripts", "internal", "verify-msi-installer.bat");
    private static final Path COMPLETE_MSI = Path.of("scripts", "15-msi-completo.bat");
    private static final Path GUIDE = Path.of("docs", "testeo", "MSI_SMOKE_TANDA_39.md");
    private static final Path REPORT = Path.of("docs", "testeo", "reportes", "REPORTE_MSI_TANDA_39.md");
    private static final Path DEV_DOC = Path.of("docs", "raiz", "TANDA_39_MSI.md");
    private static final Path ROOT_DOC = Path.of("docs", "raiz", "TANDA_39_MSI.md");
    private static final Path PACKAGING_DOC = Path.of("docs", "desarrollo", "EMPAQUETADO_WINDOWS.md");

    @Test
    void msiScriptShouldRequireValidatedAppImageAndGenerateManifest() throws IOException {
        String script = read(CREATE_MSI);
        assertTrue(script.contains("where jpackage"));
        assertTrue(script.contains("APP_IMAGE_MANIFEST=dist\\staging\\APP_IMAGE_MANIFEST.txt"));
        assertTrue(script.contains("APP_IMAGE_EXE=%APP_IMAGE_HOME%\\%APP_NAME%.exe"));
        assertTrue(script.contains("scripts\\14-app-image-completa.bat"));
        assertTrue(script.contains("--type msi"));
        assertTrue(script.contains("--win-menu"));
        assertTrue(script.contains("--win-shortcut"));
        assertTrue(script.contains("--win-dir-chooser"));
        assertTrue(script.contains("--win-upgrade-uuid"));
        assertTrue(script.contains("MSI_MANIFEST.txt"));
        assertTrue(script.contains("jpackage-msi.log"));
    }

    @Test
    void msiVerificationShouldOpenSmokeGuideAndReport() throws IOException {
        String script = read(VERIFY_MSI);
        assertTrue(script.contains("MSI_MANIFEST=dist\\installer\\MSI_MANIFEST.txt"));
        assertTrue(script.contains("docs\\testeo\\MSI_SMOKE_TANDA_39.md"));
        assertTrue(script.contains("docs\\testeo\\reportes\\REPORTE_MSI_TANDA_39.md"));
        assertTrue(script.contains("start \"\" \"%DEST_DIR%\""));
        assertTrue(script.contains("Instala manualmente el MSI"));
    }

    @Test
    void publicFlowAndDocsShouldBlockReleaseCandidateUntilMsiSmokePasses() throws IOException {
        String joined = read(COMPLETE_MSI) + "\n" + read(GUIDE) + "\n" + read(REPORT)
                + "\n" + read(DEV_DOC) + "\n" + read(ROOT_DOC) + "\n" + read(PACKAGING_DOC);
        for (String token : new String[] {
                "scripts\\14-app-image-completa.bat",
                "scripts\\internal\\create-msi-installer.bat",
                "dist\\installer\\*.msi",
                "MSI_MANIFEST.txt",
                "REPORTE_MSI_TANDA_39.md",
                "desinstal",
                "No avanzar a Tanda 40"
        }) {
            assertTrue(joined.contains(token), "Falta contrato MSI Tanda 39: " + token);
        }
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
