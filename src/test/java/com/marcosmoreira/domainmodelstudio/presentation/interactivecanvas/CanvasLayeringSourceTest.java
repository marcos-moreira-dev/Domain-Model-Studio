package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Regresión fuente: pantalla y PNG comparten la misma política semántica de capas. */
class CanvasLayeringSourceTest {

    @Test
    void surfaceAndPngExporterUseSharedLayeringPolicy() throws IOException {
        String surface = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java");
        String exporter = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasPngExporter.java");

        assertTrue(surface.contains("CanvasLayeringPolicy.standard()"),
                "La vista en pantalla debe usar la política común de capas.");
        assertTrue(exporter.contains("CanvasLayeringPolicy.standard()"),
                "El PNG debe usar la misma política común de capas.");
        assertTrue(surface.contains("layeringPolicy.orderNodes(model.visibleNodes(), model)"),
                "La superficie debe ordenar nodos por capa semántica antes de renderizar.");
        assertTrue(exporter.contains("layeringPolicy.orderNodes(model.visibleNodes(), model)"),
                "El PNG debe ordenar nodos igual que la superficie.");
    }

    @Test
    void nodeOrderingHeuristicIsNotBuriedInsideSurfaceView() throws IOException {
        String surface = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java");
        assertTrue(!surface.contains("kind.contains(\"boundary\")")
                        && !surface.contains("kind.contains(\"module\")")
                        && !surface.contains("kind.contains(\"screen\")"),
                "La heurística de capas no debe quedar enterrada dentro de InteractiveCanvasSurfaceView.");
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
