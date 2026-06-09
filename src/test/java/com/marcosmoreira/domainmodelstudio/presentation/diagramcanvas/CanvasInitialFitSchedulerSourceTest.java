package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class CanvasInitialFitSchedulerSourceTest {

    @Test
    void diagramCentersDelegateInitialFitToCommonScheduler() throws IOException {
        String scheduler = "CanvasInitialFitScheduler";
        String source = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapDiagramCenter.java"
        ), StandardCharsets.UTF_8);

        assertTrue(source.contains(scheduler), "Los centros visuales deben delegar el ajuste inicial común.");
    }
}
