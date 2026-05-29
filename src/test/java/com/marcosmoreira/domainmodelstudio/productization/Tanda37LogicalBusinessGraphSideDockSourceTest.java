package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de SideDock, propiedades, leyenda y ayuda operativa del Grafo lógico. */
class Tanda37LogicalBusinessGraphSideDockSourceTest {

    @Test
    void logicalBusinessGraphShouldExposeStructurePropertiesLegendAndHelp() throws Exception {
        String contributor = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphWorkbenchContributor.java");
        String structure = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphStructurePanel.java");
        String properties = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphPropertiesPanel.java");
        String help = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/sidedock/OperationalHelpCatalog.java");

        assertAll(
                () -> assertTrue(contributor.contains("structurePanel()")),
                () -> assertTrue(contributor.contains("propertiesPanel()")),
                () -> assertTrue(contributor.contains("SideDockModuleId.COMPONENTS")),
                () -> assertTrue(structure.contains("new Tab(\"Leyenda\"")),
                () -> assertTrue(structure.contains("new Tab(\"Validación\"")),
                () -> assertTrue(properties.contains("applyNodeChanges")),
                () -> assertTrue(properties.contains("applyEdgeChanges")),
                () -> assertTrue(help.contains("logicalBusinessGraph")),
                () -> assertTrue(help.contains("MF, FL, CU, ACC, RN, PRE, INV, POST, ENT, EST, REP, RISK y PEND"))
        );
    }

    @Test
    void logicalBusinessGraphSideDockShouldRespectTransversalShellAndBoundaries() throws Exception {
        String contributor = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphWorkbenchContributor.java");
        String visualPackage = contributor
                + read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphStructurePanel.java")
                + read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphPropertiesPanel.java")
                + read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphLegendPanel.java");

        assertAll(
                () -> assertTrue(contributor.contains("DiagramWorkbenchContributor")),
                () -> assertTrue(contributor.contains("StandardSideDockModules.appearance")),
                () -> assertFalse(visualPackage.contains("FreeGraphDocument")),
                () -> assertFalse(visualPackage.contains("presentation.canvas.DiagramCanvasView")),
                () -> assertFalse(visualPackage.contains("CONCEPTUAL_CANVAS"))
        );
    }

    private static String read(String path) throws Exception {
        return Files.readString(Path.of(path));
    }
}
