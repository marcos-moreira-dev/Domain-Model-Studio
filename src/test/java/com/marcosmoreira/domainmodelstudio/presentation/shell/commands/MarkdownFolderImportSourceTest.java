package com.marcosmoreira.domainmodelstudio.presentation.shell.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class MarkdownFolderImportSourceTest {

    @Test
    void coordinatorUsesDirectoryChooserAndProjectTabOpenerWithoutCanvasDependency() throws Exception {
        String source = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/MarkdownFolderImportCoordinator.java"));

        assertTrue(source.contains("DirectoryChooser"));
        assertTrue(source.contains("ProjectTabOpener"));
        assertTrue(source.contains("markdownBatchImportUseCase"));
        assertFalse(source.contains("InteractiveCanvasSurfaceView"));
        assertFalse(source.contains("DiagramCanvasView"));
        assertFalse(source.contains("VisualLayoutService"));
    }

    @Test
    void toolbarAndMenuExposeMarkdownFolderAction() throws Exception {
        String toolbar = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/GlobalToolbarView.java"));
        String menu = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java"));
        String shell = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java"));

        assertTrue(toolbar.contains("Carpeta MD"));
        assertTrue(menu.contains("Abrir carpeta Markdown"));
        assertTrue(shell.contains("requestImportMarkdownFolder"));
    }

    @Test
    void folderImportShouldRunOffFxThreadAndUseBoundedDialogs() throws Exception {
        String coordinator = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/MarkdownFolderImportCoordinator.java"));
        String progress = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/MarkdownFolderImportProgressDialog.java"));
        String confirmation = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/MarkdownFolderImportConfirmationDialog.java"));
        String resultDialog = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/MarkdownFolderImportResultDialog.java"));
        String operationResultDialog = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/dialogs/OperationResultDialog.java"));
        String opener = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/MarkdownImportedProjectBatchOpener.java"));

        assertTrue(coordinator.contains("Task<MarkdownBatchImportResult>"));
        assertTrue(coordinator.contains("MarkdownFolderImportConfirmationDialog"));
        assertTrue(coordinator.contains("PauseTransition"));
        assertTrue(coordinator.contains("Duration.millis(450)"));
        assertTrue(coordinator.contains("new Thread(task"));
        assertTrue(confirmation.contains("GuidedActionDialog"));
        assertTrue(confirmation.contains("Candidatos Markdown que se revisarán"));
        assertTrue(confirmation.contains("El procesamiento empezará solo después"));
        assertTrue(progress.contains("ButtonBar.ButtonData.CANCEL_CLOSE"));
        assertTrue(resultDialog.contains("OperationResultDialog"));
        assertTrue(resultDialog.contains("Copiar problemas"));
        assertTrue(resultDialog.contains("Copiar estado"));
        assertTrue(resultDialog.contains("showDeferred"));
        assertTrue(operationResultDialog.contains("StatusIconFactory"));
        assertTrue(operationResultDialog.contains("setPrefSize"));
        assertTrue(operationResultDialog.contains("Clipboard.getSystemClipboard"));
        assertTrue(opener.contains("PauseTransition"));
        assertTrue(opener.contains("BATCH_SIZE"));
    }

}
