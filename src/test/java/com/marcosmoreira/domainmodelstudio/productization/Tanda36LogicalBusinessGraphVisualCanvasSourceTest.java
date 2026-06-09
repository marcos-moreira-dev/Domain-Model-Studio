package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class Tanda36LogicalBusinessGraphVisualCanvasSourceTest {

    @Test
    void logicalBusinessGraphShouldHaveDedicatedWorkspaceAndCanvasAdapter() throws Exception {
        String workspaceKind = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/workspace/WorkspaceKind.java"));
        String shell = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java"));
        String adapter = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphCanvasAdapter.java"));

        assertTrue(workspaceKind.contains("LOGICAL_BUSINESS_GRAPH_DIAGRAM"));
        assertTrue(shell.contains("LogicalBusinessGraphEditorView"));
        assertTrue(shell.contains("WorkspaceKind.LOGICAL_BUSINESS_GRAPH_DIAGRAM"));
        assertTrue(adapter.contains("InteractiveCanvasAdapter"));
        assertTrue(adapter.contains("DiagramTypeId.LOGICAL_BUSINESS_GRAPH"));
    }

    @Test
    void logicalBusinessGraphVisualLayerMustNotUseFreeGraphDomainOrConceptualCanvas() throws Exception {
        String visualPackage = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphCanvasAdapter.java"))
                + Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphViewModel.java"));

        assertFalse(visualPackage.contains("FreeGraphDocument"));
        assertFalse(visualPackage.contains("presentation.canvas.DiagramCanvasView"));
        assertFalse(visualPackage.contains("CONCEPTUAL_CANVAS"));
    }
}
