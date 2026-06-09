package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Regresión fuente: las etiquetas de relaciones deben ser visibles en runtime y exportaciones. */
class ConnectorLabelVisibilitySourceTest {

    @Test
    void commonConnectorLabelsAreForcedAboveCanvasContent() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasConnectorLabelNodeFactory.java");
        assertTrue(source.contains("label.setViewOrder(-1000.0)"),
                "Las etiquetas comunes deben quedar por encima de nodos y relaciones.");
        assertTrue(source.contains("label.setPrefSize(width, height)"),
                "La etiqueta debe tener tamaño preferido medido antes de reubicarla.");
    }

    @Test
    void graphAndUmlLabelsHaveExplicitHighContrastCss() throws IOException {
        String css = read("src/main/resources/css/interactive-canvas.css");
        assertTrue(css.contains("interactive-canvas-connector-label-free-graph-edge-directed"));
        assertTrue(css.contains("interactive-canvas-connector-label-uml-relation-inheritance"));
        assertTrue(css.contains("-fx-background-color: rgba(255, 255, 255, 0.99)"));
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
