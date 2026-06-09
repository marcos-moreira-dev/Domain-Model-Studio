package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 26: arrastrar puntos intermedios no debe reconstruir el canvas durante el gesto. */
class Tanda26BendPointDragSourceTest {

    private static final Path RENDERER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasBendPointHandleRenderer.java");

    @Test
    void bendPointDragMustMoveTheHandleLiveAndRefreshOnlyOnRelease() throws IOException {
        String source = Files.readString(RENDERER, StandardCharsets.UTF_8);

        assertTrue(source.contains("moveHandleLive(handle, lastCanvasX, lastCanvasY, canvasPoint)"),
                "El punto intermedio debe moverse visualmente durante el drag sin recrear el canvas.");
        assertTrue(source.contains("handle.setCenterX(lastCanvasX[0])"));
        assertTrue(source.contains("handle.setCenterY(lastCanvasY[0])"));
        assertTrue(source.contains("moved[0] = true"));
        assertTrue(source.contains("if (moved[0])"));
        assertTrue(source.contains("refreshPreservingViewport.run();"),
                "El refresco completo debe quedar para mouse released.");
    }

    @Test
    void bendPointDragHandlerMustNotRefreshTheCanvasForEveryMousePixel() throws IOException {
        String source = Files.readString(RENDERER, StandardCharsets.UTF_8);
        int dragStart = source.indexOf("handle.setOnMouseDragged");
        int dragEnd = source.indexOf("handle.setOnMouseReleased");
        String dragBlock = source.substring(dragStart, dragEnd);

        assertFalse(dragBlock.contains("refreshPreservingViewport.run()"),
                "Refrescar durante MOUSE_DRAGGED destruye el handle y corta el drag de puntos intermedios.");
    }
}
