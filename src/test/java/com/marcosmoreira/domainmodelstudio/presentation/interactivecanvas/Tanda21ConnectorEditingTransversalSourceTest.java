package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class Tanda21ConnectorEditingTransversalSourceTest {

    @Test
    void surfaceInstallsConnectorGesturesBeforeBackgroundSelection() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java");
        assertTrue(source.contains("installConnectorEditingGestures();\n        installBackgroundSelection();"),
                "La selección/edición de relaciones debe interceptar el clic antes de que el fondo limpie selección.");
        assertTrue(source.contains("CanvasConnectorGestureInstaller.install"),
                "El canvas común debe instalar gestos transversales para relaciones.");
        assertTrue(source.contains("public boolean deleteSelectedBendPoint()"),
                "La superficie debe exponer una operación común para el botón Quitar punto.");
        assertTrue(source.contains("adapter.selection().selectedBendPoint().isEmpty()"),
                "Quitar punto solo debe actuar cuando hay un punto intermedio seleccionado.");
    }

    @Test
    void connectorGestureInstallerSelectsAndAddsBendPointsWithoutBlockingNodes() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasConnectorGestureInstaller.java");
        assertTrue(source.contains("CanvasInteractiveTargetPolicy"),
                "El gesto de relación debe respetar nodos, labels y handles existentes.");
        assertTrue(source.contains("targetPolicy.isBackground(target) && targetPolicy.isBackground(picked)"),
                "Solo debe operar cuando el evento no pertenece a un nodo u overlay interactivo.");
        assertTrue(source.contains("CanvasConnectorGeometry.edgeToEdgePoints"),
                "El hit test debe usar la trayectoria real con puntos intermedios.");
        assertTrue(source.contains("adapter.selectConnector(connector.id(), event.isShiftDown())"),
                "Clic sobre una relación debe seleccionarla.");
        assertTrue(source.contains("adapter.addBendPoint(connector.id(), canvasPoint.getX(), canvasPoint.getY())"),
                "Doble clic sobre una relación debe crear un punto intermedio.");
        assertTrue(source.contains("CONNECTOR_HIT_TOLERANCE / Math.max(0.25, surface.zoomFactor())"),
                "La tolerancia del hit test debe mantenerse estable visualmente con zoom.");
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
