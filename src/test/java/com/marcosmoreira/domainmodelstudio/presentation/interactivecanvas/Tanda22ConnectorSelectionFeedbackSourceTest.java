package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class Tanda22ConnectorSelectionFeedbackSourceTest {

    @Test
    void backgroundMustNotClearConnectorSelectionAfterConnectorGestureConsumesEvent() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasBackgroundGestureInstaller.java");

        assertTrue(source.contains("if (event.isConsumed())"),
                "El fondo no debe limpiar la selección si un gesto de relación ya consumió el evento.");
        assertTrue(source.contains("adapter.clearSelection()"),
                "El clic en vacío debe seguir deseleccionando cuando no hay relación/nodo activo.");
    }

    @Test
    void selectingConnectorHighlightsEveryIntermediateBendPoint() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasBendPointHandleRenderer.java");

        assertTrue(source.contains("connectorOrPointSelected(connectorId, index, model)"),
                "Los puntos intermedios deben responder a selección del conector o del punto individual.");
        assertTrue(source.contains("model.selection().isConnectorSelected(connectorId)"),
                "Al seleccionar una relación deben resaltarse todos sus puntos intermedios editables.");
    }

    @Test
    void connectorSelectionHasBlueVisualStylesAcrossCommonAndUmlCss() throws IOException {
        String commonCss = read("src/main/resources/css/interactive-canvas.css");
        String umlCss = read("src/main/resources/css/uml-class.css");

        assertTrue(commonCss.contains(".interactive-canvas-connector-selected"));
        assertTrue(commonCss.contains("-fx-stroke: -dms-accent"));
        assertTrue(umlCss.contains(".uml-class-canvas-connector-selected"));
        assertTrue(umlCss.contains(".uml-class-canvas-arrow-head-selected"));
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
