package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

final class Tanda52SecondDragSelectionSourceTest {

    @Test
    void nodeDragControllerKeepsPressedNodeAsFallbackDuringDrag() throws Exception {
        String source = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeDragController.java"));

        assertTrue(source.contains("private String draggedNodeId;"));
        assertTrue(source.contains("ensureDraggedNodeStillSelected()"));
        assertTrue(source.contains("selectionPort.selectNode(draggedNodeId, false)"));
    }

    @Test
    void specializedDiagramCentersDoNotRefreshCanvasOnPureSelectionChanges() throws Exception {
        for (String file : List.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureDiagramCenter.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorDiagramCenter.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphDiagramCenter.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphDiagramCenter.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapDiagramCenter.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowDiagramCenter.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeDiagramCenter.java")) {
            String source = Files.readString(Path.of(file));
            assertFalse(source.contains("selectedNodeProperty().addListener((observable, previous, current) -> refreshCanvasOnly())"), file);
            assertFalse(source.contains("selectedEdgeProperty().addListener((observable, previous, current) -> refreshCanvasOnly())"), file);
            assertFalse(source.contains("selectedModuleProperty().addListener((observable, previous, current) -> refreshCanvasOnly())"), file);
            assertFalse(source.contains("selectedDependencyProperty().addListener((observable, previous, current) -> refreshCanvasOnly())"), file);
            assertFalse(source.contains("selectedScreenProperty().addListener((observable, previous, current) -> refreshCanvasOnly())"), file);
            assertFalse(source.contains("selectedTransitionProperty().addListener((observable, previous, current) -> refreshCanvasOnly())"), file);
            assertFalse(source.contains("selectedComponentProperty().addListener((observable, previous, current) -> refreshCanvasOnly())"), file);
        }
    }

    @Test
    void structurePanelsUseSelectionSynchronizationGuard() throws Exception {
        for (String file : List.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureStructurePanel.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorStructurePanel.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphStructurePanel.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphStructurePanel.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapStructurePanel.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowStructurePanel.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeStructurePanel.java")) {
            String source = Files.readString(Path.of(file));
            assertTrue(source.contains("CanvasSelectionSynchronizationGuard"), file);
            assertTrue(source.contains("selectionSyncGuard.active()"), file);
            assertTrue(source.contains("selectionSyncGuard.runGuarded"), file);
        }
    }
}
