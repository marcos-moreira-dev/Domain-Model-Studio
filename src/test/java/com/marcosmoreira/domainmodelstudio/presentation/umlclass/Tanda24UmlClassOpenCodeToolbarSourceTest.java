package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** La acción principal Abrir código debe delegar en Windows/sistema por defecto y no depender de VS Code. */
class Tanda24UmlClassOpenCodeToolbarSourceTest {

    @Test
    void toolbarOpenCodeShouldUseSystemDefaultPath() throws IOException {
        String shellCommands = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/UmlClassShellCommands.java");
        String launcher = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/CodeEditorLauncher.java");

        assertTrue(shellCommands.contains("viewModel.openSelectedSourceWithSystemDefault()"),
                "El botón Abrir código del toolbar debe usar la aplicación predeterminada del sistema.");
        assertTrue(launcher.contains("url.dll,FileProtocolHandler"),
                "En Windows se debe usar FileProtocolHandler para abrir con la asociación del sistema.");
        assertTrue(launcher.contains("cmd.exe") && launcher.contains("start"),
                "Debe quedar fallback a start si FileProtocolHandler falla.");
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
