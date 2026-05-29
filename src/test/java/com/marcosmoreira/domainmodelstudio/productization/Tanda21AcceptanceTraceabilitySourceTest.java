package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Guardarraíl de Tanda 21: cada promesa visible debe estar trazada a casos de uso y estrategia de prueba.
 */
class Tanda21AcceptanceTraceabilitySourceTest {

    private static final Path MATRIX = Path.of("docs", "testeo", "MATRIZ_CASOS_USO_Y_TESTS.md");
    private static final Path UI_PLAN = Path.of("docs", "testeo", "PLAN_PRUEBAS_UI_E2E.md");
    private static final Path TANDA_DOC = Path.of("docs", "desarrollo", "TANDA_021_MATRIZ_CASOS_USO_TESTS.md");

    @Test
    void everyVisibleDiagramTypeShouldAppearInAcceptanceTraceabilityMatrix() throws IOException {
        String matrix = read(MATRIX);

        for (DiagramTypeDescriptor descriptor : new DefaultDiagramTypeRegistry().findAll()) {
            assertTrue(
                    matrix.contains("`" + descriptor.id().value() + "`"),
                    "Falta ID oficial en matriz de aceptación: " + descriptor.id().value());
            assertTrue(
                    matrix.contains(descriptor.displayName()),
                    "Falta nombre visible en matriz de aceptación: " + descriptor.displayName());
        }
    }

    @Test
    void acceptanceMatrixShouldDescribeRequiredTestLevelsAndUseCaseStates() throws IOException {
        String matrix = read(MATRIX);

        for (String token : List.of(
                "Test unitario",
                "Test integración",
                "Test contrato/fuente",
                "Test UI/E2E",
                "Smoke manual",
                "CUBIERTO",
                "PARCIAL",
                "PENDIENTE_UI_E2E",
                "MANUAL_REQUERIDO",
                "NO_APLICA")) {
            assertTrue(matrix.contains(token), "Falta nivel o estado de prueba en matriz: " + token);
        }
    }

    @Test
    void acceptanceMatrixShouldTraceTransversalUseCasesAndSpecialUmlSourceCases() throws IOException {
        String matrix = read(MATRIX);

        for (String useCase : List.of(
                "UC-PROJ-001",
                "UC-PROJ-002",
                "UC-IMPORT-001",
                "UC-CANVAS-001",
                "UC-CANVAS-002",
                "UC-CANVAS-004",
                "UC-EXPORT-001",
                "UC-EXPORT-002",
                "UC-EXPORT-003",
                "UC-EXPORT-004",
                "UC-HELP-001",
                "UC-AI-001",
                "UC-UMLSRC-001",
                "UC-UMLSRC-002")) {
            assertTrue(matrix.contains(useCase), "Falta caso de uso trazable: " + useCase);
        }
    }

    @Test
    void uiE2ePlanShouldAvoidNativeDialogAndExternalEditorFragility() throws IOException {
        String plan = read(UI_PLAN);

        for (String token : List.of(
                "TestFX",
                "Selector de archivo/carpeta",
                "Lanzador de editor externo",
                "no se debe abrir una aplicación real",
                "Exportador con destino",
                "ApplicationLaunchUiTest",
                "UmlClassOpenSourceUiTest")) {
            assertTrue(plan.contains(token), "Falta criterio UI/E2E estable: " + token);
        }
    }

    @Test
    void tandaDocumentShouldExplainWhyThisReplacesImmediateDocumentationCleanup() throws IOException {
        String doc = read(TANDA_DOC);

        assertTrue(doc.contains("La limpieza documental se desplaza"));
        assertTrue(doc.contains("trazabilidad de pruebas"));
        assertTrue(doc.contains("scripts\\02-ejecutar-tests.bat"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
