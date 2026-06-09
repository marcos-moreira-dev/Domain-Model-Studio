package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class InteractiveCanvasTargetPolicySourceTest {

    private static final Path POLICY = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasInteractiveTargetPolicy.java"
    );
    private static final Path NODE_INTERACTION = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeInteractionCoordinator.java"
    );

    @Test
    void labelsInsideRenderedNodesShouldNotBeTreatedAsBackground() throws IOException {
        String policy = Files.readString(POLICY, StandardCharsets.UTF_8);

        assertTrue(policy.contains("return current != node"),
                "Un descendiente de nodeLayer/connectorLayer/overlayLayer no debe tratarse como fondo.");
    }

    @Test
    void renderedNodesShouldCaptureMouseEventsAcrossTheirWholeBounds() throws IOException {
        String nodeInteraction = Files.readString(NODE_INTERACTION, StandardCharsets.UTF_8);

        assertTrue(nodeInteraction.contains("setPickOnBounds(true)"),
                "Toda la caja UML debe responder al clic, no solo sus labels internos.");
        assertTrue(nodeInteraction.contains("addEventFilter(MouseEvent.MOUSE_PRESSED"),
                "El nodo renderizado debe capturar el clic antes de handlers internos que puedan interferir.");
    }
}
