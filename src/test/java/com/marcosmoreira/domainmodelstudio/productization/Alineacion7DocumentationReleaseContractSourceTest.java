package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Alineación 007: documentación anti-fachada, matrices y release. */
class Alineacion7DocumentationReleaseContractSourceTest {

    private static final Path CONTRACT = Path.of(
            "docs", "alineacion", "ALINEACION_007_DOCUMENTACION_ANTIFACHADA_MATRICES_RELEASE.md");

    @Test
    void documentationReleaseContractShouldExistAndDefineAntiFacadeRule() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("Alineación 007"), "Debe identificar la alineación.");
        assertTrue(contract.contains("capacidad visible = implementación real + prueba focalizada + documentación viva + smoke verificable"));
        assertTrue(contract.contains("no modifica lógica de ejecución"));
        assertTrue(contract.contains("La documentación no debe prometer"));
    }

    @Test
    void contractShouldListRequiredLivingDocuments() throws IOException {
        String contract = read(CONTRACT);

        for (String document : requiredLivingDocuments()) {
            assertTrue(contract.contains(document), () -> "Debe mencionar documento obligatorio: " + document);
            assertTrue(Files.exists(Path.of(document)), () -> "Debe existir en el repositorio: " + document);
        }
    }

    @Test
    void contractShouldDefineLogicalBusinessGraphMatrixRow() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("ID oficial: logical-business-graph"));
        assertTrue(contract.contains("Nombre: Grafo lógico del negocio"));
        assertTrue(contract.contains("Familia: Levantamiento y análisis"));
        for (String capability : capabilityEvidence()) {
            assertTrue(contract.contains(capability), () -> "Debe mencionar capacidad/evidencia: " + capability);
        }
    }

    @Test
    void contractShouldDefineUseCasesAndSmokeChecklist() throws IOException {
        String contract = read(CONTRACT);

        for (String useCase : useCases()) {
            assertTrue(contract.contains(useCase), () -> "Debe mencionar caso de uso: " + useCase);
        }
        for (String smoke : smokeItems()) {
            assertTrue(contract.contains(smoke), () -> "Debe mencionar smoke esperado: " + smoke);
        }
    }

    @Test
    void contractShouldRequireReleaseAndKnownLimitationsHonesty() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("RELEASE_CANDIDATE_0_0_1.md"));
        assertTrue(contract.contains("LIMITACIONES_CONOCIDAS_0_0_1.md"));
        assertTrue(contract.contains("RELEASE_NOTES.md"));
        assertTrue(contract.contains("No registrar una limitación conocida cuando se sabe que existe es una forma de fachada."));
        assertTrue(contract.contains("Incluido como tipo disponible"));
        assertTrue(contract.contains("excluido del RC"));
    }

    @Test
    void contractShouldClarifyAiResourcesAndPromptImportability() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("18_grafo_logico_negocio_gramatica.md"));
        assertTrue(contract.contains("19_grafo_logico_negocio_prompt_ia.md"));
        assertTrue(contract.contains("logical_business_graph_minimo.md"));
        assertTrue(contract.contains("logical_business_graph_uens_gordito.md"));
        assertTrue(contract.contains("El prompt maestro no se importa como proyecto."));
        assertTrue(contract.contains("El prompt maestro guía a la IA para producir Markdown importable."));
    }

    @Test
    void contractShouldDefineUpdateOrderAndGuardrails() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("Orden obligatorio de actualización documental"));
        assertTrue(contract.contains("Cerrar guía académica del Grafo lógico"));
        assertTrue(contract.contains("Cerrar persistencia .dms y exportaciones"));
        assertTrue(contract.contains("Cerrar validación integral"));
        assertTrue(contract.contains("tipo visible ausente en matriz de capacidades"));
        assertTrue(contract.contains("capacidad declarada sin toolbar o salida activa"));
        assertTrue(contract.contains("release candidate que omite una limitación conocida relevante"));
    }

    @Test
    void planDocumentsShouldReferenceAlignment7AsCurrentContinuation() throws IOException {
        List<Path> docs = List.of(
                Path.of("docs", "documentacion", "MAPA_DOCUMENTACION_VIVA.md"),
                Path.of("docs", "raiz", "PLAN_TANDAS_RESTANTES.md"),
                Path.of("docs", "diagnostico", "ESTADO_AUDITORIA_ACTUAL.md"));

        for (Path doc : docs) {
            String text = read(doc);
            assertTrue(text.contains("ALINEACION_007_DOCUMENTACION_ANTIFACHADA_MATRICES_RELEASE.md"),
                    () -> doc + " debe apuntar al contrato de Alineación 7.");
        }
    }

    private static List<String> requiredLivingDocuments() {
        return List.of(
                "docs/producto/MATRIZ_CAPACIDADES_REALES.md",
                "docs/testeo/MATRIZ_CASOS_USO_Y_TESTS.md",
                "docs/testeo/MATRIZ_CASOS_USO_CATEGORIZADA.md",
                "docs/productizacion/casos-uso/09_matriz_cobertura_casos_uso_por_tipo.md",
                "docs/productizacion/casos-uso/10_checklist_smoke_visual_por_tipo.md",
                "docs/implementacion/tanda_17_smoke_qa_cierre_release/01_matriz_smoke_16_tipos.md",
                "docs/ia/RECURSOS_IA.md",
                "docs/release/RELEASE_CANDIDATE_0_0_1.md",
                "docs/release/LIMITACIONES_CONOCIDAS_0_0_1.md",
                "docs/release/RELEASE_NOTES.md");
    }

    private static List<String> capabilityEvidence() {
        return List.of(
                "Importar Markdown",
                "Exportar Markdown",
                "Exportar PNG",
                "Exportar SVG",
                "Guardar `.dms`",
                "Abrir `.dms`",
                "Recursos IA",
                "Guía académica");
    }

    private static List<String> useCases() {
        return List.of(
                "UC-LBG-001",
                "UC-LBG-007",
                "UC-LBG-011",
                "UC-LBG-015");
    }

    private static List<String> smokeItems() {
        return List.of(
                "Abrir ejemplo oficial logical_business_graph_uens_gordito.md",
                "Confirmar leyenda visible de abreviaciones",
                "Guardar .dms, cerrar y reabrir",
                "Confirmar que no cae al canvas conceptual",
                "Confirmar que no usa FreeGraphDocument como dominio");
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
