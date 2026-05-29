package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl UX-1C: el SideDock respeta contenido que ya administra su propio scroll. */
class SideDockNoDoubleScrollSourceTest {

    private static final Path FRAME_FACTORY = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/sidedock/SideDockModuleFrameFactory.java");

    @Test
    void moduleFrameMustNotWrapExistingScrollPaneAgain() throws IOException {
        String source = Files.readString(FRAME_FACTORY, StandardCharsets.UTF_8);

        assertTrue(source.contains("if (content instanceof ScrollPane existingScroll)"));
        assertTrue(source.contains("return existingScroll;"));
        assertTrue(source.contains("side-dock-content-owns-scroll"));
        assertTrue(source.contains("ScrollPane.ScrollBarPolicy.NEVER"));
        assertTrue(source.contains("scroll.setPannable(true)"));
        assertFalse(source.contains("new ScrollPane(existingScroll)"));
    }
}
