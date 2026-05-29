package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class InteractiveCanvasSurfaceInteractionSourceTest {

    private static final Path SURFACE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java"
    );
    private static final Path NODE_INTERACTION = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeInteractionCoordinator.java"
    );
    private static final Path BEND_POINT_RENDERER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasBendPointHandleRenderer.java"
    );

    @Test
    void surfaceSupportsMinimalPersistentLayoutInteractions() throws IOException {
        String source = Files.readString(SURFACE, StandardCharsets.UTF_8);
        String nodeInteraction = Files.readString(NODE_INTERACTION, StandardCharsets.UTF_8);
        String bendPointRenderer = Files.readString(BEND_POINT_RENDERER, StandardCharsets.UTF_8);

        assertTrue(source.contains("CanvasNodeDragController"),
                "La superficie común debe mover nodos mediante el puerto de layout, no modificando nodos JavaFX.");
        assertTrue(source.contains("CanvasBendPointController"),
                "La superficie común debe exponer edición mínima de puntos intermedios de conectores.");
        assertTrue(source.contains("renderBendPointHandles"),
                "La superficie debe renderizar handles cuando un conector seleccionado tiene puntos intermedios.");
        assertTrue(source.contains("event.getClickCount() >= 2"),
                "Doble clic sobre conector debe permitir agregar punto intermedio básico.");
        assertTrue(source.contains("KeyCode.DELETE") && source.contains("removeSelected"),
                "Delete/Backspace debe permitir eliminar el punto intermedio seleccionado.");
        assertTrue(nodeInteraction.contains("surface.root().requestFocus()")
                        || bendPointRenderer.contains("surface.root().requestFocus()"),
                "El canvas debe tomar foco para que los atajos de edición funcionen después de interactuar.");
    }
}
