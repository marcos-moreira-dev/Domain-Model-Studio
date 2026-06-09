package com.marcosmoreira.domainmodelstudio.presentation.shell.tabs;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles fuente para conservar pestañas limpias en la carcasa desktop. */
class EditorTabVisualPolishSourceTest {

    private static final Path TAB_CELL = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/tabs/EditorTabCellView.java");
    private static final Path TAB_BAR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/tabs/ScrollableEditorTabBarView.java");
    private static final Path TAB_CSS = Path.of("src/main/resources/css/editor-tabs.css");

    @Test
    void projectTabsMustNotUseGenericSquareIcon() throws IOException {
        String source = Files.readString(TAB_CELL);

        assertFalse(source.contains("\"□\""),
                "Las pestañas de proyecto no deben mostrar un cuadrado genérico junto al título.");
        assertTrue(source.contains("if (tab.home())"),
                "Solo la pestaña de inicio conserva icono explícito.");
    }

    @Test
    void editorTabStripMustNotReuseToolbarBackground() throws IOException {
        String source = Files.readString(TAB_BAR);
        String css = Files.readString(TAB_CSS);

        assertFalse(source.contains("main-toolbar-scroll"),
                "La barra de pestañas no debe heredar el fondo degradado de la toolbar.");
        assertTrue(css.contains(".editor-tab-scroll"));
        assertTrue(css.contains("-fx-background-color: transparent"));
        assertFalse(css.contains(".editor-tab-scroll {\n    -fx-background-color: linear-gradient"));
    }
}
