package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphRelationKind;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Alineación 003: contrato semántico del Grafo lógico del negocio. */
class Alineacion3LogicalBusinessGraphSemanticContractSourceTest {

    private static final Path CONTRACT = Path.of(
            "docs", "alineacion", "ALINEACION_003_CONTRATO_SEMANTICO_GRAFO_LOGICO.md");

    @Test
    void semanticContractShouldExistAndDefineLogicalBusinessGraphIdentity() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("Alineación 003"), "Debe identificar la alineación.");
        assertTrue(contract.contains("Contrato semántico del Grafo lógico del negocio"));
        assertTrue(contract.contains("vista visual semántica derivada/revisable del Levantamiento lógico"));
        assertTrue(contract.contains("no es un grafo libre renombrado"));
        assertTrue(contract.contains("casi árbol, pero no árbol"));
        assertTrue(contract.contains("Levantamiento lógico"));
    }

    @Test
    void semanticContractShouldListEveryOfficialNodePrefix() throws IOException {
        String contract = read(CONTRACT);

        for (LogicalBusinessGraphNodeKind kind : LogicalBusinessGraphNodeKind.values()) {
            assertTrue(contract.contains("`" + kind.prefix() + "`"),
                    () -> "El contrato debe mencionar el prefijo " + kind.prefix());
            assertTrue(contract.contains(kind.displayName()),
                    () -> "El contrato debe mencionar el nombre " + kind.displayName());
        }
    }

    @Test
    void semanticContractShouldListEveryOfficialRelationCode() throws IOException {
        String contract = read(CONTRACT);

        for (LogicalBusinessGraphRelationKind relation : LogicalBusinessGraphRelationKind.values()) {
            assertTrue(contract.contains("`" + relation.code() + "`"),
                    () -> "El contrato debe mencionar la relación " + relation.code());
        }
    }

    @Test
    void semanticEnumsShouldKeepExpectedVocabularySize() {
        assertEquals(13, LogicalBusinessGraphNodeKind.values().length,
                "El vocabulario de nodos no debe cambiar sin actualizar el contrato semántico.");
        assertEquals(17, LogicalBusinessGraphRelationKind.values().length,
                "El vocabulario de relaciones no debe cambiar sin actualizar el contrato semántico.");
    }

    @Test
    void semanticContractShouldDefineBackboneAndManualEditingScope() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("MF → FL → CU → ACC"));
        assertTrue(contract.contains("MF contiene FL"));
        assertTrue(contract.contains("FL usa CU"));
        assertTrue(contract.contains("CU ejecuta ACC"));
        assertTrue(contract.contains("CRUD estructural completo no es obligatorio"));
        assertTrue(contract.contains("no debe reutilizarse en Grafo libre"));
    }

    @Test
    void semanticContractShouldDefineValidationLevels() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("Bloqueos"));
        assertTrue(contract.contains("Advertencias fuertes"));
        assertTrue(contract.contains("Advertencias suaves"));
        assertTrue(contract.contains("INV sin protección"));
        assertTrue(contract.contains("POST sin garantía"));
        assertTrue(contract.contains("PEND sin elemento bloqueado"));
    }

    @Test
    void sourceRelationPolicyShouldStillSupportStableBackboneConnections() {
        assertTrue(LogicalBusinessGraphRelationKind.CONTAINS.canConnect(
                LogicalBusinessGraphNodeKind.MACRO_FLOW, LogicalBusinessGraphNodeKind.FLOW));
        assertTrue(LogicalBusinessGraphRelationKind.USES.canConnect(
                LogicalBusinessGraphNodeKind.FLOW, LogicalBusinessGraphNodeKind.USE_CASE));
        assertTrue(LogicalBusinessGraphRelationKind.EXECUTES.canConnect(
                LogicalBusinessGraphNodeKind.USE_CASE, LogicalBusinessGraphNodeKind.ACTION));
        assertTrue(LogicalBusinessGraphRelationKind.REQUIRES.canConnect(
                LogicalBusinessGraphNodeKind.ACTION, LogicalBusinessGraphNodeKind.PRECONDITION));
        assertTrue(LogicalBusinessGraphRelationKind.PROTECTS.canConnect(
                LogicalBusinessGraphNodeKind.ACTION, LogicalBusinessGraphNodeKind.INVARIANT));
        assertTrue(LogicalBusinessGraphRelationKind.GUARANTEES.canConnect(
                LogicalBusinessGraphNodeKind.ACTION, LogicalBusinessGraphNodeKind.POSTCONDITION));
    }

    @Test
    void planDocumentsShouldReferenceAlignment3AsCurrentContinuation() throws IOException {
        List<Path> docs = List.of(
                Path.of("docs", "documentacion", "MAPA_DOCUMENTACION_VIVA.md"),
                Path.of("docs", "raiz", "PLAN_TANDAS_RESTANTES.md"),
                Path.of("docs", "diagnostico", "ESTADO_AUDITORIA_ACTUAL.md"));

        for (Path doc : docs) {
            String text = read(doc);
            assertTrue(text.contains("ALINEACION_003_CONTRATO_SEMANTICO_GRAFO_LOGICO.md"),
                    () -> doc + " debe apuntar al contrato semántico de Alineación 3.");
        }
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
