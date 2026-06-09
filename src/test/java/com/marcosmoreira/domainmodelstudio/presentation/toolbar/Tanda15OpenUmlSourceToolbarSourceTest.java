package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 15: Abrir código debe poder pulsarse en UML Clases para informar selección/ruta. */
class Tanda15OpenUmlSourceToolbarSourceTest {

    @Test
    void openCodeButtonMustNotBeDisabledOnlyBecauseNoSourcePathIsResolved() throws IOException {
        String toolbarViewModel = Files.readString(
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/MainToolbarViewModel.java"),
                StandardCharsets.UTF_8);
        String navigation = Files.readString(
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassSourceNavigationController.java"),
                StandardCharsets.UTF_8);

        assertTrue(toolbarViewModel.contains("actionId == DiagramToolbarActionId.OPEN_UML_SOURCE"));
        assertTrue(toolbarViewModel.contains("!DiagramTypeId.UML_CLASS.equals(activeDiagramTypeProperty().get())"));
        assertFalse(toolbarViewModel.contains("umlClassDiagramViewModel.selectedSourcePath().isEmpty()"));
        assertTrue(navigation.contains("Selecciona una clase importada desde código para abrir su archivo."));
        assertTrue(navigation.contains("resolution.userMessage(node.displayName())"));
    }
}
