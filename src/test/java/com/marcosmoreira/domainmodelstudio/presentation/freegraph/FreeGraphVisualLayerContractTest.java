package com.marcosmoreira.domainmodelstudio.presentation.freegraph;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Regresiones estáticas de la capa visual y edición por clic de Grafo libre. */
class FreeGraphVisualLayerContractTest {

    private static final Path PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph");

    @Test
    void visualLayerMustExposeViewModelCanvasAdapterAndRenderKit() throws IOException {
        assertTrue(Files.exists(PACKAGE.resolve("FreeGraphViewModel.java")));
        assertTrue(Files.exists(PACKAGE.resolve("FreeGraphCanvasAdapter.java")));
        assertTrue(Files.exists(PACKAGE.resolve("FreeGraphRenderKit.java")));
        assertTrue(Files.exists(PACKAGE.resolve("FreeGraphDiagramCenter.java")));
        assertTrue(Files.exists(PACKAGE.resolve("FreeGraphEditorView.java")));
        assertTrue(Files.exists(PACKAGE.resolve("FreeGraphCanvasTool.java")));
        assertTrue(Files.exists(PACKAGE.resolve("FreeGraphCanvasToolBar.java")));
    }

    @Test
    void canvasAdapterMustUseCanonicalFreeGraphLayoutIds() throws IOException {
        String source = Files.readString(PACKAGE.resolve("FreeGraphCanvasAdapter.java"));

        assertTrue(source.contains("VisualElementLayoutIds.freeGraphNode"));
        assertTrue(source.contains("VisualElementLayoutIds.freeGraphEdge"));
        assertTrue(source.contains("DiagramTypeId.FREE_GRAPH"));
        assertTrue(source.contains("implements InteractiveCanvasAdapter"));
        assertTrue(source.contains("CanvasBackgroundClickPort"));
        assertTrue(source.contains("handleBackgroundClick"));
    }

    @Test
    void viewModelMustExposeCanvasClickEditingCommands() throws IOException {
        String source = Files.readString(PACKAGE.resolve("FreeGraphViewModel.java"));

        assertTrue(source.contains("handleBackgroundCanvasClick"));
        assertTrue(source.contains("handleNodeCanvasClick"));
        assertTrue(source.contains("addNodeAt"));
        assertTrue(source.contains("addEdgeBetween"));
        assertTrue(source.contains("FreeGraphCanvasTool.ADD_NODE"));
        assertTrue(source.contains("FreeGraphCanvasTool.ADD_EDGE"));
    }

    @Test
    void g9MustMountFreeGraphInMainShellAndToolbar() throws IOException {
        Path shell = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java");
        Path toolbar = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/FreeGraphToolbarContributor.java");

        String shellSource = Files.readString(shell);
        String toolbarSource = Files.readString(toolbar);

        assertTrue(shellSource.contains("FreeGraphEditorView"));
        assertTrue(shellSource.contains("WorkspaceKind.FREE_GRAPH_DIAGRAM"));
        assertTrue(toolbarSource.contains("freeGraphActions"));
        assertTrue(toolbarSource.contains("FREE_GRAPH_ADD_NODE_TOOL"));
        assertTrue(toolbarSource.contains("FREE_GRAPH_ADD_EDGE_TOOL"));
        assertTrue(toolbarSource.contains("Agregar nodo"));
        assertTrue(toolbarSource.contains("Agregar relación"));
        assertTrue(!toolbarSource.contains("Nodo clic"));
    }


    @Test
    void freeGraphShouldUseWorkbenchSideDockAndRectangularSelectedNodes() throws IOException {
        String editorSource = Files.readString(PACKAGE.resolve("FreeGraphEditorView.java"));
        String contributorSource = Files.readString(PACKAGE.resolve("FreeGraphWorkbenchContributor.java"));
        String renderSource = Files.readString(PACKAGE.resolve("FreeGraphRenderKit.java"));
        String adapterSource = Files.readString(PACKAGE.resolve("FreeGraphCanvasAdapter.java"));

        assertTrue(editorSource.contains("DiagramWorkbenchView"));
        assertTrue(contributorSource.contains("StandardSideDockModules.appearance"));
        assertTrue(renderSource.contains("new Rectangle"));
        assertTrue(renderSource.contains("free-graph-node-text-selected"));
        assertTrue(renderSource.contains("renderSelfLoop"));
        assertTrue(renderSource.contains("new CubicCurveTo"));
        assertTrue(adapterSource.contains("CanvasLivePreviewPort"));
        assertTrue(adapterSource.contains("CanvasDragPreviewPort"));
        assertTrue(adapterSource.contains("livePreviewNodeIds"));
        assertTrue(adapterSource.contains("movableNodeIdsForCurrentGesture"));
    }

    @Test
    void g10MustExposeVisibleExportActions() throws IOException {
        Path toolbar = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/FreeGraphToolbarContributor.java");
        String source = Files.readString(toolbar);

        assertTrue(source.contains("Exportar Grafo libre como SVG vectorial"));
        assertTrue(source.contains("Exportar Grafo libre como Markdown"));
        assertTrue(source.contains("Exportar Grafo libre como PNG"));
    }

    @Test
    void freeGraphStructurePanelMustUseReadableTabsAndRelationRows() throws IOException {
        String source = Files.readString(PACKAGE.resolve("FreeGraphStructurePanel.java"));
        String css = Files.readString(Path.of("src/main/resources/css/free-graph.css"));

        assertTrue(source.contains("free-graph-structure-tabs"));
        assertTrue(source.contains("free-graph-structure-list"));
        assertTrue(source.contains("free-graph-structure-edge-route"));
        assertTrue(source.contains("free-graph-structure-edge-label"));
        assertTrue(source.contains("free-graph-structure-empty"));
        assertTrue(css.contains(".free-graph-structure-tabs .tab-header-area .tab-header-background"));
        assertTrue(css.contains(".free-graph-structure-edge-route"));
        assertTrue(css.contains(".free-graph-structure-edge-label-empty"));
    }

}
