package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl fuente para el portapapeles visual inicial. */
class VisualSelectionClipboardSourceTest {

    private static final Path ROOT = Path.of("");

    @Test
    void canvasShouldExposeCopyPastePortAndKeyboardShortcuts() throws IOException {
        String port = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasSelectionClipboardPort.java");
        assertTrue(port.contains("boolean copySelectionToClipboard()"));
        assertTrue(port.contains("boolean pasteSelectionFromClipboard()"));

        String surface = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java");
        assertTrue(surface.contains("CanvasClipboardKeyboardInstaller.install"));
        assertTrue(surface.contains("refreshPreservingViewport()"));

        String installer = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasClipboardKeyboardInstaller.java");
        assertTrue(installer.contains("KeyCode.C"));
        assertTrue(installer.contains("KeyCode.V"));
        assertTrue(installer.contains("CanvasSelectionClipboardPort"));
        assertTrue(installer.contains("refreshPreservingViewport"));
    }

    @Test
    void freeGraphAndLogicalGraphShouldImplementClipboardPort() throws IOException {
        String freeGraphAdapter = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphCanvasAdapter.java");
        assertTrue(freeGraphAdapter.contains("CanvasSelectionClipboardPort"));
        assertTrue(freeGraphAdapter.contains("copySelectionToClipboard()"));
        assertTrue(freeGraphAdapter.contains("pasteSelectionFromClipboard()"));

        String logicalAdapter = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphCanvasAdapter.java");
        assertTrue(logicalAdapter.contains("CanvasSelectionClipboardPort"));
        assertTrue(logicalAdapter.contains("copySelectionToClipboard()"));
        assertTrue(logicalAdapter.contains("pasteSelectionFromClipboard()"));
    }

    @Test
    void payloadsShouldPreserveNodesRelationsAndLayouts() throws IOException {
        String freePayload = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/visualtransfer/FreeGraphSelectionTransferPayload.java");
        assertTrue(freePayload.contains("List<FreeGraphNode> nodes"));
        assertTrue(freePayload.contains("List<FreeGraphEdge> edges"));
        assertTrue(freePayload.contains("List<NodeLayout> nodeLayouts"));
        assertTrue(freePayload.contains("List<ConnectorLayout> connectorLayouts"));

        String logicalPayload = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/visualtransfer/LogicalBusinessGraphSelectionTransferPayload.java");
        assertTrue(logicalPayload.contains("List<LogicalBusinessGraphNode> nodes"));
        assertTrue(logicalPayload.contains("List<LogicalBusinessGraphEdge> edges"));
        assertTrue(logicalPayload.contains("List<NodeLayout> nodeLayouts"));
        assertTrue(logicalPayload.contains("List<ConnectorLayout> connectorLayouts"));
    }

    @Test
    void viewModelsShouldValidateCompatibilityAndGenerateNewIds() throws IOException {
        String freeGraph = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphViewModel.java");
        assertTrue(freeGraph.contains("VisualSelectionClipboard.current()"));
        assertTrue(freeGraph.contains("instanceof FreeGraphSelectionTransferPayload"));
        assertTrue(freeGraph.contains("uniqueFreeGraphNodeId"));
        assertTrue(freeGraph.contains("uniqueFreeGraphEdgeId"));
        assertTrue(freeGraph.contains("withFreeGraph(updatedDocument)"));

        String logicalGraph = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphViewModel.java");
        assertTrue(logicalGraph.contains("VisualSelectionClipboard.current()"));
        assertTrue(logicalGraph.contains("instanceof LogicalBusinessGraphSelectionTransferPayload"));
        assertTrue(logicalGraph.contains("uniqueLogicalNodeCode"));
        assertTrue(logicalGraph.contains("uniqueLogicalEdgeId"));
        assertTrue(logicalGraph.contains("withLogicalBusinessGraphDocument(updatedDocument)"));
    }

    @Test
    void documentationShouldKeepTransferDialogOutOfThisScope() throws IOException {
        String doc = read("docs/desarrollo/TANDA_VIS_COPY_001_PORTAPAPELES_VISUAL.md");
        assertTrue(doc.contains("Grafo libre"));
        assertTrue(doc.contains("Grafo lógico"));
        assertTrue(doc.contains("VIS-COPY-002"));
        assertTrue(doc.contains("Transferir selección"));
    }

    private static String read(String relativePath) throws IOException {
        return Files.readString(ROOT.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
