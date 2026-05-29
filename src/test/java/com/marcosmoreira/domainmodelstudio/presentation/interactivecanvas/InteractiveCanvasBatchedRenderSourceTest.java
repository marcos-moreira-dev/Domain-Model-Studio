package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class InteractiveCanvasBatchedRenderSourceTest {

    private static final Path SURFACE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java"
    );
    private static final Path NODE_INTERACTION = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeInteractionCoordinator.java"
    );
    private static final Path NODE_REGISTRY = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeVisualRegistry.java"
    );

    @Test
    void surfaceShouldRenderLargeViewsInJavaFxBatches() throws IOException {
        String source = Files.readString(SURFACE, StandardCharsets.UTF_8);

        assertTrue(source.contains("InteractiveCanvasRenderBatchPolicy.defaults()"),
                "La superficie debe decidir por política cuándo renderizar por lotes.");
        assertTrue(source.contains("renderInBatches"),
                "Las vistas grandes deben entrar por una ruta de render diferido.");
        assertTrue(source.contains("Platform.runLater"),
                "Cada lote debe repartirse en pulsos JavaFX para evitar congelamiento largo.");
        assertTrue(source.contains("renderSequence"),
                "Un refresco nuevo debe poder invalidar lotes viejos pendientes.");
        assertTrue(source.contains("connectorBatchSize()") && source.contains("nodeBatchSize()"),
                "Conectores y nodos deben tener tamaños de lote separados.");
    }

    @Test
    void draggingShouldPreviewNodeWithoutFullRefreshOnEveryMouseMove() throws IOException {
        String nodeInteraction = Files.readString(NODE_INTERACTION, StandardCharsets.UTF_8);
        String nodeRegistry = Files.readString(NODE_REGISTRY, StandardCharsets.UTF_8);

        assertTrue(nodeRegistry.contains("previewNodeDrag"),
                "El arrastre debe previsualizar el movimiento del nodo JavaFX sin reconstruir todo el canvas.");
        assertTrue(nodeRegistry.contains("setTranslateX") && nodeRegistry.contains("setTranslateY"),
                "La previsualización debe mover el nodo renderizado durante el drag.");
        assertTrue(nodeInteraction.contains("nodeDragController.end();")
                        && nodeInteraction.contains("refreshPreservingViewport.run();"),
                "El refresco completo del drag debe quedar para el cierre del gesto.");
    }
}
