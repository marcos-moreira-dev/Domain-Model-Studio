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
 * Guardarraíl de Tanda 22: la limpieza documental debe dejar un mapa de lectura,
 * diagnóstico macro y trazabilidad categorizada sin tocar código productivo.
 */
class Tanda22DocumentationCleanupAndMacroDiagnosisSourceTest {

    private static final Path DOC_MAP = Path.of("docs", "documentacion", "MAPA_DOCUMENTACION_VIVA.md");
    private static final Path MACRO_DIAGNOSIS = Path.of("docs", "diagnostico", "MACRO_DIAGNOSTICO_TANDA_022.md");
    private static final Path CATEGORIZED_MATRIX = Path.of("docs", "testeo", "MATRIZ_CASOS_USO_CATEGORIZADA.md");
    private static final Path TANDA_DOC = Path.of("docs", "desarrollo", "TANDA_022_LIMPIEZA_DOCUMENTAL_MACRO_DIAGNOSTICO.md");
    private static final Path DOCS_README = Path.of("docs", "README.md");
    private static final Path CURRENT_PLAN = Path.of("docs", "desarrollo", "PLAN_TANDAS_ACTUAL.md");

    @Test
    void documentationCleanupShouldExposeLivingDocumentationEntryPoints() throws IOException {
        String map = read(DOC_MAP);
        String readme = read(DOCS_README);

        for (String token : List.of(
                "MAPA_DOCUMENTACION_VIVA.md",
                "MACRO_DIAGNOSTICO_TANDA_022.md",
                "MATRIZ_CASOS_USO_Y_TESTS.md",
                "MATRIZ_CASOS_USO_CATEGORIZADA.md",
                "PLAN_PRUEBAS_UI_E2E.md",
                "documentación viva",
                "histórico")) {
            assertTrue(map.contains(token) || readme.contains(token), "Falta entrada documental viva: " + token);
        }
    }

    @Test
    void categorizedMatrixShouldContainAllVisibleDiagramTypes() throws IOException {
        String matrix = read(CATEGORIZED_MATRIX);

        for (DiagramTypeDescriptor descriptor : new DefaultDiagramTypeRegistry().findAll()) {
            assertTrue(
                    matrix.contains("`" + descriptor.id().value() + "`"),
                    "Falta ID oficial en matriz categorizada: " + descriptor.id().value());
            assertTrue(
                    matrix.contains(descriptor.displayName()),
                    "Falta nombre visible en matriz categorizada: " + descriptor.displayName());
        }
    }

    @Test
    void categorizedMatrixShouldGroupUseCasesByTransversalCategory() throws IOException {
        String matrix = read(CATEGORIZED_MATRIX);

        for (String category : List.of(
                "CAT-PROJ",
                "CAT-IMPORT",
                "CAT-CANVAS",
                "CAT-PERSIST",
                "CAT-EXPORT",
                "CAT-HELP-AI",
                "CAT-UML-SOURCE",
                "CAT-DOC")) {
            assertTrue(matrix.contains(category), "Falta categoría transversal: " + category);
        }

        for (String useCase : List.of(
                "UC-PROJ-001",
                "UC-PROJ-002",
                "UC-IMPORT-001",
                "UC-CANVAS-001",
                "UC-CANVAS-002",
                "UC-EXPORT-001",
                "UC-EXPORT-002",
                "UC-EXPORT-003",
                "UC-UMLSRC-001",
                "UC-UMLSRC-002",
                "UC-DOC-001",
                "UC-DOC-002",
                "UC-DOC-003")) {
            assertTrue(matrix.contains(useCase), "Falta caso categorizado: " + useCase);
        }
    }

    @Test
    void macroDiagnosisShouldListRisksPendingWorkAndExplicitNonGoals() throws IOException {
        String diagnosis = read(MACRO_DIAGNOSIS);

        for (String token : List.of(
                "Riesgos pendientes",
                "Pendientes reales antes de cierre final",
                "Qué no debe venderse como completo",
                "Javadocs",
                "Aplazado",
                "modelo conceptual",
                "README.md",
                "UI/E2E",
                "Smoke visual",
                "PDF universal",
                "Abrir código")) {
            assertTrue(diagnosis.contains(token), "Falta sección o criterio de diagnóstico: " + token);
        }
    }

    @Test
    void currentPlanShouldMoveJavadocsOutOfThisContextAndPrioritizeFinalClosure() throws IOException {
        String plan = read(CURRENT_PLAN);
        String tanda = read(TANDA_DOC);

        assertTrue(plan.contains("Tanda 22"));
        assertTrue(plan.contains("Cierre final liviano"));
        assertTrue(plan.contains("Javadocs pedagógicos en otro chat"));
        assertTrue(tanda.contains("No se agregan Javadocs"));
        assertTrue(tanda.contains("macro diagnóstico"));
        assertTrue(tanda.contains("scripts\\02-ejecutar-tests.bat"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
