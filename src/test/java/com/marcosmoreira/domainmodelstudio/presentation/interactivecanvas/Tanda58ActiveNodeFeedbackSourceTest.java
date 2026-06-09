package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Guardarraíl fuente de la Tanda 58: un nodo seleccionado debe mostrar un
 * estado activo fuerte cuando recibe clic sin depender de un refresh completo.
 */
final class Tanda58ActiveNodeFeedbackSourceTest {

    @Test
    void nodePressMarksActiveSelectionLocally() throws IOException {
        String coordinator = readMain("CanvasNodeInteractionCoordinator.java");
        String registry = readMain("CanvasNodeVisualRegistry.java");
        String css = Files.readString(Path.of("src/main/resources/css/interactive-canvas.css"));

        assertTrue(coordinator.contains("visualRegistry.markActiveNodeLocally(node.id(), additiveOrGroup)"),
                "El click sobre nodo debe aplicar feedback activo local sin reconstruir el canvas.");
        assertTrue(registry.contains("markActiveNodeLocally"),
                "El registro visual debe centralizar el estado activo de runtime.");
        assertTrue(registry.contains("interactive-canvas-node-active-selection"),
                "El estado activo debe tener una clase CSS explícita y removible.");
        assertTrue(registry.contains("clearActiveSelectionStyle"),
                "Limpiar selección local también debe limpiar el estado activo.");
        assertTrue(registry.contains("interactive-canvas-node-active-selection-target"),
                "El feedback activo debe alcanzar las figuras internas de Group/Pane sin refresh completo.");
        assertTrue(css.contains(".interactive-canvas-node-active-selection-target"),
                "La hoja de estilo debe aclarar las figuras internas marcadas por el registro visual.");
        assertTrue(css.contains(".interactive-canvas-node-active-selection .diagram-node"),
                "La hoja de estilo debe seguir cubriendo nodos basados en Shape/diagram-node.");
        assertTrue(css.contains(".interactive-canvas-node-active-selection .interactive-canvas-node"),
                "La hoja de estilo debe cubrir tarjetas JavaFX genéricas.");
        assertTrue(css.contains(".interactive-canvas-node-active-selection .architecture-canvas-zone-title-handle"),
                "Los contenedores handle-only deben recibir feedback en el título, no en todo el rectángulo.");
    }

    private static String readMain(String fileName) throws IOException {
        return Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas",
                fileName
        ));
    }
}
