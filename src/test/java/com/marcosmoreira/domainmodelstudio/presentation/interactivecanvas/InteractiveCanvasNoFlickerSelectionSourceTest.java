package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Regresión fuente: un clic simple de selección no debe reconstruir todo el canvas. */
class InteractiveCanvasNoFlickerSelectionSourceTest {

    @Test
    void simpleNodeClickKeepsLocalSelectionWithoutViewportRefresh() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeInteractionCoordinator.java");
        String releaseBody = methodBody(source, "install");
        assertTrue(releaseBody.contains("if (moved)"));
        assertFalse(releaseBody.contains("resetDragPreviewTranslations(node.id(), rendered);\\n"
                + "                    refreshPreservingViewport();\\n"
                + "                }\\n"),
                "Un clic sin movimiento no debe forzar refresh completo porque produce parpadeo perceptible.");
    }

    private static String methodBody(String source, String methodName) {
        int methodIndex = source.indexOf("public void " + methodName + "(");
        if (methodIndex < 0) {
            throw new AssertionError("No se encontró el método " + methodName);
        }
        int bodyStart = source.indexOf('{', methodIndex);
        int depth = 0;
        for (int index = bodyStart; index < source.length(); index++) {
            char current = source.charAt(index);
            if (current == '{') {
                depth++;
            } else if (current == '}') {
                depth--;
                if (depth == 0) {
                    return source.substring(bodyStart + 1, index);
                }
            }
        }
        throw new AssertionError("No se pudo extraer el método " + methodName);
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
