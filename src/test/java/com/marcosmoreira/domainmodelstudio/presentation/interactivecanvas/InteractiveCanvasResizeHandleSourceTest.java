package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class InteractiveCanvasResizeHandleSourceTest {

    @Test
    void surfaceUsesResizePortOnlyForProfilesThatAllowNodeResize() throws Exception {
        String surface = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java"));
        String layer = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasResizeHandleLayer.java"));
        String source = surface + "\n" + layer;

        assertTrue(source.contains("CanvasResizePort"));
        assertTrue(source.contains("interactionProfile.supportsNodeResize()"));
        assertTrue(surface.contains("renderResizeHandles"));
        assertTrue(source.contains("interactive-canvas-resize-handle"));
        assertTrue(layer.contains("resizePort.resizeNode"));
        assertTrue(layer.contains("resizePort.supportsNodeResize(nodeId)"));
        assertTrue(layer.contains("dragPreview"));
        assertTrue(layer.contains("interactive-canvas-resize-preview"));
    }
}
