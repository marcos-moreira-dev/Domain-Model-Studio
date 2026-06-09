package com.marcosmoreira.domainmodelstudio.presentation.architecture;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

final class ArchitectureZoneTitleDragHandleSourceTest {

    @Test
    void transparentContainersExposeOnlyATitleHandleForDragging() throws Exception {
        String renderKit = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureRenderKit.java"));

        assertTrue(renderKit.contains("architecture-canvas-zone-title-handle"));
        assertTrue(renderKit.contains("zone.setMouseTransparent(true)"));
        assertTrue(renderKit.contains("group.setPickOnBounds(false)"));
        assertTrue(renderKit.contains("titleHandle.setCursor(Cursor.MOVE)"));
        assertTrue(renderKit.contains("architecture-canvas-zone-move-glyph"));
        assertFalse(renderKit.contains("group.setPickOnBounds(true)"));
    }

    @Test
    void titleHandleIsWhiteRectangularAndSelectionAware() throws Exception {
        String css = Files.readString(Path.of("src/main/resources/css/architecture-diagram.css"));

        assertTrue(css.contains(".architecture-canvas-zone-title-handle"));
        assertTrue(css.contains("-fx-fill: rgba(255, 255, 255, 0.96);"));
        assertTrue(css.contains("-fx-arc-width: 0;"));
        assertTrue(css.contains("-fx-arc-height: 0;"));
        assertTrue(css.contains(".architecture-canvas-zone-title-handle-selected"));
        assertTrue(css.contains(".architecture-canvas-zone-move-glyph"));
    }
}
