package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Alineación 001: contrato final de producto antes de tocar código de cierre. */
class Alineacion1ProductContractSourceTest {

    private static final Path CONTRACT = Path.of(
            "docs", "alineacion", "ALINEACION_001_CONTRATO_FINAL_PRODUCTO.md");

    @Test
    void productContractShouldExistAndDefineAntiFacadeRule() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("Alineación 001"), "Debe identificar la alineación.");
        assertTrue(contract.contains("capacidad visible = implementación real + prueba verificable + documentación trazable + smoke"),
                "Debe conservar la regla anti-fachada principal.");
        assertTrue(contract.contains("Contrato mínimo de tipo productivo"),
                "Debe fijar el contrato mínimo de tipo productivo.");
        assertTrue(contract.contains("Jerarquía de verdad"),
                "Debe definir qué fuente manda cuando haya contradicciones.");
    }

    @Test
    void productContractShouldTraceAllOfficialDiagramTypes() throws IOException {
        String contract = read(CONTRACT);

        for (String typeId : officialTypeIds()) {
            assertTrue(contract.contains("`" + typeId + "`"),
                    () -> "El contrato debe mencionar el tipo oficial " + typeId);
        }
    }

    @Test
    void productContractShouldProtectSacredAreasAndHelpBoundary() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("pantalla de inicio"));
        assertTrue(contract.contains("modelo conceptual"));
        assertTrue(contract.contains("canvas conceptual"));
        assertTrue(contract.contains("DiagramCanvasView"));
        assertTrue(contract.contains("Menú `Ayuda`"));
        assertTrue(contract.contains("Guía académica teórica"));
        assertTrue(contract.contains("Botón `Ayuda` del SideDock"));
        assertTrue(contract.contains("Ayuda operativa de herramienta"));
    }

    @Test
    void productContractShouldDefineLogicalBusinessGraphExitCriteria() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("`logical-business-graph`"));
        assertTrue(contract.contains("vista visual semántica derivada del levantamiento lógico"));
        assertTrue(contract.contains("no debe quedar indefinidamente como `EXPERIMENTAL`"));
        assertTrue(contract.contains("Obligaciones antes de declarar `logical-business-graph` como `AVAILABLE`"));
        assertTrue(contract.contains("EXPORT_PNG"));
        assertTrue(contract.contains("EXPORT_SVG"));
        assertTrue(contract.contains("SAVE_DMS"));
        assertTrue(contract.contains("LOAD_DMS"));
        assertTrue(contract.contains("THEORY_HELP"));
        assertTrue(contract.contains("AI_RESOURCES"));
    }

    @Test
    void productContractShouldKeepStraightCornerRule() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("sin bordes redondeados ornamentales"));
        assertTrue(contract.contains("sin -fx-border-radius distinto de 0"));
        assertTrue(contract.contains("sin -fx-background-radius distinto de 0"));
        assertTrue(contract.contains("CornerRadii"));
    }

    private static List<String> officialTypeIds() {
        return List.of(
                "logical-business-intake",
                "logical-business-graph",
                "conceptual-model",
                "data-dictionary",
                "bpmn-basic",
                "operational-flow",
                "c4-context",
                "c4-containers",
                "technical-deployment",
                "uml-class",
                "uml-use-case",
                "uml-activity",
                "uml-sequence",
                "uml-state",
                "admin-module-map",
                "roles-permissions-map",
                "screen-flow",
                "admin-wireframes",
                "free-graph");
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
