package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class UmlClassExternalCodeNavigationSourceTest {

    @Test
    void umlWorkbenchKeepsCodeEditorConfigurationOutOfSideDock() throws IOException {
        String contributor = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassWorkbenchContributor.java"), StandardCharsets.UTF_8);
        String sideDockIds = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/sidedock/SideDockModuleId.java"), StandardCharsets.UTF_8);
        String shellView = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java"), StandardCharsets.UTF_8);

        assertFalse(contributor.contains("UmlClassCodeEditorPanel"));
        assertFalse(contributor.contains("SideDockModuleId.CODE_EDITOR"));
        assertFalse(sideDockIds.contains("CODE_EDITOR"));
        assertTrue(shellView.contains("Editor de código..."));
    }

    @Test
    void umlToolbarActionIsBackedByViewModelCommand() throws IOException {
        String toolbar = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/UmlClassToolbarContributor.java"), StandardCharsets.UTF_8);
        String shell = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java"), StandardCharsets.UTF_8);
        String viewModel = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramViewModel.java"), StandardCharsets.UTF_8);

        assertTrue(toolbar.contains("OPEN_UML_SOURCE"));
        assertTrue(shell.contains("requestOpenSelectedUmlSourceFile"));
        assertTrue(viewModel.contains("openSelectedSourceInCodeEditor"));
    }

    @Test
    void menuBarExposesPersistentCodeEditorConfiguration() throws IOException {
        String shellView = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java"), StandardCharsets.UTF_8);
        String dialog = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassCodeEditorSettingsDialog.java"), StandardCharsets.UTF_8);

        assertTrue(shellView.contains("new Menu(\"Configuración\")"));
        assertTrue(shellView.contains("Editor de código..."));
        assertTrue(dialog.contains("Elegir ejecutable"));
        assertTrue(dialog.contains("Preguntar con Windows"));
    }

    @Test
    void doubleClickNoLongerOpensSourceBecauseItInterferesWithDragging() throws IOException {
        String surface = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java"), StandardCharsets.UTF_8);
        String adapter = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassCanvasAdapter.java"), StandardCharsets.UTF_8);

        assertFalse(surface.contains("CanvasNodeOpenPort openPort"));
        assertFalse(adapter.contains("CanvasNodeOpenPort"));
    }
    @Test
    void sourceNavigationExplainsResolutionAndOffersFolderAction() throws IOException {
        String viewModel = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramViewModel.java"), StandardCharsets.UTF_8);
        String panel = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassPropertiesPanel.java"), StandardCharsets.UTF_8);
        String resolver = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassSourceFileResolver.java"), StandardCharsets.UTF_8);

        assertTrue(viewModel.contains("selectedSourceStatusSummary"));
        assertTrue(viewModel.contains("openSelectedSourceFolder"));
        assertTrue(panel.contains("Archivo fuente"));
        assertTrue(panel.contains("Abrir carpeta"));
        assertTrue(resolver.contains("UmlClassSourceFileResolution"));
        assertTrue(resolver.contains("CANDIDATE_NOT_FOUND"));
    }

}
