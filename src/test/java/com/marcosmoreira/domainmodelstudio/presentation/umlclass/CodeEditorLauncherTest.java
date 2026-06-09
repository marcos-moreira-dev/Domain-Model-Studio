package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CodeEditorLauncherTest {

    @Test
    void appendsFileWhenCommandHasNoPlaceholder() {
        CodeEditorLauncher launcher = CodeEditorLauncher.configured("code --reuse-window");

        Path file = Path.of("src/App.java").toAbsolutePath().normalize();
        List<String> command = launcher.commandFor(file);

        assertEquals(List.of("code", "--reuse-window", file.toString()), command);
    }

    @Test
    void replacesFilePlaceholderWhenConfigured() {
        CodeEditorLauncher launcher = CodeEditorLauncher.configured("code --goto %f");

        Path file = Path.of("src/App.java").toAbsolutePath().normalize();
        List<String> command = launcher.commandFor(file);

        assertEquals(List.of("code", "--goto", file.toString()), command);
    }

}
