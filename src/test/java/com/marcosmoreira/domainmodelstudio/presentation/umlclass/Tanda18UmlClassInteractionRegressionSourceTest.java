package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles Tanda 18: UML Clases debe priorizar clic/drag de clases y apertura de código útil. */
class Tanda18UmlClassInteractionRegressionSourceTest {

    @Test
    void umlConnectorLinesMustNotBlockNodeDrag() throws IOException {
        String adapter = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassCanvasAdapter.java");
        String surface = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java");

        assertTrue(adapter.contains("implements InteractiveCanvasAdapter, CanvasConnectorLabelPort, CanvasConnectorHitTestPort"));
        assertTrue(adapter.contains("public boolean connectorLineHitTestingEnabled()"));
        assertTrue(adapter.contains("return false;"));
        assertTrue(surface.contains("rendered.setMouseTransparent(true);"));
    }

    @Test
    void openCodeMustGenerateTemporaryPreviewWhenSourcePathIsMissing() throws IOException {
        String controller = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassSourceNavigationController.java");
        String writer = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassSourcePreviewWriter.java");

        assertTrue(controller.contains("sourcePreviewWriter.writePreview(node)"));
        assertTrue(controller.contains("se abrió una vista temporal generada"));
        assertTrue(writer.contains("Vista temporal generada desde el diagrama UML"));
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
