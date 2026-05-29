package com.marcosmoreira.domainmodelstudio.presentation.freegraph;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** FreeGraph es diagrama visual activo y debe quedar cubierto por las rutas transversales. */
class Tanda10FreeGraphCoverageSourceTest {

    @Test
    void freeGraphEditorUsesVisualWorkbenchAndRegistersCommonViewActions() throws IOException {
        String source = read("presentation/freegraph/FreeGraphEditorView.java");

        assertTrue(source.contains("DiagramWorkbenchView"));
        assertTrue(source.contains("new DiagramWorkbenchView"));
        assertTrue(source.contains("registerPngExportAction"));
        assertTrue(source.contains("registerDiagramFitAction"));
        assertTrue(source.contains("registerDiagramCenterAction"));
    }

    @Test
    void freeGraphCenterUsesCanonicalCanvasSurfaceAndPngExporter() throws IOException {
        String source = read("presentation/freegraph/FreeGraphDiagramCenter.java");

        assertTrue(source.contains("InteractiveCanvasSurfaceView"));
        assertTrue(source.contains("ZoomableDiagramSurface"));
        assertTrue(source.contains("CanvasInitialFitScheduler"));
        assertTrue(source.contains("InteractiveCanvasPngExporter"));
        assertTrue(source.contains("ViewportFitMode.FIT_TO_CONTENT"));
    }

    @Test
    void freeGraphIsRegisteredInWorkspaceExportAndToolbarRoutes() throws IOException {
        String shell = read("presentation/shell/MainShellView.java");
        String registry = read("presentation/exportable/ActiveOutputContributorRegistry.java");
        String policy = read("presentation/exportable/ProjectExportFormatPolicy.java");
        String toolbar = read("presentation/toolbar/FreeGraphToolbarContributor.java");

        assertTrue(shell.contains("WorkspaceKind.FREE_GRAPH_DIAGRAM"));
        assertTrue(shell.contains("freeGraphRoot"));
        assertTrue(registry.contains("FreeGraphActiveOutputContributor"));
        assertTrue(policy.contains("formatsForFreeGraph"));
        assertTrue(policy.contains("DiagramTypeId.FREE_GRAPH"));
        assertTrue(toolbar.contains("DiagramTypeId.FREE_GRAPH"));
        assertTrue(toolbar.contains("reorganizeAction"));
        assertTrue(toolbar.contains("fitToContentAction"));
        assertTrue(toolbar.contains("centerDiagramAction"));
    }

    private static String read(String relativePath) throws IOException {
        return Files.readString(
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio").resolve(relativePath),
                StandardCharsets.UTF_8);
    }
}
