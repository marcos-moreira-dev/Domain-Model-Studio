package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles Tanda 18: clic/drag de nodos no debe ser tratado como fondo. */
class Tanda18NodeGestureRegressionSourceTest {

    @Test
    void backgroundGesturesMustCheckTargetAndPickedNode() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasBackgroundGestureInstaller.java");

        assertTrue(source.contains("isBackgroundGestureTarget"));
        assertTrue(source.contains("event.getPickResult()"));
        assertTrue(source.contains("targetPolicy.isBackground(target) && targetPolicy.isBackground(picked)"));
    }

    @Test
    void nodeInteractionInstallMethodMustRemainSmall() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeInteractionCoordinator.java");

        assertTrue(source.contains("handleNodePressed"));
        assertTrue(source.contains("handleNodeDragged"));
        assertTrue(source.contains("handleNodeReleased"));
        assertFalse(source.contains("Point2D[] dragStart = new Point2D[1]"));
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
