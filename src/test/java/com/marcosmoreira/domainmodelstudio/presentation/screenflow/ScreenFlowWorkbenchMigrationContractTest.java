package com.marcosmoreira.domainmodelstudio.presentation.screenflow;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ScreenFlowWorkbenchMigrationContractTest {

    private static final Path SCREEN_FLOW_PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow"
    );

    @Test
    void screenFlowEditorMustUseWorkbenchInsteadOfLocalSplitPane() throws IOException {
        String source = Files.readString(SCREEN_FLOW_PACKAGE.resolve("ScreenFlowEditorView.java"), StandardCharsets.UTF_8);

        assertTrue(source.contains("DiagramWorkbenchView"), "El editor debe ser wrapper del workbench común.");
        assertFalse(source.contains("InteractiveDiagramCanvasView"), "El editor no debe montar el canvas mínimo legado.");
        assertFalse(source.contains("new SplitPane"), "El editor no debe reconstruir un SplitPane propio.");
    }

    @Test
    void screenFlowContributorMustExposeWorkbenchSlots() throws IOException {
        String source = Files.readString(
                SCREEN_FLOW_PACKAGE.resolve("ScreenFlowWorkbenchContributor.java"),
                StandardCharsets.UTF_8
        );

        assertTrue(source.contains("structurePanel()"), "Debe aportar slot izquierdo al workbench.");
        assertTrue(source.contains("propertiesPanel()"), "Debe aportar slot derecho al workbench.");
        assertTrue(source.contains("migratedVisualDiagram"), "Debe usar descriptor migrado, no legacy.");
    }

    @Test
    void screenFlowMustUseCanonicalSurfaceBridge() throws IOException {
        String source = Files.readString(SCREEN_FLOW_PACKAGE.resolve("ScreenFlowDiagramCenter.java"), StandardCharsets.UTF_8);

        assertTrue(source.contains("ZoomableDiagramSurface"), "Debe usar la superficie zoomable canónica.");
        assertTrue(source.contains("InteractiveCanvasSurfaceView"), "Debe usar el puente normalizado adapter + surface.");
        assertFalse(source.contains("InteractiveDiagramCanvasView"), "No debe usar el canvas mínimo legado.");
    }

    @Test
    void screenFlowRenderKitMustUseDrawingFacade() throws IOException {
        String source = Files.readString(SCREEN_FLOW_PACKAGE.resolve("ScreenFlowRenderKit.java"), StandardCharsets.UTF_8);

        assertTrue(source.contains("DiagramDrawingFacade"), "El render kit debe reutilizar fachada de dibujo común.");
        assertTrue(source.contains("safeDrawingFacade.nodes().card"), "Los nodos deben dibujarse con la fábrica transversal.");
        assertTrue(source.contains("safeDrawingFacade.connectors().polyline"), "Los conectores deben usar fábrica transversal.");
    }
}
