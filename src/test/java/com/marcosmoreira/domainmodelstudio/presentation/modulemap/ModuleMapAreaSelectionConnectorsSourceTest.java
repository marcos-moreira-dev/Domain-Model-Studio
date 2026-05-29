package com.marcosmoreira.domainmodelstudio.presentation.modulemap;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

final class ModuleMapAreaSelectionConnectorsSourceTest {

    @Test
    void areaSelectionIncludesFunctionalDependenciesAndGroupDragMovesTheirBendpoints() throws Exception {
        String adapter = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapCanvasAdapter.java"));

        assertTrue(adapter.contains("selectElementsInside(CanvasBounds selectionBounds, boolean additive)"));
        assertTrue(adapter.contains("selectionSupport.selectNodesAndConnectors(selectedNodes, selectedConnectors)"));
        assertTrue(adapter.contains("moveSelectedConnectorBendPointsBy"));
    }
}
