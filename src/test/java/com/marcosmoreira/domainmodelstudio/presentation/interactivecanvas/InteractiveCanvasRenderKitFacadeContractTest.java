package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class InteractiveCanvasRenderKitFacadeContractTest {

    @Test
    void renderKitContractExposesDrawingFacadeWithoutBreakingLegacyMethods() throws IOException {
        String source = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasRenderKit.java"
        ), StandardCharsets.UTF_8);

        assertTrue(source.contains("DiagramDrawingFacade"), "El contrato debe exponer la fachada común de dibujo.");
        assertTrue(source.contains("default Node renderNode"), "La variante con fachada debe ser default para no romper render kits viejos.");
        assertTrue(source.contains("default Node renderConnector"), "La variante de conectores con fachada debe ser default.");
    }
}
