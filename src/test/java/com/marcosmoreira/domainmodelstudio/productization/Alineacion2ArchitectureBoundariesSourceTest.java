package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Alineación 002: fronteras arquitectónicas y zonas prohibidas. */
class Alineacion2ArchitectureBoundariesSourceTest {

    private static final Path CONTRACT = Path.of(
            "docs", "alineacion", "ALINEACION_002_FRONTERAS_ARQUITECTONICAS_ZONAS_PROHIBIDAS.md");

    @Test
    void boundaryContractShouldExistAndFreezeSacredZones() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("Alineación 002"), "Debe identificar la alineación.");
        assertTrue(contract.contains("Fronteras arquitectónicas y zonas prohibidas"));
        assertTrue(contract.contains("pantalla de inicio"));
        assertTrue(contract.contains("modelo conceptual"));
        assertTrue(contract.contains("canvas conceptual"));
        assertTrue(contract.contains("DiagramCanvasView"));
        assertTrue(contract.contains("DiagramCanvasViewModel"));
        assertTrue(contract.contains("ChenDiagramRenderer"));
        assertTrue(contract.contains("CrowsFootDiagramRenderer"));
        assertTrue(contract.contains("No tocar salvo emergencia explícita"));
    }

    @Test
    void boundaryContractShouldSeparateLegacySidebarFromTransversalSideDock() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("presentation/sidebar/"),
                "Debe mencionar el sidebar legacy del modelo conceptual.");
        assertTrue(contract.contains("presentation/sidedock/"),
                "Debe mencionar el SideDock transversal.");
        assertTrue(contract.contains("Sidebar legacy conceptual"));
        assertTrue(contract.contains("SideDock transversal"));
        assertTrue(contract.contains("El Grafo lógico debe usar el SideDock transversal"));
    }

    @Test
    void boundaryContractShouldPreserveHelpAndVisualBoundaries() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("Menú `Ayuda`"));
        assertTrue(contract.contains("Guía académica teórica"));
        assertTrue(contract.contains("Botón `Ayuda` del SideDock"));
        assertTrue(contract.contains("Ayuda operativa"));
        assertTrue(contract.contains("sin bordes redondeados ornamentales"));
        assertTrue(contract.contains("no debe aplicarse de forma ciega a geometría semántica"));
    }

    @Test
    void logicalBusinessGraphDomainShouldRemainPureAndNotReuseFreeGraphDocument() throws IOException {
        Path domain = Path.of("src", "main", "java", "com", "marcosmoreira", "domainmodelstudio", "domain", "logicalbusinessgraph");

        for (Path file : javaFiles(domain)) {
            String source = read(file);
            assertFalse(source.contains("import javafx."), () -> file + " no debe importar JavaFX.");
            assertFalse(source.contains("import com.marcosmoreira.domainmodelstudio.infrastructure"),
                    () -> file + " no debe depender de infrastructure.");
            assertFalse(source.contains("import com.marcosmoreira.domainmodelstudio.presentation"),
                    () -> file + " no debe depender de presentation.");
            assertFalse(source.contains("import com.marcosmoreira.domainmodelstudio.domain.freegraph"),
                    () -> file + " no debe depender del dominio de grafo libre.");
            assertFalse(source.contains("FreeGraphDocument"),
                    () -> file + " no debe reutilizar FreeGraphDocument como dominio final.");
        }
    }

    @Test
    void conceptualCanvasShouldNotReferenceLogicalBusinessGraphSideDockOrFreeGraphDocument() throws IOException {
        for (Path file : protectedConceptualCanvasFiles()) {
            String source = read(file);
            assertFalse(source.contains("logicalbusinessgraph"),
                    () -> file + " no debe referenciar paquetes del Grafo lógico.");
            assertFalse(source.contains("LogicalBusinessGraphDocument"),
                    () -> file + " no debe referenciar el documento del Grafo lógico.");
            assertFalse(source.contains("FreeGraphDocument"),
                    () -> file + " no debe referenciar FreeGraphDocument.");
            assertFalse(source.contains("SideDock"),
                    () -> file + " no debe integrar SideDock transversal.");
            assertFalse(source.contains("WorkbenchSideDock"),
                    () -> file + " no debe integrar WorkbenchSideDock.");
        }
    }

    private static List<Path> protectedConceptualCanvasFiles() {
        return List.of(
                Path.of("src", "main", "java", "com", "marcosmoreira", "domainmodelstudio", "presentation", "canvas", "DiagramCanvasView.java"),
                Path.of("src", "main", "java", "com", "marcosmoreira", "domainmodelstudio", "presentation", "canvas", "DiagramCanvasViewModel.java"),
                Path.of("src", "main", "java", "com", "marcosmoreira", "domainmodelstudio", "presentation", "canvas", "ChenDiagramRenderer.java"),
                Path.of("src", "main", "java", "com", "marcosmoreira", "domainmodelstudio", "presentation", "canvas", "CrowsFootDiagramRenderer.java"));
    }

    private static List<Path> javaFiles(Path directory) throws IOException {
        try (Stream<Path> stream = Files.walk(directory)) {
            return stream.filter(path -> path.toString().endsWith(".java")).toList();
        }
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
