package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 15: los TabPane internos del SideDock no deben heredar franja oscura. */
class Tanda15SideDockInternalTabsCssSourceTest {

    @Test
    void workbenchCssMustStyleInternalTabPanesInsideSideDock() throws IOException {
        String css = Files.readString(Path.of("src/main/resources/css/workbench.css"), StandardCharsets.UTF_8);

        assertTrue(css.contains("Tanda 15 — tabs internos del SideDock claros"));
        assertTrue(css.contains(".workbench-side-dock .tab-pane .tab-header-area .tab-header-background"));
        assertTrue(css.contains(".side-dock-module-frame .tab-pane .tab-header-area .tab-header-background"));
        assertTrue(css.contains("-fx-background-color: #F5F8FB"));
        assertTrue(css.contains(".side-dock-module-frame .tab-pane .tab:selected"));
        assertFalse(css.contains(".workbench-side-dock .tab-pane .tab-header-background {\n    -fx-background-color: black"));
    }
}
