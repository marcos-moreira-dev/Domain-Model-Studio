package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Alineación 008: plan quirúrgico de correcciones. */
class Alineacion8SurgicalFixPlanContractSourceTest {

    private static final Path CONTRACT = Path.of(
            "docs", "alineacion", "ALINEACION_008_PLAN_QUIRURGICO_CORRECCIONES.md");

    @Test
    void surgicalPlanContractShouldExistAndDefineCentralRule() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("Alineación 008"), "Debe identificar la alineación.");
        assertTrue(contract.contains("plan de intervención ordenado"));
        assertTrue(contract.contains("primero estabilizar contratos, luego implementar capacidades, después documentar matrices, finalmente validar release candidate"));
        assertTrue(contract.contains("no modifica lógica de ejecución"));
    }

    @Test
    void contractShouldPreserveProtectedBoundaries() throws IOException {
        String contract = read(CONTRACT);

        for (String boundary : protectedBoundaries()) {
            assertTrue(contract.contains(boundary), () -> "Debe preservar frontera: " + boundary);
        }
    }

    @Test
    void contractShouldDefineAllSurgicalFixesInOrder() throws IOException {
        String contract = read(CONTRACT);

        for (String fix : surgicalFixes()) {
            assertTrue(contract.contains(fix), () -> "Debe mencionar fix quirúrgico: " + fix);
        }
    }

    @Test
    void contractShouldDefineCatalogToolbarOutputPersistenceAndTheoryTasks() throws IOException {
        String contract = read(CONTRACT);

        for (String expected : technicalTasks()) {
            assertTrue(contract.contains(expected), () -> "Debe mencionar tarea técnica: " + expected);
        }
    }

    @Test
    void contractShouldDefineValidationDocumentationAndReleaseTasks() throws IOException {
        String contract = read(CONTRACT);

        for (String expected : validationDocumentationReleaseTasks()) {
            assertTrue(contract.contains(expected), () -> "Debe mencionar tarea de validación/documentación/release: " + expected);
        }
    }

    @Test
    void contractShouldDefineExpectedTestsAndScripts() throws IOException {
        String contract = read(CONTRACT);

        for (String expected : expectedTestsAndScripts()) {
            assertTrue(contract.contains(expected), () -> "Debe mencionar test o script esperado: " + expected);
        }
    }

    @Test
    void contractShouldDefineResultingTechnicalTandas() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("Tanda 40 — Guía académica del Grafo lógico."));
        assertTrue(contract.contains("Tanda 41 — Persistencia .dms y exportaciones."));
        assertTrue(contract.contains("Tanda 42 — Validación integral del nuevo proyecto."));
        assertTrue(contract.contains("Tanda 31 — Validación local Windows / Release Candidate."));
        assertTrue(contract.contains("Tanda 9 — Deuda SRP focalizada, solo si aparece bloqueo real."));
        assertTrue(contract.contains("41A persistencia"));
        assertTrue(contract.contains("41B active output"));
        assertTrue(contract.contains("41C SVG/PNG"));
    }

    @Test
    void planDocumentsShouldReferenceAlignment8AsCurrentContinuation() throws IOException {
        List<Path> docs = List.of(
                Path.of("docs", "documentacion", "MAPA_DOCUMENTACION_VIVA.md"),
                Path.of("docs", "raiz", "PLAN_TANDAS_RESTANTES.md"),
                Path.of("docs", "diagnostico", "ESTADO_AUDITORIA_ACTUAL.md"));

        for (Path doc : docs) {
            String text = read(doc);
            assertTrue(text.contains("ALINEACION_008_PLAN_QUIRURGICO_CORRECCIONES.md"),
                    () -> doc + " debe apuntar al contrato de Alineación 8.");
        }
    }

    private static List<String> protectedBoundaries() {
        return List.of(
                "No se debe tocar pantalla de inicio.",
                "No se debe tocar modelo conceptual.",
                "No se debe tocar canvas conceptual.",
                "No se debe migrar código del modelo conceptual a otros proyectos.",
                "No se debe usar FreeGraphDocument como dominio del Grafo lógico.",
                "Si una corrección requiere tocar una zona protegida, debe detenerse");
    }

    private static List<String> surgicalFixes() {
        return List.of(
                "F1  CSS/tokens y regla visual mínima.",
                "F2  Catálogo del Grafo lógico y estado AVAILABLE condicionado.",
                "F3  Toolbar contextual y validación global.",
                "F4  Active output y exportaciones Markdown/PNG/SVG.",
                "F5  Persistencia .dms y roundtrip de layout.",
                "F6  Guía académica del Grafo lógico.",
                "F7  Recursos IA y docs IA.",
                "F8  Validación integral del Grafo lógico.",
                "F9  Matrices, smoke y release docs.",
                "F10 Release candidate local Windows.");
    }

    private static List<String> technicalTasks() {
        return List.of(
                "declarar -dms-border-strong en tokens.css",
                "crear LogicalBusinessGraphToolbarContributor",
                "conectar Diagrama > Validar proyecto con ProjectValidationCoordinator",
                "crear LogicalBusinessGraphActiveOutputContributor",
                "registrar contributor en ActiveOutputContributorRegistry",
                "serializar LogicalBusinessGraphDocument en .dms",
                "crear TheoryTopicId.LOGICAL_BUSINESS_GRAPH",
                "crear help/topics/logical-business-graph.md",
                "declarar AI_RESOURCES en capacidades del Grafo lógico");
    }

    private static List<String> validationDocumentationReleaseTasks() {
        return List.of(
                "validar backbone MF → FL → CU → ACC",
                "detectar MF sin FL, FL sin CU, CU sin ACC",
                "detectar INV sin protege y POST sin garantiza",
                "actualizar MATRIZ_CAPACIDADES_REALES.md",
                "actualizar MATRIZ_CASOS_USO_Y_TESTS.md",
                "actualizar RELEASE_CANDIDATE_0_0_1.md",
                "actualizar LIMITACIONES_CONOCIDAS_0_0_1.md",
                "BUILD SUCCESS, tests verdes, app-image funcional, MSI generado y smoke manual documentado");
    }

    private static List<String> expectedTestsAndScripts() {
        return List.of(
                "CssTokenCoverageTest",
                "DefaultDiagramTypeDefinitionsTest",
                "LogicalBusinessGraphActiveOutputContributorTest",
                "DmsProjectJsonLogicalBusinessGraphTest",
                "DefaultTheoryCatalogTest",
                "Tanda39LogicalBusinessGraphAiResourcesTest",
                "LogicalBusinessGraphValidationServiceTest",
                "ProductMinimumCoherenceTest",
                "scripts\\02-ejecutar-tests.bat",
                "scripts\\16-release-candidate.bat");
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
