package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ZoomableDiagramSurfaceDynamicWorkspaceSourceTest {

    @Test
    void surfaceUsesDynamicWorkspaceSizeForNavigationAndExport() throws IOException {
        String source = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/diagramcanvas/ZoomableDiagramSurface.java"
        ), StandardCharsets.UTF_8);

        assertTrue(source.contains("DynamicWorkspaceBoundsPolicy"));
        assertTrue(source.contains("ensureWorkspaceContains"));
        assertTrue(source.contains("workspaceWidth"));
        assertTrue(source.contains("new DiagramSurfaceExportNode(workspaceRoot, workspaceWidth, workspaceHeight)"));
    }
}
