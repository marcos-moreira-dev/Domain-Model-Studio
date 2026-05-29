package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

final class InteractiveCanvasLiveBendPointSourceTest {

    @Test
    void bendPointDragUpdatesConnectorRouteLiveWithoutRebuildingCanvasOnEveryPixel() throws Exception {
        String renderer = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasBendPointHandleRenderer.java"));

        assertTrue(renderer.contains("bendPointController.move(connectorId, index, canvasPoint.getX(), canvasPoint.getY());"));
        assertTrue(renderer.contains("connectorVisualRegistry.updateLiveRoute(connectorId, adapter, drawingFacade);"));
        assertTrue(renderer.contains("if (moved[0])"));
        assertTrue(renderer.contains("El punto ya fue actualizado en tiempo real durante MOUSE_DRAGGED"));

        int dragStart = renderer.indexOf("handle.setOnMouseDragged");
        int dragEnd = renderer.indexOf("handle.setOnMouseReleased");
        String dragBlock = renderer.substring(dragStart, dragEnd);
        assertFalse(dragBlock.contains("refreshPreservingViewport.run()"),
                "El drag del bendpoint debe actualizar la ruta en sitio sin recrear el canvas por pixel.");
    }

    @Test
    void areaSelectionCanSelectNodesAndConnectorsTogether() throws Exception {
        String port = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasSelectionPort.java"));
        String controller = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasAreaSelectionController.java"));
        String selection = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSelection.java"));

        assertTrue(port.contains("default void selectElementsInside(CanvasBounds selectionBounds, boolean additive)"));
        assertTrue(controller.contains("selectionPort.selectElementsInside(currentBounds, additive);"));
        assertTrue(selection.contains("withNodesAndConnectors"));
    }
}
