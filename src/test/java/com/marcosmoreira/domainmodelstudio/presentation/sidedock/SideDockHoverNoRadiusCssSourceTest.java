package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl UX-1B0: hover transversal seguro y botones del SideDock/workbench sin radius ornamental. */
class SideDockHoverNoRadiusCssSourceTest {

    private static final Path WORKBENCH_CSS = Path.of("src/main/resources/css/workbench.css");

    @Test
    void workbenchAndSideDockButtonsUseBlackHoverAndSquareCorners() throws IOException {
        String source = Files.readString(WORKBENCH_CSS, StandardCharsets.UTF_8);

        assertTrue(source.contains(".side-dock-rail-button:hover"));
        assertTrue(source.contains(".diagram-workbench-action-button:hover"));
        assertTrue(source.contains("-fx-background-color: #111111"));
        assertTrue(source.contains("-fx-text-fill: #FFFFFF"));
        assertTrue(source.contains("-fx-background-radius: 0"));
        assertTrue(source.contains("-fx-border-radius: 0"));
        assertTrue(source.contains(".side-dock-min-visible-rows"));
    }
}
