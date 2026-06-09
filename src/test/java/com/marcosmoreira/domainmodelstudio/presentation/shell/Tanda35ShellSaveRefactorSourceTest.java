package com.marcosmoreira.domainmodelstudio.presentation.shell;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 35: Guardar/Guardar como sale del shell principal. */
class Tanda35ShellSaveRefactorSourceTest {

    private static final Path SHELL = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java");
    private static final Path SAVE_COORDINATOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/ProjectSaveCoordinator.java");
    private static final Path UNSAVED_DIALOG = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/UnsavedChangesDialog.java");

    @Test
    void mainShellDelegatesDesktopSaveSemantics() throws IOException {
        String shell = Files.readString(SHELL, StandardCharsets.UTF_8);
        String coordinator = Files.readString(SAVE_COORDINATOR, StandardCharsets.UTF_8);

        assertTrue(shell.contains("private final ProjectSaveCoordinator projectSaveCoordinator;"));
        assertTrue(shell.contains("projectSaveCoordinator.saveCurrentProject()"));
        assertTrue(shell.contains("projectSaveCoordinator.saveCurrentProjectWithDialog(saveAs)"));
        assertFalse(shell.contains("new FileChooser();\n        chooser.setTitle(saveAs"));
        assertFalse(shell.contains("ensureDmsExtension("));

        assertTrue(coordinator.contains("saveCurrentProject()"));
        assertTrue(coordinator.contains("saveCurrentProjectWithDialog(boolean saveAs)"));
        assertTrue(coordinator.contains("ensureDmsExtension(Path targetFile)"));
        assertTrue(coordinator.contains("session.projectFile = normalizedTarget"));
    }

    @Test
    void applicationExitConfirmationStaysInUnsavedChangesDialog() throws IOException {
        String shell = Files.readString(SHELL, StandardCharsets.UTF_8);
        String dialog = Files.readString(UNSAVED_DIALOG, StandardCharsets.UTF_8);

        assertTrue(shell.contains("unsavedChangesDialog.confirmBeforeApplicationExit(projectSessionCoordinator.sessions())"));
        assertFalse(shell.contains("new ButtonType(\"Salir sin guardar\""));
        assertTrue(dialog.contains("confirmBeforeApplicationExit"));
        assertTrue(dialog.contains("Salir sin guardar"));
    }

    @Test
    void mainShellKeepsShrinkingAfterTanda35() throws IOException {
        long lines = Files.lines(SHELL).count();
        assertTrue(lines < 1000, "MainShellCommandHandler debe quedar bajo 1000 líneas tras Tanda 35; líneas actuales: " + lines);
    }
}
