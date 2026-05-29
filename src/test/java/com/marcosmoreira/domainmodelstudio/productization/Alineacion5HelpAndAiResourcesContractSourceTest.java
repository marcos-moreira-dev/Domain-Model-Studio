package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Alineación 005: ayuda académica, ayuda operativa y recursos IA. */
class Alineacion5HelpAndAiResourcesContractSourceTest {

    private static final Path CONTRACT = Path.of(
            "docs", "alineacion", "ALINEACION_005_AYUDA_ACADEMICA_OPERATIVA_RECURSOS_IA.md");

    @Test
    void helpContractShouldExistAndDefineThreeSeparateChannels() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("Alineación 005"), "Debe identificar la alineación.");
        assertTrue(contract.contains("menú Ayuda = guía académica teórica"));
        assertTrue(contract.contains("SideDock Ayuda = ayuda operativa de herramienta"));
        assertTrue(contract.contains("Recursos IA = materiales exportables"));
        assertTrue(contract.contains("La guía académica explica por qué y cuándo usar un artefacto."));
        assertTrue(contract.contains("La ayuda operativa explica cómo usar la herramienta."));
        assertTrue(contract.contains("Los recursos IA explican cómo producir o reutilizar Markdown compatible."));
    }

    @Test
    void academicHelpContractShouldDefineLogicalBusinessGraphTheoryTarget() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("TheoryTopicId.LOGICAL_BUSINESS_GRAPH"));
        assertTrue(contract.contains("help/topics/logical-business-graph.md"));
        assertTrue(contract.contains("DefaultTheoryCatalog"));
        assertTrue(contract.contains("DefaultTheoryFigureCatalog"));
        assertTrue(contract.contains("DefaultDiagramTypeDefinitions"));
        for (String section : microWikipediaSections()) {
            assertTrue(contract.contains(section), () -> "Debe mencionar la sección académica: " + section);
        }
    }

    @Test
    void menuNamingContractShouldPreferAcademicGuideOverOperationalGuide() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("Menú: Ayuda → Guía académica"));
        assertTrue(contract.contains("Stage title: Guía académica — Domain Model Studio"));
        assertTrue(contract.contains("ManualDialog"));
        assertTrue(contract.contains("ManualContent"));
        assertTrue(contract.contains("Los textos heredados con “Guía operativa”"));
    }

    @Test
    void sideDockOperationalHelpShouldRemainOperationalAndExistingSourceShouldSupportIt() throws IOException {
        String contract = read(CONTRACT);
        String operationalHelp = read(Path.of("src", "main", "java", "com", "marcosmoreira", "domainmodelstudio",
                "presentation", "sidedock", "OperationalHelpCatalog.java"));

        assertTrue(contract.contains("OperationalHelpCatalog.logicalBusinessGraph"));
        assertTrue(contract.contains("StandardSideDockModules.operationalHelp"));
        assertTrue(contract.contains("seleccionar nodos y relaciones"));
        assertTrue(contract.contains("No debe absorber explicaciones largas de teoría"));
        assertTrue(operationalHelp.contains("logicalBusinessGraph"),
                "La ayuda operativa del Grafo lógico debe existir como fuente actual.");
    }

    @Test
    void aiResourcesContractShouldListLogicalBusinessGraphResourcesAndPromptRule() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("LogicalBusinessGraphAiResourceDescriptors"));
        assertTrue(contract.contains("ClasspathAiResourceCatalog"));
        assertTrue(contract.contains("ClasspathAiResourceExporter"));
        assertTrue(contract.contains("AiResourceProductizationPolicy"));
        assertTrue(contract.contains("18_grafo_logico_negocio_gramatica.md"));
        assertTrue(contract.contains("19_grafo_logico_negocio_prompt_ia.md"));
        assertTrue(contract.contains("logical_business_graph.md"));
        assertTrue(contract.contains("logical_business_graph_minimo.md"));
        assertTrue(contract.contains("logical_business_graph_uens_gordito.md"));
        assertTrue(contract.contains("PROMPT_GUIDE"));
        assertTrue(contract.contains("exportable para copiar a una IA, pero no importable como proyecto"));
        assertTrue(contract.contains("esa marca pertenece al Markdown que la IA debe producir"));
    }

    @Test
    void currentAiResourceFilesShouldExistForTheContractedGraph() {
        for (Path resource : logicalBusinessGraphResourcePaths()) {
            assertTrue(Files.exists(resource), () -> "Debe existir el recurso IA: " + resource);
        }
    }

    @Test
    void currentSourceShouldExposeAiResourceDescriptorHooks() throws IOException {
        String descriptors = read(Path.of("src", "main", "java", "com", "marcosmoreira", "domainmodelstudio",
                "infrastructure", "resources", "LogicalBusinessGraphAiResourceDescriptors.java"));
        String officialDescriptors = read(Path.of("src", "main", "java", "com", "marcosmoreira", "domainmodelstudio",
                "infrastructure", "resources", "OfficialAiResourceDescriptors.java"));
        String policy = read(Path.of("src", "main", "java", "com", "marcosmoreira", "domainmodelstudio",
                "infrastructure", "resources", "AiResourceProductizationPolicy.java"));

        assertTrue(descriptors.contains("logical-business-graph"));
        assertTrue(descriptors.contains("prompt-grafo-logico-negocio-desde-levantamiento"));
        assertTrue(descriptors.contains("false"),
                "El prompt del Grafo lógico debe registrarse como no importable en su descriptor.");
        assertTrue(officialDescriptors.contains("LogicalBusinessGraphAiResourceDescriptors"));
        assertTrue(policy.contains("PROMPT_GUIDE"));
    }

    @Test
    void capabilityContractsShouldMentionTheoryHelpAndAiResourcesPreconditions() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("Capacidad `AI_RESOURCES`"));
        assertTrue(contract.contains("Capacidad `THEORY_HELP`"));
        assertTrue(contract.contains("docs/ia/RECURSOS_IA.md menciona logical-business-graph"));
        assertTrue(contract.contains("no declarar `THEORY_HELP` hasta crear tema, figura y catálogo"));
        assertTrue(contract.contains("AI_RESOURCES"));
        assertTrue(contract.contains("THEORY_HELP"));
    }

    @Test
    void planDocumentsShouldReferenceAlignment5AsCurrentContinuation() throws IOException {
        List<Path> docs = List.of(
                Path.of("docs", "documentacion", "MAPA_DOCUMENTACION_VIVA.md"),
                Path.of("docs", "raiz", "PLAN_TANDAS_RESTANTES.md"),
                Path.of("docs", "diagnostico", "ESTADO_AUDITORIA_ACTUAL.md"));

        for (Path doc : docs) {
            String text = read(doc);
            assertTrue(text.contains("ALINEACION_005_AYUDA_ACADEMICA_OPERATIVA_RECURSOS_IA.md"),
                    () -> doc + " debe apuntar al contrato de Alineación 5.");
        }
    }

    private static List<String> microWikipediaSections() {
        return List.of(
                "Qué es",
                "Para qué sirve",
                "Elementos principales",
                "Relaciones y lectura",
                "Casos especiales",
                "Cuándo usarlo",
                "Cuándo no usarlo",
                "Errores comunes");
    }

    private static List<Path> logicalBusinessGraphResourcePaths() {
        return List.of(
                Path.of("src", "main", "resources", "ai-resources", "18_grafo_logico_negocio_gramatica.md"),
                Path.of("src", "main", "resources", "ai-resources", "19_grafo_logico_negocio_prompt_ia.md"),
                Path.of("src", "main", "resources", "ai-resources", "official-markdown", "plantillas", "logical_business_graph.md"),
                Path.of("src", "main", "resources", "ai-resources", "official-markdown", "diagramas", "logical_business_graph_minimo.md"),
                Path.of("src", "main", "resources", "ai-resources", "official-markdown", "diagramas", "logical_business_graph_uens_gordito.md"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
