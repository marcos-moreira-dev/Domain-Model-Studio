package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ZoomableDiagramSurfaceSourceStructureTest {

    @Test
    void surfaceKeepsScrollPaneGroupLayersAndWorkspaceRoot() throws IOException {
        String source = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/diagramcanvas/ZoomableDiagramSurface.java"
        ), StandardCharsets.UTF_8);

        assertTrue(source.contains("ScrollPane scrollPane"), "La superficie debe usar ScrollPane real.");
        assertTrue(source.contains("Group zoomGroup"), "La superficie debe conservar grupo de zoom.");
        assertTrue(source.contains("Pane zoomContainer"), "La superficie debe redimensionar contenedor escalado.");
        assertTrue(source.contains("DiagramSurfaceLayers"), "La superficie debe exponer capas canónicas.");
        assertTrue(source.contains("setPannable(false)"), "El paneo debe controlarse por el controlador común.");
    }
}
