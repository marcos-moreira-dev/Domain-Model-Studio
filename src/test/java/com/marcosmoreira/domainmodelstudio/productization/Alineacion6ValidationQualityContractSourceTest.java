package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Alineación 006: validación integral y criterios de calidad. */
class Alineacion6ValidationQualityContractSourceTest {

    private static final Path CONTRACT = Path.of(
            "docs", "alineacion", "ALINEACION_006_VALIDACION_INTEGRAL_CRITERIOS_CALIDAD.md");

    @Test
    void validationContractShouldExistAndDefineCentralRule() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("Alineación 006"), "Debe identificar la alineación.");
        assertTrue(contract.contains("validación integral = integridad estructural + coherencia semántica + trazabilidad + calidad visual mínima + pruebas reproducibles"));
        assertTrue(contract.contains("La Tanda 42"));
        assertTrue(contract.contains("no implementa todavía la validación profunda"));
    }

    @Test
    void validationContractShouldDefineSourcesAndChannels() throws IOException {
        String contract = read(CONTRACT);

        for (String source : currentValidationSources()) {
            assertTrue(contract.contains(source), () -> "Debe mencionar la fuente de validación: " + source);
        }
        for (String channel : validationChannels()) {
            assertTrue(contract.contains(channel), () -> "Debe mencionar el canal de validación: " + channel);
        }
    }

    @Test
    void validationContractShouldDefineSeverityModel() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("BLOCKING"));
        assertTrue(contract.contains("WARNING"));
        assertTrue(contract.contains("INFO"));
        assertTrue(contract.contains("bloqueo"));
        assertTrue(contract.contains("advertencia fuerte"));
        assertTrue(contract.contains("advertencias suaves"));
    }

    @Test
    void validationContractShouldDefineBlockingAndWarningCases() throws IOException {
        String contract = read(CONTRACT);

        for (String expected : blockingCases()) {
            assertTrue(contract.contains(expected), () -> "Debe mencionar bloqueo esperado: " + expected);
        }
        for (String expected : warningCases()) {
            assertTrue(contract.contains(expected), () -> "Debe mencionar warning esperado: " + expected);
        }
    }

    @Test
    void validationContractShouldDefineBackboneTraceabilityAndRoundTripCriteria() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("MF → FL → CU → ACC"));
        assertTrue(contract.contains("casi árbol como lectura principal, grafo como realidad semántica"));
        assertTrue(contract.contains("¿Por qué existe esta entidad?"));
        assertTrue(contract.contains("Markdown oficial → importar → exportar Markdown → reimportar"));
        assertTrue(contract.contains("Proyecto .dms → guardar → abrir → validar"));
        assertTrue(contract.contains("exportar un grafo vacío no debe producir Markdown inválido"));
    }

    @Test
    void validationContractShouldRequireUiIntegrationAndFutureTests() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("Diagrama > Validar proyecto"));
        assertTrue(contract.contains("ProjectValidationCoordinator"));
        assertTrue(contract.contains("SideDock"));
        assertTrue(contract.contains("LogicalBusinessGraphValidationServiceTest"));
        assertTrue(contract.contains("LogicalBusinessGraphProjectValidationCoordinatorTest"));
        assertTrue(contract.contains("LogicalBusinessGraphOfficialExampleValidationTest"));
    }

    @Test
    void currentSourceShouldExposeValidationBuildingBlocks() throws IOException {
        String document = read(Path.of("src", "main", "java", "com", "marcosmoreira", "domainmodelstudio",
                "domain", "logicalbusinessgraph", "LogicalBusinessGraphDocument.java"));
        String issue = read(Path.of("src", "main", "java", "com", "marcosmoreira", "domainmodelstudio",
                "domain", "logicalbusinessgraph", "LogicalBusinessGraphIssue.java"));
        String severity = read(Path.of("src", "main", "java", "com", "marcosmoreira", "domainmodelstudio",
                "domain", "logicalbusinessgraph", "LogicalBusinessGraphIssueSeverity.java"));
        String relation = read(Path.of("src", "main", "java", "com", "marcosmoreira", "domainmodelstudio",
                "domain", "logicalbusinessgraph", "LogicalBusinessGraphRelationKind.java"));

        assertTrue(document.contains("semanticIssues"));
        assertTrue(document.contains("nodesByKind"));
        assertTrue(document.contains("incomingEdgesOf"));
        assertTrue(document.contains("outgoingEdgesOf"));
        assertTrue(issue.contains("warning"));
        assertTrue(issue.contains("blocking"));
        assertTrue(severity.contains("BLOCKING"));
        assertTrue(severity.contains("WARNING"));
        assertTrue(severity.contains("INFO"));
        assertTrue(relation.contains("canConnect"));
    }

    @Test
    void planDocumentsShouldReferenceAlignment6AsCurrentContinuation() throws IOException {
        List<Path> docs = List.of(
                Path.of("docs", "documentacion", "MAPA_DOCUMENTACION_VIVA.md"),
                Path.of("docs", "raiz", "PLAN_TANDAS_RESTANTES.md"),
                Path.of("docs", "diagnostico", "ESTADO_AUDITORIA_ACTUAL.md"));

        for (Path doc : docs) {
            String text = read(doc);
            assertTrue(text.contains("ALINEACION_006_VALIDACION_INTEGRAL_CRITERIOS_CALIDAD.md"),
                    () -> doc + " debe apuntar al contrato de Alineación 6.");
        }
    }

    private static List<String> currentValidationSources() {
        return List.of(
                "LogicalBusinessGraphDocument",
                "LogicalBusinessGraphIssue",
                "LogicalBusinessGraphIssueSeverity",
                "semanticIssues()",
                "canConnect(...)");
    }

    private static List<String> validationChannels() {
        return List.of(
                "Integridad estructural de dominio",
                "Validación semántica",
                "Validación de importación Markdown",
                "Validación de UI/workspace",
                "Validación de release");
    }

    private static List<String> blockingCases() {
        return List.of(
                "código de nodo obligatorio vacío",
                "relación hacia nodo inexistente",
                "relación semánticamente inválida",
                "grafo sin macroflujo");
    }

    private static List<String> warningCases() {
        return List.of(
                "MF sin FL",
                "FL sin CU",
                "CU sin ACC",
                "INV sin relación protege",
                "POST sin relación garantiza",
                "RN aislada",
                "ENT aislada",
                "PEND sin elemento bloqueado");
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
