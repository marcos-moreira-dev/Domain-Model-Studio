package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class InteractiveCanvasSurfaceDynamicWorkspaceSourceTest {

    @Test
    void refreshExpandsWorkspaceFromSemanticContentBoundsBeforeRendering() throws IOException {
        String source = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java"
        ), StandardCharsets.UTF_8);

        assertTrue(source.contains("surface.ensureWorkspaceContains(semanticContentBounds())"));
        assertTrue(source.contains("surface.config().contentOriginX() + bounds.x()"));
        assertTrue(source.contains("surface.config().contentOriginY() + bounds.y()"));
    }
}
