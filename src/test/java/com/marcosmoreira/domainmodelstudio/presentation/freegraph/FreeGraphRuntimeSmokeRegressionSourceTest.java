package com.marcosmoreira.domainmodelstudio.presentation.freegraph;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Regresiones fuente post-smoke: etiquetas visibles aunque el Markdown no traiga label explícito. */
class FreeGraphRuntimeSmokeRegressionSourceTest {

    @Test
    void blankEdgeLabelsFallbackToReadableRoute() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphCanvasAdapter.java");
        assertTrue(source.contains("labelFor(edge)"),
                "Las relaciones del grafo deben entregar siempre una etiqueta visual al canvas común.");
        assertTrue(source.contains("source + separator + target"),
                "Si la relación no trae etiqueta, el fallback debe mostrar origen y destino.");
    }


    @Test
    void selectedNodeDragUsesDeferredCanvasPreviewInsteadOfLiveRefresh() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphCanvasAdapter.java");
        assertTrue(source.contains("public boolean supportsLivePreview()"),
                "El grafo debe declarar explícitamente cómo maneja la previsualización de arrastre.");
        assertTrue(source.contains("return false;"),
                "El arrastre estable debe usar preview diferido del canvas común, no reconstrucción viva por pixel.");
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
