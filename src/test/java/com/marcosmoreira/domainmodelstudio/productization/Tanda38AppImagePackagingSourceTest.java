package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 38: app-image debe ser el paso empaquetado validable antes de MSI. */
class Tanda38AppImagePackagingSourceTest {

    private static final Path CREATE_APP_IMAGE = Path.of("scripts", "internal", "create-app-image.bat");
    private static final Path VERIFY_APP_IMAGE = Path.of("scripts", "internal", "verify-staged-app.bat");
    private static final Path APP_IMAGE_SCRIPT = Path.of("scripts", "14-app-image-completa.bat");
    private static final Path GUIDE = Path.of("docs", "testeo", "APP_IMAGE_SMOKE_TANDA_38.md");
    private static final Path REPORT = Path.of("docs", "testeo", "reportes", "REPORTE_APP_IMAGE_TANDA_38.md");
    private static final Path DEV_DOC = Path.of("docs", "raiz", "TANDA_38_APP-IMAGE.md");
    private static final Path ROOT_DOC = Path.of("docs", "raiz", "TANDA_38_APP-IMAGE.md");

    @Test
    void appImageScriptShouldValidateInputsGenerateManifestAndCheckExe() throws IOException {
        String script = read(CREATE_APP_IMAGE);
        assertTrue(script.contains("where jpackage"));
        assertTrue(script.contains("APP_ICON=src\\main\\resources\\branding\\domain-model-studio-icon.ico"));
        assertTrue(script.contains("--type app-image"));
        assertTrue(script.contains("--icon \"%APP_ICON%\""));
        assertTrue(script.contains("APP_IMAGE_MANIFEST.txt"));
        assertTrue(script.contains("if not exist \"%APP_EXE%\""));
        assertTrue(script.contains("scripts\\14-app-image-completa.bat"));
    }

    @Test
    void validationScriptShouldCheckPortableImageStructureAndOpenSmokeDocs() throws IOException {
        String script = read(VERIFY_APP_IMAGE);
        assertTrue(script.contains("APP_EXE=%APP_HOME%\\%APP_NAME%.exe"));
        assertTrue(script.contains("APP_DIR=%APP_HOME%\\app"));
        assertTrue(script.contains("RUNTIME_DIR=%APP_HOME%\\runtime"));
        assertTrue(script.contains("APP_IMAGE_SMOKE_TANDA_38.md"));
        assertTrue(script.contains("REPORTE_APP_IMAGE_TANDA_38.md"));
        assertTrue(script.contains("start \"\" \"%APP_EXE%\""));
    }

    @Test
    void publicScriptAndDocsShouldBlockMsiUntilAppImageIsValidated() throws IOException {
        String script = read(APP_IMAGE_SCRIPT);
        String joined = script + "\n" + read(GUIDE) + "\n" + read(REPORT) + "\n" + read(DEV_DOC) + "\n" + read(ROOT_DOC);
        for (String token : new String[] {
                "scripts\\internal\\create-app-image.bat",
                "scripts\\internal\\verify-staged-app.bat",
                "Domain Model Studio.exe",
                "dist\\\\staging\\\\Domain Model Studio\\\\app",
                "dist\\\\staging\\\\Domain Model Studio\\\\runtime",
                "REPORTE_APP_IMAGE_TANDA_38.md",
                "No avanzar a Tanda 39"
        }) {
            assertTrue(joined.contains(token), "Falta contrato app-image Tanda 38: " + token);
        }
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
