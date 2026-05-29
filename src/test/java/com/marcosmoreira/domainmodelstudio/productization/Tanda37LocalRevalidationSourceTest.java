package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 37: la revalidación local completa debe quedar ejecutable y documentada. */
class Tanda37LocalRevalidationSourceTest {

    private static final Path RETIRED_VALIDATION_SCRIPT = Path.of("scripts", "08-validacion-local-completa.bat");
    private static final Path REVALIDATION_SCRIPT = Path.of("scripts", "13-revalidacion-local-completa.bat");
    private static final Path REVALIDATION_GUIDE = Path.of("docs", "testeo", "REVALIDACION_LOCAL_COMPLETA_TANDA_37.md");
    private static final Path REVALIDATION_REPORT = Path.of("docs", "testeo", "reportes", "REPORTE_REVALIDACION_LOCAL_COMPLETA.md");
    private static final Path VALIDATION_DOC = Path.of("docs", "desarrollo", "VALIDACION_LOCAL.md");
    private static final Path ROOT_TANDA = Path.of("docs", "raiz", "TANDA_37_REVALIDACION_LOCAL_COMPLETA.md");

    @Test
    void revalidationScriptShouldRunTestsRenderSmokeAndMetrics() throws IOException {
        String script = read(REVALIDATION_SCRIPT);
        assertFalse(Files.exists(RETIRED_VALIDATION_SCRIPT), "08 fue absorbido por 13 como entry point vigente.");
        assertTrue(script.contains("00-verificar-entorno.bat"));
        assertTrue(script.contains("02-ejecutar-tests.bat"));
        assertTrue(script.contains("internal\\run-render-smoke.bat"));
        assertTrue(script.contains("06-medir-refactor.bat"));
        assertTrue(script.contains("REPORTE_REVALIDACION_LOCAL_COMPLETA.md"));
    }

    @Test
    void tanda37ScriptShouldPointToManualSmokeDocumentsWithoutLegacyWrappers() throws IOException {
        String script = read(REVALIDATION_SCRIPT);
        assertFalse(script.contains("09-smoke-ui-minimo.bat"));
        assertFalse(script.contains("11-smoke-levantamiento-logico.bat"));
        assertTrue(script.contains("UI_SMOKE_MINIMO_EJECUTABLE.md"));
        assertTrue(script.contains("REPORTE_SMOKE_UI_MINIMO.md"));
    }

    @Test
    void documentationShouldDescribeNoAdvanceCriteriaAndArtifacts() throws IOException {
        String guide = read(REVALIDATION_GUIDE);
        String report = read(REVALIDATION_REPORT);
        String validation = read(VALIDATION_DOC);
        String root = read(ROOT_TANDA);
        String joined = guide + "\n" + report + "\n" + validation + "\n" + root;
        for (String token : new String[] {
                "BUILD SUCCESS",
                "target\\smoke-render\\contact_sheet.html",
                "REPORTE_REVALIDACION_LOCAL_COMPLETA.md",
                "No pasar a Tanda 38",
                "app-image"
        }) {
            assertTrue(joined.contains(token), "Falta token de revalidación local: " + token);
        }
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
