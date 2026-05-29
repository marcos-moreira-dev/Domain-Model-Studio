package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class InteractiveCanvasRenderFailureSourceTest {

    @Test
    void surfaceShouldCatchRenderFailuresAndNotifyOptionalPort() throws Exception {
        String source = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java"));

        assertTrue(source.contains("catch (OutOfMemoryError error)"),
                "El canvas debe capturar OOM para no dejar la app muda ante vistas enormes.");
        assertTrue(source.contains("handleRenderFailure"),
                "Los fallos de render deben pasar por una rutina central de degradación.");
        assertTrue(source.contains("CanvasRenderFailurePort"),
                "El canvas debe notificar al editor especializado mediante un puerto opcional.");
        assertTrue(source.contains("surface.clearContent()"),
                "La superficie debe vaciarse si una construcción visual falla.");
    }
}
