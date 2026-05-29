package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class Tanda60BaseLayerPolicySourceTest {

    private static final Path MAIN = Path.of("src/main/java");

    @Test
    void runtimeCanvasShouldSplitBackgroundNodesFromForegroundNodes() throws Exception {
        String layers = Files.readString(MAIN.resolve("com/marcosmoreira/domainmodelstudio/presentation/diagramcanvas/DiagramSurfaceLayers.java"));
        String surface = Files.readString(MAIN.resolve("com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java"));
        String splitter = Files.readString(MAIN.resolve("com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasRenderedNodeLayers.java"));

        assertTrue(layers.contains("backgroundNodeLayer") && layers.contains("setDiagramContent(\n            Collection<? extends Node> backgroundNodes"),
                "La superficie debe tener una capa física para nodos de fondo.");
        assertTrue(surface.contains("setLayeredContent(renderedNodes.backgroundNodes(), connectorNodes, renderedNodes.foregroundNodes(), overlays)"),
                "El canvas runtime debe montar fondos, conectores, nodos y overlays por separado.");
        assertTrue(splitter.contains("CanvasVisualLayer.CONTAINER") && splitter.contains("foreground.add(rendered)"),
                "La partición de nodos debe reutilizar la política semántica transversal.");
    }

    @Test
    void pngExportShouldUseTheSameBaseLayerPolicy() throws Exception {
        String exporter = Files.readString(MAIN.resolve("com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasPngExporter.java"));

        int background = exporter.indexOf("renderedNodes.backgroundNodes()");
        int connectors = exporter.indexOf("renderConnectors(model)");
        int foreground = exporter.indexOf("renderedNodes.foregroundNodes()");
        int labels = exporter.indexOf("renderConnectorLabels(model)");

        assertTrue(background >= 0 && connectors > background,
                "PNG debe dibujar conectores encima de fondos semánticos.");
        assertTrue(foreground > connectors,
                "PNG debe dibujar tarjetas/nodos encima de relaciones.");
        assertTrue(labels > foreground,
                "PNG debe mantener labels encima de nodos y relaciones.");
    }
}
