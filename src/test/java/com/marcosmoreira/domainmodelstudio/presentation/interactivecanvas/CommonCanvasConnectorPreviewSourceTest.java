package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class CommonCanvasConnectorPreviewSourceTest {

    @Test
    void commonCanvasKeepsConnectorsUnderstandableDuringLiveNodeDrag() throws IOException {
        String coordinator = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeInteractionCoordinator.java"));
        String surface = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java"));
        String preview = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasConnectorDragPreviewLayer.java"));

        assertTrue(surface.contains("new CanvasConnectorDragPreviewLayer"),
                "La superficie común conserva la capa auxiliar de conectores temporales para compatibilidad visual.");
        assertTrue(coordinator.contains("nodeDragController.dragTo(canvasPoint.getX(), canvasPoint.getY())"),
                "El arrastre en tiempo real debe actualizar el layout durante MOUSE_DRAGGED.");
        assertTrue(coordinator.contains("refreshPreservingViewport.run()"),
                "El canvas debe refrescar conectores y labels mientras el nodo se desplaza.");
        assertTrue(coordinator.contains("connectorPreviewLayer.clear()"),
                "El preview diferido debe limpiarse cuando el modo vigente es arrastre real en layout.");
        assertTrue(preview.contains("CanvasConnectorGeometry.edgeToEdgePoints"),
                "La capa auxiliar debe conservar la misma geometría básica de conectores del canvas común.");
        assertTrue(preview.contains("layout.translatedBy(deltaX, deltaY)"),
                "El cálculo auxiliar debe poder usar layouts temporales desplazados si se reactiva el preview diferido.");
    }
}
