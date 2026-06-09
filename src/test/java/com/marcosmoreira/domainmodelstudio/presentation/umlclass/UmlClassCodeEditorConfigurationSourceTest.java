package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class UmlClassCodeEditorConfigurationSourceTest {

    private static final Path CONTRIBUTOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassWorkbenchContributor.java"
    );
    private static final Path LAUNCHER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/CodeEditorLauncher.java"
    );

    @Test
    void codeEditorShouldBeConfiguredGloballyNotAsSideDockModule() throws IOException {
        String contributor = Files.readString(CONTRIBUTOR, StandardCharsets.UTF_8);

        assertFalse(contributor.contains("CODE_EDITOR"),
                "La configuración del editor externo debe vivir en Configuración > Editor de código, no como módulo lateral redundante.");
    }

    @Test
    void windowsOpenWithShouldUseDirectShellCommand() throws IOException {
        String launcher = Files.readString(LAUNCHER, StandardCharsets.UTF_8);

        assertTrue(launcher.contains("OpenAs_RunDLL"),
                "El launcher debe conservar soporte para el diálogo Abrir con de Windows.");
        assertTrue(launcher.contains("new ProcessBuilder(\"rundll32.exe\", \"shell32.dll,OpenAs_RunDLL\""),
                "El diálogo Abrir con debe lanzarse directamente para evitar problemas de quoting con cmd/start.");
        assertTrue(launcher.contains("cmd.exe") && launcher.contains("start"),
                "En Windows, la aplicación predeterminada debe poder abrirse con la shell del sistema.");
    }
}
