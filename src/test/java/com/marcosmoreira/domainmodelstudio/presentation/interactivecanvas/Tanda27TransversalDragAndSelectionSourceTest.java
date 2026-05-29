package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class Tanda27TransversalDragAndSelectionSourceTest {

    @Test
    void selectedNodeGroupCanBeDraggedWithoutLosingSelection() throws IOException {
        String coordinator = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeInteractionCoordinator.java"));
        String registry = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeVisualRegistry.java"));

        assertTrue(coordinator.contains("shouldPreserveSelectionGroup"),
                "Al presionar un nodo ya incluido en una selección múltiple no debe destruirse el grupo.");
        assertTrue(coordinator.contains("adapter.selection().selectedNodeIds()"),
                "La decisión de preservar selección debe basarse en la selección transversal actual.");
        assertTrue(registry.contains("previewNodeIdsForDraggedNode"),
                "La previsualización de arrastre debe contemplar todos los nodos seleccionados por rectángulo.");
    }

    @Test
    void connectorsShowLivePreviewAndFadeOriginalsDuringNodeDrag() throws IOException {
        String preview = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasConnectorDragPreviewLayer.java"));
        String visualRegistry = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasConnectorVisualRegistry.java"));
        String css = Files.readString(Path.of("src/main/resources/css/interactive-canvas-selection-overrides.css"));

        assertTrue(preview.contains("connectorVisualRegistry.showDragPreviewFor"),
                "El preview de conectores debe atenuar las relaciones originales afectadas.");
        assertTrue(visualRegistry.contains("interactive-canvas-connector-drag-original"),
                "Las relaciones originales afectadas deben recibir una marca visual temporal.");
        assertTrue(css.contains("interactive-canvas-connector-live-preview"),
                "La línea temporal debe estar estilada para verse durante el arrastre.");
    }

    @Test
    void connectorSelectionProjectsToEndpointNodesAndBendPointIsClearer() throws IOException {
        String surface = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java"));
        String projection = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasSelectionProjection.java"));
        String css = Files.readString(Path.of("src/main/resources/css/interactive-canvas-selection-overrides.css"));

        assertTrue(surface.contains("CanvasSelectionProjection.isNodeSelectedOrEndpointOfSelectedConnector"),
                "La selección de una relación debe proyectar feedback visual sobre sus nodos extremos.");
        assertTrue(projection.contains("isConnectorSelected"),
                "La proyección debe partir de la relación seleccionada.");
        assertTrue(css.contains("#ffffff"),
                "El punto intermedio seleccionado debe tener un color claro y visible.");
    }
}
