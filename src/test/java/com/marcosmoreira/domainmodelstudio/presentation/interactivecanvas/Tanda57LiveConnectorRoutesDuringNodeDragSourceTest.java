package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Guardarraíl fuente de la Tanda 57: el drag de nodos debe actualizar en sitio
 * las rutas de conectores conectados sin reconstruir todo el canvas en cada pixel.
 */
final class Tanda57LiveConnectorRoutesDuringNodeDragSourceTest {

    @Test
    void nodeDragUpdatesConnectedRoutesInPlace() throws IOException {
        String coordinator = read("CanvasNodeInteractionCoordinator.java");
        String registry = read("CanvasConnectorVisualRegistry.java");
        String surface = read("InteractiveCanvasSurfaceView.java");

        assertTrue(coordinator.contains("updateLiveConnectorRoutesDuringNodeDrag()"),
                "El coordinador debe actualizar rutas vivas durante MOUSE_DRAGGED.");
        assertTrue(coordinator.contains("connectorVisualRegistry.updateLiveRoutesForMovedNodes"),
                "El coordinador debe delegar al registro visual de conectores.");
        assertTrue(registry.contains("updateLiveRoutesForMovedNodes"),
                "El registro debe exponer una operación para nodos movidos.");
        assertTrue(registry.contains("visual.touchesAny(movedNodeIds)"),
                "Solo deben recalcularse relaciones conectadas a nodos movidos.");
        assertTrue(surface.contains("connectorVisualRegistry,")
                        && surface.contains("drawingFacade,"),
                "La superficie debe inyectar el registro visual y la fachada de dibujo al coordinador.");
    }

    private static String read(String fileName) throws IOException {
        return Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas",
                fileName
        ));
    }
}
