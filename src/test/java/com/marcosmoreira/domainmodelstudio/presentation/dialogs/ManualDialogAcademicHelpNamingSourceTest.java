package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 32: el Menú Ayuda abre guía académica, no ayuda operativa. */
class ManualDialogAcademicHelpNamingSourceTest {

    private static final Path MAIN = Path.of("src/main/java");

    @Test
    void manualDialogShouldUseAcademicGuideNaming() throws IOException {
        String dialog = readJava("com/marcosmoreira/domainmodelstudio/presentation/dialogs/ManualDialog.java");

        assertTrue(dialog.contains("Guía académica — Domain Model Studio"));
        assertTrue(dialog.contains("REFERENCIA ACADÉMICA DE DIAGRAMAS"));
        assertTrue(dialog.contains("Guía académica de Domain Model Studio"));
        assertTrue(dialog.contains("ManualNavigationNode(\"Guía académica\""));
        assertFalse(dialog.contains("Guía operativa — Domain Model Studio"));
        assertFalse(dialog.contains("Guía operativa de Domain Model Studio"));
    }

    @Test
    void shellAndWelcomeShouldOpenAcademicGuide() throws IOException {
        String shell = readJava("com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java");
        String commands = readJava("com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java");
        String welcome = readJava("com/marcosmoreira/domainmodelstudio/presentation/canvas/WelcomeWorkspaceView.java");

        assertTrue(shell.contains("new MenuItem(\"Guía académica\")"));
        assertTrue(shell.contains("Guía académica abierta."));
        assertTrue(commands.contains("Guía académica abierta."));
        assertTrue(welcome.contains("Guía académica"));
        assertTrue(welcome.contains("Estudiar teoría de diagramas."));
        assertFalse(shell.contains("new MenuItem(\"Guía operativa\")"));
        assertFalse(commands.contains("Guía operativa abierta."));
    }

    private static String readJava(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
