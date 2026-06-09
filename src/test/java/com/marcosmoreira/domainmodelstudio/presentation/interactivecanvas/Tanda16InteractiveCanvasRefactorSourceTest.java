package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíles fuente del refactor transversal de canvas ejecutado en Tanda 16. */
class Tanda16InteractiveCanvasRefactorSourceTest {

    private static final Path BASE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas"
    );

    @Test
    void surfaceShouldDelegateNodeLabelAndBendPointResponsibilities() throws IOException {
        String surface = read("InteractiveCanvasSurfaceView.java");

        assertTrue(surface.contains("CanvasNodeInteractionCoordinator"));
        assertTrue(surface.contains("CanvasNodeVisualRegistry"));
        assertTrue(surface.contains("CanvasConnectorLabelOverlayRenderer"));
        assertTrue(surface.contains("CanvasBendPointHandleRenderer"));
        assertTrue(surface.contains("nodeInteractionCoordinator.install(node, rendered)"));
        assertTrue(surface.contains("connectorLabelOverlayRenderer.renderLabel(connector, model)"));
        assertTrue(surface.contains("bendPointHandleRenderer.renderBendPointHandles(connector, model)"));
    }

    @Test
    void extractedClassesShouldRemainTransversal() throws IOException {
        for (String file : List.of(
                "CanvasNodeInteractionCoordinator.java",
                "CanvasNodeVisualRegistry.java",
                "CanvasConnectorLabelOverlayRenderer.java",
                "CanvasBendPointHandleRenderer.java",
                "CanvasPointMapper.java")) {
            String source = read(file);
            assertFalse(source.contains("presentation.umlclass") || source.contains("presentation.freegraph"),
                    file + " no debe depender de presentaciones concretas.");
            assertFalse(source.contains("domain.umlclass") || source.contains("domain.freegraph"),
                    file + " no debe depender de dominios concretos.");
        }
    }

    @Test
    void surfaceShouldLeaveKnownDebtListByStayingWithinBoundaryLimit() throws IOException {
        String surface = read("InteractiveCanvasSurfaceView.java");
        long lines = surface.lines().count();
        assertTrue(lines <= 450, "La superficie interactiva debe quedar dentro del límite base de revisión humana.");
    }

    private static String read(String fileName) throws IOException {
        return Files.readString(BASE.resolve(fileName), StandardCharsets.UTF_8);
    }
}
