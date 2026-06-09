package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 14: cierre funcional y smoke focalizado de Levantamiento lógico. */
class LogicalBusinessTanda14ClosureSourceTest {

    private static final Path ROOT = Path.of("");
    private static final Path MAIN = Path.of("src/main/java");
    private static final Path TESTEO = Path.of("docs/testeo");

    @Test
    void closureDocumentsAndSmokeScriptExist() throws IOException {
        String closure = read("docs/desarrollo/TANDA_14_CIERRE_LEVANTAMIENTO_LOGICO.md");
        String smoke = read("docs/testeo/SMOKE_LEVANTAMIENTO_LOGICO_TANDA_14.md");
        String report = read("docs/testeo/reportes/REPORTE_SMOKE_LEVANTAMIENTO_LOGICO.md");

        assertTrue(closure.contains("expediente documental estructurado"));
        assertTrue(smoke.contains("LG-SMOKE-004"));
        assertTrue(smoke.contains("Exportar Markdown"));
        assertTrue(smoke.contains("logical_business_intake_uens_gordito.md"));
        assertTrue(smoke.contains("Impacto y dependencias"));
        assertFalse(smoke.contains("Derivaciones"));
        assertTrue(report.contains("LG-SMOKE-010 Cierre documental"));
        assertFalse(Files.exists(Path.of("scripts/11-smoke-levantamiento-logico.bat")),
                "El smoke de Levantamiento lógico queda como documento, no como wrapper público.");
    }

    @Test
    void smokeMatrixIncludesLogicalBusinessRoute() throws IOException {
        String uiSmoke = read(TESTEO.resolve("UI_SMOKE_MINIMO_EJECUTABLE.md"));
        String report = read(TESTEO.resolve("reportes/REPORTE_SMOKE_UI_MINIMO.md"));
        String checklist = read(TESTEO.resolve("CHECKLIST_CIERRE_BASE.md"));

        assertTrue(uiSmoke.contains("UI-SMOKE-012"));
        assertTrue(uiSmoke.contains("logical-business-intake"));
        assertTrue(report.contains("UI-SMOKE-012 Levantamiento lógico documental"));
        assertTrue(checklist.contains("SMOKE_LEVANTAMIENTO_LOGICO_TANDA_14.md"));
    }

    @Test
    void logicalBusinessKeepsEditableDocumentalScope() throws IOException {
        String view = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessDocumentView.java");
        String linked = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessLinkedRows.java");
        String help = readJava("com/marcosmoreira/domainmodelstudio/presentation/sidedock/OperationalHelpContent.java");
        String modules = readJava("com/marcosmoreira/domainmodelstudio/presentation/workbench/WorkbenchSideDockModules.java");

        assertTrue(view.contains("Actualizar documento"));
        assertTrue(linked.contains("Hyperlink"));
        assertTrue(help.contains("LOGICAL_BUSINESS_DOCUMENT"));
        assertTrue(modules.contains("WorkspaceKind.CONCEPTUAL_CANVAS"));
        assertFalse(view.contains("SplitPane"));
        assertFalse(view.contains("renderDraftCard"));
    }

    @Test
    void logicalBusinessSourcesStayReadableAfterClosure() throws IOException {
        assertLineLimit("LogicalBusinessDocumentView.java", 450);
        assertLineLimit("LogicalBusinessViewModel.java", 450);
        assertLineLimit("LogicalBusinessCrudDialogs.java", 220);
        assertLineLimit("LogicalBusinessCrudOperations.java", 280);
    }

    private static void assertLineLimit(String fileName, int max) throws IOException {
        Path path = MAIN.resolve("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/" + fileName);
        long lines = Files.lines(path, StandardCharsets.UTF_8).count();
        assertTrue(lines <= max, fileName + " tiene " + lines + " líneas; límite: " + max);
    }

    private static String readJava(String relativePath) throws IOException {
        return read(MAIN.resolve(relativePath));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    private static String read(String relativePath) throws IOException {
        return read(ROOT.resolve(relativePath));
    }
}
