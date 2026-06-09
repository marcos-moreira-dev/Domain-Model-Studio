package com.marcosmoreira.domainmodelstudio.presentation.shell;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl UX-1A: pantalla completa es acción visual del shell, no del dominio activo. */
class MainShellViewFullScreenMenuSourceTest {

    private static final Path MAIN_SHELL_VIEW = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java");

    @Test
    void viewMenuMustExposeFullScreenWithF11() throws IOException {
        String source = read();

        assertTrue(source.contains("new Menu(\"Vista\")"));
        assertTrue(source.contains("commandItem(\"Pantalla completa\", () -> ShellFullScreenAction.toggle(root, viewModel::updateStatus), \"F11\", null)"));
        String action = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/ShellFullScreenAction.java"));
        assertTrue(action.contains("static void toggle(Parent root, Consumer<String> statusConsumer)"));
        assertTrue(action.contains("stage.setFullScreen(nextFullScreen)"));
        assertTrue(action.contains("Pantalla completa activada."));
    }

    @Test
    void fullScreenMustNotBeRoutedThroughProjectCommandHandler() throws IOException {
        String source = read();

        assertFalse(source.contains("viewModel::toggleFullScreen"));
        assertFalse(source.contains("MainShellCommandHandler") && source.contains("toggleFullScreen"));
    }

    private static String read() throws IOException {
        return Files.readString(MAIN_SHELL_VIEW, StandardCharsets.UTF_8);
    }
}
